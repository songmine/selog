package io.song.selog.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

// 접근 제한이 된 경우에 다양한 처리를 하고 싶다면 
// 이와 같이 직접 AccessDeniedHandler 인터페이스를 구현하는 것이 좋음.
@Log4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		log.error("Access Denied Handler");
		log.error("Redirect.............");
		
		// 접근 제한이 걸리는 경우, redirect
		response.sendRedirect("/error/custom403");
	}

}
