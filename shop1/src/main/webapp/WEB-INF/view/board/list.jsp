<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /shop1/src/main/webapp/WEB-INF/view/board/list.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${boardName}</title>
<script type="text/javascript">
	function listpage(page) {
		document.searchform.pageNum.value=page;
		document.searchform.submit();
	}
</script>
</head>
<body>
	<h2>${boardName}</h2>
	<table class="w3-table w3-border">
		<tr>
			<form action="list" method="post" name="searchform">
				<td>
					<input type="hidden" name="pageNum" value="1">
					<select name="searchtype"  class="w3-select">
						<option value="">선택하세요</option>
						<option value="title">제목</option>
						<option value="wirter">작성자</option>
						<option value="content">내용</option>
					</select>
					<script type="text/javascript">
						searchform.searchtype.value="${param.searchtype}";
					</script>
				</td>
				<td colspan="3">
					<input type="text" name="searchcontent" value="${param.searchcontent}" class="w3-input">
				<input type="button" value="전체게시물보기" class="w3-btn w3-blue" onclick="location.href='list?boardid=${boardid}'">
				</td>				
			</form>
		</tr>
		<c:if test="${listcount > 0 }">
			<tr>
				<td colspan="5" class="w3-right">글개수:${listcount}</td>
			</tr>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>글쓴이</th>
				<th>날짜</th>
				<th>조회수</th>
			</tr>
			<c:forEach var="board" items="${boardlist}">
				<tr>
					<td>${boardno}</td>
						<c:set var="boardno" value="${boardno - 1 }" />
					<td class="w3-left">
						<c:if test="${! empty board.fileurl}">
							<a href="file/${board.fileurl}">@</a>
						</c:if>
						<c:if test ="${empty board.fileurl}">&nbsp;&nbsp;&nbsp;</c:if>
						<c:forEach begin="1" end="${board.grplevel}">&nbsp;&nbsp;</c:forEach>
						<c:if test="${board.grplevel > 0}">┖</c:if>
							<a href="detail?num=${board.num}">${board.title}</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
</body>
</html>