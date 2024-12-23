<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>アカウント登録</title>
	<jsp:include page="head.jsp"/>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<main class="register-container">
		<h1>アカウント登録</h1>
		<div class="register-content">
			<c:if test="${errorMsg != null}">
				<div class="error registerNG">
					<p><c:out value="${errorMsg.fieldName}" /><br><c:out value="${errorMsg.message}" /></p>
				</div>
			</c:if>
			<form action="RegisterServlet" method="post">
				<div class="register-form">
					<p>ユーザー名：<input type="text" name="userId" class="form" required></p>
					<p>パスワード：<input type="password" name="pass" class="form" required></p>
					<p>パスワード(確認用)：<input type="password" name="passConfirm" class="form" required></p>
				</div>
				<div class="button-container">
					<a href="LoginServlet" class="back-button button">戻る</a>
					<button type="submit" class="button">登録</button>
				</div>
			</form>
		</div>
	</main>
	<jsp:include page="footer.jsp"/>
</body>
</html>