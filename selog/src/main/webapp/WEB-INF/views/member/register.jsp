<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
    
<link rel="icon" href="${path}/resources/assets/img/common/favicon.ico">

<link href="${path}/resources/assets/css/member/register.css" rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<title>회원가입 - selog</title>

<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script src="${path}/resources/assets/js/common/ie10-viewport-bug-workaround.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">

// var token = $("meta[name='_csrf']").attr("content");
// var header = $("meta[name='_csrf_header']").attr("content");

// $(document).ajaxSend(function(e, xhr, options) {
//     xhr.setRequestHeader(header, token);
// });

$(function () {
    var token = $("meta[name='_csrf']").attr('content');
    var header = $("meta[name='_csrf_header']").attr('content');
    if(token && header) {
        $(document).ajaxSend(function(event, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }
});

$(document).ready(function() {
	$("#m_id").blur(function(){
		var blank_regExp = /[\s]/g;
		var char_regExp = /^[A-Za-z0-9\-_]{3,16}$/;
		
		var m_id = $("#m_id").val();
		
		if (m_id == "") {
// 			$("#m_id").attr("placeholder", "ID는 필수 입력값 입니다.").css("border-color", "red").css("color","red").focus();
			$("#m_id_noti").html("아이디는 필수 입력값 입니다.").css("color", "red");
			$("#m_id").attr("placeholder", "");
			$("#m_id").focus();
		} else if (blank_regExp.test(m_id.trim()) == true || m_id.trim().length < 3 || m_id.trim().length > 16 || char_regExp.test(m_id)== false) {
			$("#m_id_noti").html("아이디는 3~16자의 알파벳,숫자,혹은 - _ 으로 이루어져야 합니다.").css("color", "red");
			$("#m_id").focus();
		} else {
			$.ajax({
				type     : "POST",
				url      : "${path}/member/id_check",
				dataType : "json",
				data     : "m_id=" + m_id,
				success  : function(data) {
					if (data.valName == "1") {
						$("#m_id_noti").html("사용 중인 아이디 입니다.").css("color","red");
						$("#m_id").focus();
					} else {
						$("#m_id_noti").html("사용 가능한 아이디 입니다.").css("color","rgb(12, 166, 120)");
					}
				},
				fail     : function() {
					alert("System error.");
				}
			});
		}
	});
	
	$("#m_pwd").blur(function(){
		var m_pwd = $("#m_pwd").val();
		if (m_pwd == "") {
			$("#m_pwd_noti").html("비밀번호는 필수 입력값 입니다.").css("color", "red");
			$("#m_pwd").attr("placeholder", "");
			$("#m_pwd").focus();
		} else if (m_pwd.length < 4 || m_pwd.length > 20) {
			$("#m_pwd_noti").html("비밀번호는 4~20자로 이루어져야 합니다.").css("color", "red");
			$("#m_pwd").focus();
		} else {
			$("#m_pwd_noti").html("");
		}
	});
	
	$("#m_name").blur(function(){
		var m_name = $("#m_name").val();
		if (m_name == "") {
			$("#m_name_noti").html("이름은 필수 입력값 입니다.").css("color", "red");
			$("#m_name").attr("placeholder", "");
			$("#m_name").focus();
		} else if (m_name.length > 20) {
			$("#m_name_noti").html("이름은 10자 이하로 이루어져야 합니다.").css("color", "red");
			$("#m_name").focus();
		}  else {
			$("#m_name_noti").html("");
		}
	});
	
	$("#m_intro_short").blur(function(){
		var m_intro_short = $("#m_intro_short").val();
		if (m_intro_short.length > 32) {
			$("#m_intro_short_noti").html("한 줄 소개는 30자 이하로 이루어져야 합니다.").css("color", "red");
			$("#m_intro_short").focus();
		}  else {
			$("#m_intro_short_noti").html("");
		}
	});
	
});

function go_save() {
	var frm = document.frm;
	var id = frm.m_id.value;
	var pwd = frm.m_pwd.value;
	var name = frm.m_name.value;
	var intro = frm.m_intro_short.value;
	
	var idcheck_text = $("#m_id_noti").text();
	
	console.log(id + ", " + pwd + ", " + name + ", " + intro);
							// 뭔가 옆에 떠있는데, '가능한' 단어가 없음. 
	if (id.trim() == "" || (!$("#m_id_noti").is(':empty') && idcheck_text.indexOf('가능한') == -1)) {
		$("#m_id").attr("placeholder", "");
		$("#m_id").focus();
	} else if (pwd.trim() == "" || !$("#m_pwd_noti").is(':empty')) {
		$("#m_pwd").attr("placeholder", "");
		$("#m_pwd").focus();
	} else if (name.trim() == "" || !$("#m_name_noti").is(':empty')) {
		$("#m_name").attr("placeholder", "");
		$("#m_name").focus();
	} else if (!$("#m_intro_short_noti").is(':empty')) {
		$("#m_intro_short").attr("placeholder", "");
		$("#m_intro_short").focus();
	}  else if (id.trim() != "" && pwd.trim() != "" && name.trim() != "") {
		frm.action = "${path}/member/register";
		frm.submit();
	}
	
}
</script>

<div id="outer">
	<div id="inner">
		<h1 id="welcome">환영합니다!</h1>
		<div id="welcome_tip">기본 회원 정보를 등록해주세요.</div>
		<div id="welcome_contents">
			<form action="#" method="post" name="frm">
				
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				
				<label>아이디 <span class="nec_star">*</span></label><br>
				<input type="text" name="m_id" id="m_id" placeholder="아이디를 입력하세요">&nbsp;&nbsp;<span id="m_id_noti"></span><br>
				
				<label>비밀번호 <span class="nec_star">*</span></label><br>
				<input type="password" name="m_pwd" id="m_pwd" placeholder="비밀번호를 입력하세요">&nbsp;&nbsp;<span id="m_pwd_noti"></span><br>
				
				<label>이름 <span class="nec_star">*</span></label><br>
				<input type="text" name="m_name" id="m_name" placeholder="이름을 입력하세요">&nbsp;&nbsp;<span id="m_name_noti"></span><br>
				
				<label>한 줄 소개</label><br>
				<input type="text" name="m_intro_short" id="m_intro_short" placeholder="당신을 한 줄로 소개해보세요">&nbsp;&nbsp;<span id="m_intro_short_noti"></span><br>
		
				<div id="buttons">
					<input type="reset" value="취소" class="cancle" onclick="history.go(-1)">
					<input type="button" value="확인" class="submit" onclick="go_save();">
				</div>
			</form>
		</div>
	</div>
</div>


</body>
</html>