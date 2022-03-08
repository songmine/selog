/**
 * 
 */
	/*
	var clickModiPwdCancle = false;
	$(document).click(function(e){
	    if (!$(e.target).is('#modiPwdCancle')) {
	    	clickModiPwdCancle = false;
	    } else {
	    	clickModiPwdCancle = true;
	    }
	});
	*/
 
 	/* 회원탈퇴 모달 */
 	var btn = document.getElementById("delMemBtn");
	var modal = document.getElementById("delMemModal");
	var close = document.getElementById("delCancle");
	
	btn.onclick = function() {
	    modal.style.display = "block";
	    document.body.classList.add("stop-scroll");		//block scroll
	}
	
	close.onclick = function() {
	    modal.style.display = "none";
 	    document.body.classList.remove("stop-scroll");
	}
	
	// close Modal with ESC key
	window.onkeyup = function(e) {
		var key = e.keyCode ? e.keyCode : e.which;
		if(key == 27) {
			$("#delMemModal").fadeOut(90);
			$("body").css({overflow:'scroll'}).unbind('touchmove');
		}
	}
	
	// When the user clicks anywhere outside of the modal, close it
// 	window.onclick = function(event) {
// 	    if (event.target == modal) {
// 	        modal.style.display = "none";
// 	    }
// 	}   
	
	function delete_member_frm_btn() {
		$("#delete_member_frm").submit();
	}
	
	
	/* 비밀번호 수정 모달 */
	var modiPwdBtn = document.getElementById("m_pwd_modify"); 
	var modiPwdModal = document.getElementById("modifyPwdModal"); 
	var modiPwdClose = document.getElementById("modiPwdCancle"); 
	
	
	modiPwdBtn.onclick = function() {
		$("#old_pwd_noti").html("");
		$("#old_pwd").attr("placeholder", "현재 비밀번호를 입력해주세요.");
 	    $("#new_pwd").attr("placeholder", "새로운 비밀번호를 입력해주세요.");
 	    $("#new_pwd_check").attr("placeholder", "한 번 더 입력해주세요.");
		modiPwdModal.style.display = "block";
	    document.body.classList.add("stop-scroll");		//block scroll
	}
	
	modiPwdClose.onclick = function() {
		modiPwdModal.style.display = "none";
 	    document.body.classList.remove("stop-scroll");
 	    document.getElementById("old_pwd").value = null;
 	    document.getElementById("new_pwd").value = null;
 	    document.getElementById("new_pwd_check").value = null;
 	    $("#old_pwd_noti").empty();
 	    $("#new_pwd_noti").empty();
 	    $("#new_pwd_check_noti").empty();
	}
	
	// close Modal with ESC key
	window.onkeyup = function(e) {
		var key = e.keyCode ? e.keyCode : e.which;
		if(key == 27) {
			$("#modifyPwdModal").fadeOut(90);
			$("body").css({overflow:'scroll'}).unbind('touchmove');
		}
	}
	
	// 비밀번호 수정
	$(document).ready(function() {
		$("#old_pwd").blur(function() {
			var old_pwd = $("#old_pwd").val();
			
			if (old_pwd == "") {
				$("#old_pwd_noti").html("※ 필수 입력값 입니다.");
				$("#old_pwd").attr("placeholder", "");
				$("#old_pwd").focus();
			} else if (old_pwd.length < 4 || old_pwd.length > 20) {
				$("#old_pwd_noti").html("※ 비밀번호는 4~20자로 이루어져 있습니다.");
				$("#old_pwd").focus();
			} else {
			    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
			    var csrfToken = $('meta[name="_csrf"]').attr("content"); 

				 $.ajaxSetup({
		             beforeSend: function(xhr) {
		                 xhr.setRequestHeader(csrfHeader, csrfToken);
		             }  
		         });
         
				$.ajax({
					type     : "POST",
					url      : "/member/setting/pwd_check",
					dataType : "json",
					data     : "m_pwd=" + old_pwd,
					success  : function(result) {
						if (result.match == true) {
							$("#old_pwd_noti").html(`&nbsp;`);
						} else {
							$("#old_pwd_noti").html("※ 잘못된 비밀번호 입니다.");
							$("#old_pwd").focus();
						}
					},
					fail     : function() {
						alert("System error.");
					},
				});
			}
		});
		
		$("#new_pwd").blur(function() {
			var new_pwd = $("#new_pwd").val();
			var old_pwd = $("#old_pwd").val();
			var new_pwd_check = $("#new_pwd_check").val();
			
			if (new_pwd == "") {
				$("#new_pwd_noti").html("※ 필수 입력값 입니다.");
				$("#new_pwd").attr("placeholder", "");
				$("#new_pwd").focus();
			} else if (new_pwd === old_pwd) {
				$("#new_pwd_noti").html("※ 현재 사용 중인 비밀번호 입니다.");
				$("#new_pwd").attr("placeholder", "");
				$("#new_pwd").focus();
			} else if (new_pwd.length < 4 || new_pwd.length > 20) {
				$("#new_pwd_noti").html("※ 4~20자로 입력해주세요.");
				$("#new_pwd").focus();
			} else if (new_pwd_check != "" && new_pwd != new_pwd_check) {
				$("#new_pwd_noti").html("※ 비밀번호가 일치하지 않습니다.");
			} else {
				$("#new_pwd").attr("placeholder", "");
				$("#new_pwd_noti").html(`&nbsp;`);
				$("#new_pwd_check_noti").html(`&nbsp;`);
			}
		});
		
		$("#new_pwd_check").blur(function() {
			var new_pwd_check = $("#new_pwd_check").val();
			var new_pwd = $("#new_pwd").val();

			if (new_pwd_check == "") {
				$("#new_pwd_check_noti").html("※ 필수 입력값 입니다.");
				$("#new_pwd_check").attr("placeholder", "");
				$("#new_pwd_check").focus();
			} else if (new_pwd_check.length < 4 || new_pwd_check.length > 20) {
				$("#new_pwd_check_noti").html("※ 4~20자로 입력해주세요.");
				$("#new_pwd_check").focus();
			} else if (new_pwd != new_pwd_check) {
				$("#new_pwd_check_noti").html("※ 비밀번호가 일치하지 않습니다.");
				$("#new_pwd_check").attr("placeholder", "");
				// $("#new_pwd_check").focus();
			} else {
				$("#new_pwd_check_noti").html(`&nbsp;`);
				$("#new_pwd_noti").html(`&nbsp;`);
			}
		});
		
		
	});
	
	
	function modify_pwd() {
		var frm = document.modify_password_frm;
		var old_pwd = frm.old_pwd.value;
		var new_pwd = frm.new_pwd.value;
		var new_pwd_check = frm.new_pwd_check.value;
		
		if (old_pwd.trim() == "" || $("#old_pwd_noti").text().indexOf('※') != -1) {
			$("#old_pwd").attr("placeholder", "");
			$("#old_pwd").focus();
		} else if (new_pwd.trim() == "" || $("#new_pwd_noti").text().indexOf('※') != -1) {
			$("#new_pwd").attr("placeholder", "");
			$("#new_pwd").focus();
		} else if (new_pwd_check.trim() == "" || $("#new_pwd_check_noti").text().indexOf('※') != -1) {
			$("#new_pwd_check").attr("placeholder", "");
			$("#new_pwd_check").focus();
		} else {
		    var csrfHeader = $('meta[name="_csrf_header"]').attr("content");
		    var csrfToken = $('meta[name="_csrf"]').attr("content"); 

			 $.ajaxSetup({
	             beforeSend: function(xhr) {
	                 xhr.setRequestHeader(csrfHeader, csrfToken);
	             }  
	         });
     
			$.ajax({
				type     : "POST",
				url      : "/member/setting/pwd_check",
				dataType : "json",
				data     : "m_pwd=" + old_pwd,
				success  : function(result) {
					if (result.match == true) {
						$("#old_pwd_noti").html(`&nbsp;`);
						frm.submit();
					} else {
						$("#old_pwd_noti").html("※ 잘못된 비밀번호 입니다.");
						$("#old_pwd").focus();
					}
				},
				fail     : function() {
					alert("System error.");
				},
			});
		}
	}