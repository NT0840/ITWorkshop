package bo;

import dao.AccountsDAO;
import model.Account;
import model.Login;

public class LoginLogic {
	public boolean execute(Login login) {
		AccountsDAO dao = new AccountsDAO();
		Account account = dao.findAccount(login);
		return account.getUserId() != null; 
	}
}
