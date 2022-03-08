package io.song.selog.common.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JSONResultDTO {
	
	private String result; 	 // success, fail
	private String message;
	private Object data;
	
	// 성공 시, 데이터(data) 전송
	public static JSONResultDTO success(Object data) {
	    return new JSONResultDTO("success", null, data);
	}
	
	public static JSONResultDTO success(Object data, String value) {
	    return new JSONResultDTO("success", value, data);
	}
	
	// 실패 시, 메세지(message) 전송
	public static JSONResultDTO fail(String message) {
	    return new JSONResultDTO("fail", message, null);
	}
}
