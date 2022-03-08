<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./header.jsp"%>

<title>서비스 정책 - selog</title>
<link href="${path}/resources/assets/css/common/policy.css" rel="stylesheet">

<script type="text/javascript">
	$(document).ready(function(){
		$(".tab_title_wrapper a").click(function(){
			var idx = $(this).index();
			$(".tab_title_wrapper a").remove("active").css("color", "#868E96").css("font-weight", "normal");
			$(".tab_title_wrapper a").eq(idx).addClass("active").css("color", "#20C997").css("font-weight", "bold");
			$(".tab_content_inner > div").hide();
			$(".tab_content_inner > div").eq(idx).show();
			if (idx == 0) {
				$(".title_inner_style").css({'transform' : 'translateX(0rem)'});
			} else if (idx == 1) {
				$(".title_inner_style").css({'transform' : 'translateX(12rem)'});
			}
		});
	});
</script>

<main>
	<div class="tab_title">
		<div class="tab_title_wrapper">
			<a class="terms active">이용약관</a>
			<a class="privacy">개인정보취급방침</a>
			<div class="title_inner_style"></div>
		</div>
	</div>
	
	<div class="tab_content">
		<div class="tab_content_outer">
			<div class="tab_content_inner">
				<div>
					<%@ include file="./terms.jsp"%>
				</div>
				<div>
					<%@ include file="./privacy.jsp"%>
				</div>
			</div>
		</div>
	</div>
</main>

</body>
</html>