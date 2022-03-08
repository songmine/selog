package io.song.selog.member.persistence;

import java.util.Map;

import io.song.selog.member.domain.AuthDTO;
import io.song.selog.member.domain.MemberDTO;

public interface IMemberDAO {

	public String getTime() throws Exception;
	
	public int selIdCheck(String m_id) throws Exception;
	public int insertMember(MemberDTO mDto) throws Exception;
	public int insertAuth(AuthDTO aDto) throws Exception;
	
	public String selSignIn(MemberDTO mDto) throws Exception;
	
	public MemberDTO getMyInfo(String m_id) throws Exception;
	
	public int deleteMember(String m_id) throws Exception;
	
	public MemberDTO read(String m_id) throws Exception;

	public int modifyPwd(MemberDTO mDto) throws Exception;

	public int deleteAuth(String m_id) throws Exception;

	public int modifyMyInfo(Map<String, Object> paramMap) throws Exception;

	public int updateMemberPic(Map<String, Object> paramMap) throws Exception;
	
}
