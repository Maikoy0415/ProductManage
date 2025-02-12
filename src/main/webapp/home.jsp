<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
<title>Home</title>
</head>
<body>
<div class="mx-auto mt-5" style="width: 300px;">
	 <h1 class="text-center">Welcome!</h1>
	 
	 <form action="<%= request.getContextPath() %>/login" method="get">
    <button class="btn btn-dark w-100 mt-5">Logout</button>
</form>
</div>
</body>
</html>
