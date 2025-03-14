package test.servlet;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.entity.UserBean;
import servlet.LoginServlet;

class LoginServletTest {
	private LoginServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private RequestDispatcher dispatcher;
	private UserDAO mockUserDAO;

	@BeforeEach
	void setUp() {
		mockUserDAO = mock(UserDAO.class);
		servlet = new LoginServlet(mockUserDAO); 
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		dispatcher = mock(RequestDispatcher.class);

		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
	}

	@Test
	void testValidLogin() throws ServletException, IOException {
		// テストデータ
		UserBean mockUser = new UserBean();
		when(request.getParameter("email")).thenReturn("valid@example.com");
		when(request.getParameter("password")).thenReturn("correctpassword");
		when(mockUserDAO.authenticateUser("valid@example.com", "correctpassword")).thenReturn(mockUser);

		servlet.doPost(request, response);

		verify(session).setAttribute("user", mockUser);
		verify(response).sendRedirect("/ProductManage/home.jsp");
	}

	@Test
	void testInvalidLogin() throws ServletException, IOException {
		when(request.getParameter("email")).thenReturn("invalid@example.com");
		when(request.getParameter("password")).thenReturn("wrongpassword");
		when(mockUserDAO.authenticateUser("invalid@example.com", "wrongpassword")).thenReturn(null);

		servlet.doPost(request, response);

		verify(request).setAttribute("errorMessage", "Incorrect email or password.");
		verify(dispatcher).forward(request, response);
	}
}
