package io.song.selog.post.mapper;

import java.util.List;

import io.song.selog.post.domain.PostAttachDTO;

public interface PostAttachMapper {

	public void insert(PostAttachDTO boardAttachDto);
	public void delete(String uuid);
	public List<PostAttachDTO> findByP_Id(int p_id);
	
	public void deleteAll(int p_id);
	
	public List<PostAttachDTO> getOldFiles();		// 어제 등록된 모든 파일 목록 
	
}
