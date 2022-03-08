package io.song.selog.post.mapper;

import java.util.List;

import io.song.selog.post.domain.PostCommentDTO;

public interface PostCommentMapper {

	public int insert(PostCommentDTO pcDto);

	public List<PostCommentDTO> getList(int p_id);
	public int getCommentCntByP_Id(int p_id);			//게시글에 대한 총 댓글 수
	
	public int modify(PostCommentDTO pcDto);
	
	public int delete(int c_id);
	public PostCommentDTO read(int c_id);
	
	public void deleteAllComment(int p_id);

	public List<PostCommentDTO> myCommentList(String m_id);
	
}
