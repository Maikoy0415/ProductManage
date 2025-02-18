<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.List, model.entity.CategoryBean"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カテゴリ一覧</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="container mt-4">

	<!-- Logout Button -->
	<div class="d-flex justify-content-end mb-3">
		<form action="/ProductManage/logout" method="get">
			<button class="btn btn-dark btn-small">Logout</button>
		</form>
	</div>

	<h1 class="text-center">Category List</h1>

	<form action="category-register" method="GET" class="text-center">
		<button type="submit" class="btn btn-success mt-3">Register
			Category</button>
	</form>

	<%
	List<CategoryBean> categoryList = (List<CategoryBean>) request.getAttribute("categoryList");

	if (categoryList == null || categoryList.isEmpty()) {
	%>
	<p class="text-center mt-4">No category</p>
	<%
	} else {
	%>
	<table class="table table-striped mt-4 w-25 text-center border mx-auto">
		<thead>
			<tr>
				<th>ID</th>
				<th>Name</th>
			</tr>
		</thead>
		<tbody>
			<%
			for (CategoryBean category : categoryList) {
			%>
			<tr>
				<td><%=category.getId()%></td>
				<td><%=category.getName()%></td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
	<a href="/ProductManage/product-register"
		class="btn btn-dark d-block mx-auto w-25 mt-3">Register New
		Product</a>
	<%
	}
	%>

</body>
</html>
