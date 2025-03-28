package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.entity.ProductBean;

public class ProductDAO {
	private static final String TABLE_NAME = "products";
	private CategoryDAO categoryDao;

	public ProductDAO(CategoryDAO categoryDao) {
		this.categoryDao = categoryDao;
	}

	public List<ProductBean> getProductsByCategory(int categoryId) {
		List<ProductBean> products = new ArrayList<>();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE category_id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, categoryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ProductBean product = mapResultSetToProduct(rs);
				String categoryName = categoryDao.getCategoryNameById(product.getCategoryId());
				product.setCategoryName(categoryName != null ? categoryName : "Unknown");
				products.add(product);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return products;
	}

	public ProductBean getProductById(int productId) {
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

	public List<ProductBean> getAllProducts() {
		List<ProductBean> products = new ArrayList<>();

		String query = "SELECT * FROM products";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				ProductBean product = mapResultSetToProduct(rs);

				// Check if categoryDao is not null before accessing it
				if (categoryDao != null) {
					String categoryName = categoryDao.getCategoryNameById(product.getCategoryId());
					product.setCategoryName(categoryName != null ? categoryName : "Unknown");
				} else {
					product.setCategoryName("Unknown");
				}

				products.add(product);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return products;
	}

	public boolean addProduct(HttpServletRequest request, ProductBean product) {
		HttpSession session = request.getSession();
		Object userObj = session.getAttribute("user");

		if (userObj == null) {
			return false;
		}

		if (!isValidProduct(product)) {
			return false;
		}

		String query = "INSERT INTO " + TABLE_NAME
				+ " (name, description, price, stock_quantity, category_id, supplier_id, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { 

			connection.setAutoCommit(false);

			setProductStatement(stmt, product);
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			stmt.setTimestamp(7, currentTimestamp);
			stmt.setTimestamp(8, currentTimestamp);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						product.setId(generatedKeys.getInt(1));
					}
				}
				connection.commit();
				return true;
			} else {
				return false;
			}

		} catch (SQLException | ClassNotFoundException e) {
			System.err.println("Error in addProduct(): " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private boolean isValidProduct(ProductBean product) {
		if (product.getName() == null || product.getName().trim().isEmpty()) {
			System.out.println("Product name is invalid.");
			return false;
		}

		if (product.getPrice() <= 0) {
			System.out.println("Product price is invalid.");
			return false;
		}

		if (product.getStockQuantity() < 0) {
			System.out.println("Product stock quantity is invalid.");
			return false;
		}

		if (product.getCategoryId() <= 0) {
			System.out.println("Product category ID is invalid.");
			return false;
		}

		return true;
	}

	public boolean updateProduct(HttpServletRequest request, ProductBean product) {
		try {
			// 更新クエリ
			String query = "UPDATE " + TABLE_NAME
					+ " SET name = ?, description = ?, price = ?, stock_quantity = ?, category_id = ?, supplier_id = ?, updated_at = ? WHERE id = ?";

			System.out.println("Executing query: " + query); // クエリ内容を出力

			try (Connection connection = ConnectionManager.getConnection();
					PreparedStatement stmt = connection.prepareStatement(query)) {

				// updated_at を現在のタイムスタンプに設定
				product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

				// ステートメントにパラメータを設定
				setProductStatement(stmt, product);
				stmt.setTimestamp(7, product.getUpdatedAt()); // updated_at を設定
				stmt.setInt(8, product.getId()); // ID を設定

				System.out.println("Updated product ID: " + product.getId()); // 更新される製品のIDを表示

				// クエリの実行
				int rowsAffected = stmt.executeUpdate();
				System.out.println("Rows affected: " + rowsAffected); // 影響を受けた行数を出力

				// 影響を受けた行数が0の場合、問題が発生している可能性がある
				if (rowsAffected == 0) {
					System.out.println("No rows were updated. Check if the product ID exists in the database.");
				}

				// 成功した場合は true を返す
				return rowsAffected > 0;

			} catch (SQLException e) {
				System.err.println("SQL error while updating product: " + e.getMessage());
				e.printStackTrace();
				return false;
			}

		} catch (Exception e) {
			System.err.println("Error occurred while updating product: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteProduct(int productId) {
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
