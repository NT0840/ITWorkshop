package bo;

import dao.AccountsDAO;
import jakarta.servlet.http.HttpServletRequest;
import model.Account;
import model.Login;

public class LoginLogic {
	public boolean execute(Login login, HttpServletRequest request) {
		AccountsDAO dao = new AccountsDAO();
		Account account = dao.findAccount(login, request);
		return account != null; 
	}
}
