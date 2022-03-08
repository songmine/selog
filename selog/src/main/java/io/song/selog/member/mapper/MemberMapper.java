package io.song.selog.member.mapper;

import io.song.selog.member.domain.MemberDTO;

// 메서드의 선언만 하고, 그 실질적인 내용은 --Mapper.xml에 작성
public interface MemberMapper {

	public String getTime();
	
	public int selIdCheck(String m_id);
	public int insertMember(MemberDTO mDto);
	
	public String selSignIn(MemberDTO mDto);
	public MemberDTO getMyInfo(String m_id);
	
	public int deleteMember(String m_id);
	
	public MemberDTO read(String m_id);
	
	public int updateMemberPic(MemberDTO mDto);
	
}
