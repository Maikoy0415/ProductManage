package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.ProductBean;

public class ProductDAO {
	private static final String TABLE_NAME = "products";

	public static ProductBean getProductById(int productId) {
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, productId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapResultSetToProduct(rs);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<ProductBean> getAllProducts() {
		List<ProductBean> products = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME;
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				ProductBean product = mapResultSetToProduct(rs);
				String categoryName = CategoryDAO.getCategoryNameById(product.getCategoryId());
				product.setCategoryName(categoryName);
				products.add(product);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return products;
	}

	public static boolean addProduct(ProductBean product) {
		String query = "INSERT INTO " + TABLE_NAME
				+ " (name, description, price, stock_quantity, category_id, supplier_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {

			setProductStatement(stmt, product);

			int rowsAffected = stmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean updateProduct(ProductBean product) {
		if (product.getId() == 0) {
			System.out.println("Error: Product ID is missing.");
			return false;
		}

		String query = "UPDATE " + TABLE_NAME
				+ " SET name = ?, description = ?, price = ?, stock_quantity = ?, category_id = ?, supplier_id = ?, updated_at = ? WHERE id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {

			stmt.setString(1, product.getName());
			stmt.setString(2, product.getDescription() != null ? product.getDescription() : "");
			stmt.setInt(3, product.getPrice());
			stmt.setInt(4, product.getStockQuantity());
			stmt.setInt(5, product.getCategoryId());
			stmt.setInt(6, product.getSupplierId());
			stmt.setTimestamp(7, product.getUpdatedAt());
			stmt.setInt(8, product.getId());
			return stmt.executeUpdate() > 0;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteProduct(int productId) {
		String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, productId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static ProductBean mapResultSetToProduct(ResultSet rs) throws SQLException {
		return new ProductBean(
				rs.getInt("id"),
				rs.getString("name"),
				rs.getString("description"),
				rs.getInt("price"),
				rs.getInt("stock_quantity"),
				rs.getInt("category_id"),
				rs.getInt("supplier_id"),
				rs.getTimestamp("created_at"),
				rs.getTimestamp("updated_at"));
	}

	private static void setProductStatement(PreparedStatement stmt, ProductBean product) throws SQLException {
		stmt.setString(1, product.getName());
		stmt.setString(2, product.getDescription() != null ? product.getDescription() : "");
		stmt.setInt(3, product.getPrice());
		stmt.setInt(4, product.getStockQuantity());
		stmt.setInt(5, product.getCategoryId());
		stmt.setInt(6, product.getSupplierId());
		stmt.setTimestamp(7, product.getCreatedAt());
		stmt.setTimestamp(8, product.getUpdatedAt());

	}

}
