package servlet;

import java.io.IOException;
import java.sql.SQLException;
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

@WebServlet("/ProductManage/product-edit")
public class ProductEditServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int productId = Integer.parseInt(request.getParameter("id"));

		ProductBean product = ProductDAO.getProductById(productId);

		CategoryDAO categoryDAO = new CategoryDAO();
		List<CategoryBean> categoryList = null;
		try {
			categoryList = categoryDAO.getAllCategories();
		} catch (SQLException | ClassNotFoundException e) {
			request.setAttribute("errorMessage", "Failed to retrieve category list.");
			e.printStackTrace();
		}

		if (product == null) {
			request.setAttribute("errorMessage", "Product not found.");
			request.getRequestDispatcher("/product-list.jsp").forward(request, response);
			return;
		}

		request.setAttribute("product", product);
		request.setAttribute("categoryList", categoryList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/product-edit.jsp");
		dispatcher.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int productId = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		int price = Integer.parseInt(request.getParameter("price"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		int categoryId = Integer.parseInt(request.getParameter("category"));
		int supplierId = 1;
		Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

		ProductBean originalProduct = ProductDAO.getProductById(productId);

		String errorMessage = null;
		if (name == null || name.trim().isEmpty()) {
			errorMessage = "Product name is required.";
		} else if (price <= 0) {
			errorMessage = "Price must be greater than zero.";
		} else if (stock < 0) {
			errorMessage = "Stock cannot be negative.";
		}

		if (errorMessage != null) {
			request.setAttribute("errorMessage", errorMessage);
			request.setAttribute("product", originalProduct);

			CategoryDAO categoryDAO = new CategoryDAO();
			List<CategoryBean> categoryList = null;
			try {
				categoryList = categoryDAO.getAllCategories();
			} catch (SQLException | ClassNotFoundException e) {
				request.setAttribute("errorMessage", "Failed to retrieve category list.");
				e.printStackTrace();
			}

			request.setAttribute("categoryList", categoryList);
			request.getRequestDispatcher("/product-edit.jsp").forward(request, response);
			return;
		}

		ProductBean product = new ProductBean(productId, name, description, price, stock, categoryId, supplierId, null,
				updatedAt);
		boolean isUpdated = ProductDAO.updateProduct(product);

		if (isUpdated) {
			response.sendRedirect("/ProductManage/product-list");
		} else {
			request.setAttribute("errorMessage", "Failed to update product.");
			request.setAttribute("product", originalProduct);
			request.getRequestDispatcher("/product-edit.jsp").forward(request, response);
		}
	}

}
