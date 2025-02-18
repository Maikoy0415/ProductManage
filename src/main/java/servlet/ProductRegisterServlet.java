package servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.entity.CategoryBean;
import model.entity.ProductBean;

@WebServlet("/ProductManage/product-register")
public class ProductRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String name = request.getParameter("name");
		String priceStr = request.getParameter("price");
		String stockStr = request.getParameter("stock");
		String categoryIdStr = request.getParameter("category");
		String description = request.getParameter("description");

		if (name == null || name.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Product name is required.");
		} else if (priceStr == null || priceStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Price is required.");
		} else if (stockStr == null || stockStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Stock is required.");
		} else if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Select a category.");
		}

		if (priceStr != null && !priceStr.trim().isEmpty()) {
			try {
				int price = Integer.parseInt(priceStr);
				if (price <= 0) {
					request.setAttribute("errorMessage", "Price must be positive values.");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Price must be a valid number.");
			}
		}

		if (stockStr != null && !stockStr.trim().isEmpty()) {
			try {
				int stock = Integer.parseInt(stockStr);
				if (stock < 0) {
					request.setAttribute("errorMessage", "Stock must be positive values.");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("errorMessage", "Stock must be a valid number.");
			}
		}

		if (request.getAttribute("errorMessage") != null) {
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
			product.setSupplierId(1); // 仮
			product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

			boolean isInserted = ProductDAO.addProduct(product);

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
