package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CategoryDAO;
import model.dao.ProductDAO;

@WebServlet("/ProductManage/product-delete")
public class ProductDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductDAO productDao;

	@Override
	public void init() throws ServletException {
		CategoryDAO categoryDAO = new CategoryDAO();
		productDao = new ProductDAO(categoryDAO);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("/ProductManage/login");
			return;
		}

		String productIdStr = request.getParameter("productId");

		if (productIdStr == null || productIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Invalid product ID.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
			dispatcher.forward(request, response);
			return;
		}

		try {
			int productId = Integer.parseInt(productIdStr.trim());

			boolean isDeleted = productDao.deleteProduct(productId);
			if (isDeleted) {
				response.sendRedirect("/ProductManage/product-list");
			} else {
				request.setAttribute("errorMessage", "Failed to delete the product.");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
				dispatcher.forward(request, response);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Invalid product ID format.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Error during deletion.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
			dispatcher.forward(request, response);
		}
	}

}
