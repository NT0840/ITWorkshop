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
		
		HttpSession session = request.getSession();
		
		if(action == null) { // mypageから遷移してきた場合
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		} else if(action.equals("new")) {
			forwardPath = "WEB-INF/jsp/roadmapNew.jsp";
		} else if(action.equals("delete")) {
			// ロードマップ単位の削除
			// ロードマップのリスト、現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");

			// 現在表示中のロードマップの削除
			RoadmapLogic roadmapLogic = new RoadmapLogic(); 
			Boolean isDelete = roadmapLogic.deleteRoadmap(roadmapId);

			// 現在の表示中のRoadmapインスタンスをroadmapsから削除、セッションスコープのroadmapsを上書き
			roadmaps.remove(roadmapId.getRoadmapId() - 1);
			session.setAttribute("roadmaps", roadmaps);
			
			// セッションスコープからcurrentRoadmapIdおよび描画用のJSONを削除
			session.removeAttribute("currentRoadmapId");
			session.removeAttribute("parentElementsJson");
			session.removeAttribute("childElementsJson");
			
			forwardPath = "MypageServlet";
		} else if(action.equals("delete-parent")) {
			// 親要素の削除
			// パラメータの取得
			int currentParentNum = Integer.parseInt(request.getParameter("currentParentNum"));	
			
			// ロードマップのリスト、現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");
			
			// 削除対象のParentElementを特定
			ParentElement parentElement = roadmaps.get(roadmapId.getRoadmapId() - 1).getParentElements().get(currentParentNum - 1);

			// データベースから対象の親要素を削除
			RoadmapLogic roadmapLogic = new RoadmapLogic();
			Boolean isDeleteParentElement = roadmapLogic.deleteParentElement(parentElement);
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、roadmapsに上書き保存してセッションスコープに保存
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			roadmaps.set(roadmapId.getRoadmapId() - 1, currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(currentRoadmap, session);
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		} else if(action.equals("delete-child")) {
			// 子要素の削除
			// パラメータの取得
			int currentParentNum = Integer.parseInt(request.getParameter("currentParentNum"));	
			int currentChildNum = Integer.parseInt(request.getParameter("currentChildNum"));	
			
			// ロードマップのリスト、現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");
			
			// 削除対象のChildElementを特定
			ChildElement childElement = roadmaps.get(roadmapId.getRoadmapId() - 1).getChildElements().get(currentParentNum - 1).get(currentChildNum - 1);

			// データベースから対象の子要素を削除
			RoadmapLogic roadmapLogic = new RoadmapLogic();
			Boolean isDeleteChildElement = roadmapLogic.deleteChildElement(childElement);
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、roadmapsに上書き保存してセッションスコープに保存
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			roadmaps.set(roadmapId.getRoadmapId() - 1, currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(currentRoadmap, session);
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		}
		
		
		
		
		
		
		// マイページ画面から遷移してきた用の、表示するロードマップを特定してRoadmapIdインスタンスをセッションスコープに保存
		if(select != null) {
			int selectInt = Integer.parseInt(select);
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
		String forwardPath = null;
		// サーブレットクラスの動作を決定する「action」の値をリクエストパラメータから取得
		String action = request.getParameter("action");
		
		HttpSession session = request.getSession();
		List<Roadmap> roadmaps  = (ArrayList<Roadmap>)session.getAttribute("roadmaps");
		
		// 作成日時、更新日時セット用の現在時刻
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		
		// ロードマップコピー
		if(action.equals("copy")) {
			// パラメータの取得
			String roadmapName = request.getParameter("roadmap-name");	
			
			// ユーザーID、現在表示中のロードマップのIDをセッションスコープから取得
			UserId userId = (UserId)session.getAttribute("userId");
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			
			// ユーザーIDに紐づくroadmapIdの最大値を取得
			RoadmapLogic roadmapLogic = new RoadmapLogic();
			int roadmapIdMax = roadmapLogic.findRoadmapIdMax(userId);
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、ロードマップ名とroadmapId、作成日時、更新日時をセットしたRoadmapIdをセット
			RoadmapId newRoadmapId = new RoadmapId(roadmapId.getUserId(), roadmapIdMax + 1, roadmapName, now, now);
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			currentRoadmap.setRoadmapId(newRoadmapId);
			
			// 付随する親要素、子要素のロードマップID、作成日時、更新日時をセット
			for(ParentElement parentElement : currentRoadmap.getParentElements()) {
				parentElement.setRoadmapId(roadmapIdMax + 1);
				parentElement.setParentCreateAt(now);
				parentElement.setParentUpdateAt(now);
				
				int parentIndex = parentElement.getParentNum() - 1;
				if(!currentRoadmap.getChildElements().get(parentIndex).isEmpty()) {
					for(ChildElement childElement : currentRoadmap.getChildElements().get(parentIndex)) {
						childElement.setRoadmapId(roadmapIdMax + 1);
						childElement.setChildCreateAt(now);
						childElement.setChildUpdateAt(now);
					}
				}
			}
			
			// 作成したRoadmapをデータベースに保存
			Boolean isRoadmapCopy = roadmapLogic.copyRoadmap(currentRoadmap);
			
			// roadmapsに追加してセッションスコープを上書き
			roadmaps.add(currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			forwardPath = "MypageServlet";
			
		}
		
		// ロードマップ親要素新規作成処理
		if(action.equals("newParent")) {
			// パラメータの取得
			String parentName = request.getParameter("new-parent-name");
			
			// 現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			
			// 現在表示中のロードマップのparentNumの最大値を取得
			RoadmapLogic roadmapLogic = new RoadmapLogic(); 
			int parentNumMax = roadmapLogic.findParentNumMax(roadmapId.getUserId(), roadmapId.getRoadmapId());
			
			// 追加する親要素の組み立て、末尾に挿入するため、parentNumには戻り値+1をセット
			ParentElement parentElement = new ParentElement(
					roadmapId.getUserId(),
		            roadmapId.getRoadmapId(),
		            parentNumMax + 1, // 末尾に追加
		            parentName,
		            0,
		            "",
		            now,
		            now
		        );
			
			// 追加する親要素をデータベースに追加
			Boolean isCreateParentElement = roadmapLogic.createParentElement(parentElement);
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、roadmapsに上書き保存してセッションスコープに保存
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			roadmaps.set(roadmapId.getRoadmapId() - 1, currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(currentRoadmap, session);
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		}

		// ロードマップ子要素新規作成処理
		if(action.equals("newChild")) {
			// パラメータの取得
			String childName = request.getParameter("new-child-name");
			int belongToParentNum = Integer.parseInt(request.getParameter("new-belong-to-parent-num"));
			int childTagNum = Integer.parseInt(request.getParameter("new-child-tag-num"));
			
			// 現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			
			// 現在表示中のロードマップのchildNumの最大値を取得
			RoadmapLogic roadmapLogic = new RoadmapLogic(); 
			int childNumMax = roadmapLogic.findChildNumMax(roadmapId.getUserId(), roadmapId.getRoadmapId(), belongToParentNum);
			
			// 追加する子要素の組み立て、末尾に挿入するため、childNumには戻り値+1をセット
			ChildElement childElement = new ChildElement(
					roadmapId.getUserId(),
		            roadmapId.getRoadmapId(),
		            belongToParentNum, 
		            childNumMax + 1, // 末尾に追加
		            childName,
		            childTagNum, 
		            0,
		            "",
		            now,
		            now
		        );
			
			
			// 追加する子要素をデータベースに追加
			Boolean isCreateChildElement = roadmapLogic.createChildElement(childElement);
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、roadmapsに上書き保存してセッションスコープに保存
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			roadmaps.set(roadmapId.getRoadmapId() - 1, currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(currentRoadmap, session);
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		}
		
		// ロードマップ親要素内容変更処理
		if(action.equals("updateParent")) {
			// パラメータの取得
			int currentParentNum = Integer.parseInt(request.getParameter("currentParentNum"));
			String parentName = request.getParameter("parentName" + currentParentNum);
			int parentNum = Integer.parseInt(request.getParameter("parentNum" + currentParentNum));
			int parentStatusNum = Integer.parseInt(request.getParameter("parentStatusNum" + currentParentNum));
			String parentDescription = request.getParameter("parentDescription" + currentParentNum);
			
			// 現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			
			// roadmapsから変更対象のParentElementを特定、変更前のコピーを取っておく
			ParentElement currentParentElement = roadmaps.get(roadmapId.getRoadmapId() - 1).getParentElements().get(currentParentNum - 1);
			ParentElement parentElement = new ParentElement(
					currentParentElement.getUserId(),
		            currentParentElement.getRoadmapId(),
		            parentNum, // 新しい親要素番号
		            parentName,
		            parentStatusNum,
		            parentDescription,
		            currentParentElement.getParentCreateAt(),
		            now
		        );
			
			// 並び順変更の有無判定、その後の処理分岐
			Boolean isInsert = (currentParentElement.getParentNum() != parentElement.getParentNum());
			Boolean isUpdateParentElement;
			RoadmapLogic roadmapLogic = new RoadmapLogic(); 
			
			if(isInsert) {
				isUpdateParentElement = roadmapLogic.updateParentElementWithInsert(parentElement, currentParentElement);
			} else {
				isUpdateParentElement = roadmapLogic.updateParentElement(parentElement);
			}
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、roadmapsに上書き保存してセッションスコープに保存
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			roadmaps.set(roadmapId.getRoadmapId() - 1, currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(currentRoadmap, session);
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		}
		
		// ロードマップ子要素内容変更処理
		if(action.equals("updateChild")) {
			// パラメータの取得
			int currentParentNum = Integer.parseInt(request.getParameter("currentParentNum"));
			int currentChildNum = Integer.parseInt(request.getParameter("currentChildNum"));
			String childName = request.getParameter("childName" + currentParentNum + "-" + currentChildNum);
			int childNum = Integer.parseInt(request.getParameter("childNum" + currentParentNum + "-" + currentChildNum));
			int childTagNum = Integer.parseInt(request.getParameter("childTagNum" + currentParentNum + "-" + currentChildNum));
			int childStatusNum = Integer.parseInt(request.getParameter("childStatusNum" + currentParentNum + "-" + currentChildNum));
			String childDescription = request.getParameter("childDescription" + currentParentNum + "-" + currentChildNum);
			
			// 現在表示中のロードマップのIDをセッションスコープから取得
			RoadmapId roadmapId = (RoadmapId)session.getAttribute("currentRoadmapId");
			
			// roadmapsから変更対象のchildElementを特定、変更前のコピーを取っておく
			ChildElement currentChildElement = roadmaps.get(roadmapId.getRoadmapId() - 1).getChildElements().get(currentParentNum - 1).get(currentChildNum - 1);
			ChildElement childElement = new ChildElement(
		            currentChildElement.getUserId(),
	                currentChildElement.getRoadmapId(),
	                currentChildElement.getParentNum(), 
	                childNum, 
	                childName,
	                childTagNum,
	                childStatusNum,
	                childDescription,
	                currentChildElement.getChildCreateAt(),
	                now
		        );
			
			// 並び順変更の有無判定、その後の処理分岐
			Boolean isInsert = (currentChildElement.getChildNum() != childElement.getChildNum());
			RoadmapLogic roadmapLogic = new RoadmapLogic(); 
			
			if(isInsert) {
				Boolean isUpdateChildElement = roadmapLogic.updateChildElementWithInsert(childElement, currentChildElement);
			} else {
				Boolean isUpdateChildElement = roadmapLogic.updateChildElement(childElement);
			}
			
			// 現在の表示中のRoadmapインスタンスをデータベースから取得、roadmapsに上書き保存してセッションスコープに保存
			Roadmap currentRoadmap = roadmapLogic.findByRoadmapId(roadmapId);
			roadmaps.set(roadmapId.getRoadmapId() - 1, currentRoadmap);
			session.setAttribute("roadmaps", roadmaps);
			
			// 表示中roadmapインスタンスをJavaScriptで利用するためにJSON形式でセッションスコープに保存
			elementsToJson(currentRoadmap, session);
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		}
		
		// ロードマップ新規作成処理
		if(action.equals("newRoadmap")) {
			final int behindInsert = 1000; // 入力値の作成順(1～999)が重複していた場合に加算する値。
			
			UserId userId = (UserId)session.getAttribute("userId");
			
			
			
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
			
			forwardPath = "WEB-INF/jsp/roadmap.jsp";
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		dispatcher.forward(request, response);
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
