package test.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpSession;
import model.dao.CategoryDAO;
import model.dao.ConnectionManager;
import model.dao.ProductDAO;
import model.entity.ProductBean;
import model.entity.UserBean;

class ProductEditServletTest extends BaseServletTest {

	private ProductDAO productDao;
	private int testProductId = 5; // 既に存在する商品ID

	@BeforeEach
	void setUp() throws SQLException {
		when(request.getSession()).thenReturn(session);

		simulateLogin();

		// テストDBを有効化
		ConnectionManager.setUseTestDatabase(true);

		CategoryDAO categoryDao = mock(CategoryDAO.class);
		when(categoryDao.getCategoryNameById(anyInt())).thenReturn("Test Category");

		productDao = new ProductDAO(categoryDao);

		// 事前にテスト用の商品を挿入 (存在しない場合)
		if (productDao.getProductById(testProductId) == null) {
			// HttpServletRequestをモック（グローバルに定義されたrequestを使う）
			when(request.getParameter("name")).thenReturn("Test Product");
			when(request.getParameter("price")).thenReturn("20");
			when(request.getParameter("stock")).thenReturn("10");
			when(request.getParameter("category")).thenReturn("1");
			when(request.getParameter("description")).thenReturn("Test Description");

			// ProductBeanを作成して設定
			ProductBean testProduct = new ProductBean();
			testProduct.setId(testProductId); // 固定のIDをセット
			testProduct.setName("Test Product");
			testProduct.setPrice(20);
			testProduct.setStockQuantity(10);
			testProduct.setCategoryId(1);
			testProduct.setSupplierId(1);
			testProduct.setDescription("Test Description");
			testProduct.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			testProduct.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			// addProductを呼び出し
			productDao.addProduct(request, testProduct);
		}
	}

	@Test
	void testProductEditSuccess() throws SQLException {
		simulateLogin();
		assertLoggedIn();

		HttpSession mockSession = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(mockSession);

		UserBean mockUser = new UserBean();
		mockUser.setId(1);
		when(mockSession.getAttribute("user")).thenReturn(mockUser);

		ProductBean productBeforeEdit = productDao.getProductById(testProductId);
		assertNotNull(productBeforeEdit, "Product should exist in the database before edit");

		productBeforeEdit.setName("Updated Product A");
		productBeforeEdit.setPrice(100);
		productBeforeEdit.setStockQuantity(10);

		boolean isUpdated = productDao.updateProduct(request, productBeforeEdit);
		assertTrue(isUpdated, "Product should be updated successfully");

		ProductBean productAfterEdit = productDao.getProductById(testProductId);
		assertNotNull(productAfterEdit, "Product should exist in the database after edit");

		assertEquals("Updated Product A", productAfterEdit.getName());
		assertEquals(100, productAfterEdit.getPrice());
		assertEquals(10, productAfterEdit.getStockQuantity());
	}

	@Test
	void testProductEditFailure_NotLoggedIn() throws SQLException {
		simulateLogout();
		assertLoggedOut();

		HttpSession mockSession = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(mockSession);
		when(mockSession.getAttribute("user")).thenReturn(null);

		assertThrows(IllegalStateException.class, () -> {
			productDao.updateProduct(request, new ProductBean());
		});
	}

	@Test
	void testProductEditFailure_ProductNotFound() throws SQLException {
		simulateLogout();// ユーザーをログアウトさせる
		assertLoggedOut(); // ログアウト状態を確認

		HttpSession mockSession = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(mockSession);

		when(mockSession.getAttribute("user")).thenReturn(null);
		
		// 存在しない商品（無効なIDを持つ商品）を作成
		ProductBean nonExistentProduct = new ProductBean();
		nonExistentProduct.setId(-1);

		assertThrows(IllegalStateException.class, () -> {
			productDao.updateProduct(request, nonExistentProduct);
		});
	}

}
