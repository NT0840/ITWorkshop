 const svg = d3.select("svg");

        // 長方形のデータ
        const rectangles = [
            { x: 100, y: 100, width: 150, height: 50 },
            { x: 300, y: 200, width: 150, height: 50 },
            { x: 500, y: 100, width: 150, height: 50 }
        ];

        // 長方形を描画
        svg.selectAll("rect")
            .data(rectangles)
            .enter()
            .append("rect")
            .attr("class", "rectangle")
            .attr("x", d => d.x)
            .attr("y", d => d.y)
            .attr("width", d => d.width)
            .attr("height", d => d.height);

        // 線を描画する関数
        function drawLine(x1, y1, x2, y2) {
            svg.append("line")
                .attr("class", "line")
                .attr("x1", x1)
                .attr("y1", y1)
                .attr("x2", x2)
                .attr("y2", y2);
        }

        // 長方形の中点を計算して線を引く
        for (let i = 0; i < rectangles.length - 1; i++) {
            const rect1 = rectangles[i];
            const rect2 = rectangles[i + 1];

            const midX1 = rect1.x + rect1.width / 2;
            const midY1 = rect1.y + rect1.height;
            const midX2 = rect2.x + rect2.width / 2;
            const midY2 = rect2.y;

            drawLine(midX1, midY1, midX2, midY2);
        }