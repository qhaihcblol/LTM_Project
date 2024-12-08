<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session != null && session.getAttribute("LoggedIn") != null && (boolean) session.getAttribute("LoggedIn")) {
        response.sendRedirect("Upload.jsp");
        return;
    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="CSS_File/Login.css">
</head>
<body>
<div class="form-container">
    <form action="Authentication_Servlet?action=Login" method="post">
        <div class="form-title">Đăng nhập tài khoản</div>

        <div class="form-group">
            <label for="username">Tên đăng nhập</label>
            <input type="text" id="username" name="username" value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" required>
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="form-actions">
            <button type="submit">Đăng nhập</button>
            <button type="reset" >Reset</button>
        </div>

        <div class="form-footer">
            <p>Chưa có tài khoản? <a href="Register.jsp">Đăng ký ngay</a></p>
        </div>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error-message">
            <i class="error-icon">⚠️</i>
            <%= error %>
        </div>
        <%
            }
        %>
    </form>
</div>
</body>
</html>
