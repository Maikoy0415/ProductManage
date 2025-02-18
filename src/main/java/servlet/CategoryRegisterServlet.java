package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO;
import model.entity.CategoryBean;

@WebServlet("/category-register")
public class CategoryRegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String categoryName = request.getParameter("categoryName");
		String errorMessage = null;

		if (categoryName == null || categoryName.isEmpty()) {
			errorMessage = "Category name is required.";
		}

		if (errorMessage != null) {
			request.setAttribute("errorMessage", errorMessage);
			request.getRequestDispatcher("/category-register.jsp").forward(request, response);
			return;
		}

		CategoryBean category = new CategoryBean();
		category.setName(categoryName);

		CategoryDAO categoryDAO = new CategoryDAO();
		try {
			categoryDAO.addCategory(category);
		} catch (Exception e) {
			request.setAttribute("errorMessage", "An error occurred while registering the category.");
			request.getRequestDispatcher("/category-register.jsp").forward(request, response);
			return;
		}

		response.sendRedirect(request.getContextPath() + "/category-list");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/category-register.jsp").forward(request, response);
	}
}
