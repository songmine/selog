package io.song.selog.post.persistence.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.song.selog.member.domain.MemberDTO;
import io.song.selog.post.domain.PostDTO;
import io.song.selog.post.persistence.IPostDAO;
import lombok.extern.log4j.Log4j;

@Repository
public class PostDAOImpl implements IPostDAO {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int insertPost(PostDTO pDto) throws Exception {
		return sqlSession.insert("PostMapper.insertPost", pDto);
	}

	@Override
	public List<PostDTO> trendingList(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectList("PostMapper.trendingList", paramMap);
	}

	@Override
	public List<PostDTO> recentList(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectList("PostMapper.recentList", paramMap);
	}

	@Override
	public int viewCnt(int p_id) throws Exception {
		return sqlSession.update("PostMapper.viewCnt", p_id);
	}

	@Override
	public PostDTO selPostView(int p_id) throws Exception {
		return sqlSession.selectOne("PostMapper.selPostView", p_id);
	}

	@Override
	public List<PostDTO> selSearchList(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectList("PostMapper.selSearchList", paramMap);
	}

	@Override
	public int selSearchListCnt(String keyword) throws Exception {
		return sqlSession.selectOne("PostMapper.selSearchListCnt", keyword);
	}

	@Override
	public MemberDTO selAuthorInfo(String m_id) throws Exception {
		return sqlSession.selectOne("PostMapper.selAuthorInfo", m_id);
	}

	@Override
	public int deletePost(int p_id) throws Exception {
		return sqlSession.delete("PostMapper.deletePost", p_id);
	}

	@Override
	public int updateEditPost(PostDTO pDto) throws Exception {
		return sqlSession.update("PostMapper.updateEditPost", pDto);
	}

	@Override
	public Map<String, Object> selLikeYesNo(Map<String, Object> likeParam) throws Exception {
		return sqlSession.selectOne("PostMapper.selLikeYesNo", likeParam);
	}

	@Override
	public int insertLikeBtn(Map<String, Object> paramMap) throws Exception {
		return sqlSession.insert("PostMapper.insertLikeBtn", paramMap);
	}

	@Override
	public int updateLikeCntPlus(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update("PostMapper.updateLikeCntPlus", paramMap);
	}

	@Override
	public int deleteLikeBtn(Map<String, Object> paramMap) throws Exception {
		return sqlSession.delete("PostMapper.deleteLikeBtn", paramMap);
	}

	@Override
	public int updateLikeCntMinus(Map<String, Object> paramMap) throws Exception {
		return sqlSession.update("PostMapper.updateLikeCntMinus", paramMap);
	}

	@Override
	public Map<String, Object> selLikeCnt(Map<String, Object> paramMap) throws Exception {
		return sqlSession.selectOne("PostMapper.selLikeCnt", paramMap);
	}

	@Override
	public PostDTO selPrePost(PostDTO postView) throws Exception {
		return sqlSession.selectOne("PostMapper.selPrePost", postView);
	}

	@Override
	public PostDTO selNextPost(PostDTO postView) throws Exception {
		return sqlSession.selectOne("PostMapper.selNextPost", postView);
	}

	@Override
	public void updateCommentCnt(Integer p_id, int amount) throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("p_id", p_id);
		paramMap.put("amount", amount);
		
		sqlSession.update("PostMapper.updateCommentCnt", paramMap);
	}

	@Override
	public int selTrendListCnt(Map<String, Object> paramMapTrend) throws Exception {
		return sqlSession.selectOne("PostMapper.selTrendListCnt", paramMapTrend);
	}

	@Override
	public int selRecentListCnt(Map<String, Object> paramMapRecent) throws Exception {
		return sqlSession.selectOne("PostMapper.selRecentListCnt", paramMapRecent);
	}

	
}
