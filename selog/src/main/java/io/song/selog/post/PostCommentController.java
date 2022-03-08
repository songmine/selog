package io.song.selog.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.song.selog.post.domain.PostCommentDTO;
import io.song.selog.post.domain.PostCommentPageDTO;
import io.song.selog.post.service.IPostCommentService;
import lombok.AllArgsConstructor;

@RestController					
@RequestMapping("/comment")
@AllArgsConstructor
public class PostCommentController {
		
	private IPostCommentService pcService;
	
	private static final Logger logger = LoggerFactory.getLogger(PostCommentController.class);
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/insert",
				 consumes = "application/json",
				 produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> insert(@RequestBody PostCommentDTO pcDto) throws Exception {
		logger.info("Comment insert.................................");
		int insertCount = pcService.insert(pcDto);
		return insertCount == 1 ? new ResponseEntity<String>("success" , HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(value = "/{p_id}",
			    produces = {MediaType.APPLICATION_XML_VALUE,
	                     	MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<PostCommentPageDTO> getList(@PathVariable("p_id") int p_id) throws Exception {
		logger.info("Comment getList.................................");
		return new ResponseEntity<>(pcService.getList(p_id), HttpStatus.OK);
	}         
	
	@PreAuthorize("principal.username == #pcDto.m_id")
	@DeleteMapping(value = "/{c_id}",
				   produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@RequestBody PostCommentDTO pcDto, @PathVariable("c_id")int c_id) throws Exception {
		logger.info("Comment remove.................................");
		return pcService.remove(c_id) == 1 ? new ResponseEntity<String>("success", HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PreAuthorize("principal.username == #pcDto.m_id")
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH},
				    value = "/{c_id}",
				    consumes = "application/json",
				    produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(@RequestBody PostCommentDTO pcDto, @PathVariable("c_id")int c_id) throws Exception {
		logger.info("Comment modify.................................");
		pcDto.setC_id(c_id);
		return pcService.modify(pcDto) == 1 ? new ResponseEntity<String>("success", HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
