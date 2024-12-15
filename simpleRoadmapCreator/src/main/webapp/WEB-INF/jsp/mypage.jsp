<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マイページ</title>
</head>
<body>
    <h1>マイページ</h1>
    <a href="RoadmapServlet?action=new">新規作成</a>

    <h2>ロードマップ一覧</h2>
    <table border="1">
        <thead>
            <tr>
                <th>名称</th>
                <th>作成日時</th>
                <th>更新日時</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="roadmap" items="${roadmaps}">
            <tr>
                <td>${roadmap.roadmapId.roadmapName}</td>
                <td>${roadmap.roadmapId.roadmapCreateAt}</td>
                <td>${roadmap.roadmapId.roadmapUpdateAt}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <form action="LogoutServlet" method="post">
        <button type="submit">ログアウト</button>
    </form>
</body>
</html>