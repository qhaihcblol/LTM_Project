<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Download Files</title>
    <style>
        .file-list {
            list-style-type: none;
            padding: 0;
        }
        .file-item {
            margin: 5px 0;
        }
        .file-link {
            text-decoration: none;
            color: blue;
        }
        .file-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h2>Danh sách các file đã tải lên</h2>
<%
    List<String> fileList = (List<String>) request.getAttribute("successfulFiles");
    if (fileList != null && !fileList.isEmpty()) {
%>
<ul class="file-list">
    <%
        for (String filePath : fileList) {
            File file = new File(filePath); // Tạo đối tượng File để lấy tên file
            String fileName = file.getName();
            String relativePath = "uploads/" + fileName; // Đường dẫn tương đối tới thư mục lưu trữ
    %>
    <li class="file-item">
        <a href="<%=relativePath%>" class="file-link" download="<%= fileName %>">
            <%= fileName %>
        </a>
    </li>
    <%
        }
    %>
</ul>
<%
} else {
%>
<p>Không có file nào để tải xuống.</p>
<%
    }
%>
</body>
</html>
