<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>ロードマップ新規作成</title>
		<!-- リセットCSS -->
		<link rel="stylesheet" href="https://unpkg.com/ress/dist/ress.min.css">
		<!-- オリジナルCSS -->
	<link rel="stylesheet" href="css/style.css">
	<script src="js/roadmapNew.js" defer></script>
</head>
<body>
	<h1>ロードマップの新規作成</h1>
	<form id="roadmap-form" action="RoadmapServlet" method="POST">
		<input type="hidden" name="action" value="newRoadmap">
		<label for="roadmap-name">ロードマップの名称：</label>
		<input type="text" id="roadmap-name" name="roadmap-name" required>
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
			<a href="MypageServlet" class="back-button">戻る</a>
			<button type="submit" class="send-button">作成</button>
		</div>
	</form>
</body>
</html>