package test.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dao.CategoryDAO;
import model.dao.ConnectionManager;
import model.dao.ProductDAO;
import model.entity.ProductBean;

class ProductRegisterServletTest extends BaseServletTest {

	private ProductDAO productDao;

	@BeforeEach
	void setUp() {
		System.out.println("BeforeEach: Setting up test...");
		ConnectionManager.setUseTestDatabase(true);
		
		CategoryDAO categoryDao = mock(CategoryDAO.class);
		when(categoryDao.getCategoryNameById(anyInt())).thenReturn("Test Category");

		productDao = new ProductDAO(categoryDao);

		simulateLogin();
	}

	@Test
	void testProductRegisterSuccess() throws SQLException {
		ProductBean product = new ProductBean();
		product.setName("Product C");
		product.setPrice(1000);
		product.setStockQuantity(5);
		product.setCategoryId(1);
		product.setSupplierId(1);
		product.setDescription("Description C");

		boolean isAdded = productDao.addProduct(request, product);

		assertTrue(isAdded);

		try (Connection conn = ConnectionManager.getConnection()) {
			String sql = "SELECT * FROM products WHERE name = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, "Product C"); 
				try (ResultSet rs = stmt.executeQuery()) {
					assertTrue(rs.next(), "Product should be added to the database.");
					assertEquals("Product C", rs.getString("name"));
					assertEquals(1000, rs.getInt("price"));
					assertEquals(5, rs.getInt("stock_quantity"));

					String createdAt = rs.getString("created_at");
					String updatedAt = rs.getString("updated_at");

					assertNotNull(rs.getTimestamp("created_at"), "created_at should not be null");
					assertNotNull(rs.getTimestamp("updated_at"), "updated_at should not be null");

					assertEquals(createdAt, updatedAt, "created_at and updated_at should match initially.");
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			fail("Database connection or query failed: " + e.getMessage());
		}
	}

	@Test
	void testProductRegisterFailure_EmptyFields() {
		ProductBean product = new ProductBean();

		// 1. 名前が空の場合
		when(request.getParameter("name")).thenReturn("");
		when(request.getParameter("price")).thenReturn("100");
		when(request.getParameter("stock")).thenReturn("10");
		when(request.getParameter("category")).thenReturn("1");
		when(request.getParameter("description")).thenReturn("Description A");

		assertFalse(productDao.addProduct(request, product), "Product should not be added with empty name.");

		// 2. 価格が0の場合
		when(request.getParameter("name")).thenReturn("Product A");
		when(request.getParameter("price")).thenReturn("0");
		assertFalse(productDao.addProduct(request, product), "Product should not be added with zero price.");

		// 3. 在庫数が負の場合
		when(request.getParameter("price")).thenReturn("100");
		when(request.getParameter("stock")).thenReturn("-1");
		assertFalse(productDao.addProduct(request, product),
				"Product should not be added with negative stock quantity.");

		// 4. カテゴリIDが無効の場合
		when(request.getParameter("stock")).thenReturn("10");
		when(request.getParameter("category")).thenReturn("0");
		assertFalse(productDao.addProduct(request, product), "Product should not be added with invalid category ID.");
	}

	@Test
	void testRedirectToLoginWhenNotLoggedIn() throws Exception {
		simulateLogout();

		ProductBean product = new ProductBean();
		product.setName("Product B");
		product.setPrice(200);
		product.setStockQuantity(30);
		product.setCategoryId(1);
		product.setSupplierId(1);
		product.setDescription("Description B");

		boolean isAdded = productDao.addProduct(request, product);
		assertFalse(isAdded, "Product should not be added without login.");
	}
}
