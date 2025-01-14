<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>Register Category</title>
</head>
<body>
	<h1 style="text-align: center;">Register Category</h1>

	<form action="category-register" method="POST"
		style="text-align: center;">

		<input type="text" name="categoryId" placeholder="Enter ID"
			style="width: 100px; height: 40px; font-size: 16px; padding: 3px;" />

		<input type="text" name="categoryName" placeholder="Enter Name"
			style="width: 200px; height: 40px; font-size: 16px; padding: 3px;" />

		<input type="submit" value="Register"
			style="width: 100px; height: 50px; font-size: 18px; padding: 10px; margin-top: 20px;" />
	</form>

	<%
	String errorMessage = (String) request.getAttribute("errorMessage");
	if (errorMessage != null) {
	%>
	<p style="color: red; text-align: center;"><%=errorMessage%></p>
	<%
	}
	%>
</body>
</html>
