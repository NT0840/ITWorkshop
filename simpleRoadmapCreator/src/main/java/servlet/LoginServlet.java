package servlet;

import java.io.IOException;

import bo.LoginLogic;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ErrorMsg;
import model.Login;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ログイン画面(トップ画面)にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String pass = request.getParameter("pass");
		
		// Userインスタンス(ユーザー情報)の生成
		Login login = new Login(userId, pass);
		// ログイン処理
		LoginLogic loginLogic = new LoginLogic();
		boolean isLogin = loginLogic.execute(login);
		
		// ログイン失敗時の処理
		if(!isLogin) {
			// エラーメッセージをリクエストスコープに保存、index.jspにリダイレクト
			ErrorMsg errorMsg = new ErrorMsg("ログインエラー", "ユーザーIDまたはパスワードが異なります");
			request.setAttribute("errorMsg", errorMsg);
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);	
		} else {
			// リクエストにパラメータを設定
			request.setAttribute("action", "loginOK");
			// マイページにフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("MypageServlet");
			dispatcher.forward(request, response);	
		}
	}

}
