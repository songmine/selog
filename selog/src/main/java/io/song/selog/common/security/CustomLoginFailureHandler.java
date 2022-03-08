package io.song.selog.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.song.selog.common.security.domain.JSONResultDTO;
import lombok.extern.log4j.Log4j;

/**
 * 로그인 실패시 핸들러
 *
 */
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
	
	static final Logger logger = LoggerFactory.getLogger(CustomLoginFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		logger.info("실패시 핸들러");

		ObjectMapper mapper = new ObjectMapper();	//JSON 변경용
		
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;
		
		JSONResultDTO jsonResultDto = new JSONResultDTO();
		String message = getExceptionMessage(exception);
		
		logger.info("실패 메세지 >> " + message);
        
        jsonResultDto = JSONResultDTO.fail(message);

        if (jsonConverter.canWrite(jsonResultDto.getClass(), jsonMimeType)) {
        	logger.info("jsonConverter.canWrite(jsonResultDto.getClass(), jsonMimeType ##################");

            jsonConverter.write(jsonResultDto, jsonMimeType, new ServletServerHttpResponse(response));
        }
        logger.info("실패 반환 >>> " + jsonResultDto);

	}
	
	private String getExceptionMessage(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "비밀번호 불일치";
        } else if (exception instanceof UsernameNotFoundException) {
            return "존재하지 않는 계정";
        } else if (exception instanceof AccountExpiredException) {
            return "만료된 계정";
        } else if (exception instanceof CredentialsExpiredException) {
            return "만료된 비밀번호";
        } else if (exception instanceof DisabledException) {
            return "비활성화된 계정";
        } else if (exception instanceof LockedException) {
            return "잠겨있는 계정";
        } else {	// 미확인 에러
            return "아이디 혹은 비밀번호 불일치";
        }
    }

}
