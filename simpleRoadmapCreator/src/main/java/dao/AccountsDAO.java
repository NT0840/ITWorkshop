package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import listener.ApplicationListener;
import model.Account;
import model.Login;

public class AccountsDAO {
    
	public Account findAccount(Login login) {
		Account account = new Account();
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT * FROM ACCOUNTS WHERE USER_ID = ? AND PASS = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, login.getUserId());
			pStmt.setString(2, login.getPass());
			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();
			
			while(rs.next()) {
				account.setUserId(rs.getString("USER_ID"));
				account.setPass(rs.getString("PASS"));
				account.setEmail(rs.getString("EMAIL"));
				account.setUuid(UUID.fromString(rs.getString("UUID")));
				account.setIsVarified(rs.getBoolean("IS_VERIFIED")); 
				account.setCreateAt(rs.getTimestamp("CREATE_AT"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return account;
	}
}