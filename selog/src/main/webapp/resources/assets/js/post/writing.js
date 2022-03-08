/**
 * 
 */
 

//첨부파일 사진 사이즈, 확장자 체크
var regex = /(.*?)\.(jpg|jpeg|png|gif|bmp|pdf)$/;
var maxSize = 5242880; //5MB (1024*1024*5)
function checkExtension(fileName, fileSize) {
	if(fileSize >= maxSize) {
		alert("5MB 이하의 사진을 올려주세요.");
		return false;
	}
	if(!fileName.match(regex)) {
		alert("이미지 파일만 업로드 가능합니다.");
		return false;
	}
	return true;
}

// input #p_title maxlength 지정하기
function LimitPTitleLength(obj){
    var maxLength = parseInt(obj.getAttribute("maxlength"));
    if(obj.value.length > maxLength){
        alert("25자 이하로 입력하세요.");
        obj.value = obj.value.substring(0,maxLength);
    }
}


// input #t_name maxlength 지정하기
function LimitTagLength(obj){
    var maxLength = parseInt(obj.getAttribute("maxlength"));
    if(obj.value.length > maxLength){
        alert("10자 이하로 입력하세요.");
        obj.value = obj.value.substring(0,maxLength);
    }
}

function backToList() {
	history.go(-1);
}

function goWrite(frm){
	
	var frm = document.frm;
	
	var title = frm.p_title.value;
	var content = frm.summernote.value;
	
	if (title.trim() == ''){
		alert("제목을 입력해주세요");
		document.getElementById("p_title").focus();
		return false;
	}
	if (content.trim() == ''){
		alert("내용을 입력해주세요");
		return false;
	}
	if ($(".note-editable").html().length >= 100000) {
		alert("100000자 이하로 입력하세요. (현재 : " + $(".note-editable").html().length + "자)");
		return false;
	}
	
	document.frm.action = "writing";
	
	
	
	var formObj = $("form[role='form']");
	
	console.log("Submit Button clicked");
	var str = "";
	    
	$(".uploadResult ul li").each(function(i, obj){
		var jobj = $(obj);
	      
		console.dir(jobj);
		console.log("-------------------------");
		console.log("filename : " + jobj.data("filename"));
	      
		str += "<input type='hidden' name='attachList["+i+"].fileName'   value='"+jobj.data("filename")+"'>";
		str += "<input type='hidden' name='attachList["+i+"].uuid'       value='"+jobj.data("uuid")+"'>";
		str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
		str += "<input type='hidden' name='attachList["+i+"].fileType'   value='"+jobj.data("type")+"'>";
	});
	    
	console.log(str);
	formObj.append(str).submit();
	
	document.frm.submit();
}

function goEdit(){
	
	var frm = document.frm;
	
	var title = frm.p_title.value;
	var content = frm.summernote.value;
	
	if (title.trim() == ''){
		alert("제목을 입력해주세요");
		document.getElementById("p_title").focus();
		return false;
	}
	if (content.trim() == ''){
		alert("내용을 입력해주세요");
		return false;
	}
	if ($(".note-editable").html().length >= 100000) {
		alert("100000자 이하로 입력하세요. (현재 : " + $(".note-editable").html().length + "자)");
		return false;
	}
	
	document.frm.action = "edit_post";
	document.frm.submit();
}

$(document).ready(function() {
	var fontNames = ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체']; 
	var	fontSizes = ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'];
	var toolbar = [
		// 글꼴 설정
	    ['fontname', ['fontname']],
	    // 글자 크기 설정
	    ['fontsize', ['fontsize']],
	    // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
	    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
	    // 글자색
	    ['color', ['forecolor','color']],
	    // 표만들기
	    ['table', ['table']],
	    // 글머리 기호, 번호매기기, 문단정렬
	    ['para', ['ul', 'ol', 'paragraph']],
	    // 줄간격
	    ['height', ['height']],
	    // 그림첨부, 링크만들기, 동영상첨부
	    ['insert',['picture','link']],
// 	    ['insert',['picture','link','video']],
	    // 코드보기, 확대해서보기, 도움말
	    ['view', ['codeview','fullscreen', 'help']]
	  ];

	var setting = {
        height 		: 500,				// 에디터 높이
        minHeight 	: null,				// 최소 높이
        maxHeight 	: null,				// 최대 높이
        disableResizeEditor : true,
        focus 		: true,				// 에디터 로딩 후 포커스를 맞출지 여부
        lang 		: 'ko-KR',			// 한글 설정
//      airMode : false,
		fontNames 	: fontNames,
		fontNamesIgnoreCheck : fontNames,
		fontSizes 	: fontSizes,
        toolbar 	: toolbar,
   		placeholder : '당신의 이야기를 적어보세요...',		// placeholder 설정     
           //콜백 함수
           callbacks : { 
           	// 이미지 업로드했을 때 동작
           	onImageUpload : function(files, editor, welEditable) {
            	// 파일 업로드(다중업로드를 위해 반복문 사용)
	            for (var i = files.length-1; i >= 0; i--) {
	            	if(!checkExtension(files[i].name, files[i].size) ){
	        			return false;
	        		}
	            	// 파일 보내기
	            	for (var i = files.length - 1; i >= 0; i--) {
	            		// this = $("#summernote")
		            	uploadSummernoteImageFile(files[i], this);
	            	}
        		}
           	},
           	onPaste : function (e) {
				var clipboardData = e.originalEvent.clipboardData;
				if (clipboardData && clipboardData.items && clipboardData.items.length) {
					var item = clipboardData.items[0];
					if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
						e.preventDefault();
					}
				}
			}
           }
    };
	
    $('#summernote').summernote(setting);
    
});

$(function () {
    var token = $("meta[name='_csrf']").attr('content');
    var header = $("meta[name='_csrf_header']").attr('content');
    if(token && header) {
        $(document).ajaxSend(function(event, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }
});

// ajax로 서버에서 파일 업로드를 진행
// --> file upload 성공 후, 파일 경로를 return 받음
function uploadSummernoteImageFile(file, editor) {
	// 파일 전송을 위한 폼 생성
	formData = new FormData();
	formData.append("file", file);
	
	$.ajax({
		data : formData,
		type : "POST",
		url  : "/post/uploadSummernoteImageFile",
		contentType : false,		 /* ★★★ 보내는 데이터의 타입 ("multipart/form-data" 로 전송이 되게 false 로 넣어줌) ★★★  */
		processData : false,
		cache 		: false,
		enctype 	: 'multipart/form-data',
		dataType	: "json",
		success : function(data) {
			// 처리 성공할 경우, 에디터에 이미지 출력
// 			var url = "/resources/fileUpload/summernote_image/" + (data[0].uploadPath).replaceAll('\\', '/') + "/" + data[0].uuid + "_" + data[0].fileName;
// 			$(editor).summernote('insertImage', url);
			showUploadResult(data); 	// 업로드 결과 처리 함수
			
			var uploadUL = $('#imageBoard > ul');
			var str = "";
			$(data).each(function(i,obj){
				
				var fileCallPath = encodeURIComponent(obj.uploadPath+ "/s_" + obj.uuid + "_" + obj.fileName);
				
				str += "<li><button type='button' data-file=\'" + fileCallPath + "\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img style='margin-bottom:10px;' src='/post/display?fileName=" + fileCallPath + "'></li>";
			});
			
			uploadUL.append(str);
			
			
		},
		error:function(request, status, error, data){
            alert("Error");
    	}
		
	});
}

$("div.note-editable").on('drop', function(e) {
    for(i=0; i< e.originalEvent.dataTransfer.files.length; i++){
    	uploadSummernoteImageFile(e.originalEvent.dataTransfer.files[i],$("#summernote")[0]);
    }
   e.preventDefault();
})


function showUploadResult(uploadResultArr) {
	console.log(uploadResultArr);
		    
    if(!uploadResultArr || uploadResultArr.length == 0) { 
    	return;
    }
	    
    var uploadUL = $(".uploadResult ul");
    var str ="";
	    
    $(uploadResultArr).each(function(i, obj) {
		if(obj.fileType){
			var fileCallPath = encodeURIComponent(obj.uploadPath+ "/s_" + obj.uuid + "_" + obj.fileName);
			console.log("fileCallPath >>>>>> " +  fileCallPath);
		
			str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType + "'><div>";
			str += "<span> " + obj.fileName + "</span>";
			str += "<button type='button' data-file=\'" + fileCallPath + "\' "
			str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
// 			str += "<img src='${contextPath}/display?fileName=" + fileCallPath + "'>";
			str += "</div>";
			str +"</li>";
		} 
// 		else {
// 			var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);			      
// 		    var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
			      
// 		    str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType + "'><div>";
// 			str += "<span> " + obj.fileName + "</span>";
// 			str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='file' " 
// 			str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
// // 			str += "<img src='${contextPath}/resources/img/attach.png'></a>";
// 			str += "</div>";
// 			str +"</li>";
// 		}
	});
	    
	uploadUL.append(str);
}


$(document).ready(function(){
	
	 $("#imageBoard").on("click", "button", function(e) {
	    
	 	console.log("delete file");
		      
	 	var targetFile = $(this).data("file");
	 	var type = $(this).data("type");
	 	var targetLi = $(this).closest("li");
		    
	 	$.ajax({
	 		url: "/post/deleteFile",
	 		data: {fileName: targetFile, type:type},
	 		dataType:"text",
	 		type: "POST",
	 		success: function(result){
	 			console.log(result);
	 			targetLi.remove();
	 		}
	 	});
	 });
	
	$("#p_title").on("input", function() {
		$(".preview_title").html($(this).val());
	});
	
//	$(".note-editable").on("change", function() {
//		$(".preview_content_inner").html($(this).val());
//	});
	
	$('.note-editable').on('DOMSubtreeModified', function(){
		$(".preview_content_inner").html($(this).text());
	});

});




