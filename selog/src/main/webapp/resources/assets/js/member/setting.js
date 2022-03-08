/**
 * 
 */

// 프로필 사진 사이즈, 확장자 체크
var regex = /(.*?)\.(jpg|jpeg|png|gif|bmp|pdf)$/;
var maxSize = 5242880; //5MB
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

var profileImg = null;
var profileInput = document.querySelector('#profileImgUrl');
var profileImg = document.querySelector('.profileImgSetting');

//profileImg.addEventListener('click', () => {
//    profileInput.click();
//});

$("#profile_upload").click(function(){
	$("#profileImgUrl").trigger("click");
});

// 이미지가 선택되면 추가
profileInput.addEventListener('change', ()=> {
    var imgData = profileInput.value.substring('C:\\fakepath\\'.length);
    if (imgData) {
        profileImg = profileInput.files[0];
        upload_profile_img();
    }
});


// 프로필 사진 업로드
function upload_profile_img() {
	
    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
    var csrfToken = $('meta[name="_csrf"]').attr("content"); 
	
	var formData = new FormData();
	var inputFile = $("input[name='profileImgUrl']");
	var files = inputFile[0].files;
	
    if(profileImg) {
        formData.append('profileImgUrl', profileImg);
    }
	
	for(var i = 0; i < files.length; i++){
		if(!checkExtension(files[i].name, files[i].size) ){
			return false;
		}
		formData.append("profileImgUrl", files[i]);
	}

	 $.ajaxSetup({
         beforeSend: function(xhr) {
             xhr.setRequestHeader(csrfHeader, csrfToken);
         }  
     });
		         
	$.ajax({
		method     	: "POST",
		processData : false, 
		contentType : false,
		url      	: "/member/setting/updateMemberPic",
		data 		: formData,
		dataType	: "json",
		enctype		: "multipart/form-data",
		success  	: function(e) {
			if (e.result === 1) {
				console.log("프로필 사진 변경 성공");
				var uri = "/member/displayFile?fileName=" + e.m_id + e.m_pic;
				$(".profileImg").attr("src", uri);
			} else {
				console.log("프로필 사진 변경 실패");
			}
		},
		fail    	: function() {
			alert("System error.");
		},
	});
}

// 이름, 한 줄 소개 수정
function open_modify_myInfo() {
	$("#open_modify_btn").hide();
	$("#m_name").css("border", "1px solid  #DEE2E6").css("background-color", "white");
	$("#m_intro_short").css("border", "1px solid  #DEE2E6").css("background-color", "white");
	$("input[name='m_name']").attr("readonly", false);
	$("input[name='m_intro_short']").attr("readonly", false);
	$("#modifyMyInfoBtn").show();
}

$(document).ready(function() {
	$("#m_name").blur(function() {
		var m_name = $("#m_name").val();
		if (m_name == "") {
			$("#modiSpan").html("※ 이름은 필수 입력값 입니다.").css("color", "red");
			$("#m_name").attr("placeholder", "");
			$("#m_name").focus();
		} else if (m_name.length > 20) {
			$("#modiSpan").html("※ 이름은 10자 이하로 이루어져야 합니다.").css("color", "red");
			$("#m_name").focus();
		}  else {
			$("#modiSpan").html("");
		}
	});
	
	$("#m_intro_short").blur(function() {
		var m_intro_short = $("#m_intro_short").val();
		if (m_intro_short.length > 33) {
			$("#modiSpan").html("※ 한 줄 소개는 30자 이하로 이루어져야 합니다.").css("color", "red");
			$("#m_intro_short").focus();
		}  else {
			$("#modiSpan").html("");
		}
	});
});
	
