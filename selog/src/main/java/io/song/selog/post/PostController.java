package io.song.selog.post;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.song.selog.common.CommonController;
import io.song.selog.common.security.domain.UserDetailsDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.post.domain.PostAttachDTO;
import io.song.selog.post.domain.PostDTO;
import io.song.selog.post.service.IPostService;

@Controller
@RequestMapping("/post")
public class PostController {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	IPostService pService;
	
	@GetMapping("/writing")
	public void writingGet() throws Exception {
		logger.info("writing Get.................................");
	}
	
	@PostMapping("/writing")
	public String writingPost(PostDTO pDto,  RedirectAttributes rttr) throws Exception {
		logger.info("writing Post.................................");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsDTO member = (UserDetailsDTO)authentication.getPrincipal();
		
		String m_id = member.getUsername();
		pDto.setM_id(m_id);
		
		logger.info(pDto.toString());
		
		int result = 0;
		result = pService.insertPost(pDto);
		
		logger.info("gg >>> " + pDto.getAttachList());
		if (pDto.getAttachList() != null) {
			pDto.getAttachList().forEach(attach -> logger.info("" + attach));
		}
		
		if (result > 0) {
			rttr.addFlashAttribute("result", "새글이 추가되었습니다.");
		} else {
			rttr.addFlashAttribute("result", "등록에 실패했습니다.");
			return "redirect:/post/writing";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("")
	public String view(@RequestParam("p_id")String p_id, Model model) throws Exception {
		logger.info("view.................................");
		
		int post_id = Integer.parseInt(p_id);
		Map<String, Object> likeYesNoMap = new HashMap<>();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() != "anonymousUser") {
			Map<String, Object> likeParam = new HashMap<>();

			UserDetailsDTO member = (UserDetailsDTO)authentication.getPrincipal();
			String m_id = member.getUsername();

			likeParam.put("p_id", post_id);
			likeParam.put("m_id", m_id);
			likeYesNoMap = pService.selLikeYesNo(likeParam);
			
			model.addAttribute("likeYesNo", likeYesNoMap.get("LIKECHECK"));
		}
		
		PostDTO postView = new PostDTO();
		MemberDTO authorInfo = new MemberDTO();
		PostDTO prePost = new PostDTO();
		PostDTO nextPost = new PostDTO();
		String author_m_id = "";
		List<PostAttachDTO> attach = new ArrayList<PostAttachDTO>();
		
		postView = pService.selPostView(post_id);
		postView.setCustomDate(postView.getP_published_at());
		
		author_m_id = postView.getM_id();
		authorInfo = pService.selAuthorInfo(author_m_id);
		prePost = pService.selPrePost(postView);
		nextPost = pService.selNextPost(postView);
		attach = pService.selAttachList(post_id);
		
		logger.info(attach.toString());
		
		model.addAttribute("postView", postView);
		model.addAttribute("authorInfo", authorInfo);
		model.addAttribute("prePost", prePost);
		model.addAttribute("nextPost", nextPost);
		model.addAttribute("attach", attach);
		
		return "/post/view";
	}
	
	@GetMapping("/edit_post")
	public String edit_post_get(@RequestParam("p_id")String p_id, @RequestParam("mode")String mode, Model model) throws Exception {
		logger.info("edit_post Get.................................");
		
		int post_id = Integer.parseInt(p_id);
		PostDTO pDto = new PostDTO();
		
		pDto = pService.selPostView(post_id);
		
		model.addAttribute("PostDTO", pDto);
		model.addAttribute("mode", mode);
		
		return "/post/writing";
	}
	
	@PostMapping("/edit_post")
	public void edit_post_post(@RequestParam("p_id")String p_id, PostDTO pDto, HttpServletResponse response) throws Exception {
		logger.info("edit_post Post.................................");
		logger.info(pDto.toString());
//
//		int post_id = Integer.parseInt(p_id);
//		pDto.setP_id(post_id);
//		logger.info(pDto.toString());
		
		// 고치기
		boolean result = false;
		result = pService.updateEditPost(pDto);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		if (result) {
			pw.print("<script>" + " alert('글이 수정 되었습니다.');" + " location.href='" + "/';" + "</script>");
		} else {
			pw.print("<script>" + " alert('글 수정에 실패했습니다');" + " history.go(-1);" + "</script>");
		}
	}
	
	@GetMapping("/delete_post")
	public void delete_post(@RequestParam("p_id")String p_id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("delete_post.................................");
		
		HttpSession session = request.getSession();
		MemberDTO mDto = new MemberDTO();
		int post_id = Integer.parseInt(p_id);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		
		mDto = (MemberDTO)session.getAttribute("loginUser");
		
		if (mDto != null) {
			boolean result = pService.deletePost(post_id);
			
			if (result) {
				pw.print("<script>" + " alert('포스트가 삭제 되었습니다');" + " location.href='" + "/';" + "</script>");
			} else {
				pw.print("<script>" + " alert('포스트 삭제에 실패했습니다');" + " history.go(-1);" + "</script>");
			}
		}
		
	}
	
	@PostMapping("/clickLikeBtn")
	@ResponseBody
    public Map<String,Object> clickLikeBtn(@RequestParam Map<String,Object> paramMap){
        logger.info("click Like Button.................................");

        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> likeYesNoMap = new HashMap<>();

        int resultCode = 1;
        int likeCheck = 1;
        
        try {
        	// 유저가 좋아요 누른글 {LIKECHECK=1}, 안누른 글 {LIKECHECK=0}
            likeYesNoMap = pService.selLikeYesNo(paramMap);
            if (Integer.parseInt(likeYesNoMap.get("LIKECHECK").toString()) == 0) {
				logger.info("좋아요!");
				paramMap.put("likeCheck", likeCheck);
				pService.clickLike(paramMap); // post_like에 데이터 삽입, post테이블 p_like_cnt + 1
				resultCode = 1;
			} else if (Integer.parseInt(likeYesNoMap.get("LIKECHECK").toString()) == 1) {
				logger.info("좋아요 취소!");
				likeCheck = 0;
				paramMap.put("likeCheck", likeCheck);
				pService.clickLikeCancle(paramMap);
				resultCode = 0;
			}
            paramMap.put("likeCheck", likeCheck);
            resultMap = pService.selLikeCnt(paramMap); 
            resultMap.put("likeCheck", likeCheck);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            resultCode = -1;
        }
        
        resultMap.put("resultCode", resultCode);
        
        return resultMap; //resultCode == 1? 빨간하트 : 회색하트(0)
    }
	
	
}
