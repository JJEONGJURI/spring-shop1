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
		$("#minfo").show() //아이디 속성이 minfo 인 것만 보여줘 => 회원정보
		$("#oinfo").hide() //아이디 속성이 oinfo 인 것은 안보여줌 => 주문정보
		//위에 두줄로 처음 화면에서 minfo 만 나오고 oinfo 는 안나오는 이유
		$(".saleLine").each(function(){	//class="saleLine" 모든 태그 => 주문상품조회부분
			$(this).hide()	//안보이도록함
		})
		$("#tab1").addClass("select") //id 속성이 tab1 인거에 select class 속성 추가
	})
	function disp_div(id,tab) {
		$(".info").each(function(){
			$(this).hide()
		}) 
		
		$(".tab").each(function() {
			$(this).removeClass("select")
		})
		$("#"+id).show() //클릭된거만 보여줌
		$("#"+tab).addClass("select")
	}
	function list_disp(id) {//id=saleLine0,saleLine1...
		$("#"+id).toggle()	//toggle => 현재 보이는 경우 => 안보이도록 변경
							//			현재 안보이고 있으면 => 보이도록 변경
	}
</script>
<style type="text/css">
	.select {
		padding:3px;
		background-color: #0000ff;
	}
	.select>a {
		color : #ffffff;
		text-decoration:none;
		font-weight: bold;
	}

</style>
</head>
<%--
	* 필요한거
	파라미터	 : userid > 관리자일경우 다른사람 회원정보도 봐야해서 파라미터값 필요함
	salelist : userid가 주문한 전체 Sale 객체 목록.(List)
	user	 : userid에 해당하는 회원정보
	
 --%>

<body>
	<table class="w3-table-all">
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
	<div id="oinfo" class="info" style="display:none; width:100%;"> <%-- 안보여줌 : function에서 관리 --%>
		<table class="w3-table-all">
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
	<div id="minfo" class="info"> <%--보여지고 있음 --%>
		<table class="w3-table-all">
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
			<a href="delete?userid=${user.userid}">[회원탈퇴]</a>&nbsp;
		</c:if>		
		<c:if test="${loginUser.userid == 'admin' }">
			<a href="../admin/list">[회원목록]</a>&nbsp;
		</c:if>
	</div>
</body>
</html>