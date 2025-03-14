package servlet;

import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.UserDAO;
import model.entity.UserBean;
import model.entity.UserRole;

@WebServlet("/register")
public class UserRegisterServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/register.jsp").forward(request, response); // 登録ページに遷移
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String phoneNumber = request.getParameter("phone_number");
		String address = request.getParameter("address");
		String password = request.getParameter("password");

		String hashedPassword = hashPassword(password);

		UserBean user = new UserBean();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPhoneNumber(phoneNumber);
		user.setAddress(address);
		user.setPassword(hashedPassword);
		user.setRole(UserRole.USER);
		user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		UserDAO userDAO = new UserDAO();
		boolean isInserted = userDAO.insertUser(user);

		if (isInserted) {
			response.sendRedirect("/ProductManage/login.jsp");
		} else {
			request.setAttribute("errorMessage", "Registration failed. Please try again.");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
		}
	}

	private String hashPassword(String password) {
		return password;
	}
}
