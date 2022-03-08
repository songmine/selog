<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<c:forEach items="${search.searchList}" var="searchList">
		<div class="result_list for_boarder_top">
			<div class="user_info">
				<a href="#" onclick="coming_soon();">
					<c:choose>
						<c:when test="${searchList.m_pic eq null}">
							<img class="author-thumb" src="${path}/resources/assets/img/member/profile_default.png" alt="userPic"
								onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
						</c:when>
						<c:otherwise>
							<img class="author-thumb" src="${path}/member/displayFile?fileName=${searchList.m_id}${searchList.m_pic}" alt="userPic"
								onerror="javascript:this.src='${path}/resources/assets/img/member/profile_default.png'">
						</c:otherwise>
					</c:choose>
				</a>
				<div class="username">
					<a href="#" onclick="coming_soon();">${searchList.m_id}</a>
				</div>
			</div>
			
			<!-- 게시글 첨부 사진 '있으면' 보이게 조건걸기 -->
			<c:choose>
				<c:when test="${searchList.p_pic ne null}">
					<a href="${path}/post?p_id=${searchList.p_id}">
						<div class="result_thumbnail">
<%-- 							<img class="img-fluid" src="${path}/resources/assets/img/post/default-img.jpg" alt=""> --%>
							<img class="img-fluid" src="${path}/resources/fileUpload/summernote_image/${searchList.p_pic}"><br>
						</div>
					</a>
				</c:when>
			</c:choose>
			<!-- // 게시글 첨부 사진 '있으면' 보이게 조건걸기 -->
			
			<a href="${path}/post?p_id=${searchList.p_id}">
				<h2>${searchList.p_title}</h2>
			</a>
			<div class="search_content">
				<p>${searchList.p_content}</p>
			</div>
			<c:if test="${searchList.t_name != '' && searchList.t_name ne null}">
				<div class="tag_wrapper">
					<a href="" class="tag_a" >${searchList.t_name}</a>
				</div>
			</c:if>
			<div class="subinfo">
				<span>${searchList.customDate}</span>
				<div style="margin-left: 0.5rem; margin-right:0.5rem;">·</div>
				<span>${searchList.p_comment_cnt}개의 댓글</span>
			</div>
		</div>
	</c:forEach>
