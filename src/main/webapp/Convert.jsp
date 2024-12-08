<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chuyển đổi PDF sang Word</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f5f5f5;
        }
        .container {
            text-align: center;
            background: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        .container h1 {
            margin-bottom: 20px;
        }
        .file-input {
            margin: 20px 0;
        }
        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Chuyển đổi PDF sang Word</h1>
    <form action="FileConverter_Servlet?action=pdfToWord" method="post" enctype="multipart/form-data">
        <div class="file-input">
            <label for="file">Chọn tệp PDF (nhiều tệp được phép):</label>
            <input type="file" id="file" name="file" accept="application/pdf" multiple required>
        </div>
        <button type="submit">Chuyển đổi</button>
    </form>
</div>
</body>
</html>
