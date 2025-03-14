package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import model.entity.CategoryBean;
import model.entity.ProductBean;

@WebServlet("/ProductManage/product-register")
public class ProductRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductDAO productDao;

	public ProductRegisterServlet() {
		CategoryDAO categoryDAO = new CategoryDAO();
		this.productDao = new ProductDAO(categoryDAO);
	}

	public ProductRegisterServlet(ProductDAO productDao) {
		this.productDao = productDao;
	}

	public void setProductDao(ProductDAO productDao) {
		this.productDao = productDao;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		CategoryDAO categoryDAO = new CategoryDAO();
		try {
			List<CategoryBean> categoryList = categoryDAO.getAllCategories();
			if (categoryList != null && !categoryList.isEmpty()) {
				request.setAttribute("categoryList", categoryList);
			} else {
				request.setAttribute("errorMessage", "No categories found.");
			}
		} catch (Exception e) {
			request.setAttribute("errorMessage", "Error fetching categories.");
			e.printStackTrace();
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/product-register.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		String name = request.getParameter("name");
		String priceStr = request.getParameter("price");
		String stockStr = request.getParameter("stock");
		String categoryIdStr = request.getParameter("category");
		String description = request.getParameter("description");

		List<String> errorMessages = new ArrayList<>();

		if (name == null || name.trim().isEmpty()) {
			errorMessages.add("Product name is required.");
		}
		if (priceStr == null || priceStr.trim().isEmpty()) {
			errorMessages.add("Price is required.");
		}
		if (stockStr == null || stockStr.trim().isEmpty()) {
			errorMessages.add("Stock is required.");
		}
		if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
			errorMessages.add("Select a category.");
		}

		if (priceStr != null && !priceStr.trim().isEmpty()) {
			try {
				int price = Integer.parseInt(priceStr);
				if (price <= 0) {
					errorMessages.add("Price must be positive values.");
				}
			} catch (NumberFormatException e) {
				errorMessages.add("Price must be a valid number.");
			}
		}

		if (stockStr != null && !stockStr.trim().isEmpty()) {
			try {
				int stock = Integer.parseInt(stockStr);
				if (stock < 0) {
					errorMessages.add("Stock must be positive values.");
				}
			} catch (NumberFormatException e) {
				errorMessages.add("Stock must be a valid number.");
			}
		}

		if (!errorMessages.isEmpty()) {
			request.setAttribute("errorMessages", errorMessages);
			CategoryDAO categoryDAO = new CategoryDAO();
			try {
				List<CategoryBean> categoryList = categoryDAO.getAllCategories();
				request.setAttribute("categoryList", categoryList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.getRequestDispatcher("/product-register.jsp").forward(request, response);
			return;
		}

		try {
			int price = Integer.parseInt(priceStr);
			int stock = Integer.parseInt(stockStr);
			int categoryId = Integer.parseInt(categoryIdStr);

			ProductBean product = new ProductBean();
			product.setName(name);
			product.setPrice(price);
			product.setStockQuantity(stock);
			product.setCategoryId(categoryId);
			product.setDescription(description);
			product.setSupplierId(1);
			product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			boolean isInserted = productDao.addProduct(request, product);

			if (isInserted) {
				response.sendRedirect(request.getContextPath() + "/product-list");
			} else {
				request.setAttribute("errorMessage", "Product addition failed. Please try again.");
				CategoryDAO categoryDAO = new CategoryDAO();
				try {
					List<CategoryBean> categoryList = categoryDAO.getAllCategories();
					request.setAttribute("categoryList", categoryList);
				} catch (Exception e) {
					e.printStackTrace();
				}
				request.getRequestDispatcher("/product-register.jsp").forward(request, response);
			}
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Please enter valid numbers.");
			CategoryDAO categoryDAO = new CategoryDAO();
			try {
				List<CategoryBean> categoryList = categoryDAO.getAllCategories();
				request.setAttribute("categoryList", categoryList);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			request.getRequestDispatcher("/product-register.jsp").forward(request, response);
		}
	}
}
