package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import model.Account;
import model.ErrorMsg;
import model.Login;

public class AccountsDAO {
    private static final Logger LOGGER = Logger.getLogger(AccountsDAO.class.getName());
    
	public Account findAccount(Login login, HttpServletRequest request) {
        Connection connection = (Connection) request.getAttribute("connection");
        String sql = "SELECT * FROM ACCOUNT WHERE userId = ? AND pass = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login.getName()); // LoginインスタンスのnameがuserId
            statement.setString(2, login.getPass());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Account account = new Account();
                    account.setUserId(resultSet.getString("userId"));
                    account.setPass(resultSet.getString("pass"));
                    account.setEmail(resultSet.getString("email"));
                    account.setUuid(UUID.fromString(resultSet.getString("uuid"))); 
                    account.setIsVarified(resultSet.getBoolean("isVarified")); 
                    account.setCreateAt(resultSet.getTimestamp("createAt")); 
                    return account;
                }
            }
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "データベース操作中にエラーが発生しました", e);
        	ErrorMsg errorMsg = new ErrorMsg("データベースエラー", "データベースエラーが発生しました");
            request.setAttribute("DBError", errorMsg);
        }
        return null;
    }
}