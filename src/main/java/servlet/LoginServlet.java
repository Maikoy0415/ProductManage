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
	private UserDAO userDAO; 

	public LoginServlet() {
		this.userDAO = new UserDAO();
	}

	// テスト用のコンストラクタ（依存性注入）
	public LoginServlet(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("request method: " + request.getMethod());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserBean user = userDAO.authenticateUser(email, password); 

		if (user != null) {
			// success
			request.getSession().setAttribute("user", user);
			response.sendRedirect("/ProductManage/home.jsp");
		} else {
			// fail
			request.setAttribute("errorMessage", "Incorrect email or password.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		}
	}
}
