package servlet;

import java.io.IOException;
import java.util.List;

import bo.RoadmapLogic;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Roadmap;
import model.UserId;



@WebServlet("/MypageServlet")
public class MypageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// マイページにフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/mypage.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String action = (String)request.getAttribute("action");
		
		// ログイン以外からの遷移時
		if(action == null) {
			// 後続の処理からnullを除外するための分岐
			
		// ログイン成功時の処理
		} else if(action.equals("loginOK")) {
			// セッションスコープにユーザーIDを保存
			HttpSession session = request.getSession();
			UserId userId = new UserId(request.getParameter("userId"));
			session.setAttribute("userId", userId);
			
			// ユーザーIDに紐づくロードマップを取得、セッションスコープに保存
			RoadmapLogic roadmapLogic = new RoadmapLogic();
			List<Roadmap> roadmaps = roadmapLogic.findByUserId(userId);
			session.setAttribute("roadmaps", roadmaps);
		}
		response.sendRedirect("MypageServlet");
	}

}
