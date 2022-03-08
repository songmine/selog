package io.song.selog.member.service;

import java.util.Map;

import io.song.selog.member.domain.MemberDTO;

// Service 클래스의 메서드들은 최대한 고객의 요구사항에 일치하도록 이름 지음.
// 계층간의 연결은 느슨한 결합으로 한다.
public interface IMemberService {

	public int selIdCheck(String m_id) throws Exception;
	public int insertMember(MemberDTO mDto) throws Exception;

	public int selSignIn(MemberDTO mDto) throws Exception;
	
	public MemberDTO getMyInfo(String m_id) throws Exception;
	
	public boolean deleteMember(String m_id) throws Exception;
	
	public MemberDTO read(String m_id) throws Exception;
	
	public int modifyPwd(MemberDTO mDto) throws Exception;
	
	public int modifyMyInfo(Map<String, Object> paramMap) throws Exception;
	
	public int updateMemberPic(Map<String, Object> paramMap) throws Exception;
	
}
