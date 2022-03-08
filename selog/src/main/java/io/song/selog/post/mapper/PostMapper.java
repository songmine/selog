package io.song.selog.post.mapper;

import java.util.List;
import java.util.Map;

import io.song.selog.member.domain.MemberDTO;
import io.song.selog.post.domain.PostDTO;

public interface PostMapper {

	public int insertPost(PostDTO pDto);
	public List<PostDTO> trendingList(Map<String, Object> paramMap);
	public List<PostDTO> recentList();
	public int viewCnt(int p_id);
	public PostDTO selPostView(int p_id);
	public List<PostDTO> selSearchList(String keyword);
	public int selSearchListCnt(String keyword);
	public MemberDTO selAuthorInfo(String m_id);
	public int deletePost(int p_id);
	public int updateEditPost(PostDTO pDto);
	 
	
}
