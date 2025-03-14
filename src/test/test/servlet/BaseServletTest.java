package test.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entity.UserBean;
import model.entity.UserRole;

abstract class BaseServletTest {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

	@BeforeEach
	void setUpCommon() {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);

		when(request.getSession()).thenReturn(session);

		when(session.getAttribute("user")).thenReturn(null);
	}

	protected void simulateLogin() {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		UserBean user = new UserBean(
				1,
				"Maiko",
				"Yasukochi",
				"maiko@example.com",
				"00000000000",
				"Fukuoka",
				"Maiko12345",
				UserRole.USER,
				now,
				now);

		when(session.getAttribute("user")).thenReturn(user);
	}

	protected void simulateLogout() {
		when(session.getAttribute("user")).thenReturn(null);
	}

	protected void assertLoggedIn() {
		assertNotNull(session.getAttribute("user"), "User should be logged in.");
	}

	protected void assertLoggedOut() {
		assertNull(session.getAttribute("user"), "User should be logged out.");
	}
}
