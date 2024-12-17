<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マイページ</title>
	<!-- リセットCSS -->
	<link rel="stylesheet" href="https://unpkg.com/ress/dist/ress.min.css">
	<!-- オリジナルCSS -->
    <link rel="stylesheet" href="css/style.css">
    <script src="js/script.js" defer></script>
</head>
<body>
    <div class="mypage-container">
        <div class="menu-container">
            <h1>マイページ</h1>
            <form action="LogoutServlet" method="post">
                <button type="submit" class="button">ログアウト</button>
            </form>
        </div>
        <div class="roadmap-list">
            <div class="roadmap-headhing">
                <h2>ロードマップ一覧</h2>
                <a href="RoadmapServlet?action=new" class="button">新規作成</a>    
            </div>
            <table class="roadmap-table" id="roadmap-table">
                <thead>
                    <colgroup>
                        <col class="col-1">
                        <col class="col-2">
                        <col class="col-3">
                    </colgroup>
                    <tr>
                        <th>名称</th>
                        <th>作成日時</th>
                        <th>更新日時</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="roadmap" items="${roadmaps}">
                    <tr class="roadmap-row" data-href="RoadmapServlet?select=${roadmap.roadmapId.roadmapId}">
                        <td >${roadmap.roadmapId.roadmapName}</td>
                        <td ><fmt:formatDate value="${roadmap.roadmapId.roadmapCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                        <td ><fmt:formatDate value="${roadmap.roadmapId.roadmapUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div> 
</body>
</html>