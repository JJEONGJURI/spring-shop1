<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="port" value="${pageContext.request.localPort}" />
<c:set var="server" value="${pageContext.request.serverName}" />
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
		ws.onopen = function() {
			$("#chatStatus").text("info:connection opened")
			$("input[name=chatInput]").on("keydown",function(evt){
				if(evt.keyCode == 13) {
					let msg = $("input[name=chatInput]").val()
					ws.send(msg)
					$("input[name=chatInput]").val("")
				}
			})
		}
		ws.onmessage = function(event) {
			$("textarea").eq(0).prepend(event.data+"\n")
		}
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