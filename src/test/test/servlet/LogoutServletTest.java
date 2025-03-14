package test.servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlet.LogoutServlet;

class LogoutServletTest {
	private LogoutServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	@BeforeEach
	void setUp() {
		servlet = new LogoutServlet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);

		when(request.getSession(false)).thenReturn(session);
		when(request.getContextPath()).thenReturn("/ProductManage");
	}

	@Test
	void testLogoutWithoutSession() throws ServletException, IOException {
		servlet.doGet(request, response);
		
        verify(session).invalidate();

		verify(response).sendRedirect("/ProductManage/login.jsp");
	}
}
