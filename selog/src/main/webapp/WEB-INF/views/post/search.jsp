<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp"%>
<title>selog</title>
<link href="${path}/resources/assets/css/post/search.css" rel="stylesheet">
<style type="text/css">
.result_thumbnail {
	width:768px;
    height:426.1px;
    overflow:hidden;
}

.img-fluid {
	width: 100%;
	height: 100%;
	object-fit: cover;
}
</style>

<script type="text/javascript">
$(document).ready(function(){
	if ($("input[name='q']").val().length > 0) {
		GetList(1);
	}	
	
	$(".search_box").on("click", function(e) {
        e.preventDefault();
        e.stopPropagation();
        $(".search_box input").focus();
	});
	
	$(".search_box input").focus(function(){
		$(".search_box").css("border", "1px solid #343A40");
		$(".search_box img").css("filter", "opacity(0.8) drop-shadow(0 0 0 darkGray)");
	});
	$(".search_box input").blur(function(){
		$(".search_box").css("border", "1px solid #ADB5BD");
		$(".search_box img").css("filter", "opacity(0.4) drop-shadow(0 0 0 gray)");
	});
});

window.onpageshow = function(event) {
	if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
		// Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
		window.location.reload();
    }
}
	
// 	function enterkey() {
// 		if (window.event.keyCode == 13) {
// 			alert("엔터키");
// 		}
// 	}

function searchFn() {
	if ($("#q").val().length == 0 || $("#q").val() == null) {
		$("#q").focus();
		return false;
	}
	return true;
}
</script>

<div class="search_wrapper">
	<div>
		<div class="search_box">
			<img alt="search" src="${path}/resources/assets/img/common/search.png">
			<form action="${path}/search" method="get" name="frm" onsubmit="return searchFn();">
				<input type="text" name="q" id="q" value="${search.keyword}" placeholder="검색어를 입력하세요">
			</form>
		</div>
	</div>
	<c:choose>
		<c:when test="${!empty search.searchList}">
			<p class="search_cnt">
				<input type="hidden" name="totalPageCnt" value="${search.totalPageCnt}">
				총 <b>${search.searchListCnt}개</b>의 포스트를 찾았습니다.
			</p>
		</c:when>
		<c:when test="${search.searchListCnt == 0 && search.keyword ne null}">
			<p class="search_cnt">검색 결과가 없습니다.</p>
		</c:when>
	</c:choose>
	
	<!-- 무한스크롤로 카드리스트를 가져와서 넣는 부분 -->
	<div id="card-list-container"></div>
	
	<!-- 로딩 이미지 부분. 스크롤이 맨끝에 닿고 다음 페이지를 가져올때 동안 띄워진다. -->
	<div id="spinner">
		<div class="loading">
		    <!-- cpath/ 에서 '/'는 webapp을 의미한다. 웹앱 폴더의 svg폴더 안에 spinner-solid.svg가 들어있다.  -->
		    <img src="${path}/resources/assets/img/post/spinner-solid.svg"/> 
		</div>
	</div>
	
</div>


<script src="${path}/resources/assets/js/post/search.js"></script>

</body>
</html>