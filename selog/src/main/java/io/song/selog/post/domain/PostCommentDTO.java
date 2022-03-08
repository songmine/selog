package io.song.selog.post.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDTO {
	
	private Integer c_id;
	private int p_id;
	private Date c_published_at;
	private Date c_updated_at;
	private String c_content;
	private String m_id;
	
	
	private String customDate;					// 방금 전, n시간 전, n일 전 ...
	
	public String getCustomDate() {
		return customDate;
	}

	public void setCustomDate(Date date) {
		if (this.c_updated_at != null) {
			date = this.c_updated_at;
		}else {
			date = this.c_published_at;
		}
		
		this.customDate = Time.calculateTime(date); 		// 기존의 getter, setter에서 변경된 부분
	}
	
	private String m_pic;	// 카드리스트 회원프로필 표시 위함
	
}
