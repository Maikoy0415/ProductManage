package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO; // CategoryDAOをインポート
import model.dao.ProductDAO;
import model.entity.ProductBean;

@WebServlet("/ProductManage/product-list")
public class ProductListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			List<ProductBean> productList = ProductDAO.getAllProducts();

			for (ProductBean product : productList) {
				String categoryName = CategoryDAO.getCategoryNameById(product.getCategoryId());
				product.setCategoryName(categoryName);
			}

			request.setAttribute("productList", productList);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Error fetching product list.");
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/product-list.jsp");
		dispatcher.forward(request, response);
	}
}
