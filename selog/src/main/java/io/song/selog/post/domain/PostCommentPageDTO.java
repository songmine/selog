package io.song.selog.post.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PostCommentPageDTO {

	private int commentCnt; 		 			// 해당 게시글의 총 댓글 수를 저장할 필드
    private List<PostCommentDTO> list;
	
}
