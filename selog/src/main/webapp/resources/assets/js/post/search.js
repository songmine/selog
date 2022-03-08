/**
 * 
 */
 
 	// 스크롤 페이징
	var currentPage = 1;		// 처음 페이지 로딩될 때 
	var isLoading = false;		// 현재 페이지 로딩 여부
	var totalCount = $("input[name='totalPageCnt']").val();
	
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
	        
	        GetList(currentPage);		 // 추가로 받아올 페이지를 서버에 ajax 요청
	    }
	})
	
    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
    var csrfToken = $('meta[name="_csrf"]').attr("content"); 
	var q =  $("input[name='q']").val();
	
	// 무한 스크롤
	function GetList(currentPage){
		$.ajaxSetup({
             beforeSend: function(xhr) {
                 xhr.setRequestHeader(csrfHeader, csrfToken);
             }  
         });
		
	    $.ajax({
	        type : "post",
	        url  : "/search_page",
	        data : {"pageNo" : currentPage,
	        		"q" : q},
	        success : function(data){
	            $("#card-list-container").append(data);		// 응답된 문자열은 html 형식. 
															// 해당 문자열을 #card-list-container div에 html로 해석하라고 추가
	            $(".loading").hide();		// 로딩바 숨김
	            isLoading = false;			// 로딩 중 아니라고 표시
	            
				console.log("search pagination...");
	        },
	        error : function(request, status, error) {
		        console.log("code = "+ request.status + " message = " + request.responseText + " error = " + error);
	       },
	    });
	}
	

