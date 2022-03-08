package io.song.selog.post.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {

	private Integer p_id;           
	private String  m_id;         
	private String  p_title;        
	private String  p_public_yn;    
	private Date    p_published_at; 
	private Date    p_updated_at;   
	private String  p_content;      
	private int	    p_view_cnt;
	private int	    p_like_cnt;
	private int     p_comment_cnt;
	private String  t_name;
	private String  p_pic;
	
	private List<PostAttachDTO> attachList; 	// 글 등록 시, 한 번에 BoardAttachVO를 처리할 수 있게 함.
	
	private String customDate;					// 방금 전, n시간 전, n일 전 ...
	
	public String getCustomDate() {
		return customDate;
	}

	public void setCustomDate(Date date) {
		date = this.p_published_at;
		this.customDate = Time.calculateTime(date); 		// 기존의 getter, setter에서 변경된 부분
	}
	
	private String m_pic;	// 카드리스트 회원프로필 표시 위함
	
    private int start_p_id_num;			// 시작 게시물 번호
    private int end_p_id_num;			//   끝 게시물 번호
	
}
