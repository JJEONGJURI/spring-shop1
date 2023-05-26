<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="port" value="${pageContext.request.localPort}" /> <%-- 포트번호 : 8080 --%>
<c:set var="server" value="${pageContext.request.serverName}" /> <%--IP 주소 : localhost --%>
<c:set var="path" value="${pageContext.request.contextPath}" /> 
<%--/shop1/src/main/webapp/WEB-INF/view/chat/chat.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<script type="text/javascript">
	$(function() {
		let ws = new WebSocket("ws://${server}:${port}${path}/chatting")
		ws.onopen = function() { //서버 접속 완료인 상태
			$("#chatStatus").text("info:connection opened")
			//chatStatus  인 상태 화면에 출력해줌?
			$("input[name=chatInput]").on("keydown",function(evt){
				if(evt.keyCode == 13) { //enter key 들어올 떄
					let msg = $("input[name=chatInput]").val()
					//text의 value 값을 msg 에 담아서 전송
					ws.send(msg) //서버로 데이터를 전송하자
					$("input[name=chatInput]").val("")
				}
			})
		}
		// 서버에서 메셎지를 수신한 경우
		ws.onmessage = function(event) {
			$("textarea").eq(0).prepend(event.data+"\n")
			//event.data : 수신된 메세지 정보
			//prepend(데이터) : 앞쪽에 추가(제일 위로 올라옴)
			//append(데이터) : 뒤쪽에 추가 (밑에 추가)
		}
		//서버와 접속이 종료 된 경우 발생되는 이벤트 > on 붙으면 이벤트임 
		ws.onclose = function(event) {
			$("#chatStatus").text("info:connection close")
		}
	})
</script>
</head>
<body>
	<p><div id="chatStatus"></div>
	<textarea name="chatMsg" rows="15" cols="40"></textarea>
	<br>
	메세지입력:<input type="text" name="chatInput">
</body>
</html>