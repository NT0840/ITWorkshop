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
import model.UserId;

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
	
	public Account findByUserId(UserId userId) {
		Account account = new Account();
		
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "SELECT * FROM ACCOUNTS WHERE USER_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId.getUserId());
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
	
	public boolean insert(Account account) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "INSERT INTO ACCOUNTS (USER_ID, PASS, EMAIL, UUID, IS_VERIFIED, CREATE_AT) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, account.getUserId());
			pStmt.setString(2, account.getPass());
			pStmt.setString(3, account.getEmail());
			pStmt.setObject(4, account.getUuid());
			pStmt.setBoolean(5, account.getIsVarified());
			pStmt.setTimestamp(6, account.getCreateAt());
			
			// INSERTを実行
			int result = pStmt.executeUpdate();
			
			if(result < 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteByUserId(UserId userId) {
		try (Connection conn = DriverManager.getConnection(ApplicationListener.getJdbcUrl(), ApplicationListener.getUserName(), ApplicationListener.getPass())) {
			
			String sql = "DELETE FROM ACCOUNTS WHERE USER_ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, userId.getUserId());
			
			// DELETEを実行、結果を保存
			int result = pStmt.executeUpdate();
			if(result < 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}