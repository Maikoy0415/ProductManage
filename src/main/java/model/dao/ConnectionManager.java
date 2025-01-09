package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static final String DATABASE_NAME = "product_management";
	private static final String PROPERTIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
	private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPERTIES;
	private static final String USER = "root";
	private static final String PASS = "Maiko5415!";

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL, USER, PASS);
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
