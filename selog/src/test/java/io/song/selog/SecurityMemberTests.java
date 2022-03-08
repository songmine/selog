package io.song.selog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.service.IMemberService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/**/root-context.xml",
					   "file:src/main/webapp/WEB-INF/spring/**/security-context.xml"})
public class SecurityMemberTests {

	@Autowired
	IMemberService service;
	
//	@Test
//	public void testInsertMember() throws Exception {
//		MemberDTO mDto = new MemberDTO();
//		mDto.setM_id("test5");
//		mDto.setM_pwd("1234");
//		mDto.setM_name("테스트");
//		mDto.setM_intro_short("테스트5 소개~`~");
//		
//		service.insertMember(mDto);
//	}
	
//	@Test
//	public void testRead() throws Exception {
//		service.read("test5");
//	}
	
	@Test
	public void testModifyPwd() throws Exception {
		MemberDTO mDto = new MemberDTO();
		mDto.setM_id("test5");
		mDto.setM_pwd("1234");
		
		service.modifyPwd(mDto);
	}

}


