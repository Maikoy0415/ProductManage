<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>Register Category</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="container mt-5">

	<!-- Logout Button -->
	<div class="d-flex justify-content-end mb-3">
		<form action="/ProductManage/logout" method="get">
			<button class="btn btn-dark">Logout</button>
		</form>
	</div>

	<div class="container w-50">
		<h1 class="text-center mt-4">Register Category</h1>

		<form action="category-register" method="POST" class="mt-4">
			<div class="form-group row">
				<div class="col-12 col-md-8">
					<input type="text" id="categoryName" name="categoryName"
						placeholder="Category Name" class="form-control" required>
				</div>
				<div class="col-12 col-md-4">
					<button type="submit" class="btn btn-dark btn-block">Register</button>
				</div>
			</div>
		</form>

		<%
		String errorMessage = (String) request.getAttribute("errorMessage");
		if (errorMessage != null) {
		%>
		<div class="alert alert-danger mt-4">
			<%=errorMessage%>
		</div>
		<%
		}
		%>
	</div>

</body>

</html>
