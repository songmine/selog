package io.song.selog.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

// controller에서 예외발생 시, 이 클래스와 결합하여, 처리하게 하는 annotation
@ControllerAdvice
public class CommonExceptionAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);
//	
//	// @ControllerAdvice가 Exception 감지하면, 이 메서드 동작
//	@ExceptionHandler(Exception.class)
//	public String except(Exception ex, Model model) {
//		logger.error("Exception.............................{}", ex);
//		model.addAttribute("exception", ex);
//		logger.error("model.................................{}", model);
//		return "/error/error_page";
//	}
	
	// @ControllerAdvice가 NoHandlerFoundException 감지하면, 이 메서드 동작
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handler404(NoHandlerFoundException ex) {
		logger.info("NoHandlerFoundException.....");
		return "/error/custom404";
	}
	
	@ExceptionHandler(Exception.class)
	public String handler500(Exception e) {
		// 예외가 발생하게 되면 해당 예외 필드가 메모리에 할당된다.
		// 할당된 예외 필드의 주소 값을 받을 객체가 필요하므로 매개변수에 Exception타입의
		// e 객체를 선언해놓는다.
		logger.error("Exception........" + e.getMessage());
		return "/error/custom500";
	}

}