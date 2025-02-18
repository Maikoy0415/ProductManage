package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.entity.CategoryBean;

public class CategoryDAO {

	private static final Logger logger = Logger.getLogger(CategoryDAO.class.getName());

	public void addCategory(CategoryBean category) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO categories (name, created_at, updated_at) VALUES (?, ?, ?)";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category.getName());
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			stmt.setTimestamp(2, currentTime); // created_at
			stmt.setTimestamp(3, currentTime); // updated_at

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

	public static String getCategoryNameById(int categoryId) {
		String query = "SELECT name FROM categories WHERE id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, categoryId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("name");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CategoryBean getCategoryById(int categoryId) throws SQLException, ClassNotFoundException {
		String sql = "SELECT id, name, created_at, updated_at FROM categories WHERE id = ?";
		CategoryBean category = null;

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, categoryId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					category = new CategoryBean();
					category.setId(rs.getInt("id"));
					category.setName(rs.getString("name"));
					category.setCreatedAt(rs.getTimestamp("created_at"));
					category.setUpdatedAt(rs.getTimestamp("updated_at"));
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			logger.severe("Error while fetching category by ID: " + e.getMessage());
			throw e;
		}

		return category;
	}

	public List<CategoryBean> getAllCategories() throws SQLException, ClassNotFoundException {
		List<CategoryBean> categoryList = new ArrayList<>();
		String sql = "SELECT id, name, created_at, updated_at FROM categories";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				CategoryBean category = new CategoryBean();
				category.setId(rs.getInt("id"));
				category.setName(rs.getString("name"));
				category.setCreatedAt(rs.getTimestamp("created_at"));
				category.setUpdatedAt(rs.getTimestamp("updated_at"));
				categoryList.add(category);
			}

		} catch (SQLException | ClassNotFoundException e) {
			logger.severe("Error while fetching categories: " + e.getMessage());
			throw e;
		}

		return categoryList;
	}

	public void updateCategory(CategoryBean category) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE categories SET name = ?, updated_at = ? WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category.getName());
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Set updated_at
			stmt.setInt(3, category.getId());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				logger.info("Category has been successfully updated.");
			} else {
				logger.warning("Failed to update the category.");
			}

		} catch (SQLException | ClassNotFoundException e) {
			logger.severe("Error while updating category: " + e.getMessage());
			throw e;
		}
	}

	public void deleteCategory(int categoryId) throws SQLException, ClassNotFoundException {
		String sql = "DELETE FROM categories WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, categoryId);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				logger.info("Category has been successfully deleted.");
			} else {
				logger.warning("Failed to delete the category.");
			}

		} catch (SQLException | ClassNotFoundException e) {
			logger.severe("Error while deleting category: " + e.getMessage());
			throw e;
		}
	}
}
