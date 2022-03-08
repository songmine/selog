package io.song.selog.post.domain;

import lombok.Data;

@Data
public class PostAttachDTO {
	
	private String uuid;		// UUID 값 (Primary Key)
	private String uploadPath;	// 업로드 경로
	private String fileName;	// 원본파일 이름
	private boolean fileType;	// 이미지 파일 여부 판단
	private int p_id;			// 글 번호 (Foreign Key)
	
}
