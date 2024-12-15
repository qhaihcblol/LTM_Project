<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Header.jsp" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chuyển đổi PDF sang Word</title>
    <link rel="stylesheet" href="CSS_File/Upload.css">
</head>
<body>
<div class="container">
    <form action="FileConverter_Servlet?action=pdfToWord" method="post" enctype="multipart/form-data">
        <h1>Chuyển đổi PDF sang Word</h1>
        <div class="file-input-container">
            <label for="file">Chọn tệp PDF</label>
            <input type="file" id="file" name="file" accept="application/pdf" multiple required>

            <div class="file-list-container" id="file-list">
                <p>Chưa có tệp nào được chọn.</p>
            </div>

            <button id="convert-btn" type="submit" style="display: none;">Chuyển đổi</button>
        </div>
    </form>

</div>

<script>
    document.getElementById("file").addEventListener("change", function (event) {
        var fileList = event.target.files;
        var fileListContainer = document.getElementById("file-list");
        var convertButton = document.getElementById("convert-btn");

        if (fileList.length === 0) {
            fileListContainer.innerHTML = "<p>Chưa có tệp nào được chọn.</p>";
            convertButton.style.display = "none";
            return;
        }

        var ul = document.createElement("ul");
        for (var i = 0; i < fileList.length; i++) {
            var li = document.createElement("li");
            li.textContent = fileList[i].name;
            ul.appendChild(li);
        }

        fileListContainer.innerHTML = "";
        fileListContainer.appendChild(ul);
        convertButton.style.display = "block";
    });
</script>

</body>
</html>
