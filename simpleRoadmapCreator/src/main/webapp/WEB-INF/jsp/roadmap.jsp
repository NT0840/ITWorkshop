<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.*, java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ロードマップ</title>
	<!-- リセットCSS -->
	<link rel="stylesheet" href="https://unpkg.com/ress/dist/ress.min.css">
	<!-- オリジナルCSS -->
    <link rel="stylesheet" href="css/style.css">
    <script src="js/script.js" defer></script>
</head>
<body>
  <!-- 上部ボタン -->
  <div class="top-buttons">
    <button class="btn mypage">マイページ</button>
    <button class="btn copy">コピー</button>
    <button class="delete-button delete">削除</button>
  </div>

  <!-- タイトルと日付 -->
  <div class="title">
    <h1>ロードマップタイトル</h1>
    <p>作成日時：XXXX/XX/XX</p>
    <p>更新日時：XXXX/XX/XX</p>
  </div>

  <!-- 親要素と子要素 -->
  <div class="roadmap">
    <div class="parent-element">
      <div class="box parent">親要素1</div>
      <div class="line"></div>
      <div class="box child">子要素1</div>
      <div class="box child">子要素2</div>
    </div>
    <div class="parent-element">
      <div class="box parent">親要素2</div>
    </div>
    <div class="parent-element">
      <div class="box parent">親要素3</div>
      <div class="line"></div>
      <div class="box child">子要素1</div>
    </div>
  </div>

  <!-- 追加ボタン -->
  <div class="add-buttons">
    <button class="btn add-parent">親要素の追加</button>
    <button class="btn add-child">子要素の追加</button>
  </div>
</body>
</html>
