/**
 * 
 */
	// textarea maxlength 지정하기
	function Limit(obj){
	    var maxLength = parseInt(obj.getAttribute("maxlength"));
	    if(obj.value.length > maxLength){
	        alert("300자 이하로 입력하세요.");
	        obj.value = obj.value.substring(0,maxLength);
	    }
	}
	
	
	// 댓글 등록
	function add(comment, callback, error){
		$.ajax({
			type : "post",
			url : "/comment/insert", 
			data : JSON.stringify(comment),					// json객체를 String객체로 반환시켜 줌		*JSON.parse() : String -> json 반환
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr) {		//XmlHttpRequest --> Ajax 방식으로 서버와 브라우저가 데이터 주고받을 때 사용하는 API
															// 				 --> JS는 xhr객체 사용하여.. 서버와 통신하고 데이터를 교환한다. 
				if (callback) {								//  callback함수 : 어떤 이벤트가 발생했거나 특정 시점에 도달했을 때, 시스템에서 호출하는 함수(나중에 호출되는 함수)
					callback(result);
				}
			},
			error : function(xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		});
	}
	
	// 댓글 불러오기(Ajax)
	function getCommentList(p_id){
	    $.ajax({
	        type	 	: "GET",
	        url 	 	: "/comment/" + p_id,
	        dataType    : "json",
	        data	    : $("#commentForm").serialize(),
	        contentType : "application/x-www-form-urlencoded; charset=UTF-8", 
	        success : function(data){
	            var html = "";
	            var commentCnt = data.list.length;
	            
	            
	            if(commentCnt == 0){
	                html += "<div>";
	                html += 	"<div class='no_comment_here'>";
	                html += 		"<table class='table'><h6><strong>등록된 댓글이 없습니다.</strong></h6></table>";
	                html += 	"</div>";
	                html += "</div>";
	            } else {
	                for(i = 0; i < commentCnt; i++){
	                    // html += "<div>";
	                    html += 	'<div class="comment_wrapper for_boarder_top" id="c_id'+data.list[i].c_id+'">';
	                    html +=		 '<div class="comment_info">';
	                    html += 		'<div class="c_author">';
	                    html +=			  '<a onclick="coming_soon();">';
	                    if (data.list[i].m_pic == null) {
		                    html += 		'<img alt="comment_author_pic" src="/resources/assets/img/member/profile_default.png">';
						} else {
		                    html += 		'<img alt="comment_author_pic" src="/member/displayFile?fileName='+data.list[i].m_id+data.list[i].m_pic+'">';
//		                    html += 		'<img alt="comment_author_pic" src="/resources/fileUpload/profile/'+data.list[i].m_id+data.list[i].m_pic+'">';
						}
	                    html +=			  '</a>';
	                    html += 		  '<div class="authour_date">';
	                    html += 			'<div class="author_name">';
	                    html += 				'<a onclick="coming_soon();">'+data.list[i].m_id+'</a>';
	                    html += 		 	'</div>';
	                    if (data.list[i].c_updated_at == null) {
		                    html += 		'<div class="comment_date">'+data.list[i].customDate+'</div>';
						} else {
		                    html += 		'<div class="comment_date">'+data.list[i].customDate+' (수정됨)</div>';
						}
	                    html += 		  '</div>';
	                    html += 		 '</div>';
						if (data.list[i].m_id == m_id) {
		                    html += 		'<div class="comment_action">';
		                    html += 			'<span id="comment_upd_btn" onclick="updateForm(' + data.list[i].c_id + ', \'' + data.list[i].m_id + '\', \'' + data.list[i].c_content + '\', \'' + data.list[i].m_pic + '\', \'' + data.list[i].customDate + '\', \'' + data.list[i].c_updated_at + '\');">수정</span>';
		                    html += 			'<span id="comment_del_btn" onclick="remove();" data-cid="'+data.list[i].c_id+'" data-mid="'+data.list[i].m_id+'">삭제</span>';
		                    html += 		'</div>';
						}
	                    html += 	  '</div>';
	                    html += 	  '<div class="comment_content_div">';
	                    html += 		'<div>';
	                    html += 			'<div class="c_content_inner">';
	                    html += 				'<div>';
	                    html += 					'<p>'+data.list[i].c_content+'</p>';
	                    html += 				'</div>';
	                    html += 			'</div>';
	                    html += 		 '</div>';
	                    html += 	   '</div>';
	                    html += 	'</div>';
	                    // html += '</div>';
	                }
	            }
	            
	            $("#commentCnt").html(commentCnt);
	            $("#commentList").html(html);
	            
	        },
	        error:function(request,status,error){
	            alert("Error");
	        }
	    });
	}
	
	// 댓글 삭제
	function remove(c_id, m_id, callback, error){
		
		var deleteBtn = document.querySelector("#comment_del_btn");
		var c_id = deleteBtn.dataset.cid;
		var m_id = deleteBtn.dataset.mid;
		
		$.ajax({
			type : "delete",
			url : "/comment/" + c_id,
			data: JSON.stringify({c_id:c_id, m_id:m_id}),
			contentType: "application/json; charset=utf-8",
			success : function(deleteResult, status, xhr) {
				console.log("댓글 삭제 성공!");
				getCommentList(p_id);
				if (callback) {
					callback(deleteResult);
				}
			},
			error : function(xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		});
	}
	
	// 댓글 수정 폼
	function updateForm(c_id, m_id, c_content, m_pic, customDate, c_updated_at) {
		
		var html = "";
		// html += "<div>";
        html += 	'<div class="comment_wrapper for_boarder_top" id="c_id'+c_id+'">';
        html +=		 '<div class="comment_info">';
        html += 		'<div class="c_author">';
        html +=			  '<a onclick="coming_soon();">';
	    if (m_pic == 'null') {
	        html += 		'<img alt="comment_author_pic" src="/resources/assets/img/member/profile_default.png">';
		} else {
	        html += 		'<img alt="comment_author_pic" src="/member/displayFile?fileName='+m_id+m_pic+'">';
//	        html += 		'<img alt="comment_author_pic" src="/resources/fileUpload/profile/'+m_id+m_pic+'">';
		}
	    html +=			  '</a>';
	    html += 		  '<div class="authour_date">';
	    html += 			'<div class="author_name">';
	    html += 				'<a onclick="coming_soon();">'+m_id+'</a>';
	    html += 		 	'</div>';
	    if (c_updated_at == "null") {
            html += 		'<div class="comment_date">'+customDate+'</div>';
		} else {
            html += 		'<div class="comment_date">'+customDate+' (수정됨)</div>';
		}
	    html += 		  '</div>';
	    html += 		 '</div>';
	    html += 	  '</div>';
		html += 	  '<div>';
		html += 		'<form action="" method="post" id="commentUpdateForm">';
		html += 			'<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>';
		html += 			'<input type="hidden" name="p_id" id="comment_p_id" value="${postView.p_id}">';
		html += 			'<textarea rows="" cols="" placeholder="댓글을 작성하세요" name="c_content" id="update_c_content" class="comment_content" maxlength="300" oninput="Limit(this)">'+c_content+'</textarea>';
		html += 			'<div class="reply_button_area">';
		html += 				'<input type="button" id="comment_cancle_btn" value="취소" onClick="getCommentList('+p_id+')">';
		html += 				'<input type="button" id="comment_submit_btn" value="댓글 수정" onclick="update(' + c_id + ', \'' + m_id + '\')">';
		html += 			'</div>';
		html += 		'</form>';
		html += 	  '</div>';
        html += 	'</div>';
	    // html += '</div>';

		$("#c_id"+c_id).replaceWith(html);
		$("#c_id"+c_id + " #update_c_content").focus();
		
	}
	
	// 댓글 수정
	function update(c_id, m_id, c_content, callback, error) {
		var c_content = $("#update_c_content").val();
		var comment = {
				c_id	  : c_id,
				c_content : c_content,
				m_id 	   : m_id
		};
		$.ajax({
			type : "put",	// patch로 해도 됨
			url : "/comment/" + comment.c_id,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr){
				console.log("댓글 수정 성공!");
				getCommentList(p_id);
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		});
	}
	
	