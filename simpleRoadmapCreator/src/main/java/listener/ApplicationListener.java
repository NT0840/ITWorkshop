package listener;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    private String driverClassName;
    private static String jdbcUrl;
    private static String userName;
    private static String pass;

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	ServletContext context = event.getServletContext();
    	String jsonFilePath = "/WEB-INF/json/DBConnection.json";
        // JSONファイルの読み込み
        try (InputStream inputStream = context.getResourceAsStream(jsonFilePath)) {
            if (inputStream == null) {
                throw new RuntimeException("JSONファイルが見つかりません");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            driverClassName = jsonNode.get("driverClassName").asText();
            jdbcUrl = jsonNode.get("jdbcUrl").asText();
            userName = jsonNode.get("userName").asText();
            pass = jsonNode.get("pass").asText();
        } catch (IOException e) {
            throw new RuntimeException("JSONファイルの読み込みに失敗しました", e);
        }
		// JDBCドライバの読み込み
		try {
			Class.forName(driverClassName);
		} catch(ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバが読み込めませんでした");
		}
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPass() {
        return pass;
    }
}
