<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Download Files</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .file-list {
            list-style-type: none;
            padding: 0;
        }
        .file-item {
            margin: 10px 0;
        }
        .file-link {
            text-decoration: none;
            color: #4CAF50;
            font-size: 18px;
        }
        .file-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h2>Danh sách các file đã tải lên</h2>
<%
    List<File> fileList = (List<File>) request.getAttribute("ListFile");
    if (fileList != null && !fileList.isEmpty()) {
%>
<ul class="file-list">
    <%
        for (File file : fileList) {
            String fileName = file.getName();
            String filePath = file.getAbsolutePath();
            System.out.println("File path: " + filePath);
    %>
    <li class="file-item">
        <a href="<%= filePath %>" class="file-link" download="<%= fileName %>">
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
