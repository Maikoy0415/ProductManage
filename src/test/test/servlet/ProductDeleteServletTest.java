package test.servlet;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CategoryDAO;
import model.dao.ConnectionManager;
import model.dao.ProductDAO;
import servlet.ProductDeleteServlet;

class ProductDeleteServletTest {

	private ProductDAO productDao;
	private ProductDeleteServlet productDeleteServlet;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private HttpSession session;

	@Mock
	private RequestDispatcher dispatcher;

	@BeforeEach
	void setUp() {
		ConnectionManager.setUseTestDatabase(true);

		MockitoAnnotations.initMocks(this);

		CategoryDAO categoryDao = new CategoryDAO();
		productDao = new ProductDAO(categoryDao);
		productDeleteServlet = new ProductDeleteServlet();

		Mockito.when(request.getSession(false)).thenReturn(session);
	}

	@Test
	void testProductDeleteSuccess() throws SQLException {
		int productId = 5; // 既にデータベースに存在する商品IDを入力する

		if (!isProductInDatabase(productId)) {
			System.err.println("Error: Product with ID " + productId + " does not exist in the database.");
			fail("Precondition failed: Product should exist in the database before deletion.");
		}

		boolean isDeleted = productDao.deleteProduct(productId);
		assertTrue(isDeleted, "Product should be deleted successfully");

		boolean isProductExists = isProductInDatabase(productId);
		assertFalse(isProductExists, "Product should not exist in the database after deletion");
	}

	@Test
	void testProductDelete_NotLoggedIn() throws Exception {
		Mockito.when(session.getAttribute("user")).thenReturn(null);

		productDeleteServlet.doPost(request, response);

		Mockito.verify(response).sendRedirect("/ProductManage/login");

		Mockito.verify(request, Mockito.times(1)).getSession(false);
	}

	@Test
	void testProductDelete_InvalidProductId() throws Exception {
		RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession(false)).thenReturn(session);
		Mockito.when(session.getAttribute("user")).thenReturn(new Object());

		// 不正な商品IDを設定（null や 空文字）
		Mockito.when(request.getParameter("productId")).thenReturn("invalid");
		Mockito.when(request.getRequestDispatcher("/product-list.jsp")).thenReturn(dispatcher);

		productDeleteServlet.doPost(request, response);

		Mockito.verify(request, Mockito.times(1)).setAttribute(Mockito.eq("errorMessage"), Mockito.anyString());

		Mockito.verify(dispatcher, Mockito.times(1)).forward(request, response);
	}

	private boolean isProductInDatabase(int productId) throws SQLException {
		String query = "SELECT id FROM products WHERE id = ?";
		try (Connection conn = ConnectionManager.getConnection()) {

			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setInt(1, productId);
				try (ResultSet rs = stmt.executeQuery()) {
					boolean exists = rs.next();
					System.out.println("Product ID " + productId + " exists: " + exists);
					return exists;
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Database connection or query failed", e);
		}
	}

}
