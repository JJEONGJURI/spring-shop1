<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<%--/shop1/src/main/webapp/WEB-INF/view/user/mypage.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>mypage</title>
<script type="text/javascript"> 
	$(function(){	//layout에 제이쿼리부분 있어야 사용가능함
		//<script type="text/javascript" src= 
//"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js">
		$("#minfo").show()
		$("#oinfo").hide()
		$(".saleLine").each(function(){
			$(this).hide()
		})
		$("#tab1").addClass("select")
	})
	function disp_div(id,tab) {
		$(".info").each(function(){
			$(this).hide()
		}) 
		
		$(".tab").each(function() {
			$(this).removeClass("select")
		})
		$("#"+id).show()
		$("#"+tab).addClass("select")
	}
	function list_disp(id) {
		$("#"+id).toggle()	//toggle => 현재 보이는 경우 => 안보이도록 변경
							//			현재 안보이고 있으면 => 보이도록 변경
	}
</script>
<style type="text/css">
	.select {
		padding:3px;
		background-color: #0000ff;
	}
	.xelect>a {
		color : #ffffff;
		text-decoration:none;
		font-weight: bold;
	}

</style>
</head>
<%--
	* 필요한거
	파라미터	 : userid 
	salelist : userid가 주문한 전체 Sale 객체 목록.(List)
	user	 : userid에 해당하는 회원정보
	
 --%>

<body>
	<table>
		<tr>
			<td id="tab1" class="tab">
				<a href="javascript:disp_div('minfo','tab1')">회원정보</a>
			</td>
			<c:if test="${param.userid != 'admin' }">
				<td id="tab2" class="tab">
					<a href="javascript:disp_div('oinfo','tab2')">주문정보</a>
				</td>
			</c:if>
		</tr>
	</table>
	<div id="oinfo" class="info" style="display:none; width:100%;">
		<table>
			<tr>
				<th>주문번호</th>
				<th>주문일자</th>
				<th>주문금액</th>
			</tr>
			<c:forEach items="${salelist}" var="sale" varStatus="stat">
				<tr>
					<td align="center">
						<a href="javascript:list_disp('saleLine${stat.index}')">${sale.saleid}</a> 
						<%--주문번호에 하이퍼링크 걸었음 : 주문아이디를 클릭하면 상품에 해당하는 정보가 보임 --%>
					</td>
					<td align="center">
						<fmt:formatDate value="${sale.saledate}" pattern="yyyy-MM-dd"/>
					</td>
					<td align="right">
						<fmt:formatNumber value="${sale.total}" pattern="###,###" />원
					</td>
				</tr>
				<tr id="saleLine${stat.index}" class="saleLine"> 
					<td colspan="3" align="center">
						<table>
							<tr>
								<td>상품명</td>
								<td>상품가격</td>
								<td>주문수량</td>
								<td>상품총액</td>
							</tr>
							<c:forEach items="${sale.itemList}" var="saleItem">
								<tr>
									<td class="title">${saleItem.item.name}</td>
									<td>
										<fmt:formatNumber value="${saleItem.item.price}" pattern="###,###" />원
									</td>
									<td>${saleItem.quantity}</td>
									<td>
										<fmt:formatNumber value="${saleItem.item.price * saleItem.quantity}" pattern="###,###"/>
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</c:forEach>		
		</table>
	</div>
	<div id="minfo" class="info">
		<table>
			<tr>
				<td>아이디</td>
				<td>${user.userid}</td>
			</tr>
			<tr>
				<td>이름</td>
				<td>${user.username}</td>
			</tr>			
			<tr>
				<td>우편번호</td>
				<td>${user.postcode}</td>
			</tr>			
			<tr>
				<td>전화번호</td>
				<td>${user.phoneno}</td>
			</tr>	
			<tr>
				<td>이메일</td>
				<td>${user.email}</td>
			</tr>
			<tr>
				<td>생년월일</td>
				<td>
					<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd" />
				</td>
			</tr>
		</table>
		<br>
		<a href="update?userid=${user.userid}">[회원정보수정]</a>&nbsp;
		<a href="password">[비밀번호수정]</a>&nbsp;
		<c:if test="${loginUser.userid != 'admin' }">
			<a href="delete?userid=">[회원탈퇴]</a>&nbsp;
		</c:if>		
		<c:if test="${loginUser.userid == 'admin' }">
			<a href="../admin/list">[회원목록]</a>&nbsp;
		</c:if>
	</div>
</body>
</html>