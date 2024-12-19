<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="model.*, java.util.*" %>

<script>
document.addEventListener('DOMContentLoaded', function () {
    MicroModal.init();

    //特定文字数以上を「...」に置き換える
    function truncateText(text, maxLength) {
   	return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
   	}

	// 現在表示中のロードマップID(数値)の取得
	<c:set var="roadmapIndex" value="${sessionScope.currentRoadmapId.roadmapId - 1}" />
   	const roadmapId = <c:out value="${roadmapIndex}" />;

   	// 現在表示中のロードマップの要素のリストを取得
   	<c:set var="parentElementsJson" value="${sessionScope.parentElementsJson}" />
   	<c:set var="childElementsJson" value="${sessionScope.childElementsJson}" />
   	
   	// JSONデータをJavaScript変数に格納
    const parentElements = <c:out value="${parentElementsJson}" escapeXml="false" />; // JSTLを使用, エスケープを無効にする
    const childElements = <c:out value="${childElementsJson}" escapeXml="false" />; // JSTLを使用
   	
 	// 長方形のデータを格納する配列
   	let rectangles = [];

    // SVG領域の設定
    const width = 800;
    let height = 600; // 初期値として600を設定
    
   	//長方形の座標とサイズを変数に格納
   	const rectX = 50; // 開始x座標
   	const rectY = 50; // 開始y座標
   	const rectWidth = 300; // 横幅
   	const rectHeight = 50; // 縦幅
   	const rectMarginTop = 40; // 縦方向のマージン
   	const rectRadiusX = 10; // 角の丸み
   	const rectRadiusY = 10; // 角の丸み

   	let yOffset = 0; // Y軸のオフセット
   	
 	// for文を使用して長方形のデータを配列に追加
   	for (let i = 0; i < parentElements.length; i++) {
   	    rectangles.push({
   	        x: rectX,
   	        y: rectY + yOffset,
   	        width: rectWidth,
   	        height: rectHeight, 
   	        rx: rectRadiusX, 
   	        ry: rectRadiusY, 
   	     	name: parentElements[i].parentName
   	    });
   	    if (childElements[i] && childElements[i].length > 0) { // 左辺はi番目の要素が存在するか
   	   	    for (let j = 0; j < childElements[i].length; j++) {
   	   	  		rectangles.push({
	     	        x: width - (rectX + rectWidth),
	     	        y: rectY + yOffset,
	     	        width: rectWidth,
	     	        height: rectHeight, 
	     	        rx: rectRadiusX, 
	     	        ry: rectRadiusY, 
	     	     	name: childElements[i][j].childName
     	    	});
 	 	    	yOffset += (rectHeight + rectMarginTop);
   	   	    }
   	   	} else {
   	   		yOffset += (rectHeight + rectMarginTop);
   	   	}
   	}

    // SVG要素を追加し、高さを動的に設定する
   	height = rectY + yOffset + 50; // 最後の長方形が描画される位置に少し余裕を持たせる;

   	//<main class="roadmap-main">内にSVG要素を追加
   	const svg = d3.select("main.roadmap-main")
   	  .append("svg")
   	  .attr("viewBox", "0 0 " + width + " " + height) // viewBoxを設定
   	  .attr("preserveAspectRatio", "xMinYMin meet") // アスペクト比を維持
   	  .attr("width", "100%") // 横幅を100%に設定
   	  

   	//SVG領域に長方形を描画
   	svg.selectAll("g") // SVG要素内のグループ（g要素）の名称は慣例
      .data(rectangles)
      .enter()
   	  .append("g")
   	  .each(function(d, i) { // each関数を使用して、各データ項目に対して長方形とテキストを個別に追加
   	   	  // 図形を追加
   	   	  d3.select(this)
    	    .append("rect")
	   	    .attr("class", "rectangle") // CSSクラスを適用
	   	    .attr("x", d => d.x) // 開始x座標
	   	    .attr("y", d => d.y) // 開始y座標
	   	    .attr("width", d => d.width) // 横幅
	   	    .attr("height", d => d.height) // 縦幅
	   	    .attr("rx", d => d.rx)  // 角の丸みを追加
	        .attr("ry", d => d.ry);  // 角の丸みを追加

	       // テキストを追加
	       <c:set var="count" value= "i" />
	       d3.select(this)
            .append("text")
            .attr("class", "text")
            .attr("x", d.x + d.width / 2)
            .attr("y", d.y + d.height / 2)
            .attr("dominant-baseline", "middle")
            .attr("text-anchor", "middle")
            .text(d => d.name);
   	  });

 	// 大項目と子要素を結ぶ線を描画する処理
    yOffset = 50; // Yオフセットリセット（最初から計算）
    
    for (let i = 0; i < parentElements.length; i++) {
        let parentY = rectY + yOffset - (rectHeight / 2); // 大項目中央Y座標
        
        if (childElements[i] && childElements[i].length > 0) { 
            for (let j = 0; j < childElements[i].length; j++) {
                let childY = parentY + (j * (rectHeight + rectMarginTop)); // 子要素中央Y座標
                
                svg.append("line")
                    .attr("class", "line")
                    .attr("x1", rectX + rectWidth)   // 大項目右端X座標
                    .attr("y1", parentY)              // 大項目中央Y座標
                    .attr("x2", width - (rectX + rectWidth))      // 子要素左端X座標
                    .attr("y2", childY);   // 子要素中央Y座標

                    yOffset += (rectHeight + rectMarginTop); // オフセット加算
            }
        } else {
        	yOffset += (rectHeight + rectMarginTop); // 次の大項目へのオフセット加算
        }

        if (parentElements[i + 1]) {
        	svg.append("line")
	            .attr("class", "line")
	            .attr("x1", rectX + (rectWidth / 2)) // 大項目の中央X座標
	            .attr("y1", parentY + (rectHeight / 2)) // 現在の大項目の下辺
	            .attr("x2", rectX + (rectWidth / 2)) // 次の大項目の中央X座標
	            .attr("y2", parentY + yOffset); // 次の大項目の上端
         }

        
        
        
    }
	  
});      
</script>