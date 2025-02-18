package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO;
import model.entity.CategoryBean;

@WebServlet("/category-list")
public class CategoryListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CategoryDAO categoryDAO = new CategoryDAO();

		try {

			List<CategoryBean> categoryList = categoryDAO.getAllCategories();

			request.setAttribute("categoryList", categoryList);

		} catch (Exception e) {
			request.setAttribute("errorMessage", "Failed to retrieve the category list.");
			e.printStackTrace();
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/category-list.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
