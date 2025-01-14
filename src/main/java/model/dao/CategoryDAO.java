package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CategoryDAO {

	private static final Logger logger = Logger.getLogger(CategoryDAO.class.getName());

	public void addCategory(int categoryId, String categoryName) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO categories (id, name) VALUES (?, ?)";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, categoryId);
			stmt.setString(2, categoryName);
			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				logger.info("Category has been successfully registered.");
			} else {
				logger.warning("Failed to register the category.");
			}

		} catch (SQLException | ClassNotFoundException e) {
			logger.severe("An error occurred while registering the category: " + e.getMessage());
			throw e;
		}
	}

	public List<Map<String, Object>> getAllCategories() {

		List<Map<String, Object>> categoryList = new ArrayList<>();

		try (Connection conn = ConnectionManager.getConnection();

				PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM categories");
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Map<String, Object> category = new HashMap<>();
				category.put("id", rs.getInt("id"));
				category.put("name", rs.getString("name"));
				categoryList.add(category);
			}

			if (categoryList.isEmpty()) {

				logger.info("No categories found.");

			} else {

				logger.info("Fetched " + categoryList.size() + " categories.");

			}

		} catch (SQLException | ClassNotFoundException e) {

			logger.severe("Error while fetching categories: " + e.getMessage());
			e.printStackTrace();

		}

		return categoryList;
	}
}
