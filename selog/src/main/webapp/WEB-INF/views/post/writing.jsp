<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<c:choose>
	<c:when test="${mode eq 'edit'}">
		<title>(작성중) ${PostDTO.p_title}</title>
	</c:when>
	<c:otherwise>
		<title>새 글 작성</title>
	</c:otherwise>
</c:choose>
<link href="${path}/resources/assets/css/post/writing.css" rel="stylesheet">

<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<link rel="icon" href="${path}/resources/assets/img/common/favicon.ico">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script> -->
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/common/bootstrap.min.js"></script> -->

<link rel="stylesheet" href="${path}/resources/assets/css/summernote/summernote-lite.css">
<link rel="stylesheet" href="${path}/resources/assets/css/post/font-awesome/css/font-awesome.min.css" type="text/css">

<script src="${path}/resources/assets/js/summernote/summernote-lite.js"></script>
<script src="${path}/resources/assets/js/summernote/lang/summernote-ko-KR.js"></script>
<script>
//${result} is from Controller
var result = "${result}";
if (result != "") {
	console.log("writing");
	alert(result);
}
</script>
<style type="text/css">

.note-toolbar {
	top: 0px;
    display: flex;
    -webkit-box-align: center;
    align-items: center;
    background: white;
    z-index: 10;
    transition: all 0.125s ease-in 0s;
    flex-wrap: wrap;
    margin-bottom: 1rem !important;
    padding-left: 3rem !important;
    padding-right: 3rem !important;
    width: auto;
    border: none;
}

.note-editing-area {
    flex: 1 1 0%;
    min-height: 0px;
    display: flex;
    flex-direction: column;
    padding-left: 3rem !important;
}

.note-editor.note-frame, .note-editor.note-frame .note-statusbar, .note-editor.note-frame .note-status-output:empty {
	border: none;
}

.note-editor.note-frame .note-statusbar {
	background-color: transparent;
}

.note-editing-area {
	height: 860px;
}

</style>
</head>
<body>

<script type="text/javascript">
$(document).ready(function(){
	var frm = document.frm;
	
	var mode = frm.mode.value;
	if(mode == 'edit') {
		frm.p_title.value = "${PostDTO.p_title}";
		frm.t_name.value = "${PostDTO.t_name}";
	}
});
</script>

<form action="${path}/post/writing" method="post" name="frm" role="form" >
	<input type="hidden" name="mode" value="${mode}">
	<input type="hidden" name="p_id" value="${PostDTO.p_id}">
  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	
	<!-- <form action="${path}/post/writing.io" method="post" name="frm" enctype="multipart/form-data"> -->
	<div class="wrapper_div">
		<div class="left">
			<div class="left_outer">
				<div class="left_inner">
					<input type="hidden" name="m_id" value="${sessionScope.loginUser.m_id}">
					<div style="max-height: 720px; opacity: 1;">
						<div class="p_title_div">
							<input type="text" name="p_title" id="p_title" placeholder="제목을 입력하세요" maxlength="25" oninput="LimitPTitleLength(this)">
							<div class="p_title_border_bottom"></div>
							<input type="text" name="t_name" id="t_name" placeholder="태그를 입력하세요" maxlength="10" oninput="LimitTagLength(this)">
							<div class="under_tag"></div>
						</div>
					</div>
					<textarea id="summernote" name="p_content" id="p_content" style="display: none;">
						<c:if test="${mode eq 'edit'}">${PostDTO.p_content}</c:if>
						<div id="imageBoard">
							<ul></ul>
						</div>
					</textarea>
					<div class="button_area">
						<div class="button_inner">
							<div class="button_1"></div>
							<div class="button_2">
								<input type="button" class="exitWriting" value="나가기" onclick="backToList()">
								
								<c:choose>
									<c:when test="${mode eq 'edit'}">
										<input type="button" class="submitWriting" value="수정하기" onclick="goEdit();" />
									</c:when>
									<c:otherwise>
										<input type="button" class="submitWriting" value="글 올리기" onclick="goWrite();" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="right">
			<div class="preview_div">
				<h1 class="preview_title"></h1>
				<div class="preview_content">
					<div class="preview_content_inner"></div>
				</div>
			</div>
		</div>
	</div>
</form>

<div class="uploadResult" style="display:none;"> 
	<ul>
  	</ul>
</div>

<script src="${path}/resources/assets/js/post/writing.js"></script>

</body>
</html>