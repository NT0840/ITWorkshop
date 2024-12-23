<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ロードマップ作成アプリ</title>
	<jsp:include page="WEB-INF/jsp/head.jsp"/>
</head>
<body>
	<jsp:include page="WEB-INF/jsp/header.jsp"/>
	<main class="top-container">
		<div class="top-upside-container">
			<div class="ad-container">
				<div>
					<div class="top-text">
						<p class="top-text-title fade-in">ロードマップ作成アプリ</p>
						<p class="top-text-description fade-in">シンプルなロードマップで目標設定</p>
						<p class="top-text-description fade-in">大・中・小の各目標の進捗を可視化</p>
					</div>
				</div>
			</div>
			<div class="login-container">
				<form action="LoginServlet" method="post">
					<p class="login-text">ログイン画面</p>
					<c:if test="${errorMsg != null}">
						<div class="error">
							<p><c:out value="${errorMsg.fieldName}" /><br><c:out value="${errorMsg.message}" /></p>
						</div>
					</c:if>
					<div class="login-form">
						<p>ユーザー名：<br><input type="text" name="userId" class="form" required><br></p>
						<p>パスワード：<br><input type="password" name="pass" class="form" required><br></p>
					</div>
					<div class="login-button">
						<a href="RegisterServlet" class="button to-mypage">ユーザー登録</a>
						<input type="submit" value="ログイン" class="button">
					</div>
				</form>
			</div>
		</div>
	</main>
	<jsp:include page="WEB-INF/jsp/footer.jsp"/>
</body>
</html>