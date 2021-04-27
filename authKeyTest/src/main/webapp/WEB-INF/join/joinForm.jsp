<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

<script>
	var chk_timer = false;
	var timer = null;		// 타이머 반복함수 저장
	var isRunning = false;	// 타이머 실행중
	
	
	function authTimer(){
		var time = 180; // 3분
		var min = '';
		var sec = '';
		isRunning = true;
		
		timer = setInterval(function(){
			min = parseInt(time/60);
			sec = time%60;

			$('#authTime').html("인증시간  : " + min + "분" + sec + "초");
			time--;
			// chk_timer가 true인경우 clearInterval(t), false인경우는 계속 시간 감
			
			if(chk_timer){
				clearInterval(timer);
				isRunning = false;
			}
			
			if(time < 0){
				clearInterval(timer);
				$('#authTime').html('시간초과');
				
				//시간 초과되면 ajax로 session의 authkey를 제거할것
				
				$.ajax({
					url : "/api/mail/authTimer",
					type : "POST",
					success : function(result){
						if(result == "OK"){
							alert("인증시간이 지나 인증키가 초기화되었습니다. 다시 인증메일을 받으세요.");
						}
					}
				})
				isRunning = false;
			}
		}, 1000);
	
		
	}
	

	//인증메일 보내기
	function fn_auth(){
		var userId = $('#userId').val();
		
		$.ajax({
				url : "/api/mail/send",
				type : "POST",
				data : {"userId" : userId},
				success : function(result){
					if(result == "OK"){
						alert("인증메일을 성공적으로 보냈습니다.");
						$('#authDiv').append('</br><span id="authTime"></span>');
						
						// 타이머 실행중인 경우 현재 실행중인 타이머 종료 후 재실행
						if(isRunning){
							clearInterval(timer);
							$('#authTime').remove();
							authTimer();
						}else {
							authTimer();
						}
						
					} else {
						alert("인증메일 전송에 실패했습니다. 다시 시도해주세요.");
					}
				}
		})
	}
	
	//인증번호 확인
	function fn_authChk(){
		var authKey = $('#authChk').val();
		
		if(authKey == ""){
			alert('인증키를 입력해주세요');
			return false;
		}
		
		$.ajax({
			url : "/api/mail/authChk",
			type : "POST",
			data : {"authKey" : authKey},
			success : function(result){
				if(result == "OK"){
					var content = '<span id="authOK" auth="true">인증이 완료되었습니다.</span>'
					$('#authDiv').html(content);
					chk_timer = true;
				}else {
					alert("인증시간이 만료되었거나, 인증키가 일치하지 않습니다.\n 다시 인증메일을 전송해주세요.");
					$('#authChk').val("");
				}
			}
		})
	}
	
	
	
	</script>


<body>


	간단 회원가입 폼 <br/> <br/> <br/>
	

	이메일 <input type="text" id="userId"/><button type="button" onclick="fn_auth()">인증하기</button> <br/>
	<div id="authDiv">
		인증 확인 : <input type="text" id="authChk"/> <button type="button" onclick="fn_authChk()">인증 확인</button><br/>
	</div>
	
</body>
</html>