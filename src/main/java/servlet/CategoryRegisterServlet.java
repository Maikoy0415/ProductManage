package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO;

@WebServlet("/category-register")
public class CategoryRegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String categoryIdStr = request.getParameter("categoryId");
		String categoryName = request.getParameter("categoryName");

		String errorMessage = null;

		if (categoryIdStr == null || categoryIdStr.isEmpty()) {
			errorMessage = "Category ID is required.";
		} else if (!categoryIdStr.matches("\\d+")) {
			errorMessage = "Please enter a numeric value for the category ID.";
		} else if (categoryName == null || categoryName.isEmpty()) {
			errorMessage = "Category name is required.";
		}

		if (errorMessage != null) {
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("/category-register.jsp").forward(request, response);
			return;
		}

		int categoryId = Integer.parseInt(categoryIdStr);

		CategoryDAO categoryDAO = new CategoryDAO();
		try {
			categoryDAO.addCategory(categoryId, categoryName);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "An error occurred while registering the category.");
			request.getRequestDispatcher("/category-register.jsp").forward(request, response);
			return;
		}

		response.sendRedirect("CategoryListServlet");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/category-register.jsp").forward(request, response);
	}
}
