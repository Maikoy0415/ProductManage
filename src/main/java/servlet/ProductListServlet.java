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
		if (session == null || session.getAttribute("user") == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
			return;
		}

		try {
			String categoryIdParam = request.getParameter("categoryId");
			List<ProductBean> productList;

			if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
				try {
					int categoryId = Integer.parseInt(categoryIdParam);
					productList = productDao.getProductsByCategory(categoryId);
				} catch (NumberFormatException e) {
					request.setAttribute("errorMessage", "Invalid category ID.");
					productList = productDao.getAllProducts();
				}
			} else {
				productList = productDao.getAllProducts();
			}

			request.setAttribute("productList", productList);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Error fetching product list: " + e.getMessage());
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
		dispatcher.forward(request, response);
	}

}
