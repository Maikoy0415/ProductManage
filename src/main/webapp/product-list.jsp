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
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body class="container mt-5">

	<!-- Back Button -->
		<button type="button" class="btn btn-small btn-outline-secondary float-start" onclick="window.location.href='home.jsp';">Back</button>

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

	<!-- Delete Confirmation Modal -->
	<div class="modal fade" id="deleteConfirmModal" tabindex="-1"
		aria-labelledby="deleteConfirmModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header d-flex justify-content-center">
					<h5 class="modal-title text-danger" id="deleteConfirmModalLabel">
						<i class="fas fa-trash"></i> Confirm Deletion
					</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					Are you sure you want to delete the product "<span
						id="productNameToDelete"></span>"?
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-outline-secondary"
						data-bs-dismiss="modal">Back</button>

					<!-- Form for delete confirmation -->
					<form action="/ProductManage/product-delete" method="POST"
						id="deleteForm">
						<input type="hidden" name="productId" id="productIdToDelete">
						<button type="submit" class="btn btn-outline-danger">Yes</button>
					</form>
				</div>
			</div>
		</div>
	</div>


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
				<td class="border"><%=(product.getDescription() != null && !product.getDescription().isEmpty())
		? product.getDescription()
		: "No description"%></td>
				<td class="border"><a
					href="/ProductManage/product-edit?id=<%=product.getId()%>"
					class="btn btn-primary btn-sm">Edit</a>
					<button class="btn btn-danger btn-sm delete-btn"
						data-bs-toggle="modal" data-bs-target="#deleteConfirmModal"
						data-id="<%=product.getId()%>" data-name="<%=product.getName()%>">Delete</button>

				</td>
			</tr>
			<%
			}
			} else {
			%>
			<tr>
				<td colspan="6" class="text-center">No products found</td>
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
	<script>
    document.addEventListener("DOMContentLoaded", function() {
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', function() {
                const productId = this.getAttribute('data-id');
                const productName = this.getAttribute('data-name');

                // 商品IDをフォームに設定
                document.getElementById('productIdToDelete').value = productId;

                // 商品名をモーダルに設定
                const productNameElement = document.getElementById('productNameToDelete');
                if (productNameElement) {
                    productNameElement.innerText = productName;
                }
            });
        });
    });
</script>



</body>
</html>
