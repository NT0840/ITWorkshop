<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>アカウント削除完了画面</title>
	<jsp:include page="head.jsp"/>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<main class="account-deleteOK-container">
		<h1>アカウント削除完了</h1>
		<div class="account-deleteOK-content">
			<div class="account-deleteOK">
				<p>アカウントの削除が完了しました。</p>
				<p>ご利用ありがとうございました。。</p>
			</div>
			<div class="button-container">
				<a href="LoginServlet" class="back-button button">ログイン画面へ</a>
			</div>
		</div>
	</main>
	<jsp:include page="footer.jsp"/>
</body>
</html>