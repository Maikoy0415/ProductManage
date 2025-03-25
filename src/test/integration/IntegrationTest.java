package integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CategoryDAO;
import model.dao.ConnectionManager;
import model.dao.ProductDAO;
import model.entity.ProductBean;
import model.entity.UserBean;
import servlet.LogoutServlet;

public class IntegrationTest {
	private static final String BASE_URL = "http://localhost:8080/ProductManage";
	private static final String PRODUCT_LIST_URL = "/product-list";
	private static final String PRODUCT_REGISTER_URL = "/product-register";

	// ユーザーログインのモック
	public String loginUser(HttpServletRequest request, String email, String password) throws Exception {
		String query = "SELECT * FROM users WHERE email = ? AND password = ?";
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				HttpSession session = request.getSession();
				if (session == null) {
					session = mock(HttpSession.class);
				}

				UserBean user = new UserBean();
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));

				when(request.getSession()).thenReturn(session);
				session.setAttribute("user", user);

				return "Login successful, redirected to home.jsp";
			} else {
				return "Login failed or unexpected response";
			}
		}
	}

	// GETリクエストを送信し、レスポンスを取得
	public String sendGetRequest(String url) throws Exception {
		URI uri = new URI(BASE_URL + url);
		URL obj = uri.toURL();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException("Failed to get response from server, HTTP code: " + responseCode);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			return response.toString();
		}
	}

	public int sendGetRequestWithStatus(String url) throws Exception {
		URI uri = new URI(BASE_URL + url);
		URL obj = uri.toURL();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("GET");

		return con.getResponseCode();
	}

	public int getResponseStatus(String url) throws Exception {
		URI uri = new URI(BASE_URL + url);
		URL obj = uri.toURL();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		return con.getResponseCode();
	}

	// テスト用のデータベース接続確認
	@Test
	public void testDatabaseConnection() {
		ConnectionManager.setUseTestDatabase(true);
		String query = "SELECT * FROM users WHERE email = 'testuser@example.com' AND password = 'testpass'";
		try (Connection conn = ConnectionManager.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			assertTrue(rs.next(), "User should exist in the database");
		} catch (Exception e) {
			fail("Database connection failed: " + e.getMessage());
		}
	}

	// ログイン機能テスト
	@Test
	public void testLogin() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		String email = "testuser@example.com";
		String password = "testpass";

		loginUser(request, email, password);
		verify(session).setAttribute(eq("user"), any(UserBean.class));
	}

	// ログアウト機能テスト
	@Test
	public void testLogout() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);

		LogoutServlet servlet = new LogoutServlet();
		servlet.doGet(request, response);

		verify(session).invalidate();
		verify(response).sendRedirect(BASE_URL + "/login.jsp");
	}

	// ログイン後の商品登録ページへのアクセス
	@Test
	public void testProductRegistrationPageAccessAfterLogin() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		loginUser(request, "testuser@example.com", "testpass");

		int statusCode = getResponseStatus(PRODUCT_REGISTER_URL);
		System.out.println("Response Code: " + statusCode);

		assertEquals(200, statusCode, "Expected status code 200 but got: " + statusCode);
	}

	// ログイン後の商品一覧ページへのアクセス
	@Test
	public void testProductListPageAccessAfterLogin() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		loginUser(request, "testuser@example.com", "testpass");

		int statusCode = getResponseStatus(PRODUCT_LIST_URL);
		System.out.println("Response Code: " + statusCode);

		assertEquals(200, statusCode, "Expected status code 200 but got: " + statusCode);
	}

	// ログインなしで商品一覧ページにアクセス
	@Test
	public void testAccessProductListWithoutLogin() throws Exception {
		// 未ログイン状態のリクエストをシミュレート
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);

		int responseCode = sendGetRequestWithStatus(PRODUCT_LIST_URL);

		assertEquals(302, responseCode, "Expected redirect but got a different status code.");
	}

	// 製品登録後に商品一覧が更新されることを確認
	@Test
	public void testProductRegistrationUpdatesProductList() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);

		UserBean user = new UserBean();
		user.setEmail("testuser@example.com");
		when(session.getAttribute("user")).thenReturn(user);

		CategoryDAO categoryDAO = mock(CategoryDAO.class);
		ProductDAO productDao = new ProductDAO(categoryDAO);

		boolean isProductAdded = submitProductRegistration(request, "Register Test", "Description");

		if (isProductAdded) {
			String redirectUrl = BASE_URL + "/product-list";

			String response = sendGetRequest(redirectUrl);

			System.out.println("Response HTML: " + response);

			List<ProductBean> productList = productDao.getAllProducts();
			ProductBean newProduct = productList.stream()
					.filter(p -> p.getName().equals("Register Test"))
					.findFirst()
					.orElseThrow(() -> new AssertionError("Register Test not found in DB"));

			assertTrue(response.contains("id=\"" + newProduct.getId() + "\""),
					"Expected product ID " + newProduct.getId() + " to be in the list but was not found in response.");
		} else {
			fail("Product registration failed");
		}
	}

	// 商品の編集が正しく行われることを確認
	@Test
	public void testProductEditUpdatesProductList() throws SQLException, ClassNotFoundException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);

		when(request.getSession(false)).thenReturn(session);

		UserBean user = new UserBean();
		user.setEmail("testuser@example.com");
		when(session.getAttribute("user")).thenReturn(user);

		ProductBean product = new ProductBean();
		product.setId(2);
		product.setName("Updated Product");
		product.setDescription("Updated Description");
		product.setPrice(1500);
		product.setStockQuantity(20);
		product.setCategoryId(1);
		product.setSupplierId(1);

		ProductDAO productDAO = new ProductDAO(new CategoryDAO());
		boolean result = productDAO.updateProduct(request, product);

		assertTrue(result, "Product update should be successful");

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM products WHERE id = ?")) {

			stmt.setInt(1, product.getId());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				assertEquals("Updated Product", rs.getString("name"));
				assertEquals("Updated Description", rs.getString("description"));
				assertEquals(1500, rs.getInt("price"));
				assertEquals(20, rs.getInt("stock_quantity"));
				assertNotNull(rs.getTimestamp("updated_at"));
			} else {
				fail("Product not found in the database.");
			}
		} catch (SQLException e) {
			fail("Database error occurred while checking updated product: " + e.getMessage());
		}
	}

	@Test
	public void testProductDeletionUpdatesProductList() throws Exception {
		ConnectionManager.setUseTestDatabase(true); 

		int productIdToDelete = 1; // ここに削除する商品のIDを手動で入力

		deleteProduct(productIdToDelete);

		String response = sendGetRequest(PRODUCT_LIST_URL);

		assertFalse(response.contains("Product ID: " + productIdToDelete));
	}

	private void deleteProduct(int productId) throws Exception {
		String sql = "DELETE FROM products WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, productId);
			stmt.executeUpdate();
		}
	}

	private boolean submitProductRegistration(HttpServletRequest request, String name, String description)
			throws Exception {

		ProductBean product = new ProductBean();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(100); 
		product.setStockQuantity(10); 
		product.setCategoryId(1); 
		product.setSupplierId(1); 

		when(request.getParameter("name")).thenReturn(name);
		when(request.getParameter("description")).thenReturn(description);
		when(request.getParameter("price")).thenReturn("100");
		when(request.getParameter("stock_quantity")).thenReturn("10");
		when(request.getParameter("category_id")).thenReturn("1");
		when(request.getParameter("supplier_id")).thenReturn("1");

		ProductDAO productDao = new ProductDAO(null);

		boolean isAdded = productDao.addProduct(request, product);

		if (isAdded) {
			List<ProductBean> productList = productDao.getAllProducts();

			boolean exists = productList.stream()
					.anyMatch(p -> p.getName().equals(name) && p.getDescription().equals(description));

			if (!exists) {
				throw new AssertionError("The registered product was not found in the database: " + name);
			}

			String response = sendGetRequest(PRODUCT_LIST_URL);

			System.out.println("Response HTML: " + response);

			if (!response.contains(name)) {
				throw new AssertionError("The new product was not found on the product list page: " + name);
			}

			return true;
		}

		return false;
	}

}
