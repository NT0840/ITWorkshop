<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>簡易ロードマップ作成アプリ</title>
	<!-- リセットCSS -->
	<link rel="stylesheet" href="https://unpkg.com/ress/dist/ress.min.css">
	<!-- オリジナルCSS -->
    <link rel="stylesheet" href="css/style.css">
    
    <link rel="icon" type="image/x-icon" href="image/favicon16.ico">
</head>
<body>
	<jsp:include page="WEB-INF/jsp/header.jsp"/>
	<div class="top-container">
		<div class="ad-container">
			<div>test</div>
		</div>
		<div class="login-container">
			<c:if test="${errorMsg != null}">
				<c:out value="${errorMsg.fieldName}" /></br>
				<c:out value="${errorMsg.message}" />
			</c:if>
			<form action="LoginServlet" method="post">
				<p>ログイン画面</p>
				ユーザー名：<br><input type="text" name="userId" class="form" required><br>
				パスワード：<br><input type="password" name="pass" class="form" required><br>
				<div class="login-button">
					<input type="submit" value="ログイン" class="button">
				</div>
			</form>
		</div>
	</div>
	<jsp:include page="WEB-INF/jsp/footer.jsp"/>
</body>
</html>