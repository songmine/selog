package io.song.selog.member.persistence.impl;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.song.selog.member.domain.AuthDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.persistence.IMemberDAO;

@Repository
public class MemberDAOImpl implements IMemberDAO {

	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESPACE = "io.song.selog.member.mapper.MemberMapper";

	// 테스트용
	@Override
	public String getTime() throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".getTime");
	}

	@Override
	public int selIdCheck(String m_id) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".selIdCheck", m_id);
	}

	@Override
	public int insertMember(MemberDTO mDto) throws Exception {
		return sqlSession.insert(NAMESPACE + ".insertMember", mDto);
	}

	@Override
	public String selSignIn(MemberDTO mDto) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".selSignIn", mDto);
	}

	@Override
	public MemberDTO getMyInfo(String m_id) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".getMyInfo", m_id);
	}

	@Override
	public int deleteMember(String m_id) throws Exception {
		return sqlSession.delete(NAMESPACE + ".deleteMember", m_id);
	}

	@Override
	public MemberDTO read(String m_id) throws Exception {
		return sqlSession.selectOne(NAMESPACE + ".read", m_id);
	}

	@Override
	public int modifyPwd(MemberDTO mDto) throws Exception {
		return sqlSession.update(NAMESPACE + ".modifyPwd", mDto);
	}

	@Override
	public int insertAuth(AuthDTO aDto) throws Exception {
		return sqlSession.insert(NAMESPACE + ".insertAuth", aDto);
	}

	@Override
	public int deleteAuth(String m_id) throws Exception {
		return sqlSession.delete(NAMESPACE + ".deleteAuth", m_id);
	}

	@Override
	public int modifyMyInfo(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update(NAMESPACE + ".modifyMyInfo", paramMap);
	}

	@Override
	public int updateMemberPic(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert(NAMESPACE + ".updateMemberPic", paramMap);
	}

}
