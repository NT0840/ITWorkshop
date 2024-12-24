<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ロードマップ表示画面</title>
	<jsp:include page="head.jsp"/>
    
    <script src="https://cdn.jsdelivr.net/npm/micromodal/dist/micromodal.min.js" defer></script>
    <script src="https://d3js.org/d3.v7.min.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/script.js" defer></script>
</head>
<body>
  <jsp:include page="header.jsp"/>
  <div class="roadmap-container">
    <!-- 上部ボタン -->
    <div class="roadmap-top-button-container">
      <a href="MypageServlet" class="button back-button to-mypage">マイページ</a>
      <button type="button" class="button" data-micromodal-trigger="modal-copy">コピー</button>
      <button type="button" class="button delete-button" data-micromodal-trigger="modal-delete">削除</button>
    </div>
  
    <!-- タイトルと日付 -->
    <div class="roadmap-title-container">
      <div class="roadmap-info-container">
        <h1><c:out value="${sessionScope.currentRoadmapId.roadmapName}" /></h1>
        <div class="roadmap-time">
          <p>作成日時：<fmt:formatDate value="${sessionScope.currentRoadmapId.roadmapCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
          <p>更新日時：<fmt:formatDate value="${sessionScope.currentRoadmapId.roadmapUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
        </div>
      </div>
      <!-- 追加ボタン -->
      <div class="add-button-container">
          <button class="button add-element-button" data-micromodal-trigger="modal-new-parent">親要素の追加</button>
          <button class="button add-element-button" data-micromodal-trigger="modal-new-child">子要素の追加</button>
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
        <form action="RoadmapServlet" method="post">
	        <main class="modal-copy">
		        <p>ロードマップ「<c:out value="${sessionScope.currentRoadmapId.roadmapName}" />」を<br>コピーして新しいロードマップとして保存します。<br>(コピー後はマイページに遷移します)<br></p>
		        <input type="text" class="form" name="roadmap-name" placeholder="新規ロードマップ名" maxlength="20" required>
	        </main>
	        <footer class="modal-footer">
		        <button type="button" class="button back-button" data-micromodal-close>戻る</button>
				<input type="hidden" name="action" value="copy">
			<button type="submit" class="button">作成</button>
	        </footer>
        </form>
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
          <button type="button" class="button back-button" data-micromodal-close>戻る</button>
          <form action="RoadmapServlet" method="get">
			<input type="hidden" name="action" value="delete">
			<button type="submit" class="button delete-button">削除</button>
          </form>
        </footer>
      </div>
    </div>
  </div>
  
  <!-- モーダル(親要素新規作成ー) -->
  <c:set var="roadmapIndex" value="${sessionScope.currentRoadmapId.roadmapId - 1}" />
  <div class="modal micromodal-slide" id="modal-new-parent" aria-hidden="true">
    <div class="modal-overlay" tabindex="-1" data-micromodal-close>
      <div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modal-new-parent-title">
        <header>
          <h2 id="modal-new-parent-title">親要素の新規追加</h2>
        </header>
        <form action="RoadmapServlet" method="post">
        	<input type="hidden" name="action" value="newParent">
        	<main class="modal-new-parent">
        		<p>親要素を新規作成して末尾に追加します。</p>
        		<label for="new-parent-name">新規親要素名：
          			<input type="text" class="form" id="new-parent-name" name="new-parent-name" maxlength="20" required>
          		</label>
        	</main>
        	<div class="modal-footer">
        		<button type="button" class="button back-button" data-micromodal-close>戻る</button>
        		<button type="submit" class="button">作成</button>
        	</div>
        </form>
      </div>
    </div>
  </div>
  
  <!-- モーダル(子要素新規作成ー) -->
  <div class="modal micromodal-slide" id="modal-new-child" aria-hidden="true">
    <div class="modal-overlay" tabindex="-1" data-micromodal-close>
      <div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modal-new-child-title">
        <header>
          <h2 id="modal-new-child-title">子要素の新規追加</h2>
        </header>
        <form action="RoadmapServlet" method="post">
        	<input type="hidden" name="action" value="newChild">
        	<main class="modal-new-child">
        		<p>子要素を新規作成して<br>指定の親要素の末尾に追加します。</p>
        		<label for="new-child-name">新規子要素名：
          			<input type="text" class="form" id="new-child-name" name="new-child-name" maxlength="20" required>
          		</label>
          		<label for="new-belong-to-parent-num">所属親要素：
   					<select name="new-belong-to-parent-num" id="new-belong-to-parent-num">
   						<c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
   							<option value="${parentElement.parentNum}" <c:if test="${parentElement.parentNum == 0}">selected</c:if>><c:out value="${parentElement.parentNum}:${parentElement.parentName}" /></option>
   						</c:forEach>
   					</select>
  				</label>
          		<label for="new-child-tag-num">タグ(任意)：
    				<select name="new-child-tag-num" id="new-child-tag-num">
					    <option value="0" selected>未選択</option>
					    <option value="1">必須</option>
					    <option value="2">選択</option>
					    <option value="3">余力があれば</option>
					</select>
      			</label>
        	</main>
        	<div class="modal-footer">
        		<button type="button" class="button back-button" data-micromodal-close>戻る</button>
        		<button type="submit" class="button">作成</button>
        	</div>
        </form>
      </div>
    </div>
  </div>
  
  
  <!-- モーダル(親要素描画) -->
  <c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
  	<div class="modal micromodal-slide" id="modalParent${parentElement.parentNum}" aria-hidden="true">
  		<div class="modal-overlay" tabindex="-1" data-micromodal-close>
  			<div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modalTitleParent${parentElement.parentNum}">
  				<form action="RoadmapServlet" method="post">
	  				<header class="element-header">
	  					<textarea type="text" class="element-title" name="parentName${parentElement.parentNum}" maxlength="20" required>${parentElement.parentName}</textarea>
	  					<div class="element-button-container">
	  						<button type="button" class="button delete-button element-delete-button" data-micromodal-trigger="modalParentDelete${parentElement.parentNum}">要素の削除</button>
	  					</div>
	  				</header>
	  				<main class="modal-element">
	  					<div class="element-time">
	  						<p>作成日時：<fmt:formatDate value="${parentElement.parentCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
	  						<p>更新日時：<fmt:formatDate value="${parentElement.parentUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
						</div>
	          			<div>
          					<div class="element-content-upside">
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
         								<option value="0" <c:if test="${parentElement.parentStatusNum == 0}">selected</c:if>>未完了</option>
         								<option value="1" <c:if test="${parentElement.parentStatusNum == 1}">selected</c:if>>完了</option>
		          					</select>
	          					</label>
          					</div>
          					<div class="element-content-downside">
          						<label for="parentDescription${parentElement.parentNum}">内容:</label>
          						<div class="element-textarea-container" onclick="focusTextarea(event)">
          							<textarea id="parentDescription${parentElement.parentNum}" name="parentDescription${parentElement.parentNum}" maxlength="10000">${parentElement.parentDescription}</textarea>
          						</div>
          					</div>
          					<div class="modal-footer">
          						<button type="button" class="button back-button" data-micromodal-close>戻る</button>
          						<div>
          							<input type="hidden" name="currentParentNum" value="${parentElement.parentNum}">
          							<input type="hidden" name="action" value="updateParent">
          							<button type="submit" class="button">変更</button>
          						</div>
          					</div>
	          			</div>
	          		</main>
	          	</form>
  			</div>
  		</div>
 	</div>        	
  </c:forEach>
  
  <!-- モーダル(親要素削除) -->
  <c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
	<div class="modal micromodal-slide" id="modalParentDelete${parentElement.parentNum}" aria-hidden="true">
		<div class="modal-overlay" tabindex="-1" data-micromodal-close>
			<div class="modal-container-delete" role="dialog" aria-modal="true" aria-labelledby="modal-parent-delete-title">
				<header>
					<h2 id="modal-parent-delete-title">親要素の削除</h2>
				</header>
				<main class="modal-delete">
					<p>親要素「<c:out value="${parentElement.parentName}" />」を削除します。<br>本当によろしいですか？</p>
				</main>
				<footer class="modal-footer">
					<button type="button" class="button back-button" data-micromodal-close>戻る</button>
					<form action="RoadmapServlet" method="get">
						<input type="hidden" name="action" value="delete-parent">
						<input type="hidden" name="currentParentNum" value="${parentElement.parentNum}">
						<button type="submit" class="button delete-button">削除</button>
					</form>
				</footer>
			</div>
		</div>
	</div>
  </c:forEach>
  
  <!-- モーダル(子要素描画) -->
  <c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
  	  <c:set var="parentIndex" value="${parentElement.parentNum - 1}" />
	  <c:forEach var="childElement" items="${roadmaps[roadmapIndex].childElements[parentIndex]}">
	  	<div class="modal micromodal-slide" id="modalChild${parentElement.parentNum}-${childElement.childNum}" aria-hidden="true">
	  		<div class="modal-overlay" tabindex="-1" data-micromodal-close>
	  			<div class="modal-container" role="dialog" aria-modal="true" aria-labelledby="modalTitleChild${parentElement.parentNum}-${childElement.childNum}">
	  				<form action="RoadmapServlet" method="post">
		  				<header class="element-header">
	  						<input type="text" class="element-title" name="childName${parentElement.parentNum}-${childElement.childNum}" value="${childElement.childName}" maxlength="20" required>
	  						<div class="element-button-container">
	  							<button type="button" class="button delete-button element-delete-button" data-micromodal-trigger="modalChildDelete${parentElement.parentNum}-${childElement.childNum}">要素の削除</button>
	  						</div>
	  					</header>
		  				<main class="modal-element">
		  					<div class="element-time">
		  						<p>作成日時：<fmt:formatDate value="${childElement.childCreateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
		  						<p>更新日時：<fmt:formatDate value="${childElement.childUpdateAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
							</div>
		          			<div>
	          					<div class="element-content-upside">
	          						<!-- 所属親要素の変更は別モーダルにて実装予定
	          						<label for="belongToParentNum${childElement.childNum}">所属親要素：
			          					<select name="belongToParentNum${childElement.childNum}" id="belongToParentNum${childElement.childNum}">
			          						<c:forEach var="parentElementOption" items="${roadmaps[roadmapIndex].parentElements}">
			          							<c:choose>
			          								<c:when test="${parentElementOption.parentNum == childElement.parentNum}">
			          									<option value="${parentElementOption.parentNum}" selected><c:out value="${parentElementOption.parentNum}:${parentElementOption.parentName}" /></option>
			          								</c:when>
			          								<c:otherwise>
			          									<option value="${parentElementOption.parentNum}"><c:out value="${parentElementOption.parentNum}:${parentElementOption.parentName}" /></option>
			          								</c:otherwise>
			          							</c:choose>
			          						</c:forEach>
			          					</select>
		          					</label>
		          					-->
		          					<label for="childNum${parentElement.parentNum}-${childElement.childNum}">子要素番号(挿入位置):
			          					<select name="childNum${parentElement.parentNum}-${childElement.childNum}" id="childNum${parentElement.parentNum}-${childElement.childNum}">
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
		          					<label for="childTagNum${parentElement.parentNum}-${childElement.childNum}">タグ:
		          						<select name="childTagNum${parentElement.parentNum}-${childElement.childNum}" id="childTagNum${parentElement.parentNum}-${childElement.childNum}">
										    <option value="0" <c:if test="${childElement.childTagNum == 0}">selected</c:if>>未選択</option>
										    <option value="1" <c:if test="${childElement.childTagNum == 1}">selected</c:if>>必須</option>
										    <option value="2" <c:if test="${childElement.childTagNum == 2}">selected</c:if>>選択</option>
										    <option value="3" <c:if test="${childElement.childTagNum == 3}">selected</c:if>>余力があれば</option>
										</select>
		          					</label>
		          					<label for="childStatusNum${parentElement.parentNum}-${childElement.childNum}">ステータス:
			          					<select name="childStatusNum${parentElement.parentNum}-${childElement.childNum}" id="childStatusNum${parentElement.parentNum}-${childElement.childNum}">
	         								<option value="0" <c:if test="${childElement.childStatusNum == 0}">selected</c:if>>未完了</option>
	         								<option value="1" <c:if test="${childElement.childStatusNum == 1}">selected</c:if>>完了</option>
			          					</select>
		          					</label>
	          					</div>
	          					<div class="element-content-downside">
	          						<label for="childDescription${parentElement.parentNum}-${childElement.childNum}">内容:</label>
	          						<div class="element-textarea-container" onclick="focusTextarea(event)">
	          							<textarea id="childDescription${parentElement.parentNum}-${childElement.childNum}" name="childDescription${parentElement.parentNum}-${childElement.childNum}" maxlength="10000">${childElement.childDescription}</textarea>
	          						</div>
	          					</div>
	          					<div class="modal-footer">
	          						<button type="button" class="button back-button" data-micromodal-close>戻る</button>
	          						<div>
	          							<input type="hidden" name="currentParentNum" value="${parentElement.parentNum}">
	          							<input type="hidden" name="currentChildNum" value="${childElement.childNum}">
	          							<input type="hidden" name="action" value="updateChild">
	          							<button type="submit" class="button">変更</button>
	          						</div>
	          					</div>
	          				</div>
          				</main>
          			</form>
	  			</div>
	  		</div>
	 	</div>        	
	  </c:forEach>
  </c:forEach>
  
  <!-- モーダル(子要素削除) -->
  <c:forEach var="parentElement" items="${roadmaps[roadmapIndex].parentElements}">
  	<c:set var="parentIndex" value="${parentElement.parentNum - 1}" />
    <c:forEach var="childElement" items="${roadmaps[roadmapIndex].childElements[parentIndex]}">
		<div class="modal micromodal-slide" id="modalChildDelete${parentElement.parentNum}-${childElement.childNum}" aria-hidden="true">
			<div class="modal-overlay" tabindex="-1" data-micromodal-close>
				<div class="modal-container-delete" role="dialog" aria-modal="true" aria-labelledby="modal-child-delete-title">
					<header>
						<h2 id="modal-child-delete-title">子要素の削除</h2>
					</header>
					<main class="modal-delete">
						<p>子要素「<c:out value="${childElement.childName}" />」を削除します。<br>本当によろしいですか？</p>
					</main>
					<footer class="modal-footer">
						<button type="button" class="button back-button" data-micromodal-close>戻る</button>
						<form action="RoadmapServlet" method="get">
							<input type="hidden" name="action" value="delete-child">
							<input type="hidden" name="currentParentNum" value="${parentElement.parentNum}">
							<input type="hidden" name="currentChildNum" value="${childElement.childNum}">
							<button type="submit" class="button delete-button">削除</button>
						</form>
					</footer>
				</div>
			</div>
		</div>
    </c:forEach>
  </c:forEach>
</body>
</html>
