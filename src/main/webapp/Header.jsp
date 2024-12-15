<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>PDF Converter</title>
    <link rel="stylesheet" href="CSS_File/Header.css">
</head>
<body>
<div class="header">
    <div class="logo">
        PDF Converter
    </div>
    <div class="navbar">
        <%
            String username = (String) session.getAttribute("username");
            if (username == null) {
        %>
        <a href="Authentication_Servlet?action=Login">Login</a>
        <a href="Authentication_Servlet?action=Register">Register</a>
        <%
        } else {
        %>
        <span>Welcome, <%= username %></span>
        <a href="Authentication_Servlet?action=Logout">Logout</a>
        <a href="Authentication_Servlet?action=Download">History</a>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
