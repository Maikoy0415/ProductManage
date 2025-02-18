<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.entity.ProductBean"%>
<%
List<ProductBean> productList = (List<ProductBean>) request.getAttribute("productList");
String errorMessage = (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product List</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="container mt-5">

	<!-- Logout Button -->
	<form action="/ProductManage/logout" method="get">
		<button class="btn btn-dark btn-small float-end">Logout</button>
	</form>

	<h1 class="mb-4 text-center">Product List</h1>

	<%
	if (errorMessage != null) {
	%>
	<div class="alert alert-danger" role="alert">
		<%=errorMessage%>
	</div>
	<%
	}
	%>

	<table class="table table-striped w-75 mx-auto text-center border">
		<thead>
			<tr>
				<th class="border">Product Name</th>
				<th class="border">Price (Yen)</th>
				<th class="border">Stock</th>
				<th class="border">Category</th>
				<th class="border">Description</th>
				<th class="border">Actions</th>
			</tr>
		</thead>
		<tbody>
			<%
			if (productList != null && !productList.isEmpty()) {
				for (ProductBean product : productList) {
			%>
			<tr>
				<td class="border"><%=product.getName()%></td>
				<td class="border"><%=product.getPrice()%></td>
				<td class="border"><%=product.getStockQuantity()%></td>
				<td class="border"><%=product.getCategoryName() != null ? product.getCategoryName() : "N/A"%></td>
				<td class="border"><%=(product.getDescription() != null && !product.getDescription().isEmpty()) ? product.getDescription()
		: "No description"%></td>
				<td class="border"><a
					href="/ProductManage/product-edit?id=<%=product.getId()%>"
					class="btn btn-primary btn-sm">Edit</a> <a
					href="/ProductManage/product-delete?id=<%=product.getId()%>"
					class="btn btn-danger btn-sm">Delete</a></td>
			</tr>
			<%
			}
			} else {
			%>
			<tr>
				<td colspan="5" class="text-center">No products found</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>

	<a href="/ProductManage/product-register"
		class="btn btn-success d-block mx-auto w-75">Register New Product</a>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
