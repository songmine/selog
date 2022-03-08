package io.song.selog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//스프링 bean 컨테이너가 관리하는 bean을 테스트하려면 @RunWith와 @ContextConfiguration 어노테이션을 사용
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml",
					   "file:src/main/webapp/WEB-INF/spring/security-context.xml"})
public class PasswordEncoderTest {

	private static final Logger logger = LoggerFactory.getLogger(PasswordEncoderTest.class);
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/* passwordEncode의 encode메소드를 이용해 문자열 "1234"를 인코딩한 후 출력(원래는 assert 메서드로 검사하는 것이 정석)
     * 실행할 때마다 다른 결과가 나온다. 또한, 인코딩된 문자열을 원래 문자열로 바꿀수 없다.(단방향 암호화)
     */
	@Test
    public void passwordEncode() throws Exception{
        logger.info("passwordEncode Test : " +passwordEncoder.encode("1234"));
    }
	
	@Test
    public void passwordTest() throws Exception{
		 String encodePasswd = "$2a$10$xgWp2kXNabPQys6CBRShwOmz7f4/u6Gxf38XJkcGe/HHJak7t.Akm";
    	 String password = "1234";
    	
    	 // 결과가 true이면 encodePasswd는 password가 인코딩된 문자열이라는 뜻
    	 // * Spring security는 내부적으로 matches() 메서드를 이용해서 검증을 수행
    	 boolean test = passwordEncoder.matches(password, encodePasswd);
    	 logger.info("test : " + test);
    } 

}
