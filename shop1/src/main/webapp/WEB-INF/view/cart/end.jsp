<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /shop1/src/main/webapp/WEB-INF/view/cart/end.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<%--jspHeader 가져오려면 include 적어야함 
	include : 두개의 jsp 하나의 파일로(서블릿으로) 만들어진다.
--%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문완료</title>
</head>
<body>
	<h2>${sale.user.username}님이 주문하신 정보 입니다.</h2>
	<h2>배송지 정보</h2>
	<table>
		<tr>
			<td width="30%">주문아이디</td>
			<td width="70%">${sessionScope.loginUser.userid}</td>
		</tr>
		<tr>
			<td width="30%">이름</td>
			<td width="70%">${sessionScope.loginUser.username}</td>
		</tr>
		<tr>
			<td width="30%">우편번호</td>
			<td width="70%">${sessionScope.loginUser.postcode}</td>
		</tr>	
		<tr>
			<td width="30%">주소</td>
			<td width="70%">${sessionScope.loginUser.address}</td>
		</tr>	
		<tr>
			<td width="30%">전화번호</td>
			<td width="70%">${sessionScope.loginUser.phoneno}</td>
		</tr>
	</table>
	<h2>주문 상품</h2>
	<table>
		<tr>
			<th>상품명</th>
			<th>가격</th>
			<th>수량</th>
			<th>합계금액</th>
		</tr>
		<c:forEach items="${sale.itemList}" var="saleitem">
		<tr>
			<td>${saleitem.item.name}</td>
			<td><fmt:formatNumber value="${saleitem.item.price}" pattern="###,###"/></td>
			<td>${saleitem.quantity}</td>
			<td> <fmt:formatNumber value="${saleitem.item.price * saleitem.quantity}" pattern="###,###"/></td>
		</c:forEach>
		<tr>
			<td colspan="4" align="right">
				총 주문금액 : <fmt:formatNumber value="${sale.total}" pattern="###,###"/>
			</td>
			<td colspan="4">
				<a href="../item/list">상품 목록</a>
			</td>
		</tr>
	</table>
</body>
</html>