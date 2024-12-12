package filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//JSONパーライブラリを追加 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;

/**
 * Servlet Filter implementation class DBConnectionFilter
 */
@WebFilter("/dao/*")
public class DBConnectionFilter extends HttpFilter {
    private String driverClassName;
    private String connectionUrl;
    private String username;
    private String password;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	 // DBConnection.jsonファイルのパスを取得
        String jsonFilePath = filterConfig.getInitParameter("jsonFilePath");

        try (InputStream inputStream = getServletContext().getResourceAsStream(jsonFilePath);
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            // JSONパーサーを使用してJSONファイルを読み込み
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(reader);

            // JSONファイルからデータベース接続情報を取得
            driverClassName = jsonNode.get("driverClassName").asText();
            connectionUrl = jsonNode.get("connectionUrl").asText();
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();

        } catch (IOException e) {
            // 例外処理
            e.printStackTrace();
            throw new ServletException("JSONファイルの読み込みに失敗しました。", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try (Connection connection = DriverManager.getConnection(connectionUrl, username, password)) {
            // 接続をリクエスト属性に格納
            request.setAttribute("connection", connection);
            chain.doFilter(request, response);
        } catch (SQLException e) {
            // 例外処理
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        // リソースの解放
    }

}
