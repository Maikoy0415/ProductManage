package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.UserBean;
import model.entity.UserRole;

public class UserDAO {

	// ユーザーIDでユーザーを取得
	public static UserBean getUserById(int userId) {
		String sql = "SELECT * FROM users WHERE id = ?";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs); // UserBeanにマッピング
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null; // ユーザーが見つからない場合
	}

	// メールアドレスでユーザーを取得
	public static UserBean getUserByEmail(String email) {
		String sql = "SELECT * FROM users WHERE email = ?";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return mapUser(rs); // UserBeanにマッピング
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null; // ユーザーが見つからない場合
	}

	// 全てのユーザーを取得
	public static List<UserBean> getAllUsers() {
		List<UserBean> userList = new ArrayList<>();
		String sql = "SELECT * FROM users";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				userList.add(mapUser(rs)); // UserBeanにマッピング
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userList;
	}

	// 新しいユーザーを挿入
	public static boolean insertUser(UserBean user) {
		String sql = "INSERT INTO users (first_name, last_name, email, phone_number, address, password, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getPhoneNumber());
			pstmt.setString(5, user.getAddress());
			pstmt.setString(6, user.getPassword());
			pstmt.setString(7, user.getRole().name());
			pstmt.setTimestamp(8, user.getCreatedAt());
			pstmt.setTimestamp(9, user.getUpdatedAt());
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0; // 成功した場合はtrue
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false; // 挿入失敗
	}

	// ユーザー情報を更新
	public static void updateUser(UserBean user) {
		String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ?, password = ?, role = ?, updated_at = ? WHERE id = ?";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getPhoneNumber());
			pstmt.setString(5, user.getAddress());
			pstmt.setString(6, user.getPassword());
			pstmt.setString(7, user.getRole().name());
			pstmt.setTimestamp(8, user.getUpdatedAt());
			pstmt.setInt(9, user.getId());
			pstmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// ユーザーを削除
	public static void deleteUser(int userId) {
		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// メールアドレスとパスワードでユーザー認証
	public static UserBean authenticateUser(String email, String password) {
	    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
	    try (Connection conn = ConnectionManager.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, email);
	        pstmt.setString(2, password);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return mapUser(rs); // UserBeanにマッピング
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return null; // 認証失敗
	}

	// ResultSet を UserBean にマッピング
	private static UserBean mapUser(ResultSet rs) throws SQLException {
		return new UserBean(
				rs.getInt("id"),
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("email"),
				rs.getString("phone_number"),
				rs.getString("address"),
				rs.getString("password"),
				UserRole.fromString(rs.getString("role")),
				rs.getTimestamp("created_at"),
				rs.getTimestamp("updated_at"));
	}
}
