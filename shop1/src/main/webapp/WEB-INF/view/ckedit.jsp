<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /shop1/src/main/webapp/WEB-INF/view/ckedit.jsp--%>
<script>
	window.parent.CKEDITOR.tools.callFunction
	(${param.CKEditorFuncNum}, '${fileName}','이미지 업로드 완료');
	//fileName 에는 위치, 경로, 이름 다 들어있다.?
	//fileName ex : /shop1/board/imgfile/스크린샷 2023-05-25 104206.png
	//절대경로로 넣으면 안됨.
</script>
