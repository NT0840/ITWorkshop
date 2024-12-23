package filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/WEB-INF/jsp/*")
public class LoginCheckFilter extends HttpFilter {

	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// セッションを取得（false: セッションが存在しない場合は新規作成しない）
        HttpSession session = request.getSession(false);
        
        // セッションが存在し、ログインユーザー情報が存在する場合
        if (session != null && session.getAttribute("userId") != null) {
            // 通常のリクエスト処理を続行
        	chain.doFilter(request, response);
        } else {
            // ログインしていない場合、トップ画面（ログインページ）にリダイレクト
        	response.sendRedirect("index.jsp");
        }
	}
}
