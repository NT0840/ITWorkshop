package bo;

import dao.AccountsDAO;
import model.Account;
import model.UserId;

public class AccountLogic {
	public Account findAccount(UserId userId) {
		AccountsDAO accountsDao = new AccountsDAO();
		Account account = accountsDao.findByUserId(userId);
		return account; 
		
	}
	public boolean registerAccount(Account account) {
		AccountsDAO accountsDao = new AccountsDAO();
		Boolean isInsert = accountsDao.insert(account);
		return isInsert; 
	}
	
	public boolean deleteAccount(UserId userId) {
		AccountsDAO accountsDao = new AccountsDAO();
		Boolean isDelete = accountsDao.deleteByUserId(userId);
		return isDelete; 
	}

}
