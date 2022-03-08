package io.song.selog.common.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.song.selog.post.domain.PostAttachDTO;
import io.song.selog.post.mapper.PostAttachMapper;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {

	@Autowired
	private PostAttachMapper attachMapper;
	
	// 어제 날짜 폴더 경로
	private String getFolderYesterday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -1);					// GregorianCalendar로 어제날짜
		
		String str = sdf.format(cal.getTime());		// 2022-01-28

		return str.replace("-", File.separator);	// 2022\01\28
	}
	
	// @Scheduled 어노테이션 내에는 cron이라는 속성을 부여해서 주기를 제어
	// 초 분 시 일 월 요일 년도(optional)
	// @Scheduled(cron="0/10 * * * * *")	// 10초마다
	@Scheduled(cron="0 10 14 * * *")		// 매일 14:10 실행
	public void checkFiles() throws Exception {
		log.warn("File Check Task run ...............");
		log.warn("NOW : " + new Date());
		
		// 어제 업로드된 첨부파일 목록(DB)
		List<PostAttachDTO> fileList = attachMapper.getOldFiles();
		fileList.forEach(dbList -> log.warn("DB에 저장된 첨부파일 : " + dbList));
		
		
		/* collection, array 원소 가공 시, stream이용하여 lambda함수형식으로 간결,깔끔하게 요소들 처리 가능
		 *       map : 요소들을 특정조건에 해당하는 값으로 변환
		 *       		 (eg) 요소들을 대,소문자 변형 등 의 작업을 하고 싶을 때 사용 가능 합니다.
		 *       filter :  요소들을 조건에 따라 걸러내는 작업
		 *       		 (eg) 길이의 제한, 특정문자포함 등 의 작업을 하고 싶을 때 사용 가능합니다.
		 *       sorted : 요소들을 정렬해주는 작업
		 * 요소들의 가공이 끝났다면 리턴해줄 결과를 collect 를 통해 만들어줍니다. 
		 */
		 
		// 어제 업로드 된 첨부파일 목록으로 파일경로 구함
		List<Path> fileListPaths 					// Paths.get() : 파일의 경로. 전체경로 한꺼번에 지정해도 되고, 상/하위 디렉토리를 나열해도 됨.
			= fileList.stream().map(attach -> Paths.get("D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\summernote_image",
									   				     attach.getUploadPath(),
									   				     attach.getUuid() + "_" + attach.getFileName()))
							   .collect(Collectors.toList());
		
		
		// 썸네일 파일의 경로 목록
		fileList.stream().filter(attach -> attach.isFileType() == true)
							   .map(attach -> Paths.get("D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\summernote_image",
				   				    attach.getUploadPath(),
				   				    "s_" + attach.getUuid() + "_" + attach.getFileName()))
							   .forEach(a -> fileListPaths.add(a));
		log.warn("===================================");
		fileListPaths.forEach(p -> log.warn("fileListPaths : " + p));
		
		
		// 어제 디렉토리 경로						// Paths.get(~~~).toFile() : java.io.File타입으로 변환 후 반환
		File targetDir = Paths.get("D:\\work-space\\sts-4-4.10.RELEASE\\selog\\src\\main\\webapp\\resources\\fileUpload\\summernote_image", getFolderYesterday()).toFile();
		
		
		// 어제 디렉토리와 비교해서, 삭제할 파일 경로 구하기
		// DB(tbl_attach)에 없는 파일 목록 구하기
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);
		
		log.warn("===================================");
		for (File file : removeFiles) {
			log.warn("지울 파일 : " +file.getAbsolutePath());
			file.delete();
		}
		
	}

}
