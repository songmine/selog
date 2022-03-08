package io.song.selog.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.song.selog.common.security.domain.UserDetailsDTO;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.service.IMemberService;
import io.song.selog.post.domain.PostAttachDTO;
import io.song.selog.post.domain.PostDTO;
import io.song.selog.post.service.IPostService;

@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private IPostService pService;
	
	@Autowired
	private IMemberService mService;
	
	@GetMapping("/")
	public String indexGet(String pageNo, Model model) throws Exception {
		logger.info("index(Get).................................");

		final int POST_COUNT_PER_PAGE = 15;	// 한 페이지에 몇 개씩 표시할지
		int pageNum = 1;				// 보여줄 페이지 번호
		
		String strPageNum = pageNo;
		
		if (strPageNum != null) {								// 페이지 번호가 넘어온다면,
			pageNum = Integer.parseInt(strPageNum);				// (숫자로 바꿔서)보여줄 페이지 번호로 지정
		}
		
		int start_p_id_num = 0 + (pageNum -1) * POST_COUNT_PER_PAGE;	// 보여줄 페이지의 시작 ROWNUM - 0 부터 시작
		int end_p_id_num = pageNum * POST_COUNT_PER_PAGE;				// 보여줄 페이지의 끝
		
		PostDTO pDto = new PostDTO();
		pDto.setStart_p_id_num(start_p_id_num);
		pDto.setEnd_p_id_num(end_p_id_num);
		
		Map<String, Object> paramMapTrend = new HashMap<>();
		Map<String, Object> paramMapRecent = new HashMap<>();
		paramMapTrend.put("duration", "week");
		paramMapTrend.put("pageNo", pageNum);
		paramMapTrend.put("pagePerCnt", POST_COUNT_PER_PAGE);
		
		paramMapRecent.put("pageNo", pageNum);
		paramMapRecent.put("pagePerCnt", POST_COUNT_PER_PAGE);
		
		int trendListCnt = 0;					// 트렌드 글 갯수 (전체 row의 개수를 담을 지역변수)
		int recentListCnt = 0;					// 최신 글 갯수 (전체 row의 개수를 담을 지역변수)
		int trendTotalPageCnt = 0;				// 트렌트 전체 페이지 갯수
		int recentTotalPageCnt = 0;				// 최신 전체 페이지 갯수
		
		List<PostDTO> trendingList = pService.trendingList(paramMapTrend);
		List<PostDTO> recentList = pService.recentList(paramMapRecent);
		List<PostAttachDTO> attach = new ArrayList<PostAttachDTO>();
		trendListCnt = pService.selTrendListCnt(paramMapTrend);
		recentListCnt = pService.selRecentListCnt(paramMapRecent);
		
		trendingList.forEach(trend -> {
			trend.setCustomDate(trend.getP_published_at());
		});
		
		recentList.forEach(recent -> {
			recent.setCustomDate(recent.getP_published_at());
		});
		
		trendTotalPageCnt  = (int)Math.ceil(trendListCnt  / (double) POST_COUNT_PER_PAGE);
		recentTotalPageCnt = (int)Math.ceil(recentListCnt / (double) POST_COUNT_PER_PAGE);
		
		model.addAttribute("trendingList", trendingList);
		model.addAttribute("recentList", recentList);
		model.addAttribute("d", "week");
		model.addAttribute("trendTotalPageCnt", trendTotalPageCnt);
		model.addAttribute("recentTotalPageCnt", recentTotalPageCnt);
		model.addAttribute("pageNum", pageNum);
		
		return "common/index";
	}
	
	@PostMapping("/")
	public String indexPost(String duration, String pageNo, Model model) throws Exception {
		logger.info("index(Post).................................");
		
		final int POST_COUNT_PER_PAGE = 15;	// 한 페이지에 몇 개씩 표시할지
		int pageNum = 1;				// 보여줄 페이지 번호
		
		String strPageNum = pageNo;
		
		if (strPageNum != null) {								// 페이지 번호가 넘어온다면,
			pageNum = Integer.parseInt(strPageNum);				// (숫자로 바꿔서)보여줄 페이지 번호로 지정
		}
		
		int start_p_id_num = 0 + (pageNum -1) * POST_COUNT_PER_PAGE;	// 보여줄 페이지의 시작 ROWNUM - 0 부터 시작
		int end_p_id_num = pageNum * POST_COUNT_PER_PAGE;				// 보여줄 페이지의 끝
		
		PostDTO pDto = new PostDTO();
		pDto.setStart_p_id_num(start_p_id_num);
		pDto.setEnd_p_id_num(end_p_id_num);
		
		Map<String, Object> paramMapTrend = new HashMap<>();
		Map<String, Object> paramMapRecent = new HashMap<>();
		paramMapTrend.put("duration", duration);
		paramMapTrend.put("pageNo", pageNum);
		paramMapTrend.put("pagePerCnt", POST_COUNT_PER_PAGE);
		
		paramMapRecent.put("pageNo", pageNum);
		paramMapRecent.put("pagePerCnt", POST_COUNT_PER_PAGE);
	
		int trendListCnt = 0;					// 트렌드 글 갯수 (전체 row의 개수를 담을 지역변수)
		int recentListCnt = 0;					// 최신 글 갯수 (전체 row의 개수를 담을 지역변수)
		int trendTotalPageCnt = 0;				// 트렌트 전체 페이지 갯수
		int recentTotalPageCnt = 0;				// 최신 전체 페이지 갯수
		
		List<PostDTO> trendingList = pService.trendingList(paramMapTrend);
		List<PostDTO> recentList = pService.recentList(paramMapRecent);
		trendListCnt = pService.selTrendListCnt(paramMapTrend);
		recentListCnt = pService.selRecentListCnt(paramMapRecent);
		
		trendingList.forEach(trend -> {
			trend.setCustomDate(trend.getP_published_at());
		});
		
		recentList.forEach(recent -> {
			recent.setCustomDate(recent.getP_published_at());
		});
		
		trendTotalPageCnt  = (int)Math.ceil(trendListCnt  / (double) POST_COUNT_PER_PAGE);
		recentTotalPageCnt = (int)Math.ceil(recentListCnt / (double) POST_COUNT_PER_PAGE);
		
		model.addAttribute("trendingList", trendingList);
		model.addAttribute("recentList", recentList);
		model.addAttribute("d", duration);
		model.addAttribute("trendTotalPageCnt", trendTotalPageCnt);
		model.addAttribute("recentTotalPageCnt", recentTotalPageCnt);
		model.addAttribute("pageNum", pageNum);
		
		return "common/index";
	}
	
	@PostMapping("/index_page")
	public String index_page(String duration, String pageNo, Model model) throws Exception {
		logger.info("index_page *pagenation*.................................");
		logger.info("페이징duration >>>>> " + duration);
		final int POST_COUNT_PER_PAGE = 15;	// 한 페이지에 몇 개씩 표시할지
		int pageNum = 1;				// 보여줄 페이지 번호
		
		String strPageNum = pageNo;
		
		if (strPageNum != null) {								// 페이지 번호가 넘어온다면,
			pageNum = Integer.parseInt(strPageNum);				// (숫자로 바꿔서)보여줄 페이지 번호로 지정
		}
		
		int start_p_id_num = 0 + (pageNum -1) * POST_COUNT_PER_PAGE;	// 보여줄 페이지의 시작 ROWNUM - 0 부터 시작
		int end_p_id_num = pageNum * POST_COUNT_PER_PAGE;				// 보여줄 페이지의 끝
		
		PostDTO pDto = new PostDTO();
		pDto.setStart_p_id_num(start_p_id_num);
		pDto.setEnd_p_id_num(end_p_id_num);
		
		Map<String, Object> paramMapTrend = new HashMap<>();
		Map<String, Object> paramMapRecent = new HashMap<>();
		paramMapTrend.put("duration", duration);
		paramMapTrend.put("pageNo", pageNum);
		paramMapTrend.put("pagePerCnt", POST_COUNT_PER_PAGE);
		
		paramMapRecent.put("pageNo", pageNum);
		paramMapRecent.put("pagePerCnt", POST_COUNT_PER_PAGE);
		
		int trendListCnt = 0;					// 트렌드 글 갯수 (전체 row의 개수를 담을 지역변수)
		int recentListCnt = 0;					// 최신 글 갯수 (전체 row의 개수를 담을 지역변수)
		int trendTotalPageCnt = 0;				// 트렌트 전체 페이지 갯수
		int recentTotalPageCnt = 0;				// 최신 전체 페이지 갯수
		
		List<PostDTO> trendingList = pService.trendingList(paramMapTrend);
		List<PostDTO> recentList = pService.recentList(paramMapRecent);
		trendListCnt = pService.selTrendListCnt(paramMapTrend);
		recentListCnt = pService.selRecentListCnt(paramMapRecent);
		
		trendingList.forEach(trend -> {
			trend.setCustomDate(trend.getP_published_at());
		});
		
		recentList.forEach(recent -> {
			recent.setCustomDate(recent.getP_published_at());
		});
		
		trendTotalPageCnt  = (int)Math.ceil(trendListCnt  / (double) POST_COUNT_PER_PAGE);
		recentTotalPageCnt = (int)Math.ceil(recentListCnt / (double) POST_COUNT_PER_PAGE);
		
		model.addAttribute("trendingList", trendingList);
		model.addAttribute("recentList", recentList);
		model.addAttribute("d", duration);
		model.addAttribute("trendTotalPageCnt", trendTotalPageCnt);
		model.addAttribute("recentTotalPageCnt", recentTotalPageCnt);
		model.addAttribute("pageNum", pageNum);
		
		if (duration == null) {
			return "common/index_trend";
		} else {
			return "common/index_recent";
		}
	}
	
	@GetMapping("/search")
	public String search(String q, String pageNo, Model model) throws Exception {
		logger.info("search.................................");
		
		final int POST_COUNT_PER_PAGE = 10;	// 한 페이지에 몇 개씩 표시할지
		int pageNum = 1;				// 보여줄 페이지 번호
		
		String strPageNum = pageNo;

		if (strPageNum != null) {								// 페이지 번호가 넘어온다면,
			pageNum = Integer.parseInt(strPageNum);				// (숫자로 바꿔서)보여줄 페이지 번호로 지정
		}
		
		int start_p_id_num = 0 + (pageNum -1) * POST_COUNT_PER_PAGE;	// 보여줄 페이지의 시작 ROWNUM - 0 부터 시작
		int end_p_id_num = pageNum * POST_COUNT_PER_PAGE;				// 보여줄 페이지의 끝
		
		PostDTO pDto = new PostDTO();
		pDto.setStart_p_id_num(start_p_id_num);
		pDto.setEnd_p_id_num(end_p_id_num);
		
		Map<String, Object> searchMap = new HashMap<>();
		List<PostDTO> searchList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("keyword", q);
		paramMap.put("pageNo", pageNum);
		paramMap.put("pagePerCnt", POST_COUNT_PER_PAGE);
		int searchListCnt = 0;				// 검색 결과 갯수 (전체 row의 개수를 담을 지역변수)
		int totalPageCnt = 0;				// 전체 페이지 갯수
		
		if (q != null) {
			searchList = pService.selSearchList(paramMap);
			searchListCnt = pService.selSearchListCnt(q);
			
			searchList.forEach(search -> {
				search.setCustomDate(search.getP_published_at());
			});
			
			totalPageCnt = (int)Math.ceil(searchListCnt / (double) POST_COUNT_PER_PAGE);
			
			searchMap.put("keyword", q);
			searchMap.put("searchList", searchList);
			searchMap.put("searchListCnt", searchListCnt);
			searchMap.put("totalPageCnt", totalPageCnt);
			searchMap.put("pageNum", pageNum);
			
			model.addAttribute("search", searchMap);
		}
		return "post/search";
	}
	
	@PostMapping("/search_page")
	public String search_page(String q, String pageNo, Model model) throws Exception {
		logger.info("search *pagination*.................................");
		
		final int POST_COUNT_PER_PAGE = 10;	// 한 페이지에 몇 개씩 표시할지
		int pageNum = 1;				// 보여줄 페이지 번호
		
		String strPageNum = pageNo;

		if (strPageNum != null) {								// 페이지 번호가 넘어온다면,
			pageNum = Integer.parseInt(strPageNum);				// (숫자로 바꿔서)보여줄 페이지 번호로 지정
		}
		
		int start_p_id_num = 0 + (pageNum -1) * POST_COUNT_PER_PAGE;	// 보여줄 페이지의 시작 ROWNUM - 0 부터 시작
		int end_p_id_num = pageNum * POST_COUNT_PER_PAGE;				// 보여줄 페이지의 끝
		
		PostDTO pDto = new PostDTO();
		pDto.setStart_p_id_num(start_p_id_num);
		pDto.setEnd_p_id_num(end_p_id_num);
		
		Map<String, Object> searchMap = new HashMap<>();
		List<PostDTO> searchList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("keyword", q);
		paramMap.put("pageNo", pageNum);
		paramMap.put("pagePerCnt", POST_COUNT_PER_PAGE);
		int searchListCnt = 0;				// 검색 결과 갯수 (전체 row의 개수를 담을 지역변수)
		int totalPageCnt = 0;				// 전체 페이지 갯수
		
		if (q != null) {
			searchList = pService.selSearchList(paramMap);
			searchListCnt = pService.selSearchListCnt(q);
			
			searchList.forEach(search -> {
				search.setCustomDate(search.getP_published_at());
			});
			
			totalPageCnt = (int)Math.ceil(searchListCnt / (double) POST_COUNT_PER_PAGE);
			
			searchMap.put("keyword", q);
			searchMap.put("searchList", searchList);
			searchMap.put("searchListCnt", searchListCnt);
			searchMap.put("totalPageCnt", totalPageCnt);
			searchMap.put("pageNum", pageNum);
			
			model.addAttribute("search", searchMap);
		}
		return "post/search_page";
	}
	
	
	@GetMapping("/setting")
	public String setting(Model model) throws Exception {
		logger.info("setting.................................");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsDTO member = (UserDetailsDTO)authentication.getPrincipal();
		MemberDTO mDto = new MemberDTO();
		
		String m_id = member.getUsername();
		
		mDto = mService.getMyInfo(m_id);
		
		model.addAttribute("myInfo", mDto);
		
		return "member/setting";
	}
	
	@GetMapping("/policy")
	public String policy() throws Exception {
		logger.info("policy.................................");
		return "common/policy";
	}
	
	@GetMapping("/error/custom403")
	public String handleAccessDenied() {
		logger.info("Handle Access Denied...................");
		return "/error/custom403";
	}
	
	@GetMapping("/error/custom404")
	public String handler404() {
		logger.info("Handle 404.............................");
		return "/error/custom404";
	}
	
	@GetMapping("/error/custom405")
	public String handler405() {
		logger.info("Handle 405.............................");
		return "/error/custom405";
	}
	
	/*
	@GetMapping("/sign_in")
	public void sign_in(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response); 
		
		try {
			// 여러가지 이유로 이전페이지 정보가 없는 경우가 있음.
			// https://stackoverflow.com/questions/6880659/in-what-cases-will-http-referer-be-empty
			request.getSession().setAttribute("prevPage", savedRequest.getRedirectUrl());
		} catch(NullPointerException e) {
			request.getSession().setAttribute("prevPage", "/");
		}
	}
	*/
	
}
