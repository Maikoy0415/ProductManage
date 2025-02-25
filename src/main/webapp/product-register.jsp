<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.entity.CategoryBean"%>
<%
List<CategoryBean> categories = (List<CategoryBean>) request.getAttribute("categoryList");
String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Product</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="container mt-5">
	<!-- Back Button -->
	<button type="button"
		class="btn btn-small btn-outline-secondary float-start"
		onclick="window.location.href='home.jsp';">Back</button>

	<!-- Logout Button -->
	<div class="d-flex justify-content-end mb-3">
		<form action="/ProductManage/logout" method="get">
			<button class="btn btn-dark btn-small">Logout</button>
		</form>
	</div>

	<h1 class="mb-4 text-center">Register Product</h1>
	<%
	if (errorMessage != null) {
	%>
	<div class="alert alert-danger  w-50 mx-auto" role="alert">
		Error:
		<%=errorMessage%>
	</div>
	<%
	}
	%>

	<form action="/ProductManage/product-register" method="post"
		class="needs-validation w-50 mx-auto" novalidate>
		<div class="mb-3">
			<label for="name" class="form-label">Product Name:</label> <input
				type="text" id="name" name="name" class="form-control" required>
		</div>

		<div class="mb-3">
			<label for="price" class="form-label">Price:</label> <input
				type="number" id="price" name="price" class="form-control" required>
		</div>

		<div class="mb-3">
			<label for="stock" class="form-label">Stock:</label> <input
				type="number" id="stock" name="stock" class="form-control" required>
		</div>

		<div class="mb-3">
			<label for="category" class="form-label">Category:</label> <select
				id="category" name="category" class="form-select" required>
				<%
				List<CategoryBean> categoryList = (List<CategoryBean>) request.getAttribute("categoryList");

				if (categoryList != null && !categoryList.isEmpty()) {
				%>
				<option value="">Select a category</option>
				<%
				for (CategoryBean category : categoryList) {
				%>
				<option value="<%=category.getId()%>"><%=category.getName()%></option>
				<%
				}
				} else {
				%>
				<option disabled>No categories available</option>
				<%
				}
				%>
			</select>
		</div>

		<div class="mb-3">
			<label for="description" class="form-label">Description:</label>
			<textarea id="description" name="description" class="form-control"
				rows="3" required></textarea>
		</div>

		<button type="submit" class="btn btn-dark w-100 mt-3">Register</button>
	</form>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
