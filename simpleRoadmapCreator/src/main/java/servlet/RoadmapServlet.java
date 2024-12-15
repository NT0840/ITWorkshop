package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ChildElement;
import model.ParentElement;
import model.Roadmap;
import model.RoadmapId;
import model.UserId;

@WebServlet("/RoadmapServlet")
public class RoadmapServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPath = null;
		// サーブレットクラスの動作を決定する「action」の値をリクエストパラメータから取得
		String action = request.getParameter("action");
		if(action == null) {
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		} else if(action.equals("new")) {
			forwardPath = "WEB-INF/jsp/roadmapNew.jsp";
		}
		// マイページにフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// サーブレットクラスの動作を決定する「action」の値をリクエストパラメータから取得
		String action = request.getParameter("action");
		
		HttpSession session = request.getSession();
		List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");
		
		if(action.equals("newRoadmap")) {
			final int behindInsert = 1000; // 入力値の作成順(1～999)が重複していた場合に加算する値
			Roadmap roadmap = new Roadmap();
			List<ParentElement> parentElements = new ArrayList<>();
			List<List<ChildElement>> childElements = new ArrayList<>();
			
			UserId userId = (UserId)session.getAttribute("userId");
			
			// 作成日時、更新日時セット用の現在時刻
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			
			// セッションスコープから取得したroadmapsから、roadmapIdの最大値+1を取得、roadmapNew.jspで入力された"roadmap-name"の値もroadmapIdインスタンスにセット
			
			String roadmapName = request.getParameter("roadmap-name");
			RoadmapId roadmapId = new RoadmapId(userId.getUserId(), ロードマップID(int), roadmapName, now, now);
			
			// roadmapNew.jspで入力した子要素・親要素の情報を取得					
			// リクエスト内のパラメーターをキーと値のMapで取得。パラメータ重複時用にString[]になっている
			Map<String, String[]> parameterMap = request.getParameterMap();
			Map<Integer, Map<Integer, ChildElement>> childElementsMap = new TreeMap<>(); // <親要素作成順、<子要素作成順、子要素情報>>
			Map<Integer, ParentElement> parentElementsMap = new TreeMap<>(); // <親要素作成順、親要素情報>
			
			// 子要素の取得
			for (String paramName : parameterMap.keySet()) {
				if (paramName.startsWith("child-order-")) {
					String[] parts = paramName.split("-");
					int parentIndex = Integer.parseInt(parts[2]);
					int childIndex = Integer.parseInt(parts[3]);

					String childOrder = request.getParameter(paramName);
					String childName = request.getParameter("child-name-" + parentIndex + "-" + childIndex);
					String childTag = request.getParameter("child-tag-" + parentIndex + "-" + childIndex);

//					ChildElement childElement = new ChildElement(childOrder, childName, childTag);
//					ParentElement parentElement = parentElements.get(parentIndex);
//					if (parentElement != null) {
//						parentElement.addChildElement(childElement);
					
					// 上のロードマップIDの空きを検索するDAO作成
					// 子要素取得の中身調整、子要素については作成順重複時は、子要素作成順にbegindInsertを加算して格納するだけ
					// 親要素の方は重複する場合以降の調整、作成順重複時は、親要素作成順にbehindInsertを加算(子要素のMap含めて2箇所)
					// Map内情報を直接的に変更することはできないため、既存の値を取得、既存のキーを削除、再格納の手順となる。
			        // 1. 既存の値を取得
//			        String value = map.get(oldKey);
			        // 2. 既存のキーを削除
//			        map.remove(oldKey);
			        // 3. 新しいキーで再格納
//			        if (value != null) {
//			            map.put(newKey, value);
//			        }
					}
				}
			
			// 親要素の取得
			for (String paramName : parameterMap.keySet()) {
				if (paramName.startsWith("parent-order-")) {
					// "parent-order-"の文字数の長さ分だけ省いた残りの数値だけをparentIndexとして格納
					int parentIndex = Integer.parseInt(paramName.substring("parent-order-".length()));
					// "parent-order-X"の中の値を得るだけなのでそのまま取得
					int parentOrder = Integer.parseInt(request.getParameter(paramName));
					// "parent-order-X"と同列のparent-name-Xの値を取得
					String parentName = request.getParameter("parent-name-" + parentIndex);

					ParentElement parentElement = new ParentElement(userId.getUserId(), roadmapId.getRoadmapId(), parentOrder, parentName, 0, "", now, now);
					// 重複する場合は末尾にputするためにbehindInsertを加算する
					if(parentElementsMap.containsKey(parentIndex)) {
						parentIndex += behindInsert;
					}
					parentElementsMap.put(parentIndex, parentElement);
				}
				
				// Mapに格納した親要素情報、子要素情報をListに連番で格納する
				// Mapに格納されているものを、キーが小さい順に値を取り出す
//		        for (Map.Entry<Integer, String> entry : parentElements.entrySet()) {
//		        }
		        }
				
			}
			}
		
			// Roadmapインスタンスを組み立ててaddしてセッションスコープに保存する操作、あと表示中roadmapインスタンスとしても保存
			
			
			
		

		
		
		
		
		doGet(request, response);
	}

}
