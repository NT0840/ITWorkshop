package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import model.Account;
import model.Login;

public class AccountsDAO {
	public Account findAccount(Login login, HttpServletRequest request) throws SQLException {
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
        }
        return null;
    }
}