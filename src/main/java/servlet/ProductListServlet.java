package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.entity.ProductBean;

@WebServlet("/ProductManage/product-list")
public class ProductListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductDAO productDao;
	private CategoryDAO categoryDao;

	public ProductListServlet() {
		super();
		this.categoryDao = new CategoryDAO();
		this.productDao = new ProductDAO(categoryDao);
	}

	public void setProductDao(ProductDAO productDao) {
		this.productDao = productDao;
	}

	public void setCategoryDao(CategoryDAO categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null) {
			session = request.getSession(true);
			System.out.println("ProductListServlet: Session created");
		}

		if (session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			System.out.println("ProductListServlet: User attribute is null");
			return;
		}

		System.out.println("ProductListServlet: User session is valid");

		String categoryIdParam = request.getParameter("categoryId");
		List<ProductBean> productList;

		if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
			try {
				int categoryId = Integer.parseInt(categoryIdParam);
				productList = productDao.getProductsByCategory(categoryId);
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Invalid category ID: " + categoryIdParam);
				productList = productDao.getAllProducts();
			}
		} else {
			productList = productDao.getAllProducts();
		}

		request.setAttribute("productList", productList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
		dispatcher.forward(request, response);
	}
}
