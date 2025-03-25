package integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestHelper {
	private Connection conn;

	public TestHelper(Connection conn) {
		this.conn = conn;
	}

	// ログイン処理: 成功した場合はセッション(Map)を返す
	public Map<String, Object> login(String username, String password) throws Exception {
		Map<String, Object> session = new HashMap<>();

		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				session.put("user", username);
				return session;
			}
		}
		return null; // 認証失敗時は null を返す
	}

	// ログアウト処理: セッション情報をクリア
	public void logout(Map<String, Object> session) {
		session.clear();
	}

	// 商品一覧ページにアクセスできるか確認
	public boolean canAccessProductList(Map<String, Object> session) {
		return session.containsKey("user");
	}

	// 商品登録
	public int registerProduct(Map<String, Object> session, String name, String description, int price)
			throws Exception {
		if (!session.containsKey("user"))
			return -1; // 未ログインなら処理しない

		String sql = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, name);
			pstmt.setString(2, description);
			pstmt.setInt(3, price);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next())
				return rs.getInt(1);
		}
		return -1;
	}

	// 商品削除
	public boolean deleteProduct(Map<String, Object> session, int productId) throws Exception {
		if (!session.containsKey("user"))
			return false; // 未ログインなら処理しない

		String sql = "DELETE FROM products WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, productId);
			return pstmt.executeUpdate() > 0;
		}
	}

	// 商品詳細情報の取得
	public Map<String, String> getProductDetails(Map<String, Object> session, int productId) throws Exception {
		if (!session.containsKey("user"))
			return null; // 未ログインなら処理しない

		String sql = "SELECT * FROM products WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, productId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Map<String, String> product = new HashMap<>();
				product.put("name", rs.getString("name"));
				product.put("description", rs.getString("description"));
				product.put("price", String.valueOf(rs.getInt("price")));
				return product;
			}
		}
		return null;
	}

	// 商品一覧の取得
	public List<Integer> getProductList(Map<String, Object> session) throws Exception {
		if (!session.containsKey("user"))
			return Collections.emptyList();

		List<Integer> productList = new ArrayList<>();
		String sql = "SELECT id FROM products";
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				productList.add(rs.getInt("id"));
			}
		}
		return productList;
	}

	// 商品編集
	public boolean editProduct(Map<String, Object> session, int productId, String newName, String newDescription,
			int newPrice) throws Exception {
		if (!session.containsKey("user"))
			return false; // 未ログインなら処理しない

		String sql = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newName);
			pstmt.setString(2, newDescription);
			pstmt.setInt(3, newPrice);
			pstmt.setInt(4, productId);
			return pstmt.executeUpdate() > 0;
		}
	}
}
