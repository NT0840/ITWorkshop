<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>アカウント登録完了</title>
	<jsp:include page="head.jsp"/>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<main class="register-container">
		<h1>アカウント登録完了</h1>
		<div class="register-content">
			<div class="registerOK">
				<p>アカウントの登録が完了しました。</p>
			</div>
			<div class="button-container">
				<a href="LoginServlet" class="back-button button">ログイン画面へ</a>
			</div>
		</div>
	</main>
	<jsp:include page="footer.jsp"/>
</body>
</html>