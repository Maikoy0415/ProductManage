<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1"
	crossorigin="anonymous">
<title>Home</title>
</head>
<body>

	<div class="mx-auto mt-5" style="width: 300px;">
		<h2 class="text-center">Welcome, ${user.firstName}!</h2>

		<a href="product-list"
			class="btn btn-outline-dark w-100 mt-5">Product List</a>

		<a href="product-register"
			class="btn btn-outline-dark w-100 mt-3">Register Product</a>

		<a href="category-list"
			class="btn btn-outline-dark w-100 mt-3">Category List</a>

		<a href="category-register"
			class="btn btn-outline-dark w-100 mt-3">Register Category</a>

		<form action="/ProductManage/logout" method="get">
			<button class="btn btn-dark w-100 mt-5">Logout</button>
		</form>
	</div>
</body>
</html>
