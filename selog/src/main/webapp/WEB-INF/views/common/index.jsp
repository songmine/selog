<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./header.jsp"%>
<title>selog</title>
<link href="${path}/resources/assets/css/common/index.css" rel="stylesheet">
<style type="text/css">
.div_for_img {
	width:320px;
    height:217px;
    overflow:hidden;
}

.img-fluid {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

img { 
	display: inline-block; 
	content: "";
	border: none;
}

</style>
<script type="text/javascript">
	$(document).ready(function(){
		GetList(1);
	});

	//${result} is from Controller
	var result = "${result}";
	if (result != "") {
		console.log("index");
		alert(result);
	}
	
	// 클립보드에 문의메일 복사
	function copyToClipBoard() {
		var contact = $("#contact_copy").text();
		
		// textarea 생성 
		const textArea = document.createElement('textarea'); 
		// textarea 추가 
		document.body.appendChild(textArea); 
		// textara의 value값으로 div내부 텍스트값 설정
		textArea.value = contact; 
		// textarea 선택 및 복사 
		textArea.select(); document.execCommand('copy'); 
		// textarea 제거 
		document.body.removeChild(textArea);
		
		alert("클립보드에 복사되었습니다.");
	}
</script>

<!-- Begin container ================================================== -->
<div class="container">

	<!-- Begin List Posts ================================================== -->
	<section class="recent-posts">
	<div class="section-title">
		 <ul class="section-title-nav" style="height: 59px;">
		 	<div class="list_div2">
			 	<div class="list_div1">
			         <li class="trending active" style="margin: 0 16px;">
			         	<span class="trending_span under_tab" style="padding-bottom: 10px;">
			         		<img alt="clock" src="${path}/resources/assets/img/index/increase.png" style="width: 20px; vertical-align: text-bottom;">&nbsp;&nbsp;트렌딩
		         		</span>
		         	</li>
			         <li class="recent" style="margin: 0 16px;">
			         	<span class="recent_span" style="padding-bottom: 10px;">
			         		<img alt="clock" src="${path}/resources/assets/img/index/clock.png" style="width: 20px; vertical-align: text-bottom;">&nbsp;&nbsp;최근
		         		</span>
		         	</li>
			         <div class="filter_date">
			         	<form action="${path}/" method="post" name="frm" class="selectBox">
			         	  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<select name="duration" id="duration" style="margin-left: 9px;" class="select_style" onchange="selDuration();">
								<option value="today" <c:if test="${d eq 'today'}">selected='selected'</c:if>>오늘</option>
							    <option value="week"  <c:if test="${d == '' || d eq null || d eq 'week'}">selected='selected'</c:if>>이번 주</option>
							    <option value="month" <c:if test="${d eq 'month'}">selected='selected'</c:if>>이번 달</option>
							    <option value="year"  <c:if test="${d eq 'year'}"> selected='selected'</c:if>>올해</option>
							</select>
						</form>
			         </div>
			 	</div>
		         <div class="drop_menu1" style="float: right; margin-right: 10px;">
					<img alt="kebab_menu" src="${path}/resources/assets/img/index/kebab.png" style="width: 20px;">
		         </div>
		     </div>
	         <div class="drop_div1 drop_hide1">
				<div class="menu_wrapper1" style="opacity: 1; transform: scale(1);">
					<a class="drop_a1" href="${path}/policy" style="text-decoration: none;"><div class="drop_con1" style="padding-bottom: 0;">서비스 정책</div></a><br>
			        <a class="drop_a1" style="text-decoration: none;">
			        	<div class="drop_con1" style="padding-top: 0; font-weight: 500;" title="클릭 시, 이메일이 복사됩니다." onclick="copyToClipBoard();">문의<br><div id="contact_copy">contact@selog.io</div></div>
			        </a>
				</div>
	        </div>
         </ul>
	</div>
	<div class="card-columns listrecent">
		<input type="hidden" name="trendTotalPageCnt"  id="trendTotalPageCnt"  value="${trendTotalPageCnt}">
		<input type="hidden" name="recentTotalPageCnt" id="recentTotalPageCnt" value="${recentTotalPageCnt}">
		
		<!-- 트렌딩 TAB -->
		<div id="card-list-container">
			<c:choose>
				<c:when test="${trendTotalPageCnt eq 0 || empty trendTotalPageCnt || empty trendingList}">
					<div class="no_list_here">
							<img class="no_list_here_icon" alt="noting_to_see" src="${path}/resources/assets/img/index/empty_post.png">
						</div>
						<div class="no_list_here">
							<div class="no_list_here_noti">등록된 글이 존재하지 않습니다.</div>
					</div>
				</c:when>
			</c:choose>
		</div>
		
		<!-- 최근 TAB  -->
		<div class="another_tab">
			<c:choose>
				<c:when test="${recentTotalPageCnt eq 0 || empty recentTotalPageCnt || empty recentList}">
					<div class="no_list_here">
						<img class="no_list_here_icon" alt="noting_to_see" src="${path}/resources/assets/img/index/empty_post.png">
					</div>
					<div class="no_list_here">
						<div class="no_list_here_noti">등록된 글이 존재하지 않습니다.</div>
					</div>
				</c:when>
			</c:choose>
		</div>
	</div>
	</section>
	<!-- // List Posts ================================================== -->
	
	<!-- 로딩 이미지 부분. 스크롤이 맨끝에 닿고 다음 페이지를 가져올때 동안 띄워진다. -->
	<div id="spinner">
		<div class="loading">
		    <!-- cpath/ 에서 '/'는 webapp을 의미한다. 웹앱 폴더의 svg폴더 안에 spinner-solid.svg가 들어있다.  -->
		    <img src="${path}/resources/assets/img/post/spinner-solid.svg"/> 
		</div>
	</div>
	
</div>
<!-- // .container ================================================== -->

<script src="${path}/resources/assets/js/common/index.js"></script>

</body>
</html>
