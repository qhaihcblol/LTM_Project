<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chuyển đổi PDF sang Word</title>
    <link rel="stylesheet" href="CSS_File/Upload.css">
</head>
<body>
<div class="container">
    <h1>Chuyển đổi PDF sang Word</h1>
    <form action="FileConverter_Servlet?action=pdfToWord" method="post" enctype="multipart/form-data">
        <div class="file-input">
            <label for="file">Chọn tệp PDF (nhiều tệp được phép):</label>
            <input type="file" id="file" name="file" accept="application/pdf" multiple required>
        </div>
        <div class="file-list" id="file-list">
            <p>Chưa có tệp nào được chọn.</p>
        </div>
        <button type="submit">Chuyển đổi</button>
    </form>
</div>

<script>
    document.getElementById("file").addEventListener("change", function(event) {
        var fileList = event.target.files;
        var fileListContainer = document.getElementById("file-list");

        // Nếu không có tệp nào được chọn
        if (fileList.length === 0) {
            fileListContainer.innerHTML = "<p>Chưa có tệp nào được chọn.</p>";
            return;
        }

        // Tạo danh sách các tệp đã chọn
        var ul = document.createElement("ul");
        for (var i = 0; i < fileList.length; i++) {
            var li = document.createElement("li");
            li.textContent = fileList[i].name;
            ul.appendChild(li);
        }

        // Thay thế nội dung của file-list
        fileListContainer.innerHTML = "";
        fileListContainer.appendChild(ul);
    });
</script>

</body>
</html>
