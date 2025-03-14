package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String DATABASE_NAME = "product_management";  // 本番DB
    private static final String TEST_DATABASE_NAME = "test_db";  // テスト用DB
    private static final String PROPERTIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPERTIES;  // 本番用URL
    private static final String TEST_URL = "jdbc:mysql://localhost:3306/" + TEST_DATABASE_NAME + PROPERTIES;  // テスト用URL
    
    private static final String USER = "root";
    private static final String PASS = "Maiko5415!";

    private static boolean useTestDatabase = false;  // テスト用DBを使用するかどうかのフラグ

    // Connection を取得するメソッド
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = useTestDatabase ? TEST_URL : URL;  // テスト用DBの場合はTEST_URLを使用
        return DriverManager.getConnection(url, USER, PASS);
    }

    // テスト用DBの使用を設定
    public static void setUseTestDatabase(boolean useTest) {
        useTestDatabase = useTest;
    }

    // testing the connection
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            // Connection was successful
            System.out.println("Connection successful.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
