package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.UserDAO;
import model.entity.UserBean;

@WebServlet("/ProductManage/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("request method: " + request.getMethod());
		RequestDispatcher dispatcher = request.getRequestDispatcher(request.getContextPath() + "/login.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserBean user = UserDAO.authenticateUser(email, password);

		if (user != null) {
			// success
			request.getSession().setAttribute("user", user);
			response.sendRedirect("/home.jsp");
		} else {
			// fail
			request.setAttribute("errorMessage", "Incorrect email or password.");
			RequestDispatcher dispatcher = request.getRequestDispatcher(request.getContextPath() + "/login.jsp");
			dispatcher.forward(request, response);
		}
	}
}
