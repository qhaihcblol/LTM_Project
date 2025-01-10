<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="Header.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="Model.Bean.Task"%>
<%@ page import="Model.BO.FileConverter_BO"%>
<%@ page import="java.io.File"%>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Chuyển đổi PDF sang Word</title>
<link rel="stylesheet" href="CSS_File/Upload.css">
<style type="text/css">
button:hover {
	background-color: #45a049;
}

p {
	margin: 0;
	color: #666;
}

.file-list-container ul li {
	display: flex; 
	align-items: center; 
	justify-content: space-between; 
	margin-bottom: 5px; 
	border: 1px solid #4CAF50; 
	padding: 10px; 
	border-radius: 5px; 
	background-color: white; 
}

.filename {
	flex-grow: 1; 
	margin-right: 10px;
	color: #28a745; 
	font-weight: bold;
}

.status-download-container {
	display: flex; 
	align-items: center; 
	justify-content: flex-end; 
	width: 160px;
}

.status {
	padding: 5px 10px; 
	border-radius: 20px; 
	font-weight: bold; 
	width: 110px; 
	text-align: center; 
	color: white;
}

.download-link {
	display: inline-block;
	margin-left: 10px; 
	visibility: hidden; 
}

.download-link.show {
	visibility: visible; 
}

.download-link img {
	width: 24px;
	height: 24px;
	vertical-align: middle;
}
/* Thêm các class cho trạng thái */
.status.PENDING {
	color: FFFFFF;
	background-color: #24D25E;
	border: 1px solid #24D25E;
}

.status.PROCESSING {
	color: 1ED760;
	background-color: #E8F5E9;
	border: 1px solid #24D25E;
}

.status.DONE {
	color: green;
	background-color: #e8f5e9;
	border: 1px solid #4CAF50;
}

.status.FAILED {
	color: red;
	background-color: #f8d7da;
	border: 1px solid #dc3545;
}
/* Ẩn các task cũ */
.file-list-container ul li.hidden {
	display: none;
}
</style>
</head>
<body>
	<div class="container">
		<form action="FileConverter_Servlet?action=pdfToWord" method="post"
			enctype="multipart/form-data">
			<h1>Chuyển đổi PDF sang Word</h1>
			<div class="file-input-container">
				<label for="file">Chọn tệp PDF</label> <input type="file" id="file"
					name="file" accept="application/pdf" multiple required>

				<div class="file-list-container" id="file-list">
					<p>Chưa có tệp nào được chọn.</p>
				</div>

				<button id="convert-btn" type="submit">Chuyển đổi</button>
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
            return;
        }
    });

    function updateTaskStatus(taskId, status, outputFilePath) {
        var taskElement = document.getElementById("file-" + taskId);
        if (taskElement) {
            var statusSpan = taskElement.querySelector(".status");
            statusSpan.textContent = status;

            statusSpan.classList.remove("PENDING", "PROCESSING", "DONE", "FAILED");

            statusSpan.classList.add(status);

            var downloadLink = taskElement.querySelector(".download-link");
            if (status === "DONE" && outputFilePath) {
                var fileName = outputFilePath.split(/[\\/]/).pop(); // Lấy tên file từ đường dẫn
                downloadLink.href = "uploads/" + fileName;
                downloadLink.download = fileName;
                downloadLink.classList.add("show");
            } else {
                downloadLink.classList.remove("show");
            }
        }
    }

    function fetchAndUpdateTasks() {
        fetch('TaskStatus_Servlet')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(tasks => {
                var fileListContainer = document.getElementById("file-list");
                var ul = fileListContainer.querySelector("ul");
                if (!ul) {
                    ul = document.createElement("ul");
                    fileListContainer.innerHTML = "";
                    fileListContainer.appendChild(ul);
                }

                var liElements = ul.querySelectorAll("li");
                for (let li of liElements) {
                    li.classList.add("hidden");
                }

                tasks.forEach(task => {
                    var taskElement = document.getElementById("file-" + task.id);
                    if (!taskElement) {
                        var li = document.createElement("li");
                        li.id = "file-" + task.id;
                        li.innerHTML = "<span class='filename'>" + task.inputFilePath.split(/[\\/]/).pop() + "</span>" +
                            "<div class='status-download-container'>" +
                            "<span class='status " + task.status + "'>" + task.status + "</span>" +
                            "<a class='download-link' href='#' download=''><img src='Image/download.png' alt='Download'></a>" +
                            "</div>";

                        var added = false;
                        for (let i = 0; i < ul.children.length; i++) {
                            var currentTaskId = parseInt(ul.children[i].id.split("-")[1]);
                            if (task.id > currentTaskId) {
                                ul.insertBefore(li, ul.children[i]);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            ul.appendChild(li);
                        }
                    } else {
                        taskElement.classList.remove("hidden");
                    }
                    updateTaskStatus(task.id, task.status, task.outputFilePath);
                });

                var hiddenLiElements = ul.querySelectorAll("li.hidden");
                for (let li of hiddenLiElements) {
                    ul.removeChild(li);
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
            });
    }

    fetchAndUpdateTasks();
    setInterval(fetchAndUpdateTasks, 2000);
</script>
</body>
</html>