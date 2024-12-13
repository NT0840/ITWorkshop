<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>簡易ロードマップ作成アプリ</title>
</head>
<body>
	<h1>ログイン画面</h1>
	<c:if test="${errorMsg != null}">
		<c:out value="${errorMsg.fieldName}" /></br>
		<c:out value="${errorMsg.message}" />
	</c:if>
	<form action="LoginServlet" method="post">
		ユーザー名：<input type="text" name="userId"><br>
		パスワード：<input type="password" name="pass"><br>
		<input type="submit" value="ログイン">
	</form>
</body>
</html>