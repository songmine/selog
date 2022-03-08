package io.song.selog.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import io.song.selog.common.security.domain.JSONResultDTO;
import io.song.selog.common.security.domain.UserDetailsDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.mapper.MemberMapper;

/**
 * 로그인 성공시 핸들러
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);
	
	// RequestCache : 로그인 화면을 보여주기 전에 사용자 요청을 저장하고 이를 꺼내오는 메카니즘을 정의하는 인터페이스
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
    @Autowired
    MemberMapper membermapper;
    
    /**
	 * 로그인이 성공하고나서 로직
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		/*
		 * Authentication 객체 : Spring Security에서 한 유저의 '인증 정보'를 가지고 있는 객체.
		 *						 사용자가 인증 과정을 '성공적'으로 마치면, 
		 *						 Spring Security는 사용자의 정보 및 인증성공여부를 가지고 --> Authentication 객체를 생성한 후 보관한다.
		 */
		logger.warn("Login Success!");

		/**
		 * SavedRequest: 사용자의 요청은 SavedRequest 인터페이스를 구현한 클래스 단위로 저장
		 * 				즉 사용자가 요청했던 request 파라미터 값들, 그 당시의 헤더값들 등이 'SavedRequest 인터페이스를 구현한 클래스'에 담겨지게 되는 것
		 * 				RequestCache 인터페이스를 구현한 클래스는 이 'DefaultSavedRequest 클래스' 객체를 저장하게 되는것
		 */
		SavedRequest savedRequest = requestCache.getRequest(request, response);

        clearAuthenticationAttributes(request);
        if (savedRequest != null) {
            requestCache.removeRequest(request, response);
           // clearAuthenticationAttributes(request);
        }

        String accept = request.getHeader("accept");

        UserDetailsDTO securityUser = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            if (principal != null && principal instanceof UserDetails) {
                securityUser = (UserDetailsDTO) principal;
            }
        }

        // 일반 응답일 경우
        if (accept == null || accept.matches(".*application/json.*") == false) {
            request.getSession(true).setAttribute("loginNow", true);
//            redirectStrategy.sendRedirect(request, response, "/");  // 메인으로 돌아가!
            
            // 이전페이지로 돌아가기 위해서는 인증페이지로 가기 전 URL을 기억해 놓았다가  
            String referrer = request.getHeader("Referer");
            request.getSession().setAttribute("prevPage", referrer);
            redirectStrategy.sendRedirect(request, response, (String)request.getSession().getAttribute("prevPage"));
            return;
        }

        // application/json(ajax) 요청일 경우 아래의 처리!
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        JSONResultDTO jsonResultDto = JSONResultDTO.success(securityUser); 
        if (jsonConverter.canWrite(jsonResultDto.getClass(), jsonMimeType)) {
            jsonConverter.write(jsonResultDto, jsonMimeType, new ServletServerHttpResponse(response));
        }
        
        HttpSession session = request.getSession();
        MemberDTO mDto = new MemberDTO();
        
        String m_id = securityUser.getUsername();
        mDto = membermapper.getMyInfo(m_id);
        session.setAttribute("loginUser", mDto);
        
	}
	
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
	//  로그인이 성공했을 때 세션에 있는 에러 제거
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
		
}
