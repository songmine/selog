package io.song.selog.post.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.song.selog.member.domain.MemberDTO;
import io.song.selog.post.domain.PostAttachDTO;
import io.song.selog.post.domain.PostDTO;

public interface IPostService {

	public int insertPost(PostDTO pDto) throws Exception;
	
	public List<PostDTO> trendingList(Map<String, Object> paramMap) throws Exception;
	public List<PostDTO> recentList(Map<String, Object> paramMap) throws Exception;
	
	public PostDTO selPostView(int p_id) throws Exception;
	
	public List<PostDTO> selSearchList(Map<String, Object> paramMap) throws Exception;
	public int selSearchListCnt(String keyword) throws Exception;
	
	public MemberDTO selAuthorInfo(String m_id) throws Exception;
	public boolean deletePost(int p_id) throws Exception;
	
	public boolean updateEditPost(PostDTO pDto) throws Exception;
	
	public Map<String, Object> selLikeYesNo(Map<String, Object> likeParam) throws Exception;
	
	public void clickLike(Map<String, Object> paramMap) throws Exception;
	public void clickLikeCancle(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> selLikeCnt(Map<String, Object> paramMap) throws Exception;

	public PostDTO selPrePost(PostDTO postView) throws Exception;
	public PostDTO selNextPost(PostDTO postView) throws Exception;

	public int selTrendListCnt(Map<String, Object> paramMapTrend) throws Exception;
	public int selRecentListCnt(Map<String, Object> paramMapRecent) throws Exception;

	public List<PostAttachDTO> selAttachList(int p_id) throws Exception;
	
}
