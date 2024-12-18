<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ロードマップ表示画面</title>
	<!-- リセットCSS -->
	<link rel="stylesheet" href="https://unpkg.com/ress/dist/ress.min.css">
	<!-- オリジナルCSS -->
    <link rel="stylesheet" href="css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/micromodal/dist/micromodal.min.js" defer></script>
    <script src="https://d3js.org/d3.v7.min.js" defer></script>
    <script src="js/roadmapWrite.js" defer></script>
</head>
<body>
  <div class="roadmap-container">
    <!-- 上部ボタン -->
    <div class="roadmap-top-button-container">
      <a href="MypageServlet" class="button back-button to-mypage">マイページ</a>
      <button class="button" data-micromodal-trigger="modal-copy">コピー</button>
      <button class="button delete-button" data-micromodal-trigger="modal-delete">削除</button>
    </div>
  
    <!-- タイトルと日付 -->
    <div class="roadmap-title-container">
      <h1><c:out value="${sessionScope.currentRoadmapId.roadmapName}" /></h1>
      <div class="roadmap-info-container">
        <div class="roadmap-title">
          <p>作成日時：<fmt:formatDate value="${sessionScope.currentRoadmapId.roadmapCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
          <p>更新日時：<fmt:formatDate value="${sessionScope.currentRoadmapId.roadmapUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
        </div>
          <!-- 追加ボタン -->
        <div class="add-button-container">
          <button class="button">親要素の追加</button>
          <button class="button">子要素の追加</button>
        </div>
      </div>
    </div>
    
    <main class="roadmap-main">
    </main>
  
  
    

  
  <!-- モーダル(ロードマップコピー) -->
  <div class="modal micromodal-slide" id="modal-copy" aria-hidden="true">
    <div class="modal-overlay" tabindex="-1" data-micromodal-close>
      <div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modal-copy-title">
        <header>
          <h2 id="modal-copy-title">ロードマップのコピー</h2>
        </header>
        <main class="modal-copy">
          <p>ロードマップ「<c:out value="${sessionScope.currentRoadmapId.roadmapName}" />」を<br>コピーして新しいロードマップとして保存します。<br>(コピー後はマイページに遷移します)<br></p>
          <input type="text" class="form" placeholder="新規ロードマップ名">
        </main>
        <footer class="modal-footer">
          <button class="button back-button" data-micromodal-close>戻る</button>
          <form action="RoadmapServlet" method="post">
			<input type="hidden" name="action" value="copy">
			<input type="hidden" name="roadmapId" value="${sessionScope.currentRoadmapId.roadmapId}">
			<button type="submit" class="button">作成</button>
          </form>
        </footer>
      </div>
    </div>
  </div>
  
  <!-- モーダル(ロードマップ削除) -->
  <div class="modal micromodal-slide" id="modal-delete" aria-hidden="true">
    <div class="modal-overlay" tabindex="-1" data-micromodal-close>
      <div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modal-delete-title">
        <header>
          <h2 id="modal-delete-title">ロードマップの削除</h2>
        </header>
        <main class="modal-delete">
          <p>ロードマップ「<c:out value="${sessionScope.currentRoadmapId.roadmapName}" />」を削除します。<br>本当によろしいですか？</p>
        </main>
        <footer class="modal-footer">
          <button class="button back-button" data-micromodal-close>戻る</button>
          <form action="RoadmapServlet" method="get">
			<input type="hidden" name="action" value="delete">
			<input type="hidden" name="roadmapId" value="${sessionScope.currentRoadmapId.roadmapId}">
			<button type="submit" class="button delete-button">削除</button>
          </form>
        </footer>
      </div>
    </div>
  </div>
  


</body>
</html>
