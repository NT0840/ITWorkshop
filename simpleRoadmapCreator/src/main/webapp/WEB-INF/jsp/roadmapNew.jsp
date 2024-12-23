<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ロードマップ新規作成</title>
	<jsp:include page="head.jsp"/>
	
	<script src="js/roadmapNew.js" defer></script>
</head>
<body>
	<jsp:include page="header.jsp"/>
	<div class="roadmap-new-container">
		<h1>ロードマップの新規作成</h1>
		<form id="roadmap-form" action="RoadmapServlet" method="post">
			<input type="hidden" name="action" value="newRoadmap">
			<label for="roadmap-name">名称：</label>
			<input type="text" id="roadmap-name" name="roadmap-name" maxlength="20" required>
			<div class="parent-container">
				<div class="element-heading">
					<h2 class="parent-heading">親要素</h2>
					<h2 class="child-heading">子要素</h2>
				</div>	
				<div class="parent-row">
					<div class="parent-input-group">
					</div>
					<div class="child-container">
					</div>
				</div>
				<button type="button" class="add-button parent-button" onclick="addParentRow()">+親要素の追加</button>
			</div>
		
			<div class="button-container">
				<a href="MypageServlet" class="back-button button">戻る</a>
				<button type="submit" class="button">作成</button>
			</div>
		</form>
	</div>
	<jsp:include page="footer.jsp"/>
</body>
</html>