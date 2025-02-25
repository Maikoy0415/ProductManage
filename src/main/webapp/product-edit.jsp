<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.entity.ProductBean"%>
<%@ page import="model.entity.CategoryBean"%>
<%@ page import="java.util.List"%>
<%
ProductBean product = (ProductBean) request.getAttribute("product");
String errorMessage = (String) request.getAttribute("errorMessage");
List<CategoryBean> categoryList = (List<CategoryBean>) request.getAttribute("categoryList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Product</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="container mt-5 mb-3 w-25 mx-auto">

	<h1 class="text-center">Edit Product</h1>

	<form action="/ProductManage/product-edit" method="POST">
		<input type="hidden" name="id" value="<%=product.getId()%>">

		<div class="mb-3">
			<label for="name" class="form-label">Product Name</label> <input
				type="text" class="form-control" id="name" name="name"
				value="<%=product.getName()%>">
			<%
			if (errorMessage != null && errorMessage.contains("Product name")) {
			%>
			<div class="text-danger mt-2">
				<%=errorMessage%>
			</div>
			<%
			}
			%>

		</div>

		<div class="mb-3">
			<label for="description" class="form-label">Description</label>
			<textarea class="form-control" id="description" name="description"
				required>
				<%=product.getDescription() != null ? product.getDescription() : ""%>
			</textarea>
		</div>

		<div class="mb-3">
			<label for="price" class="form-label">Price (Yen)</label> <input
				type="number" class="form-control" id="price" name="price"
				value="<%=product.getPrice()%>" required>
			<%
			if (errorMessage != null && errorMessage.contains("Price")) {
			%>
			<div class="text-danger mt-2">
				<%=errorMessage%>
			</div>
			<%
			}
			%>
		</div>

		<div class="mb-3">
			<label for="stock" class="form-label">Stock</label> <input
				type="number" class="form-control" id="stock" name="stock"
				value="<%=product.getStockQuantity()%>" required>
			<%
			if (errorMessage != null && errorMessage.contains("Stock")) {
			%>
			<div class="text-danger mt-2">
				<%=errorMessage%>
			</div>
			<%
			}
			%>
		</div>

		<div class="mb-3">
			<label for="category" class="form-label">Category</label> <select
				class="form-select" id="category" name="category" required>
				<%
				for (CategoryBean category : categoryList) {
					String selected = (product.getCategoryId() == category.getId()) ? "selected" : "";
				%>
				<option value="<%=category.getId()%>" <%=selected%>><%=category.getName()%></option>
				<%
				}
				%>
			</select>
		</div>

		<div class="row mt-4">
			<div class="col-6">
				<button type="submit" class="btn btn-outline-primary w-100">Save</button>
			</div>
			<div class="col-6">
				<a href="/ProductManage/product-list"
					class="btn btn-outline-secondary w-100">Back</a>
			</div>
		</div>
	</form>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
