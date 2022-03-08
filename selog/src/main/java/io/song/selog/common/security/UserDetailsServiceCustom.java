package io.song.selog.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.song.selog.common.security.domain.UserDetailsDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.mapper.MemberMapper;
import lombok.extern.log4j.Log4j;

@Log4j
public class UserDetailsServiceCustom implements UserDetailsService{
	// UserDetailsService : Spring Security에서 '로그인할 때' 전달된 정보를 기반으로 'DB에서 유저를 가져오는' 책임을 가지는 인터페이스
	/* JDBC 이용하는 방식 ==> 편리하지만 사용자의 여러정보중 username만 이용하는 단점.
	 * 				          이를 해결하기 위해 직접 UserDetailsService를 구현.
	 * 				          UserDetailsService를 이용하면, '원하는 객체'를 인증과 권한체크에 활용할 수 있다
	 */
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	// UserDetails : 일반 서비스의 사용자 객체를 -> Spring Security에서 사용하는 사용자 객체와 '호환'해주는 어댑터
	public UserDetails loadUserByUsername(String inputMId) throws UsernameNotFoundException {
		log.warn("Load user by INPUT m_id : " + inputMId);
		
		// 최종적으로 리턴해야할 객체
		UserDetailsDTO userDetails = new UserDetailsDTO();
		
		// 사용자 정보 select
		// inputMId == (member 테이블)m_id
		MemberDTO memberDTO = memberMapper.read(inputMId);
		log.warn("Queried by memberMapper(interface) : " + memberDTO);
		
		if (memberDTO == null) { 		// 사용자 정보 없으면 null 처리
			return null;
		} else {						// 사용자 정보 있을 경우 로직 전개 (userDetails에 데이터 넣기)
			userDetails.setUsername(memberDTO.getM_id());
			userDetails.setPassword(memberDTO.getM_pwd());
			// 사용자 권한 select해서 받아온 List<String> 객체 주입
//			userDetails.setAuthorities(memberMapper.selectUserAuthOne(inputMId));
		}
		return userDetails;
	}

}
