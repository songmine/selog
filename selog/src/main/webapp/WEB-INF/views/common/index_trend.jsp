<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<!-- begin post -->
	<c:forEach items="${trendingList}" var="trendingList">
		<div class="card card_entire item" style="height: 10%;">
			
			<!-- 게시글 첨부 사진 '있으면' 보이게 조건걸기 -->
			<c:choose>
				<c:when test="${trendingList.p_pic ne null}">
					<a class="card_img_a" href="${path}/post?p_id=${trendingList.p_id}">
						<div class="div_for_img">
<%-- 							<img class="img-fluid" src="${path}/resources/assets/img/post/default-img.jpg" alt=""> --%>
							<img class="img-fluid" src="${path}/resources/fileUpload/summernote_image/${trendingList.p_pic}"><br>
						</div>
					</a>
				</c:when>
			</c:choose>
			<!-- // 게시글 첨부 사진 '있으면' 보이게 조건걸기 -->
			
			<div class="card-block content_outer">
				<a href="${path}/post?p_id=${trendingList.p_id}" class="content_inner">
					<h4 class="card-title con_title">${trendingList.p_title}</h4>
					<div class="con_content_wrapper con_content" style="height: 79px; margin-bottom: 12px;">
						${trendingList.p_content}
						<%-- <p class="card-text con_content">${trendingList.p_content}</p> --%>
					</div>
				</a>
				<div class="sub-info">
					<%-- <fmt:formatDate value="${trendingList.p_published_at}" pattern="yyyy년 MM월 dd일"/> --%>
					${trendingList.customDate}
					· ${trendingList.p_comment_cnt}개의 댓글
				</div>
				
				<!-- 게시글 첨부 사진 '없으면' 보이게 조건걸기 -->
				<c:choose>
					<c:when test="${trendingList.p_pic eq null}">
						<a class="card_img_a" href="${path}/post?p_id=${trendingList.p_id}">
							<div class="div_for_img" style="padding-top: 67.1%;">
<!-- 								<img class="img-fluid" > -->
							</div>
						</a>
					</c:when>
				</c:choose>
				<!-- // 게시글 첨부 사진 '없으면' 보이게 조건걸기 -->
				
			</div>
			<div class="card_bottom_area">
				<a href="#" class="card_userinfo">
					<c:choose>
						<c:when test="${trendingList.m_pic eq null}">
							<img class="author-thumb" src="${path}/resources/assets/img/member/profile_default.png" alt="userPic"
								onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
						</c:when>
						<c:otherwise>
							<img class="author-thumb" src="${path}/member/displayFile?fileName=${trendingList.m_id}${trendingList.m_pic}" alt="userPic"
								onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
						</c:otherwise>
					</c:choose>
					<span>by <b>${trendingList.m_id}</b></span>
				</a>
				<div class="card_likes">
					<img alt="likes" src="${path}/resources/assets/img/index/like.png">${trendingList.p_like_cnt}
				</div>
			</div>
		</div>
	</c:forEach>
	<!-- end post -->
