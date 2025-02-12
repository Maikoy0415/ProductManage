<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1"
	crossorigin="anonymous">
<title>Register Account</title>
</head>
<body>
	<div class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-6">
				<h1 class="text-center">Register Account</h1>

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

				<!-- フォームのactionをUserRegisterServletに変更 -->
				<form action="/ProductManage/register" method="post">
					<div class="mb-3">
						<label for="firstName" class="form-label">First Name</label> <input
							type="text" class="form-control" id="firstName" name="firstName"
							required>
					</div>

					<div class="mb-3">
						<label for="lastName" class="form-label">Last Name</label> <input
							type="text" class="form-control" id="lastName" name="lastName"
							required>
					</div>

					<div class="mb-3">
						<label for="email" class="form-label">Email</label> <input
							type="email" class="form-control" id="email" name="email"
							required>
					</div>

					<div class="mb-3">
						<label for="phone_number" class="form-label">Phone Number</label>
						<input type="text" class="form-control" id="phone_number"
							name="phone_number">
					</div>

					<div class="mb-3">
						<label for="address" class="form-label">Address</label> <input
							type="text" class="form-control" id="address" name="address">
					</div>

					<div class="mb-3">
						<label for="password" class="form-label">Password</label> <input
							type="password" class="form-control" id="password"
							name="password" required>
					</div>

					<button type="submit" class="btn btn-outline-dark w-100 mt-3">Register</button>
				</form>

				<hr>
				<div class="text-center">
					<a href="/ProductManage/login.jsp" class="btn btn-link">Already have an account? Login</a>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-pzjw8f+ua7Kw1TIq0gP8QK6gOt7VhXk9r4p+5sV6p5jtB4l5C5qa5v4jsMfcRrRz"
		crossorigin="anonymous"></script>
</body>
</html>
