package io.song.selog.member;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.song.selog.common.security.domain.UserDetailsDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.service.IMemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private IMemberService mService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/register")
	public void registerGet() throws Exception {
		logger.info("register Get.................................");
	}
	
	@PostMapping("/id_check")
	public void id_check(String m_id, HttpServletResponse response) throws Exception {
		logger.info("id_check.................................");
		
		int message = 0;
		
		m_id = m_id.trim();
		message = mService.selIdCheck(m_id);
		
		JSONObject jObj = new JSONObject();
		jObj.put("valName", message);
		jObj.put("m_id", m_id);
		
		response.setContentType("application/x-json; charset=UTF-8");
		response.getWriter().print(jObj);
	}
	
	@PostMapping("/register")
	public void registerPost(MemberDTO mDto, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("register Post.................................");
		
		int result = mService.insertMember(mDto);
		
		request.setAttribute("result", result);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		if (result > 0) {
			pw.print("<script>" + " alert('회원 가입이 완료되었습니다!');" + " location.href='" + "/';" + "</script>");
		} else {
			pw.print("<script>" + " alert('회원 가입에 실패했습니다');" + " history.go(-1);" + "</script>");
		}

	}
	
	@PreAuthorize("principal.username == #m_id")
	@PostMapping("/delete_member")
	public String delete_member(@RequestParam("m_id")String m_id, RedirectAttributes rttr) throws Exception {
		logger.info("delete_member.................................");
		
		boolean result = mService.deleteMember(m_id);
		
		if (result) {
			SecurityContextHolder.clearContext();
			rttr.addFlashAttribute("result", "회원 탈퇴 되셨습니다.");
		} else {
			rttr.addFlashAttribute("result", "회원 탈퇴에 실패했습니다.");
			return "redirect:/setting";
		}
		return "redirect:/";
	}
	
	@PostMapping("/setting/changeMyInfo")
	@ResponseBody
	 public Map<String,Object> changeMyInfo(@RequestParam Map<String,Object> paramMap) throws Exception{
        logger.info("change MyInfo.................................");
        
        Map<String,Object> resultMap = new HashMap<>();
        int resultCode = 0;
        
        resultCode = mService.modifyMyInfo(paramMap);
        
        resultMap.put("resultCode", resultCode);	// resultCode == 1? 수정성공 : 실패
        
        return resultMap;
    } 
	
	@PostMapping("/setting/pwd_check")
	@ResponseBody
	public Map<String, Object> pwd_check(@RequestParam Map<String, Object> paramMap) throws Exception {
		logger.info("pwd_check.................................");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		String param_pwd = (String) paramMap.get("m_pwd"); 
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() != "anonymousUser") {

			UserDetailsDTO member = (UserDetailsDTO)authentication.getPrincipal();
			String m_pwd = member.getPassword();
			
			boolean match = passwordEncoder.matches(param_pwd, m_pwd);
			resultMap.put("match", match); 
		}
		
		return resultMap;
	}
	
	@PostMapping("/setting/modify_password")
	public String modify_password(@RequestParam("m_pwd")String m_pwd, RedirectAttributes rttr) throws Exception {
		logger.info("modify_password.................................");
		
		MemberDTO mDto = new MemberDTO();
		int result = 0;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal() != "anonymousUser") {

			UserDetailsDTO member = (UserDetailsDTO)authentication.getPrincipal();
			String m_id = member.getUsername();
			
			mDto.setM_id(m_id);
			mDto.setM_pwd(m_pwd);
			
			result = mService.modifyPwd(mDto);
		}
		
		if (result > 0) {
			SecurityContextHolder.clearContext();
			rttr.addFlashAttribute("result", "비밀번호가 변경되었습니다. 다시 로그인 해주세요.");
		} else {
			rttr.addFlashAttribute("result", "비밀번호 변경에 실패하였습니다.");
			return "redirect:/setting";
		}
		
		return "redirect:/";
	}

	
}
