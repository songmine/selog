/**
 * 
 */
 	var recent_tab_flag = false;	// 처음 로딩 시 '트렌딩' 탭 이므로, 최근 탭 false로 선언 및 초기화 
 
	 $(document).ready(function(){
		$(".drop_menu1").click(function(event){
		    event.stopPropagation();
		    $(".drop_hide1").toggle();
		});
		$(document).click(function(){
		    $(".drop_hide1").hide();
		});
		$(".drop_menu").click(function(){
		    $(".drop_hide1").hide();
		});
		
		
		$(".section-title-nav li").click(function(){
			var idx = $(this).index();
			$(".section-title-nav li span").remove("under_tab").css("border-color", "transparent").css("color", "#868e96").css("filter", "opacity(0.4) drop-shadow(0 0 0 gray)");
			$(".section-title-nav li span").eq(idx).addClass("under_tab").css("border-color", "#000").css("color", "#343a40").css("filter", "opacity(0.8) drop-shadow(0 0 0 darkGray)");
	 		$(".card-columns > div").hide();
	 		$(".card-columns > div").eq(idx).show();
	 		if(idx == 1) {
		 		recent_tab_flag = true;
		 		$(".filter_date").hide();
		 		 $("#card-list-container").empty();
		 		GetList_recent(1);
	 		} else if (idx == 0) {
		 		recent_tab_flag = false;
		 		$(".filter_date").show();
		 		$(".another_tab").empty();
		 		GetList(1);
	 		}
		});
	});
	
	window.onpageshow = function(event) {
		if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
			// Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
			window.location.reload();
	    }
	}
	 
	// select박스 옵션 클릭시 해당기간에 맞는 리스트 출력
	function selDuration() {
		 document.frm.submit();
	
		 var box = $("#duration").val();
		 
		 if (box == 'today') {
			 $("#duration").val('today').prop("selected", true);
		 } else if (box == 'week') {
			 $("#duration").val('week').prop("selected", true);
		 } else if (box == 'month') {
			 $("#duration").val('month').prop("selected", true);
		 } else if (box == 'year') {
			 $("#duration").val('year').prop("selected", true);
		 }
	}

 	// 무한 스크롤 페이징 (Infinite scroll Pagination)
	var currentPage = 1;		// 처음 페이지 로딩될 때 
	var isLoading = false;		// 현재 페이지 로딩 여부
	var totalCount = 0;			// 전체 페이지 수 

	if (recent_tab_flag == true) {
		totalCount = $("input[name='recentTotalPageCnt']").val();
	} else {
		totalCount = $("input[name='trendTotalPageCnt']").val();
	}
	
	// 웹 브라우저의 창을 스크롤 할 때 마다 호출되는 함수 등록
	$(window).on("scroll",function(){
	    var scrollTop = $(window).scrollTop();			 // 스크롤바 위로 남은 높이 (0 --> 최상단)
	    var windowHeight = $(window).height();			 // 웹 브라우저의 창의 높이
	    var documentHeight = $(document).height();		 // 문서 전체의 높이
	    var isBottom = scrollTop + windowHeight + 10 >= documentHeight;	 // 바닥까지 스크롤 되었는지 여부 확인
	    
	    if(isBottom){
	        // 만일 현재 마지막 페이지 || 현재 로딩 중 ==> 페이징 함수 실행x
	        if(currentPage == totalCount || isLoading){
	        	return; 	
	        }
	        isLoading = true;			 // 현재 로딩 중이라고 표시
	        $(".loading").show();		 // 로딩바 띄우고
	        currentPage++;				 // 요청페이지 번호 +1 증가
	        console.log("scrolling... Page #" + currentPage);
	        
	        if (recent_tab_flag == true) {
				GetList_recent(currentPage);
			} else {
		        GetList(currentPage);		 // 추가로 받아올 페이지를 서버에 ajax 요청
			}
	    }
	})
	
    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
    var csrfToken = $('meta[name="_csrf"]').attr("content"); 
	var duration =  $("#duration").val();
	
	// 무한 스크롤 (트렌딩 탭)
	function GetList(currentPage){
		$.ajaxSetup({
             beforeSend: function(xhr) {
                 xhr.setRequestHeader(csrfHeader, csrfToken);
             }  
         });
		
	    $.ajax({
	        type : "post",
	        url  : "/index_page",
	        data : {"pageNo" : currentPage,
	        		"duration" : duration},
	        success : function(data){
	            $("#card-list-container").append(data);		// 응답된 문자열은 html 형식. 
															// 해당 문자열을 #card-list-container div에 html로 해석하라고 추가
	            $(".loading").hide();		// 로딩바 숨김
	            isLoading = false;			// 로딩 중 아니라고 표시
	            
				console.log("## GetList(trending tab)... " +duration+" ##");
	        },
	        error : function(request, status, error) {
		        console.log("code = "+ request.status + " message = " + request.responseText + " error = " + error);
	       },
	    });
	}
	
	
	// 무한 스크롤 (최근 탭)
	function GetList_recent(currentPage){
		$.ajaxSetup({
             beforeSend: function(xhr) {
                 xhr.setRequestHeader(csrfHeader, csrfToken);
             }  
         });
		
	    $.ajax({
	        type : "post",
	        url  : "/index_page",
	        data : {"pageNo" : currentPage},
	        success : function(data){
	            $(".another_tab").append(data);		// 응답된 문자열은 html 형식. 
															// 해당 문자열을 #card-list-container div에 html로 해석하라고 추가
	            $(".loading").hide();		// 로딩바 숨김
	            isLoading = false;			// 로딩 중 아니라고 표시
	            
				console.log("## GetList_recent... ##");
	        },
	        error : function(request, status, error) {
		        console.log("code = "+ request.status + " message = " + request.responseText + " error = " + error);
	       },
	    });
	}

