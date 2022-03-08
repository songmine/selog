<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style type="text/css">
.for_boarder_top + .comment_wrapper {
	border-top: 1px solid rgb(233, 236, 239);
}

.no_comment_here {
	margin-bottom: 50px;
	
}
</style>

<div class="view_reply_div">
	<h4><span id="commentCnt">${postView.p_comment_cnt}</span>개의 댓글</h4>
	<div class="reply_entire">
		<div>
			<form action="" method="post" id="commentForm">
			 	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="hidden" name="p_id" id="comment_p_id" value="${postView.p_id}">
				
				<textarea rows="" cols="" placeholder="댓글을 작성하세요" name="c_content" id="comment_c_content" class="comment_content" maxlength="300" oninput="Limit(this)"></textarea>
				<div class="reply_button_area">
					<input type="button" id="comment_submit_btn" value="댓글 작성">
				</div>
			</form>
		</div>
		<div class="show_all_comment" id="commentList"></div>
	</div>
</div>


<script src="${path}/resources/assets/js/post/comment.js"></script>
<script type="text/javascript">

	var csrfHeaderName = "${_csrf.headerName}";
	var csrfTokenValue = "${_csrf.token}";
	
	// 모든 ajax 전송 시, 토큰값 전달하게끔 기본 설정으로 지정
 	// 		첨부파일의 경우, jQuery를 이용해서 Ajax로 CSRF 토큰을 전송 시 beforeSend 를 이용. 
	// 		--> 매번 Ajax 사용 시 beforeSend 를 호출해야 하는 번거로움
	$(document).ajaxSend(function(e, xhr, options){
		xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	});
	
	var p_id = $("#comment_p_id").val();
	var c_content = "";
	var m_id = "";
	<sec:authorize access="isAuthenticated()">
		m_id = '<sec:authentication property="principal.username"/>';
	</sec:authorize>
	
	$(document).ready(function() {
		$("#comment_submit_btn").on("click", function(){
			var c_content = $("#comment_c_content").val();

			if (c_content == "") {
				$("#comment_c_content").focus();
			} else if (m_id == "" && m_id == null) {
				alert("로그인 후 이용가능 합니다.");
			} else if (m_id != "" && m_id != null && c_content != "" && p_id != "") {
				var comment = {
						p_id	  : p_id,
						c_content : c_content,
						m_id 	   : m_id
				};
				
				// 댓글 추가
		        add(comment, function(result){
		        	console.log("댓글 추가 성공!");
		        	getCommentList(p_id);
		        	$("#comment_c_content").val("");
		        	$("#comment_c_content").attr("placeholder", "댓글을 작성하세요");
		        });
			}
		});
	});
	
	// 초기 페이지 로딩시 댓글 불러오기
	$(function(){
	    getCommentList(p_id);
	});

</script>
