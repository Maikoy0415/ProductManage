<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.List, java.util.Map"%>
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
</style>
</head>
<body>
	<h1 style="text-align: center;">カテゴリ一覧</h1>

	<%
	List<Map<String, Object>> categoryList = (List<Map<String, Object>>) request.getAttribute("categoryList");

	if (categoryList == null || categoryList.isEmpty()) {
		out.println("<p style='text-align: center;'>カテゴリがありません。</p>");
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
			for (Map<String, Object> category : categoryList) {
				Integer id = (Integer) category.get("id");
				String name = (String) category.get("name");
			%>
			<tr>
				<td><%=id%></td>
				<td><%=name%></td>
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
