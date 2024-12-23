package servlet;

import java.io.IOException;

import bo.AccountLogic;
import bo.RoadmapLogic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserId;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションスコープからユーザーIDを取得
		HttpSession session = request.getSession(); 
		UserId userId = (UserId) session.getAttribute("userId");
		
		// ユーザーIDに紐づく各種データを削除
		AccountLogic accountLogic = new AccountLogic();
		Boolean isDeleteAccount = accountLogic.deleteAccount(userId);
		
		RoadmapLogic roadmapLogic = new RoadmapLogic();
		Boolean isDeleteRoadmap= roadmapLogic.deleteByUserId(userId);
		
		// セッションスコープの破棄
		session.invalidate();
		
		// リダイレクト
		response.sendRedirect("AccountTransferServlet");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
