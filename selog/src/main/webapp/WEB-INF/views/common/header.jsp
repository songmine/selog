<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="icon" href="${path}/resources/assets/img/common/favicon.ico">
<!-- Bootstrap core CSS -->
<link href="${path}/resources/assets/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="${path}/resources/assets/css/common/mediumish.css" rel="stylesheet">
<link href="${path}/resources/assets/css/common/header.css" rel="stylesheet">
<!-- Fonts -->
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Righteous" rel="stylesheet">

<!-- Bootstrap core JavaScript -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="${path}/resources/assets/js/common/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
<script src="${path}/resources/assets/js/common/bootstrap.min.js"></script>
<script src="${path}/resources/assets/js/common/ie10-viewport-bug-workaround.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://kit.fontawesome.com/c96f939578.js" crossorigin="anonymous"></script>

<script>
	$(document).ready(function(){
	// 	 $(".drop_menu").click(function(){
	//      	$(this).next("div").toggleClass("drop_hide");
	//      });
		$(".drop_menu").click(function(event){
		   event.stopPropagation();
		   $(".drop_hide").toggle();
		});
		 
		$(document).click(function(){
		   $(".drop_hide").hide();
		});
	});
	
	function checkForm() {
		var frm = document.sign_in;
		var m_id = frm.m_id.value;
		var m_pwd = frm.m_pwd.value;
		
		var blank_regExp = /[\s]/g;
		var char_regExp = /^[A-Za-z0-9\-_]{3,16}$/;
			
		if (m_id == "") {
			$("#check_m_id").html("* 아이디를 입력해주세요.").css("color", "red");
			$("#m_id").focus();
			return false;
		} else if (blank_regExp.test(m_id.trim()) == true || m_id.trim().length < 3 || m_id.trim().length > 16 || char_regExp.test(m_id)== false) {
			$("#check_m_id").html("* 잘못된 아이디 형식입니다.").css("color", "red");
			$("#m_id").focus();
			return false;
		} else {
			$("#check_m_id").html("");
		}
		
		if(m_pwd == ''){
		    $("#check_m_pwd").html("* 비밀번호를 입력해주세요.").css("color", "red");
			$("#m_pwd").focus();
			return false;
		} else if (m_pwd.length < 4 || m_pwd.length > 20) {
			$("#check_m_pwd").html("* 비밀번호는 4~20자로 이루어져야 합니다.").css("color", "red");
			$("#m_pwd").focus();
			return false;
		} else {
			$("#check_m_pwd").html("");
		}
		 return true;
	}
	
	function sign_out_frm_btn() {
		$("#sign_out_frm").submit();
	}
	
	function coming_soon() {
		alert("준비중입니다.");
	}
</script>

<sec:csrfMetaTags />
<script> 
    var csrfParameter = $('meta[name="_csrf_parameter"]').attr("content");
    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
    var csrfToken = $('meta[name="_csrf"]').attr("content");

	$(function(){
	    $("#sign_in_frm").submit(function(event) {
	        event.preventDefault();
	        var params = "m_id=" + $("#m_id").val() + "&m_pwd=" + $("#m_pwd").val();
	
	        $.ajaxSetup({
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }  
            });
	
	        $.ajax({
	            url 	 : "${path}/sign_in",
	            type 	 : "post",
	            dataType : "json",
	            data 	 : params,
	            success  : function(response) {
                	console.log(response);
	                if (response.result == "fail") {
	                	if ($('#check_m_id').is(':empty') && $('#check_m_pwd').is(':empty')){
							// $("#sign_in_fail_msg").html("⚠️ " + response.message);
							$("#sign_in_fail_msg").html("⛔ " + response.message);
                		} else if (!$('#check_m_id').is(':empty') || !$('#check_m_pwd').is(':empty')) {
                			$("#sign_in_fail_msg").html("");
                		}
					} else if (response.result == "success") {
						$("#sign_in_fail_msg").html("");
						location.reload();
					}
	            }, error : function(jqXHR, status, e) {
	                console.error(status + " : " + e);
	            }
	        });
	    });
	});
</script>	
</head>
<body>
<!-- Begin Nav  -->
<nav class="navbar navbar-toggleable-md navbar-light fixed-top mediumnavigation" style="background-color: #F8F9FA;">
	<button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="container"  style="padding: 0 10px;">
		<!-- Begin Logo -->
		<a class="navbar-brand" href="${path}/">
			<img src="${path}/resources/assets/img/common/logo.svg" alt="logo" width="71" height="24">
		</a>
		<!-- End Logo -->
		<div class="collapse navbar-collapse" id="navbarsExampleDefault">
			<!-- Begin Menu -->
			<div class="navbar-nav ml-auto">
				<a class="nav-link" href="${path}/search"><img alt="search" src="${path}/resources/assets/img/common/search.png" width="18px;"></a>
					
					<sec:authorize access="isAuthenticated()">
						<button class="nav-link" id="wrtingBtn" style="margin-right:1.25rem; cursor: pointer;" onclick="location.href='${path}/post/writing';">새 글 작성</button>
						<a class="drop_menu">
						
							<!-- 프로필 사진 없을 때, 있을 때 -->
							<c:choose>
								<c:when test="${sessionScope.loginUser.m_pic eq null}">
									<img for="dropBtn" class="author-thumb profileImg" style="margin-right: 0px;" src="${path}/resources/assets/img/member/profile_default.png" alt="default_profile_pic"
										onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
								</c:when>
								<c:otherwise>
									<img for="dropBtn" class="author-thumb profileImg" style="margin-right: 0px;" src="${path}/member/displayFile?fileName=${sessionScope.loginUser.m_id}${sessionScope.loginUser.m_pic}" alt="profile_pic"
										onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
								</c:otherwise>
							</c:choose>
						
							<img alt="dropdown" src="${path}/resources/assets/img/common/dropdown_arrow.png" width="18px;" style="padding: .5em 0; margin-left: .6em;">
							</a>
						<div class="drop_div drop_hide">
							<div class="menu_wrapper">
								<a class="drop_a" href="#" style="text-decoration: none;" onclick="coming_soon();"><div class="drop_con" style="padding-bottom: 0;">내 셀로그</div></a><br>
	 								<%-- <a class="drop_a" href="${path}/member/${userId}">내 셀로그</a><br> < --%>
							    <a class="drop_a" href="${path}/setting" style="text-decoration: none;"><div class="drop_con" style="padding-top: 0; padding-bottom: 0;">설정</div></a><br>
						       <form action="${path}/sign_out" method="post" id="sign_out_frm">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							        <a class="drop_a" style="text-decoration: none;" onclick="return sign_out_frm_btn();"><div class="drop_con" style="padding-top: 0;">로그아웃</div></a>
						       </form>
						        
							</div>
				        </div>
				    </sec:authorize>
					
					<sec:authorize access="isAnonymous()">
						<button class="nav-link" id="signInBtn">로그인</button>
					</sec:authorize>
					
			</div>
			<!-- End Menu -->
		</div>
	</div>
</nav>
<!-- End Nav
================================================== -->

<!-- 	Start sign In modal -->
	<div class="modal" id="signInModal">
		<div class="modal_content">
			<div class="signIn-gray">
				<div>
					<img alt="Welcome!" src="${path}/resources/assets/img/common/welcome.png" width="168px;">
					<div class="welcome">환영합니다!</div>
				</div>
			</div>
			<div class="signIn-white">
				<div class="exit-wrapper">
					<span class="modal_close">&times;</span>                                                               
				</div>
				<div class="white-content">
					<div class="white-content-inner">
						<div class="white-wrapper">
							<h2>로그인</h2>
						   	<form action="${path}/sign_in" name="sign_in" method="post" onsubmit="return checkForm();" id="sign_in_frm">
								
								<input type="hidden" id="token" data-token-name="${_csrf.parameterName}" value="${_csrf.token}"/>
								
								<input name="m_id" type="text" id="m_id" class="id" placeholder="아이디를 입력해주세요."><span id="check_m_id"></span><br>
								<input name="m_pwd" type="password" id="m_pwd" class="id" placeholder="비밀번호를 입력해주세요."><span id="check_m_pwd"></span><br>
								
								<input type="submit" value="로그인" class="signIn_btn" id="signIn-button">
				        		<div id="sign_in_fail_msg"></div>
							</form>
						</div>
			        	<div class="white-foot">
			        		아직 회원이 아니신가요?
			        		<a href="${path}/member/register">회원가입</a>
			        	</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
<script type="text/javascript">
	var modal = document.getElementById('signInModal');
	var btn = document.getElementById("signInBtn");
	var close = document.getElementsByClassName("modal_close")[0];
	
	if (btn != null) {
		btn.onclick = function() {
		    modal.style.display = "block";
		    document.body.classList.add("stop-scroll");		//block scroll
		}
	}
		
	close.onclick = function() {
	    modal.style.display = "none";
 	    document.body.classList.remove("stop-scroll");
 	    document.getElementById("m_id").value = null;
 	    document.getElementById("m_pwd").value = null;
 	    document.getElementById("check_m_id").innerHTML = '';
 	    document.getElementById("check_m_pwd").innerHTML = '';
 	    document.getElementById("sign_in_fail_msg").innerHTML = '';
	}
	
	// close Modal with ESC key
	window.onkeyup = function(e) {
		var key = e.keyCode ? e.keyCode : e.which;
		if(key == 27) {
			$(".modal").fadeOut(90);
			$("body").css({overflow:'scroll'}).unbind('touchmove');
		}
	}
	
	// When the user clicks anywhere outside of the modal, close it
// 	window.onclick = function(event) {
// 	    if (event.target == modal) {
// 	        modal.style.display = "none";
// 	    }
// 	}   
	
</script>
