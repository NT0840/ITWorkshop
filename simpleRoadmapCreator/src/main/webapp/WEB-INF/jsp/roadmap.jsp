<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<title>ロードマップ</title>
<!-- リセットCSS -->
<link rel="stylesheet" href="https://unpkg.com/ress/dist/ress.min.css">
<!-- オリジナルCSS -->
<link rel="stylesheet" href="css/style.css">
<script src="js/roadmap.js" defer></script> </head>
<body>
    <div class="roadmap-header">
        <div class="roadmap-info">
            <h2><c:out value="${sessionScope.currentRoadmapId.roadmapName}" /></h2>
            <p>作成日時: <c:out value="${sessionScope.currentRoadmapId.roadmapCreateAt}" /></p>
            <p>更新日時: <c:out value="${sessionScope.currentRoadmapId.roadmapUpdateAt}" /></p>
        </div>
        <div class="add-buttons">
            <button type="button" class="add-button" onclick="addParentElement()">親要素の追加</button>
            <button type="button" class="add-button" onclick="addChildElement()">子要素の追加</button>
        </div>
        <div class="roadmap-actions">
            <a href="MypageServlet" class="button">マイページ</a>
            <button type="button" class="button">コピー</button>
            <button type="button" class="button">削除</button>
        </div>
    </div>

    <div class="roadmap-content">
        <div class="parent-elements">
            <c:forEach var="parentElement" items="${roadmap.parentElements}" varStatus="status">
                <div class="parent-element" data-parent-num="${parentElement.parentNum}">
                    <p class="element-name">${parentElement.parentName}</p>
                    <div class="child-elements">
                        <c:forEach var="childElement" items="${roadmap.childElements[status.index]}">
                            <div class="child-element" data-child-num="${childElement.childNum}">
                                <p class="element-name">${childElement.childName}</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>