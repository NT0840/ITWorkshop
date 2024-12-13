package filter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;

@WebFilter("/*")
public class DBConnectionFilter extends HttpFilter {
    private String driverClassName;
    private String connectionUrl;
    private String username;
    private String password;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        String jsonFilePath = "/WEB-INF/json/DBConnection.json";

        try (InputStream inputStream = getServletContext().getResourceAsStream(jsonFilePath)) {
            if (inputStream == null) {
                throw new ServletException("JSONファイルが見つかりません: " + jsonFilePath);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputStream);

            driverClassName = jsonNode.get("driverClassName").asText();
            connectionUrl = jsonNode.get("connectionUrl").asText();
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();

            // データベース接続を初期化
            try {
                Class.forName(driverClassName);
                connection = DriverManager.getConnection(connectionUrl, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                throw new ServletException("データベース接続の初期化に失敗しました。", e);
            }

        } catch (IOException e) {
            throw new ServletException("JSONファイルの読み込みに失敗しました。", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 接続をリクエスト属性に格納
            request.setAttribute("connection", connection);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException("フィルタ処理中にエラーが発生しました。", e);
        }
    }

    @Override
    public void destroy() {
        // データベース接続をクローズ
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
