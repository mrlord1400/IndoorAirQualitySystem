<%-- 
    Document   : user
    Created on : 18/02/2026, 1:20:55 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Management - Lab Care</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
</head>
<body class="container mt-4">

    <%-- 
        // PHẦN TIÊU ĐỀ: 
        // Hiển thị tiêu đề chính của trang quản lý người dùng
    --%>
    <h2 class="text-center text-primary mb-4">USER MANAGEMENT LIST</h2>

    <%-- 
        // PHẦN THÔNG BÁO:
        // Lấy tham số 'msg' từ URL (nếu có) để hiển thị thông báo phản hồi từ Controller
        // Ví dụ: thông báo "Xóa người dùng thành công"
    --%>
    <c:if test="${not empty param.msg}">
        <div class="alert alert-info">${param.msg}</div>
    </c:if>

    <div class="mb-3 d-flex justify-content-between">
        <a href="addUser.jsp" class="btn btn-success"> + Add New User </a>
        
        <%-- 
            // FORM TÌM KIẾM:
            // Phương thức GET giúp giữ các từ khóa tìm kiếm trên thanh URL
            // Gửi dữ liệu về 'UserController' để xử lý logic tìm kiếm
        --%>
        <form action="UserController" method="GET" class="form-inline">
            <input type="hidden" name="action" value="search"> // Xác định hành động là tìm kiếm
            <input type="text" name="txtSearch" class="form-control mr-sm-2" placeholder="Search by name...">
            <button type="submit" class="btn btn-outline-primary">Search</button>
        </form>
    </div>

    <table class="table table-hover table-bordered">
        <thead class="thead-dark">
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Status</th>
                <th>Role</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%-- 
                // VÒNG LẶP HIỂN THỊ:
                // Lặp qua danh sách đối tượng 'LIST_USERS' nhận được từ Controller (Attribute)
            --%>
            <c:forEach var="user" items="${LIST_USERS}">
                <tr>
                    <td>${user.userID}</td>
                    <td>${user.username}</td>
                    <td>${user.fullname}</td>
                    <td>${user.email}</td>
                    <td>
                        <%-- // Chuyển đổi trạng thái (boolean) sang label hiển thị --%>
                        <span class="badge ${user.status ? 'badge-success' : 'badge-secondary'}">
                            ${user.status ? 'Active' : 'Locked'}
                        </span>
                    </td>
                    <td>
                        <%-- // Logic phân quyền: Gán tên vai trò dựa trên roleID (1: Admin, 2: Lab Manager...) --%>
                        <c:choose>
                            <c:when test="${user.roleID == 1}">Admin</c:when>
                            <c:when test="${user.roleID == 2}">Lab Manager</c:when>
                            <c:when test="${user.roleID == 3}">Technician</c:when>
                            <c:otherwise>Viewer</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${user.createdAt}</td>
                    <td>
                        <a href="UserController?action=edit&userId=${user.userID}" 
                           class="btn btn-sm btn-warning">Edit</a>
                        
                        <a href="UserController?action=delete&userId=${user.userID}" 
                           class="btn btn-sm btn-danger" 
                           onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
