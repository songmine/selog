package io.song.selog.post.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.song.selog.post.domain.PostCommentDTO;
import io.song.selog.post.domain.PostCommentPageDTO;
import io.song.selog.post.mapper.PostCommentMapper;
import io.song.selog.post.persistence.IPostDAO;
import io.song.selog.post.service.IPostCommentService;

@Service
public class PostCommentServiceImpl implements IPostCommentService {

	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Autowired
	private PostCommentMapper pcMapper;
	
	@Autowired
	private IPostDAO pDao;
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public int insert(PostCommentDTO pcDto) throws Exception {
		logger.info("PostCommentServiceImpl insert.....................");
		pDao.updateCommentCnt(pcDto.getP_id(), +1);
		return pcMapper.insert(pcDto);
	}

	@Override
	public PostCommentPageDTO getList(int p_id) throws Exception {
		logger.info("PostCommentServiceImpl getList.....................");
		
		List<PostCommentDTO> pcDto = pcMapper.getList(p_id);
		pcDto.forEach(comment -> {
			comment.setCustomDate(comment.getC_published_at());
		});
		
		return new PostCommentPageDTO(pcMapper.getCommentCntByP_Id(p_id), pcDto);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public int remove(int c_id) throws Exception {
		logger.info("PostCommentServiceImpl remove.....................");
		
		PostCommentDTO pcDto = pcMapper.read(c_id);
		pDao.updateCommentCnt(pcDto.getP_id(), -1);

		return pcMapper.delete(c_id);
	}
	
	@Override
	public int modify(PostCommentDTO pcDto) throws Exception {
		logger.info("PostCommentServiceImpl modify.....................");
		return pcMapper.modify(pcDto);
	}

	
}
