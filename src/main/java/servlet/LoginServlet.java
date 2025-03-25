package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.entity.UserBean;

@WebServlet("/ProductManage/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public LoginServlet() {
        this.userDAO = new UserDAO();
    }

    public LoginServlet(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // セッションが存在し、すでにログインしている場合は、home.jsp にリダイレクト
        if (request.getSession(false) != null && request.getSession().getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            return;
        }

        // ログインページを表示
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // ユーザー認証
        UserBean userBean = userDAO.authenticateUser(email, password);
        if (userBean != null) {
            // ログイン成功
            System.out.println("Login successful. Email: " + userBean.getEmail());
            System.out.println("User first name: " + userBean.getFirstName()); // ← 追加
            HttpSession session = request.getSession(true); // セッションを作成
            session.setAttribute("user", userBean); // ユーザー情報をセッションに格納

            // セッションの作成を確認
            System.out.println("Session created with ID: " + session.getId());

            // ホームページにリダイレクト
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            // ログイン失敗
            System.out.println("Login failed. Email: " + email); // デバッグログ
            request.setAttribute("errorMessage", "Incorrect email or password.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response); // ログインページへフォワード
        }
    }
}
