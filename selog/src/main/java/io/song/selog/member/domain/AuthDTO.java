package io.song.selog.member.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthDTO implements Serializable {
	
	private String m_id;
	private String auth;

}
