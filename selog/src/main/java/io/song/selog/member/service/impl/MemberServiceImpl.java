package io.song.selog.member.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.song.selog.member.domain.AuthDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.persistence.IMemberDAO;
import io.song.selog.member.service.IMemberService;
import io.song.selog.post.domain.PostCommentDTO;
import io.song.selog.post.mapper.PostCommentMapper;
import io.song.selog.post.persistence.IPostDAO;

@Service
public class MemberServiceImpl implements IMemberService {

	private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	@Autowired
	private IMemberDAO memberDao;
	
	@Autowired
	private IPostDAO postDao;
	
	@Autowired
	private PostCommentMapper pcMapper;
	
	@Autowired
	BCryptPasswordEncoder pwdEncoder;
	
	// 회원가입 시, 아이디 중복 체크
	@Override
	public int selIdCheck(String m_id) throws Exception {
		logger.info("MemberServiceImpl selIdCheck.....................");
		return memberDao.selIdCheck(m_id);
	}
	
	// 회원 가입
	@Transactional
	@Override
	public int insertMember(MemberDTO mDto) throws Exception {
		logger.info("MemberServiceImpl insertMember....................");
		
		String ori_m_pwd = mDto.getM_pwd();
		mDto.setM_pwd(pwdEncoder.encode(ori_m_pwd));
		
		AuthDTO aDto = new AuthDTO();
		aDto.setM_id(mDto.getM_id());
		aDto.setAuth("member");
		
		memberDao.insertMember(mDto);
		
		return memberDao.insertAuth(aDto);
	}

	// 로그인
	@Override
	public int selSignIn(MemberDTO mDto) throws Exception {
		logger.info("MemberServiceImpl selSignIn......................");
		String result = memberDao.selSignIn(mDto);
		return result != null? 1 : -1;
	}

	// 설정 페이지를 위한 값 불러오기
	@Override
	public MemberDTO getMyInfo(String m_id) throws Exception {
		logger.info("MemberServiceImpl getMyInfo......................");
		return memberDao.getMyInfo(m_id);
	}

	// 회원 탈퇴
	@Transactional
	@Override
	public boolean deleteMember(String m_id) throws Exception {
		logger.info("MemberServiceImpl deleteMember....................");
		
		// TODO : 회원 권한, 게시글(게시글 첨부파일, 게시글 댓글, 게시글 좋아요) 함께 삭제 (우선 DB에서 처리)
		memberDao.deleteAuth(m_id);
		
		// 탈퇴 후, 게시글 댓글 수 업데이트 (좋아요 수, 조회수는 업데이트 하지 않음)
		List<PostCommentDTO> myCommentList = pcMapper.myCommentList(m_id);
		myCommentList.forEach(list -> {
			int myCommentCntPerPost = 0;
			
			myCommentCntPerPost = pcMapper.getCommentCntByP_Id(list.getP_id());
//			logger.info("내 댓글 갯수 >>>> 게시글 번호 : " + list.getP_id() + ", 댓글 수 : " + (-myCommentCntPerPost));
			
			try {
				postDao.updateCommentCnt(list.getP_id(), -myCommentCntPerPost);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		return memberDao.deleteMember(m_id) == 1;
	}

	@Override
	public MemberDTO read(String m_id) throws Exception {
		logger.info("MemberServiceImpl read............................");
		return memberDao.read(m_id);
	}

	// 비밀번호 수정
	@Override
	public int modifyPwd(MemberDTO mDto) throws Exception {
		logger.info("MemberServiceImpl modifyPwd.......................");
		String before_m_pwd = mDto.getM_pwd();
		mDto.setM_pwd(pwdEncoder.encode(before_m_pwd));
		return memberDao.modifyPwd(mDto);
	}

	// 이름, 한 줄 소개 수정
	@Override
	public int modifyMyInfo(Map<String, Object> paramMap) throws Exception {
		logger.info("MemberServiceImpl modifyMyInfo.......................");
		return memberDao.modifyMyInfo(paramMap);
	}

	// 프로필 이미지 업로드 (생성+제거)
	@Override
	public int updateMemberPic(Map<String, Object> paramMap) throws Exception {
		logger.info("MemberServiceImpl updateMemberPic.......................");
		return memberDao.updateMemberPic(paramMap);
	}
	

}
