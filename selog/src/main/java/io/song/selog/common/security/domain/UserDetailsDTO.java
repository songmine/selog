package io.song.selog.common.security.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/* Spring Security 로그인을 위한 UserDetails DTO 객체 */
// UserDetails : 일반 서비스의 사용자 객체를 
//		-> Spring Security에서 사용하는 사용자 객체와 '호환'해주는 어댑터
public class UserDetailsDTO implements UserDetails {

	// 안만들어도 상관없지만 Warning이 발생함
	private static final long serialVersionUID = 1L;
	
	private String m_id; 			// ID
	private String m_pwd; 			// PW
	private List<GrantedAuthority> authorities;		// 계정이 가진 권한 목록
	
	/* 나중에 객체의 데이터를 완성시켜줘야 하므로 setter를 추가 */
	// setter
	public void setUsername(String username) {
		this.m_id = username;
	}

	// setter
	public void setPassword(String password) {
		this.m_pwd = password;
	}

	// setter
	public void setAuthorities(List<String> authList) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (int i = 0; i < authList.size(); i++) {
			authorities.add(new SimpleGrantedAuthority(authList.get(i)));
		}
		
		this.authorities = authorities;
	}

	// ID
	@Override
	public String getUsername() {
		return m_id;
	}
	
	// PWD
	@Override
	public String getPassword() {
		return m_pwd;
	}
	
	// 권한
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	// 계정 만료 안됐는지?
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 안잠겼는지?
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 패스워드 만료됐는지?
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 됐는지?
	@Override
	public boolean isEnabled() {
		return true;
	}

}
