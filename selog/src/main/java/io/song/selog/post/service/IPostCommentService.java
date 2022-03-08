package io.song.selog.post.service;

import io.song.selog.post.domain.PostCommentDTO;
import io.song.selog.post.domain.PostCommentPageDTO;

public interface IPostCommentService {

	public int insert(PostCommentDTO pcDto) throws Exception;
	public PostCommentPageDTO getList(int p_id) throws Exception;	// List<PostCommentDTO> + CommentCnt = PostCommentPageDTO
	public int remove(int c_id) throws Exception;
	public int modify(PostCommentDTO pcDto) throws Exception;

	
}
