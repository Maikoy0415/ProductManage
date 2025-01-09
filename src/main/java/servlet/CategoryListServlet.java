package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO;

@WebServlet("/category-list")
public class CategoryListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Map<String, Object>> categoryList = categoryDAO.getAllCategories();
        
        // データが取得できているか確認
        if (categoryList == null) {
            System.out.println("categoryList is null");
        } else if (categoryList.isEmpty()) {
            System.out.println("categoryList is empty");
        } else {
            System.out.println("categoryList size: " + categoryList.size());
        }

        // categoryList をリクエスト属性として設定
        request.setAttribute("categoryList", categoryList);

        // デバッグ出力
        System.out.println("categoryList in servlet: " + categoryList);

        // JSPにフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("/category-list.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
