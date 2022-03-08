<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp"%>
<title>설정 - selog</title>
<link href="${path}/resources/assets/css/member/setting.css" rel="stylesheet">
<script src="${path}/resources/assets/js/common/jquery.min.js"></script>


<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<style type="text/css">

.custom_content {
	width: 460px;
	height: 570px;
}

.custom_btn_area {
	justify-content: center;
	padding: 0 12px;
}

p {
	margin-top: 0.3rem;
    margin-bottom: 1rem;
}

</style>
<script type="text/javascript">
function save_modify_myInfo() {
	var name = $("input[name='m_name']").val();
	var intro = $("input[name='m_intro_short']").val();
	
	var m_id = "";
	<sec:authorize access="isAuthenticated()">
		m_id = '<sec:authentication property="principal.username"/>';
	</sec:authorize>
	
	if (name.trim() == "" && !$("#modiSpan").is(':empty')) {
		$("#m_name").attr("placeholder", "");
		$("#m_name").focus();
	} else if (!$("#modiSpan").is(':empty')) {
		$("#m_intro_short").attr("placeholder", "");
		$("#m_intro_short").focus();
	} else if (name.trim() != "") {
		
		var csrfParameter = $('meta[name="_csrf_parameter"]').attr("content");
	    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
	    var csrfToken = $('meta[name="_csrf"]').attr("content"); 
		
		 $.ajaxSetup({
             beforeSend: function(xhr) {
                 xhr.setRequestHeader(csrfHeader, csrfToken);
             }  
         });
		
		$.ajax({
            type : "POST",  
            url : "${path}/member/setting/changeMyInfo",       
            dataType : "json",   
            data : "m_name=" + name + "&m_intro_short=" + intro + "&m_id=" + m_id,
            success : function(jdata) {
                if (jdata.resultCode == 1) {
                	$("#modifyMyInfoBtn").hide();
        			$("#m_name").css("border", "none").css("background-color", "#F8F9FA");
        			$("#m_intro_short").css("border", "none").css("background-color", "#F8F9FA");
        			$("input[name='m_name']").attr("readonly", true);
        			$("input[name='m_intro_short']").attr("readonly", true);
        			$("#open_modify_btn").show();
        			if($("#m_intro_short").val() == "" ) {
        				$("input[name='m_intro_short']").attr("placeholder", "한 줄 소개");
        			}
                } else {
                    console.log("저장 오류", "error", "확인", function(){});
                }
            },
            error : function() {
            	alert("System error.");
            }
            
        });
	}
}
</script>

<main>
	<section id="up_section">
		<div class="thumbnail_area">
			<c:choose>
				<c:when test="${myInfo.m_pic eq null}">
					<img class="profileImg profileImgSetting" alt="default_profile_pic" src="${path}/resources/assets/img/member/profile_default.png" 
						 onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
				</c:when>
				<c:otherwise>
					<img class="profileImg profileImgSetting" alt="profile_pic" src="/member/displayFile?fileName=${myInfo.m_id}${myInfo.m_pic}" 
						 onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
				</c:otherwise>
			</c:choose>
			
			<form action="" id="thumbnail_frm" name="thumbnail_frm" method="post" enctype="multipart/form-data" onsubmit="return false;" accept-charset="UTF-8">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<input type="hidden" id="m_id_setting" name="m_id" value="${myInfo.m_id}">
				<input type="file" id="profileImgUrl" name="profileImgUrl" style="display: none;"/>
				<input type="button" id="profile_upload" value="이미지 업로드"/>
<!-- 				<input type="button" id="profile_upload" onclick="onclick=document.all.profileImgUrl.click()" value="이미지 업로드"/> -->
			</form>
			<button type="button" id="profile_remove" onclick="delete_profile_img();">이미지 제거</button>
		</div>
		
			<div class="info_area">
				<h3><input name="m_name" id="m_name" value="${myInfo.m_name}" readonly="readonly"></h3>
				<c:choose>
					<c:when test="${empty myInfo.m_intro_short}">
						<p><input name="m_intro_short" id="m_intro_short" placeholder="한 줄 소개" readonly="readonly"></p>
					</c:when>
					<c:otherwise>
						<p><input name="m_intro_short" id="m_intro_short" value="${myInfo.m_intro_short}" readonly="readonly"></p>
					</c:otherwise>
				</c:choose>
				<div id="modi_span_div_wrapper">
					<span id="modiSpan"></span>
					<div id="setting_changeMyInfoDiv">
						<a style="margin-left: 8px;" onclick="open_modify_myInfo();" id="open_modify_btn">수정</a>
						<button id="modifyMyInfoBtn" style="display: none;" onclick="save_modify_myInfo();" id="save_modify_btn">저장</button>
					</div>
				</div>
			</div>	
		
	</section>
	<section id="down_section">
		<div class="down_contents">
			<div class="down_contents_wrapper">
				<h3>셀로그 제목</h3>
<%-- 				<div class="setting-contents">${myInfo.m_id}.log</div> --%>
				<div class="setting-contents">
					<input name="p_slug" id="p_slug" value="${myInfo.m_id}.log" readonly="readonly">
				</div>
				<a href="#" style="color: #12BB86; text-decoration: underline;" onclick="coming_soon();">수정</a>
			</div>
			<div class="setting-desc">개인 페이지 좌측 상단에 나타나는 페이지 제목입니다.</div>
		</div>
		<div class="down_contents" style="border-top: 1px solid rgb(233, 236, 239);">
			<div class="down_contents_wrapper">
				<h3>아이디</h3>
				<div class="setting-contents">${myInfo.m_id}</div>
				<a href="#" style="color: #12BB86; text-decoration: underline;" id="m_pwd_modify">비밀번호 수정</a>
			</div>
			<div class="setting-desc">회원가입 및 로그인 시 필요한 문자입니다.</div>
		</div>
		<div class="down_contents" style="border-top: 1px solid rgb(233, 236, 239);">
			<div class="down_contents_wrapper">
				<h3>회원 탈퇴</h3>
				<div id="setting_delDiv">
					<button id="delMemBtn">회원 탈퇴</button>
				</div>
			</div>
			<div class="setting-desc">탈퇴 시 작성하신 포스트 및 댓글이 모두 삭제되며 복구되지 않습니다.</div>
		</div>
	</section>
</main>


<!-- 	Start 회원탈퇴 modal -->
<div class="modal_confirm" id="delMemModal">
	<div class="modal_confirm_content">
		<div>
			<h3>회원 탈퇴</h3>
			<div class="modal_confirm_msg">정말로 탈퇴 하시겠습니까?</div>
			<div class="modal_btn_area">
				<button class="modal_confirm_cancle" id="delCancle">취소</button>

				<form action="${path}/member/delete_member" method="post" id="delete_member_frm">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="hidden" name="m_id" value="${myInfo.m_id}">
					<button class="modal_confirm_del" onclick="return delete_member_frm_btn();">확인</button>
		        </form>

			</div>
		</div>
	</div>
</div>

<!-- 	Start 비밀번호 수정 modal -->
<div class="modal_confirm" id="modifyPwdModal">
	<div class="modal_confirm_content custom_content">
		<div>
			<h3>&nbsp;비밀번호 수정</h3>
			<div class="modal_btn_area custom_btn_area">

				<form action="${path}/member/setting/modify_password" method="post" name="modify_password_frm" onsubmit="return false">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					
					<label>현재 비밀번호 <span class="nec_star">*</span></label><br>
					<input type="password" id="old_pwd" class="my_old_pwd" placeholder="현재 비밀번호를 입력해주세요."><br>
					<p>&nbsp;<span id="old_pwd_noti"></span></p>
					
					<label>비밀번호 <span class="nec_star">*</span></label><br>
					<input type="password" id="new_pwd" placeholder="새로운 비밀번호를 입력해주세요."><br>
					<p>&nbsp;<span id="new_pwd_noti"></span></p>
					
					<label>비밀번호 확인 <span class="nec_star">*</span></label><br>
					<input type="password" id="new_pwd_check" name="m_pwd" placeholder="한 번 더 입력해주세요."><br>
					<p>&nbsp;<span id="new_pwd_check_noti"></span></p>
					
					<button type="button" class="modal_confirm_cancle" id="modiPwdCancle">취소</button>
					<input  type="submit" class="modal_confirm_del" value="확인" onclick="modify_pwd();">
		        </form>

			</div>
		</div>
	</div>
</div>

<script src="${path}/resources/assets/js/member/setting_modal.js"></script>

<script type="text/javascript">
	//${result} is from Controller
	var result = "${result}";
	if (result != "") {
		console.log("setting");
		alert(result);
	}
	
	// 프로필 사진 제거
	function delete_profile_img() {
		var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
	    var csrfToken = $('meta[name="_csrf"]').attr("content"); 
		
		var m_id = "";
		<sec:authorize access="isAuthenticated()">
			m_id = '<sec:authentication property="principal.username"/>';
		</sec:authorize>
		
		 $.ajaxSetup({
	         beforeSend: function(xhr) {
	             xhr.setRequestHeader(csrfHeader, csrfToken);
	         }  
	     });
			         
		$.ajax({
			url      	: "/member/setting/deleteMemberPic",
			method     	: "POST",
			data 		: "m_id=" + m_id,
			dataType	: "json",
			success  	: function(e) {
				if (e.resultCode === 1) {
					console.log("프로필 사진 삭제 성공");

					var uri = "${path}/resources/assets/img/member/profile_default.png";
					$(".profileImg").attr("src", uri);
				} else {
					console.log("프로필 사진 삭제 실패");
				}
			},
			fail    	: function() {
				alert("System error.");
			},
		});
	}
</script>

</body>
<script src="${path}/resources/assets/js/member/setting.js"></script>

</html>