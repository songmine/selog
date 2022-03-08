package io.song.selog.post.persistence;

import java.util.List;
import java.util.Map;

import io.song.selog.member.domain.MemberDTO;
import io.song.selog.post.domain.PostDTO;

public interface IPostDAO {

	public int insertPost(PostDTO pDto) throws Exception;
	
	public List<PostDTO> trendingList(Map<String, Object> paramMap) throws Exception;
	public List<PostDTO> recentList(Map<String, Object> paramMap) throws Exception;
	
	public int viewCnt(int p_id) throws Exception;
	public PostDTO selPostView(int p_id) throws Exception;
	
	public List<PostDTO> selSearchList(Map<String, Object> paramMap) throws Exception;
	public int selSearchListCnt(String keyword) throws Exception;
	
	public MemberDTO selAuthorInfo(String m_id) throws Exception;
	public int deletePost(int p_id) throws Exception;
	
	public int updateEditPost(PostDTO pDto) throws Exception;

	public Map<String, Object> selLikeYesNo(Map<String, Object> likeParam) throws Exception;

	public int insertLikeBtn(Map<String, Object> paramMap) throws Exception;
	public int updateLikeCntPlus(Map<String, Object> paramMap) throws Exception;

	public int deleteLikeBtn(Map<String, Object> paramMap) throws Exception;
	public int updateLikeCntMinus(Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> selLikeCnt(Map<String, Object> paramMap) throws Exception;

	public PostDTO selPrePost(PostDTO postView) throws Exception;
	public PostDTO selNextPost(PostDTO postView) throws Exception;

	public void updateCommentCnt(Integer p_id, int amount) throws Exception;

	public int selTrendListCnt(Map<String, Object> paramMapTrend) throws Exception;
	public int selRecentListCnt(Map<String, Object> paramMapRecent) throws Exception;
	
}
