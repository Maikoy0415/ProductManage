<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.List, model.entity.CategoryBean"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Category List</title>
<style>
table {
	width: 50%;
	border-collapse: collapse;
	margin: 20px auto;
}

table, th, td {
	border: 1px solid black;
}

th, td {
	padding: 10px;
	text-align: center;
}

th {
	background-color: #f2f2f2;
}

form {
	text-align: center;
}
</style>
</head>
<body>
	<h1 style="text-align: center;">Category List</h1>

	<form action="category-register" method="GET">
		<input type="submit" value="Register New Category"
			style="width: 200px; height: 40px; font-size: 16px;">
	</form>

	<%
	List<CategoryBean> categoryList = (List<CategoryBean>) request.getAttribute("categoryList");

	if (categoryList == null || categoryList.isEmpty()) {
		out.println("<p style='text-align: center;'>There are no categories.</p>");
	} else {
	%>
	<table>
		<thead>
			<tr>
				<th>Category ID</th>
				<th>Category Name</th>
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
	<%
	}
	%>


</body>
</html>
