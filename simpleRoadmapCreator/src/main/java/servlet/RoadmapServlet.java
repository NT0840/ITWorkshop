package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

import bo.RoadmapLogic;
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
		String select = request.getParameter("select");
		
		if(action == null) { // mypageから遷移してきた場合
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		} else if(action.equals("new")) {
			forwardPath = "WEB-INF/jsp/roadmapNew.jsp";
		}
		
		// マイページ画面から遷移してきた用の、表示するロードマップを特定してRoadmapIdインスタンスをセッションスコープに保存
		if(select != null) {
			int selectInt = Integer.parseInt(select);
			HttpSession session = request.getSession();
			List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");
			for(Roadmap roadmap : roadmaps) {
				if(selectInt == roadmap.getRoadmapId().getRoadmapId()) {
					session.setAttribute("currentRoadmapId", roadmap.getRoadmapId());
					elementsToJson(roadmap, session);
					break;
				}
			}
		}
		
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// サーブレットクラスの動作を決定する「action」の値をリクエストパラメータから取得
		String action = request.getParameter("action");
		
		HttpSession session = request.getSession();
		List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");
		
		if(action.equals("newRoadmap")) {
			final int behindInsert = 1000; // 入力値の作成順(1～999)が重複していた場合に加算する値。
			
			UserId userId = (UserId)session.getAttribute("userId");
			
			// 作成日時、更新日時セット用の現在時刻
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			
			// セッションスコープから取得したroadmapsから、roadmapIdの最大値を取得(セット時は+1でセット)、roadmapNew.jspで入力された"roadmap-name"の値もroadmapIdインスタンスにセット
			RoadmapLogic roadmapLogic = new RoadmapLogic();
			int roadmapIdNew = roadmapLogic.findRoadmapIdMax(userId) + 1;
			String roadmapName = request.getParameter("roadmap-name");
			RoadmapId roadmapId = new RoadmapId(userId.getUserId(), roadmapIdNew, roadmapName, now, now);
			
			// roadmapNew.jspで入力した子要素・親要素の情報を取得					
			// リクエスト内のパラメーターをキーと値のMapで取得。パラメータ重複時用にString[]になっている
			Map<String, String[]> parameterMap = request.getParameterMap();
			Map<Integer, Map<Integer, ChildElement>> childElementsMap = new TreeMap<>(); // <親要素作成順、<子要素作成順、子要素情報>>
			Map<Integer, ParentElement> parentElementsMap = new TreeMap<>(); // <親要素作成順、親要素情報>
			
			// 親要素の取得
			for (String paramName : parameterMap.keySet()) {
				if (paramName.startsWith("parent-order-")) {
					// "parent-order-"の文字数の長さ分だけ省いた残りの数値だけをparentIndexとして格納
					int parentIndex = Integer.parseInt(paramName.substring("parent-order-".length()));
					// "parent-order-X"の中の値を得るだけなのでそのまま取得
					int parentOrder = Integer.parseInt(request.getParameter(paramName));
					// "parent-order-X"と同列のparent-name-Xの値を取得
					String parentName = request.getParameter("parent-name-" + parentIndex);

					// 重複する場合は末尾にputするためにbehindInsertを加算する
					if(parentElementsMap.containsKey(parentOrder)) {
						parentOrder += behindInsert;
					}
					ParentElement parentElement = new ParentElement(userId.getUserId(), roadmapId.getRoadmapId(), parentOrder, parentName, 0, "", now, now);
					parentElementsMap.put(parentOrder, parentElement);
				}
			}
			
			// 子要素の取得
			int prevParentIndex = -1; // 親要素が切り替わる判定用
			List<Integer> overlap = new ArrayList<>(); // 追加した親要素作成順(Mapのキー)を格納して重複判定に使用
			for (String paramName : parameterMap.keySet()) {
				if (paramName.startsWith("child-order-")) {
					// "-"で分割して文字列に格納、パラメータの名称の親要素部分、子要素部分を取得
					String[] parts = paramName.split("-");
					int parentIndex = Integer.parseInt(parts[2]);
					int childIndex = Integer.parseInt(parts[3]);

					int childOrder = Integer.parseInt(request.getParameter(paramName));
					int parentOrder = Integer.parseInt(request.getParameter("parent-order-" + parentIndex));
					String childName = request.getParameter("child-name-" + parentIndex + "-" + childIndex);
					int childTagNum = Integer.parseInt(request.getParameter("child-tag-" + parentIndex + "-" + childIndex));
					
					// 重複する場合は末尾にputするためにparentOrderにbehindInsertを加算する(ParentElementsMapに対応させる目的)
					if(parentIndex != prevParentIndex && overlap.contains(parentOrder)) {
						parentOrder += behindInsert;
						parentIndex = prevParentIndex; // 次のループでparentIndexが同一の場合にifブロックに入れるための代入
					}
					// 親要素に対応する子要素のMapを取り出す
					Map<Integer, ChildElement> innerMap = childElementsMap.get(parentOrder);
					ChildElement childElement = new ChildElement(userId.getUserId(), roadmapId.getRoadmapId(), parentOrder, childOrder, childName, childTagNum, 0, "", now, now);
					// すでに子要素のMapがある場合、childElementをputして、大元のMapへ上書き
					if(innerMap != null) {
						// 重複する場合は末尾にputするためにchildOrderにbehindInsertを加算する
						if(innerMap.containsKey(childOrder)) {
							childOrder += behindInsert;
							childElement.setChildNum(childOrder);
						}
						innerMap.put(childOrder, childElement);
						childElementsMap.put(parentOrder, innerMap);
					} else { // 子要素1つめの場合
						Map<Integer, ChildElement> innerMapNew = new TreeMap<>();
						innerMapNew.put(childOrder, childElement);
						childElementsMap.put(parentOrder, innerMapNew);
					}
					prevParentIndex = parentIndex;
					overlap.add(parentOrder);
				}
			}
			
			// Map内に格納した情報を各Listに連番で保存する
			int parentCount = 1; // 連番用の数字
			int childCount = 1;
			List<ParentElement> parentElements = new ArrayList<>();
			List<List<ChildElement>> childElements = new ArrayList<>();
			// 親要素, Mapに格納されているものを、キーが小さい順に値を取り出す
			for (Map.Entry<Integer, ParentElement> entry : parentElementsMap.entrySet()) {
			    ParentElement parentElement = new ParentElement(
			            entry.getValue().getUserId(),
			            entry.getValue().getRoadmapId(),
			            parentCount, // 新しい親要素番号
			            entry.getValue().getParentName(),
			            entry.getValue().getParentStatusNum(),
			            entry.getValue().getParentDescription(),
			            entry.getValue().getParentCreateAt(),
			            entry.getValue().getParentUpdateAt()
			        );
				parentElements.add(parentElement);
				
				// 子要素, Mapに格納されているものを、キーが小さい順に値を取り出す
				// 親要素に対応する子要素のMapを取り出す。存在しないならnull
				Map<Integer, ChildElement> innerMap = childElementsMap.get(entry.getKey());
				List<ChildElement> innerChildElements = new ArrayList<>();
				if(innerMap != null) {
					for (Map.Entry<Integer, ChildElement> entryChild : innerMap.entrySet()) {
						ChildElement childElement = new ChildElement(
				                entryChild.getValue().getUserId(),
				                entryChild.getValue().getRoadmapId(),
				                parentCount, // 親要素番号
				                childCount,   // 子要素番号
				                entryChild.getValue().getChildName(),
				                entryChild.getValue().getChildTagNum(),
				                entryChild.getValue().getChildStatusNum(),
				                entryChild.getValue().getChildDescription(),
				                entryChild.getValue().getChildCreateAt(),
				                entryChild.getValue().getChildUpdateAt()
				            );
						innerChildElements.add(childElement);
						childCount++;
					}
				}
				childElements.add(innerChildElements);
				parentCount++;
				childCount = 1;
			}

			// Roadmapインスタンスを組み立ててスコープから取得したroadmapsにaddしてセッションスコープに保存
			Roadmap roadmap = new Roadmap(roadmapId, parentElements, childElements);
			roadmaps.add(roadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスとして、roadmapIdインスタンスをセッションスコープに保存
			session.setAttribute("currentRoadmapId", roadmap.getRoadmapId());
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(roadmap, session);
			
			// 組み立てたRoadmapインスタンスをデータベースに保存
			Boolean isCreateRoadmap = roadmapLogic.createRoadmap(roadmap);
		
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/roadmap.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	private void elementsToJson(Roadmap roadmap, HttpSession session) {
		Gson gson = new Gson();
		List<ParentElement> parentElements = roadmap.getParentElements();
		String parentElementsJson = gson.toJson(parentElements);
		session.setAttribute("parentElementsJson", parentElementsJson);
		
		List<List<ChildElement>> childElements = roadmap.getChildElements();
		String childElementsJson = gson.toJson(childElements);
		session.setAttribute("childElementsJson", childElementsJson);
		
	}
}
