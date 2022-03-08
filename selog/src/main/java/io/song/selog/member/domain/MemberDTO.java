package io.song.selog.member.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDTO implements Serializable {

	private String m_id;
	private String m_pwd;
	private String m_name;
	private String m_intro_short;
	private String m_pic;
	private String m_registered_at;

	private boolean enabled;
	private List<AuthDTO> authList;		//사용자는 여러 개의 권한을 가질 수 있다

}
