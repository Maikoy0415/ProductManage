package test.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.entity.ProductBean;
import servlet.ProductListServlet;

class ProductListServletTest extends BaseServletTest {
	private ProductListServlet servlet;
	private ProductDAO productDao;
	private CategoryDAO categoryDao;

	@BeforeEach
	void setUp() throws Exception {
		super.setUpCommon();

		categoryDao = new CategoryDAO();
		productDao = new ProductDAO(categoryDao);

		servlet = new ProductListServlet();

		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(request.getRequestDispatcher("/product-list.jsp")).thenReturn(dispatcher);

		servlet.setProductDao(productDao);
		servlet.setCategoryDao(categoryDao);
	}

	@Test
	void testProductListWhenLoggedIn() throws ServletException, IOException {
		simulateLogin();

		List<ProductBean> productList = productDao.getAllProducts();

		servlet.doGet(request, response);

		verify(request).setAttribute(eq("productList"), anyList());

		Object attribute = request.getAttribute("productList");
		if (attribute instanceof List<?>) {
			List<?> actualList = (List<?>) attribute;
			assertNotNull(actualList);
			assertEquals(productList.size(), actualList.size());

			for (int i = 0; i < productList.size(); i++) {
				ProductBean expected = productList.get(i);
				ProductBean actual = (ProductBean) actualList.get(i);

				assertEquals(expected.getName(), actual.getName());
				assertEquals(expected.getDescription(), actual.getDescription());
				assertEquals(expected.getPrice(), actual.getPrice());
			}
		}

		verify(request.getRequestDispatcher("/product-list.jsp")).forward(request, response);
	}

	@Test
	void testUnauthorizedAccessWhenLoggedOut() throws ServletException, IOException {
		simulateLogout();

		servlet.doGet(request, response);

		verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
	}

	@Test
	void testUnauthorizedAccessWhenNoLoginSession() throws ServletException, IOException {
		when(request.getSession(false)).thenReturn(null);

		servlet.doGet(request, response);

		verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
	}

	protected void simulateLogin() {
		HttpSession session = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(new Object());
	}

	protected void simulateLogout() {
		HttpSession session = mock(HttpSession.class);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
	}
}
