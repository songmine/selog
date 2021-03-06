package io.song.selog.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import io.song.selog.common.util.MediaUtils;
import io.song.selog.member.domain.MemberDTO;
import io.song.selog.member.service.IMemberService;
import io.song.selog.post.domain.PostAttachDTO;
import io.song.selog.post.domain.PostDTO;
import net.coobird.thumbnailator.Thumbnails;

@Controller
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private IMemberService mService;
	
	@RequestMapping(value = "/member/setting/updateMemberPic")
	@ResponseBody
	public Map<String,Object> updateMemberPic(HttpServletRequest request, @RequestParam("profileImgUrl") MultipartFile multipartFile, Model model) throws Exception {
		logger.info("update Member Pic.................................");
        
		Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        HttpSession session = request.getSession();
        MemberDTO mDto = new MemberDTO();
        int result = 0;
        
		String upload_path =  "D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\profile\\"; 		// ????????? ????????? ???????????? ??????

		String m_id = SecurityContextHolder.getContext().getAuthentication().getName();
		String originalFilename = multipartFile.getOriginalFilename();
		paramMap.put("m_id", m_id);
		paramMap.put("m_pic", originalFilename);

		mDto = mService.getMyInfo(m_id);
		if (mDto.getM_pic() != null) {	// ?????? ????????? ????????? ?????? ??????
			File file = new File(upload_path + mDto.getM_id() + mDto.getM_pic()); // ?????? +(??????????????????)??????????????? + ?????? ??????????????? ????????? ????????????
			file.delete(); // ???????????? ??????
		}
		
		multipartFile.transferTo(new File(upload_path + mDto.getM_id() + multipartFile.getOriginalFilename()));  // ????????? ?????????
		
		result = mService.updateMemberPic(paramMap); // ????????? ???????????? db??? update
		
		if (result == 1) {
			resultMap.put("m_id", m_id);
			resultMap.put("m_pic", originalFilename);
			
			mDto = mService.getMyInfo(mDto.getM_id());
        	session.removeAttribute("loginUser");
        	session.setAttribute("loginUser", mDto);
		}

		resultMap.put("result", result);
		
		return resultMap;
	}
	
	@PostMapping("/member/setting/deleteMemberPic")
	@ResponseBody
	 public Map<String,Object> deleteMemberPic(@RequestParam Map<String,Object> paramMap, HttpServletRequest request) throws Exception {
        logger.info("delete Member Pic.................................");
        
        Map<String,Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();
        
        int resultCode = 0;
        MemberDTO mDto = new MemberDTO();
       
        mDto = mService.getMyInfo((String) paramMap.get("m_id"));
        
		String upload_path =  "D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\profile\\"; 		// ????????? ????????? ???????????? ??????
		File file = new File(upload_path + mDto.getM_id() + mDto.getM_pic()); // ?????? +(??????????????????)??????????????? + ?????? ??????????????? ????????? ????????????
		file.delete(); // ???????????? ??????
		
        paramMap.put("m_pic", null);
        resultCode = mService.updateMemberPic(paramMap);

        if (resultCode == 1) {
			mDto = mService.getMyInfo(mDto.getM_id());
        	
        	session.removeAttribute("loginUser");
        	session.setAttribute("loginUser", mDto);
		}
        
        resultMap.put("resultCode", resultCode);	 // resultCode == 1? ???????????? : ??????
        
        return resultMap;
    } 
	
	@ResponseBody
	@RequestMapping("/member/displayFile")
	public ResponseEntity<byte[]> displayProfileFile(String fileName) throws Exception {
        logger.info("display Profile File.................................");

        // ???????????? fileName == m_id + m_pic
        ResponseEntity<byte[]> entity = null;
        HttpHeaders headers = new HttpHeaders();	// ?????? ?????? (???????????? ???????????? ???????????? ?????? header??? body??? ???????????? ???)
		InputStream in = null;	// ????????? ????????? ?????????????????? ?????? ?????????
		String upload_path =  "D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\profile\\"; 		// ????????? ????????? ???????????? ??????

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);	// ?????????
			MediaType mType = MediaUtils.getMediaType(formatName);	// ????????? ???????????? MediaUtils??????????????? ??????????????? ?????? ????????? ???????????? ??????.
			in = new FileInputStream(upload_path + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			} else {
				fileName = fileName.substring(fileName.indexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);	// ??????????????? ????????? ??????
				headers.add("Content-Disposition",			// ??? ????????? ????????? ???????????????
						"attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			}
			// ???????????????, ??????, HTTP ????????????
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return entity;
	}

	
	// ?????? ?????? ?????? (??? / ??? / ???)
	// ?????? ????????? ????????? ???????????? ??????. ????????? ????????? ?????? ????????? ?????? ??? ?????? ??????.
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}

	// ????????? ???????????? ??????
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@PreAuthorize("isAuthenticated()")	
	@PostMapping(value = "/post/uploadSummernoteImageFile", 
				 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<PostAttachDTO>> uploadSummernoteImageFile(@RequestParam("file") MultipartFile[] file) {
		logger.info("Upload Summernote Image File...................");
		
		PostDTO pDto = new PostDTO();
		List<PostAttachDTO> attachList = new ArrayList<PostAttachDTO>();

		// ????????? ?????? ??????
		String fileRoot = "D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\summernote_image\\";

		String uploadFolderPath = getFolder();
		File uploadFolder = new File(fileRoot, getFolder());
		logger.info("uploadFolder path : " + uploadFolderPath);
		
		if (uploadFolder.exists() == false) {
			uploadFolder.mkdirs();
		}
		
		for (MultipartFile uploadFile : file) {
			logger.info("-------------------------------------");
			logger.info("Upload File original FileName : " + uploadFile.getOriginalFilename());
			logger.info("Upload File Extension : " + uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".")));
			logger.info("Upload File Size : " + uploadFile.getSize());
			logger.info("Upload File ContentType : " + uploadFile.getContentType());
			
			PostAttachDTO attachDto = new PostAttachDTO();
			
			String originalFileName = uploadFile.getOriginalFilename();
			originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);		// originalFileName
			attachDto.setFileName(originalFileName);
			
			UUID uuid = UUID.randomUUID();
			originalFileName = uuid.toString() + "_" + originalFileName;								// uuid_originalFileName
			
			// ?????? ??????
			try {
				File saveFile = new File(uploadFolder, originalFileName);
				uploadFile.transferTo(saveFile);
				
				attachDto.setUuid(uuid.toString());
				attachDto.setUploadPath(uploadFolderPath);
				
				if (checkImageType(saveFile)) {
					attachDto.setFileType(true);		// ????????? ?????? ?????? setting
					
					// ????????? ????????? ?????????, ???????????? ???????????? ??????
					File thumbnail = new File(uploadFolder, "s_" + originalFileName);
					Thumbnails.of(saveFile).size(150, 150).toFile(thumbnail);
				}
				
				attachList.add(attachDto);
				
				pDto.setAttachList(attachList);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		return new ResponseEntity<List<PostAttachDTO>>(attachList, HttpStatus.OK);
	}
	
	
	// ????????? ?????? ???????????? ???????????? ?????????
	@GetMapping("/post/display")
	@ResponseBody
	public ResponseEntity<byte[]> postAttachDisplay(String fileName) {
		logger.info("Post Attach Display...................");
		logger.info("fileName : " + fileName);
		
		// ????????? ?????? ??????
		String fileRoot = "D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\summernote_image\\";
				
		File file = new File(fileRoot + "\\" + fileName);
		logger.info("file : " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			// ????????? ????????? ????????? ????????? probeContentType??? ????????? ???????????????.
			header.add("Contetn-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	// ???????????? ??????
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/post/deleteFile")
	@ResponseBody
	public ResponseEntity<String> postAttachDelete(String fileName, String type) {
		logger.info("Post Attach Delete...................");
		logger.info("deleteFile : " + fileName);
		
		// ????????? ?????? ??????
		String fileRoot = "D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\summernote_image\\";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");
		
		File file;
		
		try {
			file = new File(fileRoot + "\\" + URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			if (type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				logger.info("largeFileName : " + largeFileName);
				
				file = new File(largeFileName);
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("deleted", headers, HttpStatus.OK);
	}
	
}
