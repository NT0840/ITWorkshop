//// 不要
//
//document.addEventListener('DOMContentLoaded', function () {
//      MicroModal.init();
//});      
//
//// 特定文字数以上を「...」に置き換える
//function truncateText(text, maxLength) {
//  return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
//}
//        
//// SVG領域の設定
//const width = 800;
//const height = 600;
//
//// <main class="roadmap-main">内にSVG要素を追加
//const svg = d3.select("main.roadmap-main")
//    .append("svg")
//    .attr("viewBox", "0 0 " + width + " " + height) // viewBoxを設定
//    .attr("preserveAspectRatio", "xMinYMin meet") // アスペクト比を維持
//    .attr("width", "100%") // 横幅を100%に設定
//    
//// 長方形の座標とサイズを変数に格納
//const rectX = 50; // 開始x座標
//const rectY = 50; // 開始y座標
//const rectWidth = 300; // 横幅
//const rectHeight = 50; // 縦幅
//const rectRadiusX = 10; // 角の丸み
//const rectRadiusY = 10; // 角の丸み
//
//// SVG領域に長方形を描画
//svg.append("rect")
//    .attr("class", "rectangle") // CSSクラスを適用
//    .attr("x", rectX) // 開始x座標
//    .attr("y", rectY) // 開始y座標
//    .attr("width", rectWidth) // 横幅
//    .attr("height", rectHeight) // 縦幅
//    .attr("rx", rectRadiusX) // 縦幅
//    .attr("ry", rectRadiusY) // 縦幅
//            
//// テキスト要素を追加
////const elementWidth = 
//svg.append("text")
//	.attr("class", "text") // CSSクラスを適用
//    .attr("x", rectX + rectWidth / 2) // テキストのx座標（長方形の中心）
//    .attr("y", rectY + rectHeight / 2) // テキストのy座標（長方形の中心）
//    .attr("dominant-baseline", "middle") // テキストを垂直方向に中央揃え
//    .text(function(d) { return truncateText("20XX年までに●●をする", 20); });
//    

    