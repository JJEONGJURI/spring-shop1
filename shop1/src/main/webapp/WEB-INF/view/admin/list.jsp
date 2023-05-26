<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp"%>
<%--/shop1/src/main/webapp/WEB-INF/view/admin/list.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록</title>
<script type="text/javascript">
	function allchkbox(allchk) {
		$(".idchks").prop("checked", allchk.checked)
		//prop : 제이쿼리에서 prop 는 속성등록 or 조회 하는 함수이다.
		//class = "idchks" 인 태그들 속성의 checked=allchkk 태그듸 checked 값으로 변경
	}
</script>
<style type="text/css">
.noline {
	text-decoration: none;
}
</style>
</head>
<body>
	<h2>회원목록</h2>
	<form action="mailForm" method="post">
		<table class="w3-table-all">
			<tr>
				<%-- 
				list?sort=10 : 아이디의 오름차순 정렬 
				list?sort=11 : 아이디의 내림차순 정렬
			--%>
				<th>아이디 <a href="list?sort=10" class="noline">▲</a> <a
					href="list?sort=11" class="noline">▼</a>
				</th>
				<th>이름 <a href="list?sort=20" class="noline">▲</a> <a
					href="list?sort=21" class="noline">▼</a>
				</th>
				<th>전화번호 <a href="list?sort=30" class="noline">▲</a> <a
					href="list?sort=31" class="noline">▼</a>
				</th>
				<th>생일 <a href="list?sort=40" class="noline">▲</a> <a
					href="list?sort=41" class="noline">▼</a>
				</th>
				<th>이메일 <a href="list?sort=50" class="noline">▲</a> <a
					href="list?sort=51" class="noline">▼</a>
				</th>
				<th></th>
				<th><input type="checkbox" name="allchk"
					onchange="allchkbox(this)"></th>
			</tr>
			<c:forEach items="${list}" var="user">
				<tr>
					<td>${user.userid}</td>
					<td>${user.username}</td>
					<td>${user.phoneno}</td>
					<td><fmt:formatDate value="${user.birthday}"
							pattern="yyyy-MM-dd" /></td>
					<td>${user.email}</td>
					<td><a href="../user/update?userid=${user.userid}">수정</a> <a
						href="../user/delete?userid=${user.userid}">강제탈퇴</a> <a
						href="../user/mypage?userid=${user.userid}">회원정보</a></td>
					<td><input type="checkbox" name="idchks" class="idchks"
						value="${user.userid}"></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="7" class="w3-center"><input type="submit"
					value="메일보내기"></td>
			</tr>
		</table>
	</form>
</body> 
</html>