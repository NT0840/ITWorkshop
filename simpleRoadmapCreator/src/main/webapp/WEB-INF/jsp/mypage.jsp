<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>マイページ</title>
	<jsp:include page="head.jsp"/>
	<script src="https://cdn.jsdelivr.net/npm/micromodal/dist/micromodal.min.js" defer></script>
    <script src="js/mypage.js" defer></script>
</head>
<body>
	<jsp:include page="header.jsp"/>
    <div class="mypage-container">
        <div class="menu-container">
        	<div>
        		<h1>マイページ</h1>
        		<p>ようこそ、${userId.userId}さん！</p>
        	</div>
        	<div class="mypage-button-container">
	        	<form action="LogoutServlet" method="post">
	        		<button type="button" class="button delete-button" data-micromodal-trigger="modal-delete-account">アカウント削除</button>
	                <button type="submit" class="button">ログアウト</button>
	            </form>
        	</div>
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
            <c:if test="${fn:length(roadmaps) == 0}">
		    	<p>作成されたロードマップはありません。</p>
			</c:if>
        </div>
    </div> 
    <jsp:include page="footer.jsp"/>
    
  <!-- モーダル(アカウント削除) -->
  <div class="modal micromodal-slide" id="modal-delete-account" aria-hidden="true">
    <div class="modal-overlay" tabindex="-1" data-micromodal-close>
      <div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modal-delete-account-title">
        <header>
          <h2 id="modal-delete-account-title">アカウントの削除</h2>
        </header>
        <main class="modal-delete">
          <p>アカウントを削除します。<br>本当によろしいですか？</p>
        </main>
        <footer class="modal-footer">
          <button type="button" class="button back-button" data-micromodal-close>戻る</button>
          <form action="AccountServlet" method="get">
			<button type="submit" class="button delete-button">削除</button>
          </form>
        </footer>
      </div>
    </div>
  </div>
    
</body>
</html>