package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import bo.AccountLogic;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.ErrorMsg;
import model.UserId;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// アカウント登録画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPath = null;
		String redirectPath = null;
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String pass = request.getParameter("pass");
		String passConfirm = request.getParameter("passConfirm");
		
		// 作成日時、更新日時セット用の現在時刻
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		
		if(!pass.equals(passConfirm)) {
			// エラーメッセージをリクエストスコープに保存、register.jspにフォワード
			ErrorMsg errorMsg = new ErrorMsg("アカウント登録エラー", "パスワードが一致していません");
			request.setAttribute("errorMsg", errorMsg);
			forwardPath = "WEB-INF/jsp/register.jsp";
		} else {
			UserId userIdNew = new UserId(userId);
			
			// ユーザーIDの重複チェック
			AccountLogic accountLogic = new AccountLogic();
			Account account =  accountLogic.findAccount(userIdNew);
			
			if(account.getUserId() != null) { // ユーザーIDが既に登録済みの場合
				// エラーメッセージをリクエストスコープに保存、register.jspにフォワード
				ErrorMsg errorMsg = new ErrorMsg("アカウント登録エラー", "入力したユーザー名は使われています");
				request.setAttribute("errorMsg", errorMsg);
				forwardPath = "WEB-INF/jsp/register.jsp";
			} else {
				// データベースへの登録処理
				UUID uuid = UUID.randomUUID();
				Account accountNew = new Account(userId, pass, uuid.toString(), uuid, false, now); // 暫定的にEmailにuuidを文字列としてセット(ユニークキー制約のため)
				Boolean isRegister = accountLogic.registerAccount(accountNew);
				
				redirectPath = "RegisterTransferServlet";
			}
			
		}
		
		if(forwardPath != null) {
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
			dispatcher.forward(request, response);	
		} else {
			// リダイレクト
			response.sendRedirect(redirectPath);
		}
	}

}
