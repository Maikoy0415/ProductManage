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
<title>Login</title>
</head>
<body>
	<div class="mx-auto mt-5" style="width: 400px;">
		<h1 class="text-center">Login</h1>

		<%-- エラーメッセージの表示 --%>
		<%
		String errorMessage = (String) request.getAttribute("errorMessage");
		if (errorMessage != null) {
		%>
		<div class="alert alert-danger" role="alert">
			<%=errorMessage%>
		</div>
		<%
		}
		%>

		<form action="/ProductManage/login" method="post">

			<div class="mb-3">
				<label for="email" class="form-label">Email</label> <input
					type="email" class="form-control" id="email" name="email" required>
			</div>
			<div class="mb-3">
				<label for="pass" class="form-label">Password</label> <input
					type="password" class="form-control" id="pass" name="password"
					required>
			</div>
			<button type="submit" class="btn btn-outline-dark w-100 mt-3">Login</button>
		</form>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-pzjw8f+ua7Kw1TIq0gP8QK6gOt7VhXk9r4p+5sV6p5jtB4l5C5qa5v4jsMfcRrRz"
		crossorigin="anonymous"></script>
</body>
</html>
