<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp"%>

<title>글 상세 - selog</title>
<link href="${path}/resources/assets/css/post/view.css" rel="stylesheet">

<style type="text/css">
#heartImg:hover {
	filter: drop-shadow(0 0 0 #000000);
}

.like_space_outer:hover {
	cursor: pointer;
}

#postAttachImg {
	margin: 10px;
	max-width: 600px;
	max-height: 600px;
}
</style>
<script type="text/javascript">
	window.onpageshow = function(event) {
		if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
			// Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
			window.location.reload();
	    }
	}
	
	// 게시글 좋아요
    var csrfParameter = $('meta[name="_csrf_parameter"]').attr("content");
    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
    var csrfToken = $('meta[name="_csrf"]').attr("content"); 
	
	var p_id = ${postView.p_id};
	var m_id = "";
	<sec:authorize access="isAuthenticated()">
		m_id = '<sec:authentication property="principal.username"/>';
	</sec:authorize>
	
	$(document).ready(function(){
		$(".like_space_outer").click(function(){
			if (m_id != "" && m_id != null) {
				changeHeart();
			} else {
				alert("로그인 후 이용가능 합니다.");
			}
		});
	});
	
	function changeHeart() {
		 $.ajaxSetup({
             beforeSend: function(xhr) {
                 xhr.setRequestHeader(csrfHeader, csrfToken);
             }  
         });
		
		$.ajax({
            type : "POST",  
            url : "${path}/post/clickLikeBtn",       
            dataType : "json",   
            data : "p_id="+p_id + "&m_id=" + m_id,
            error : function() {
                console.log("통신 에러", "error", "확인", function(){});
            },
            success : function(jdata) {
                if (jdata.resultCode == - 1) {
                    console.log("좋아요 오류", "error", "확인", function(){});
                } else {
                    if (jdata.likeCheck == 1) {
                        $("#heartImg").attr("src","${path}/resources/assets/img/post/like_after.png");
                        $("#likeCnt").empty();
                        $("#likeCnt").append(jdata.LIKECNT);
                    } else if (jdata.likeCheck == 0) {
                        $("#heartImg").attr("src","${path}/resources/assets/img/post/like_before.png");
                        $("#likeCnt").empty();
                        $("#likeCnt").append(jdata.LIKECNT);
                        
                    }
                }
            }
        });
	}
</script>

<div class="view_title_div">
	<div>
		<h1>${postView.p_title}</h1>
		
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal.username" var="m_id"/>
				<c:if test="${m_id eq postView.m_id}">
					<div class="under_title_right">
						<button class="view_cnt" cnt-title="총 조회 수 : ${postView.p_view_cnt}">통계</button>
						<button id="modify_post" onclick="location.href='${path}/post/edit_post?p_id=${postView.p_id}&mode=edit';">수정</button>
						<button id="del_post_btn">삭제</button>
					</div>
				</c:if>
		</sec:authorize>
		<div class="under_title_left">
			<div class="upper_username">
				<span class="username">
					<a href="#">${postView.m_id}</a>
				</span>
				<span style="margin-left: 0.5rem; margin-right:0.5rem;">·</span>
				<span>${postView.customDate}</span>
				<c:if test="${sessionScope.loginUser.m_id eq postView.m_id && postView.p_public_yn ne 'y'}">
					<span style="margin-left: 0.5rem; margin-right:0.5rem;">·</span>
					<div class="private_div">
						<img alt="private" src="${path}/resources/assets/img/post/padlock.png"> 비공개
					</div>
				</c:if>
				
			</div>			
		</div>
		<div class="for_space">
			<c:if test="${postView.t_name != '' && postView.t_name ne null}">
				<a href="#" class="tag_name_space">${postView.t_name}</a>
			</c:if>
		</div>
		<div class="like_space">
			<div class="like_space_outer">
				<div class="like_space_square">
					<div class="heardBtn">
						<c:choose>
							<c:when test="${likeYesNo eq 0 || empty likeYesNo}">
								<img id="heartImg" alt="heart" src="${path}/resources/assets/img/post/like_before.png" style="width: 48px; cursor: pointer;" tabindex="0">
							</c:when>
							<c:otherwise>
								<img id="heartImg" alt="heart" src="${path}/resources/assets/img/post/like_after.png" style="width: 48px; cursor: pointer;" tabindex="0">
							</c:otherwise>
						</c:choose>
					</div>
					<div class="likeCnt" id="likeCnt">${postView.p_like_cnt}</div>
				</div>
			</div>
		</div>
		<div class="for_space2">
			<div class="for_space_out">
				<div class="for_space_in"></div>
			</div>
		</div>
	
	</div>
</div>
<div class="view_content_div">
	<div class="view_content_out">
		<div class="view_content_in">
			${postView.p_content}
			
			<c:choose>
				<c:when test="${not empty attach}">
					<c:forEach items="${attach}" var="attachList">
						<img alt="postAttachImg" id="postAttachImg" src="${path}/resources/fileUpload/summernote_image/${attachList.uploadPath}/${attachList.uuid}_${attachList.fileName}"><br>
					</c:forEach>
				</c:when>
			</c:choose>
			
		</div>
	</div>
</div>
<div class="view_author_div">
	<div>
		<div class="view_author_wrapper">
			<a href="#">
				<c:choose>
					<c:when test="${authorInfo.m_pic eq null}">
						<img alt="userProfilePicture" src="${path}/resources/assets/img/member/profile_default.png"
							onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
					</c:when>
					<c:otherwise>
						<img alt="userProfilePicture" src="${path}/member/displayFile?fileName=${authorInfo.m_id}${authorInfo.m_pic}"
							onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
					</c:otherwise>
				</c:choose>
			</a>
			<div class="name_desc">
				<div class="author_name">
					<a href="">${authorInfo.m_name}</a>
				</div>
				<div class="author_short_intro">${authorInfo.m_intro_short}</div>
			</div>
		</div>
		<div class="author_under_wrapper"></div>
		<div class="under_line"></div>
	</div>
</div>

<c:choose>
	<c:when test="${prePost ne null || nextPost ne null}" >
		<div class="pre_next_div">
			<div class="go_this_post move_area">
				<c:choose>
					<c:when test="${prePost ne null}" >
						<a href="${path}/post?p_id=${prePost.p_id}" class="go_post pre_post">
							<div class="left_arrow_area">
								<img alt="left_arrow" src="${path}/resources/assets/img/post/left_arrow.png">
							</div>
							<div class="pre_area">
								<div class="pre_area_desc">이전 포스트</div>
								<h3>${prePost.p_title}</h3>
							</div>
						</a>
					</c:when>
				</c:choose>
			</div>
			<div class="go_this_post move_area" style="margin-left: 3rem;">
				<c:choose>
					<c:when test="${nextPost ne null}" >
						<a href="${path}/post?p_id=${nextPost.p_id}" class="go_post next_post">
							<div class="right_arrow_area">
								<img alt="right_arrow" src="${path}/resources/assets/img/post/right_arrow.png">
							</div>
							<div class="next_area">
								<div class="next_area_desc">다음 포스트</div>
								<h3>${nextPost.p_title}</h3>
							</div>
						</a>
					</c:when>
				</c:choose>
			</div>
		</div>
	</c:when>
</c:choose>

<%@ include file="./comment.jsp"%>

<!-- 	Start 포스트 삭제 modal -->
<div class="modal_confirm1" id="del_post_modal">
	<div class="modal_confirm_content1">
		<div>
			<h3>포스트 삭제</h3>
			<div class="modal_confirm_msg1">정말로 삭제 하시겠습니까?</div>
			<div class="modal_btn_area1">
				<button class="modal_confirm_cancle1" id="del_cancle">취소</button>
				<button class="modal_confirm_del1" onclick="location.href='${path}/post/delete_post?p_id=${postView.p_id}';">확인</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var btn1 = document.getElementById("del_post_btn");
	var modal1 = document.getElementById("del_post_modal");
	var close1 = document.getElementById("del_cancle");
	
	if (btn1 != null) {
		btn1.onclick = function() {
		    modal1.style.display = "block";
		    document.body.classList.add("stop-scroll");		//block scroll
		}
	}
	
	close1.onclick = function() {
	    modal1.style.display = "none";
 	    document.body.classList.remove("stop-scroll");
	}
	
	// close Modal with ESC key
	window.onkeyup = function(e) {
		var key = e.keyCode ? e.keyCode : e.which;
		if(key == 27) {
			$("#del_post_modal").fadeOut(90);
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
		
</body>
</html>