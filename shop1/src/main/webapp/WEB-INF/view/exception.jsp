<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%-- 
	isErrorPage="true" => exception 내장개게 전달됨.
  						  내가 에러 페이지다. 

--%>
<%-- /shop1/src/main/webapp/WEB-INF/view/exception.jsp --%>
<script>
	alert("${exception.message}") 
	//exception의 실제 객체는 CartEmptyException임
	//CartEmptyException.getMessage() 메서드 호출
	location.href="${exception.url}"
	//CartEmptyException.getUrl() 메서드 호출
</script>