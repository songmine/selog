package io.song.selog.post.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.song.selog.member.domain.MemberDTO;
import io.song.selog.post.domain.PostAttachDTO;
import io.song.selog.post.domain.PostDTO;
import io.song.selog.post.mapper.PostAttachMapper;
import io.song.selog.post.mapper.PostCommentMapper;
import io.song.selog.post.mapper.PostMapper;
import io.song.selog.post.persistence.IPostDAO;
import io.song.selog.post.service.IPostService;

@Service
public class PostServiceImpl implements IPostService {

	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
	
	@Autowired
	private IPostDAO postDao;
	
	@Autowired
	private PostMapper pMapper;
	
	@Autowired
	private PostAttachMapper attachMapper;
	
	@Autowired
	private PostCommentMapper commentMapper;

	// 글 작성
	@Transactional
	@Override
	public int insertPost(PostDTO pDto) throws Exception {
		logger.info("PostServiceImpl insert......................");
		
		int result = 0;
		
		// 게시글 등록 시, "이미지" 첨부파일 존재여부 확인 후, 존재하면 같이 등록
		if (pDto.getAttachList() != null) {
			if (pDto.getAttachList().get(0) != null) {
				pDto.setP_pic(pDto.getAttachList().get(0).getUploadPath()+"/"+pDto.getAttachList().get(0).getUuid()+"_"+pDto.getAttachList().get(0).getFileName());
			}
		}
		
		result = postDao.insertPost(pDto);
		
		if (pDto.getAttachList() != null) {
			logger.info("PostServiceImpl insert with attach.................");
			logger.info("게시글번호 >>>> " + pDto.getP_id());
			pDto.getAttachList().forEach(attach -> {
				attach.setP_id(pDto.getP_id()-1);
				attachMapper.insert(attach);
			});
			logger.info("PostServiceImpl END insert with attach.................");
		}
		
		logger.info("Result >>> " + result);
		return result;
	}

	// 트렌딩 글 목록 + 기간 필터링
	@Override
	public List<PostDTO> trendingList(Map<String, Object> paramMap) throws Exception {
		logger.info("PostServiceImpl trendingList......................");
		
		return postDao.trendingList(paramMap);
	}

	// 최신 글 목록
	@Override
	public List<PostDTO> recentList(Map<String, Object> paramMap) throws Exception {
		logger.info("PostServiceImpl recentList......................");
		return postDao.recentList(paramMap);
	}

	// 글을 보기 위한 정보조회
	/*
	 * Isolation.READ_COMMITTED                                       
	 * dirty read 방지 : 트랜잭션이 커밋되어 확정된 데이터만을 읽는 것을 허용                  
	 * 어떠한 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 해당 데이터에 접근할 수 없다.   
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public PostDTO selPostView(int p_id) throws Exception {
		logger.info("PostServiceImpl selPostView......................");
		postDao.viewCnt(p_id);
		return postDao.selPostView(p_id);
	}

	// 글 검색
	@Override
	public List<PostDTO> selSearchList(Map<String, Object> paramMap) throws Exception {
		logger.info("PostServiceImpl selSearchList......................");
		return postDao.selSearchList(paramMap);
	}

	// 검색 건수
	@Override
	public int selSearchListCnt(String keyword) throws Exception {
		logger.info("PostServiceImpl selSearchListCnt......................");
		return postDao.selSearchListCnt(keyword);
	}

	// 글 상세의 작성자 정보
	@Override
	public MemberDTO selAuthorInfo(String m_id) throws Exception {
		logger.info("PostServiceImpl selAuthorInfo......................");
		return postDao.selAuthorInfo(m_id);
	}

	// 글 삭제
	@Transactional
	@Override
	public boolean deletePost(int p_id) throws Exception {
		logger.info("PostServiceImpl deletePost......................");
		
//		attachMapper.deleteAll(p_id);
//		commentMapper.deleteAllComments(p_id);
		
		// postDao.deletePost(p_id) 호출의 결과가 정상적으로 완료되었으면 1, 비정상적 완료이면 0의 값 리턴되는데,
		// 정상완료(1)인지 비교해서 TRUE / FALSE 값인 boolean 값을 리턴한다.
		return postDao.deletePost(p_id) == 1;
	}

	// 글 수정
	@Transactional
	@Override
	public boolean updateEditPost(PostDTO pDto) throws Exception {
		logger.info("PostServiceImpl updateEditPost......................");
		
		// 1순위 : 첨부파일 전체 삭제 (+ 서버파일 삭제.... ==> 전체 파일 삭제됨 ==> 스케쥴러 이용해서 서버 사진 삭제)
//		attachMapper.deleteAll(pDto.getP_id());
		
		// 2순위 : 전체 수정
		boolean modifyResult = postDao.updateEditPost(pDto) == 1;
		
		// 3순위 : DML
//		if (pDto.getAttachList() != null) {
//			if (modifyResult && pDto.getAttachList().size() > 0) {
//				pDto.getAttachList().forEach(attach -> {
//					attach.setP_id(pDto.getP_id());
//					attachMapper.insert(attach);
//				});
//			}
//		}

		return modifyResult;
	}

	// 글 상세보기 시, 내가 좋아요 누른 글인지 확인
	@Override
	public Map<String, Object> selLikeYesNo(Map<String, Object> likeParam) throws Exception {
		logger.info("PostServiceImpl selLikeYesNo......................");

		return postDao.selLikeYesNo(likeParam);
	}

	// 좋아요 버튼 클릭 시, 테이블에 데이터 생성
	// post테이블 좋아요수 컬럼에 + 1
	@Transactional
	@Override
	public void clickLike(Map<String, Object> paramMap) throws Exception {
		logger.info("PostServiceImpl clickLike......................");
		postDao.insertLikeBtn(paramMap);
		postDao.updateLikeCntPlus(paramMap);
	}

	// 좋아요 버튼 취소 시, 테이블에 데이터 삭제
	// post테이블 좋아요수 컬럼에 - 1
	@Transactional
	@Override
	public void clickLikeCancle(Map<String, Object> paramMap) throws Exception {
		logger.info("PostServiceImpl clickLike Cancle......................");
		postDao.deleteLikeBtn(paramMap);
		postDao.updateLikeCntMinus(paramMap);
	}
	
	// 좋아요 / 좋아요 취소 시 좋아요 수
	@Override
	public Map<String, Object> selLikeCnt(Map<String, Object> paramMap) throws Exception {
		logger.info("PostServiceImpl selLikeCnt......................");
		return postDao.selLikeCnt(paramMap);
	}

	// 상세 글의 이전 글 정보
	@Override
	public PostDTO selPrePost(PostDTO postView) throws Exception {
		logger.info("PostServiceImpl selPrePost......................");
		return postDao.selPrePost(postView);
	}

	// 상세 글의 다음 글 정보
	@Override
	public PostDTO selNextPost(PostDTO postView) throws Exception {
		logger.info("PostServiceImpl selNextPost......................");
		return postDao.selNextPost(postView);
	}

	// 트렌드 글 건수
	@Override
	public int selTrendListCnt(Map<String, Object> paramMapTrend) throws Exception {
		logger.info("PostServiceImpl selTrendListCnt......................");
		return postDao.selTrendListCnt(paramMapTrend);
	}

	// 최신 글 건수
	@Override
	public int selRecentListCnt(Map<String, Object> paramMapRecent) throws Exception {
		logger.info("PostServiceImpl selRecentListCnt......................");
		return postDao.selRecentListCnt(paramMapRecent);
	}
	
	// 게시물 상세보기 시, 첨부파일 목록
	@Override
	public List<PostAttachDTO> selAttachList(int p_id) throws Exception {
		logger.info("PostServiceImpl selAttachList......................");
		return attachMapper.findByP_Id(p_id);
	}
	
}
