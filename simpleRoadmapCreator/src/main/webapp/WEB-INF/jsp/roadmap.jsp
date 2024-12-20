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
    <script src="js/script.js" defer></script>
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
        <div class="roadmap-time">
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
    	<jsp:include page="roadmapWrite.jsp" />
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
  
  
  <!-- モーダル(親要素描画) -->
  <c:set var="roadmapIndex" value="${sessionScope.currentRoadmapId.roadmapId - 1}" />
  <c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
  	<div class="modal micromodal-slide" id="modalParent${parentElement.parentNum}" aria-hidden="true">
  		<div class="modal-overlay" tabindex="-1" data-micromodal-close>
  			<div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modalTitleParent${parentElement.parentNum}">
  				<header class="parent-header">
  					<h2 id="modalTitleParent${parentElement.parentNum}">${parentElement.parentName}</h2>
  				</header>
  				<main class="modal-parent">
  					<div class="parent-time">
  						<p>作成日時：<fmt:formatDate value="${parentElement.parentCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
  						<p>更新日時：<fmt:formatDate value="${parentElement.parentUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
					</div>
          			<div>
          				<form action="RoadmapServlet" method="post">
          					<div class="parent-content-upside">
	          					<label for="parentNum${parentElement.parentNum}">親要素番号(挿入位置):
		          					<select name="parentNum${parentElement.parentNum}" id="parentNum${parentElement.parentNum}">
		          						<c:forEach var="parentElementOption" items="${roadmaps[roadmapIndex].parentElements}">
		          							<c:choose>
		          								<c:when test="${parentElement.parentNum == parentElementOption.parentNum}">
		          									<option value="${parentElementOption.parentNum}" selected><c:out value="${parentElementOption.parentNum}:${parentElementOption.parentName}" /></option>
		          								</c:when>
		          								<c:otherwise>
		          									<option value="${parentElementOption.parentNum}"><c:out value="${parentElementOption.parentNum}:${parentElementOption.parentName}" /></option>
		          								</c:otherwise>
		          							</c:choose>
		          						</c:forEach>
		          					</select>
	          					</label>
	          					<label for="parentStatusNum${parentElement.parentNum}">ステータス:
		          					<select name="parentStatusNum${parentElement.parentNum}" id="parentStatusNum${parentElement.parentNum}">
		          						<c:choose>
		         							<c:when test="${parentElement.parentStatusNum == 0}">
		         								<option value="0" selected>未完了</option>
		         								<option value="1">完了</option>
		         							</c:when>
		         							<c:otherwise>
		         								<option value="0">未完了</option>
		         								<option value="1" selected>完了</option>
		         							</c:otherwise>
		         						</c:choose>
		          					</select>
	          					</label>
          					</div>
          					<div class="parent-content-downside">
          						<label for="parentDescription${parentElement.parentNum}">内容:</label>
          						<div class="parent-textarea-container" onclick="focusTextarea(event)">
          							<textarea id="parentDescription${parentElement.parentNum}" name="parentDescription${parentElement.parentNum}">${parentElement.parentDescription}</textarea>
          						</div>
          					</div>
          					<div class="modal-footer">
          						<button class="button back-button" data-micromodal-close>戻る</button>
          						<div>
          							<input type="hidden" name="roadmapId" value="${sessionScope.currentRoadmapId.roadmapId}">
          							<input type="hidden" name="currentParentNum" value="${parentElement.parentNum}">
          							<input type="hidden" name="action" value="updateParent">
          							<button type="submit" class="button">変更</button>
          						</div>
          					</div>
          				</form>
          			</div>
          		</main>
  			</div>
  		</div>
 	</div>        	
  </c:forEach>
  
   <!-- モーダル(子要素描画) -->
  <c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
  	  <c:set var="parentIndex" value="${parentElement.parentNum}" />
	  <c:forEach var="childElement" items="${roadmaps[roadmapIndex].childElements[parentIndex]}">
	  	<div class="modal micromodal-slide" id="modalChild${childElement.childNum}" aria-hidden="true">
	  		<div class="modal-overlay" tabindex="-1" data-micromodal-close>
	  			<div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modalTitleChild${childElement.childNum}">
	  				<header class="child-header">
  						<h2 id="modalTitleChild${childElement.childNum}">${childElement.childName}</h2>
  					</header>
	  				<main class="modal-child">
  					<div class="child-time">
  						<p>作成日時：<fmt:formatDate value="${childElement.childCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
  						<p>更新日時：<fmt:formatDate value="${childElement.childUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
					</div>
          			<div>
          				<form action="RoadmapServlet" method="post">
          					<div class="child-content-upside">
	          					<label for="childNum${childElement.childNum}">子要素番号(挿入位置):
		          					<select name="childNum${childElement.childNum}" id="childNum${childElement.childNum}">
		          						<c:forEach var="childElementOption" items="${roadmaps[roadmapIndex].childElements[parentIndex]}">
		          							<c:choose>
		          								<c:when test="${childElement.childNum == childElementOption.childNum}">
		          									<option value="${childElementOption.childNum}" selected><c:out value="${childElementOption.childNum}:${childElementOption.childName}" /></option>
		          								</c:when>
		          								<c:otherwise>
		          									<option value="${childElementOption.childNum}"><c:out value="${childElementOption.childNum}:${childElementOption.childName}" /></option>
		          								</c:otherwise>
		          							</c:choose>
		          						</c:forEach>
		          					</select>
	          					</label>
	          					<label for="childStatusNum${childElement.childNum}">ステータス:
		          					<select name="childStatusNum${childElement.childNum}" id="childStatusNum${childElement.childNum}">
		          						<c:choose>
		         							<c:when test="${childElement.childStatusNum == 0}">
		         								<option value="0" selected>未完了</option>
		         								<option value="1">完了</option>
		         							</c:when>
		         							<c:otherwise>
		         								<option value="0">未完了</option>
		         								<option value="1" selected>完了</option>
		         							</c:otherwise>
		         						</c:choose>
		          					</select>
	          					</label>
          					</div>
          					<div class="child-content-downside">
          						<label for="childDescription${childElement.childNum}">内容:</label>
          						<div class="child-textarea-container" onclick="focusTextarea(event)">
          							<textarea id="childDescription${childElement.childNum}" name="childDescription${childElement.childNum}">${childElement.childDescription}</textarea>
          						</div>
          					</div>
          					<div class="modal-footer">
          						<button class="button back-button" data-micromodal-close>戻る</button>
          						<div>
          							<input type="hidden" name="roadmapId" value="${sessionScope.currentRoadmapId.roadmapId}">
          							<input type="hidden" name="currentChildNum" value="${childElement.childNum}">
          							<input type="hidden" name="action" value="updateChild">
          							<button type="submit" class="button">変更</button>
          						</div>
          					</div>
          				</form>
          			</div>
          		</main>
	  			</div>
	  		</div>
	 	</div>        	
	  </c:forEach>
  </c:forEach>


</body>
</html>
