package Controller;

import Model.BO.FileConverter_BO;
import Model.BO.TaskWorker;
import Model.Bean.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@MultipartConfig
@WebServlet(name = "FileConverter_Servlet", value = "/FileConverter_Servlet")
public class FileConverter_Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("pdfToWord".equals(action)) {
            List<File> uploadedFiles = getInputFile(req);
            if (uploadedFiles.isEmpty()) {
                resp.getWriter().write("Không có file nào được tải lên.");
                return;
            }

            int userId = getUserId(req);
            List<Task> tasks = new ArrayList<>();
            for (File uploadedFile : uploadedFiles) {
                Task task = new Task();
                task.setUserId(userId);
                task.setInputFilePath(uploadedFile.getAbsolutePath());
                task.setOutputFilePath(null);
                task.setStatus("PENDING");
                FileConverter_BO.addTask(task);
                tasks.add(task);
            }

            // Khởi chạy worker (nếu chưa chạy)
            TaskWorker.startWorker();

            // Chuyển hướng đến trang Upload.jsp và truyền danh sách task
            req.setAttribute("tasks", tasks);
            req.getRequestDispatcher("Upload.jsp").forward(req, resp);
        }
    }

    public List<File> getInputFile(HttpServletRequest req) throws IOException, ServletException {
        List<File> uploadedFiles = new ArrayList<>();
        String uploadDirPath = req.getServletContext().getRealPath("/uploads");
        File uploadDirFile = new File(uploadDirPath);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        Collection<Part> fileParts = req.getParts();
        for (Part part : fileParts) {
            if ("file".equals(part.getName()) && part.getSize() > 0) {
                String fileName = getFileName(part);
                String baseName = fileName.replaceFirst("[.][^.]+$", ""); // Tên file không có đuôi
                String extension = fileName.substring(fileName.lastIndexOf(".")); // Đuôi file

                int fileIndex = 0;
                String uniqueFileName = fileName;
                File uploadFile = new File(uploadDirFile, uniqueFileName);

                // Kiểm tra trùng lặp và thêm số thứ tự vào tên file
                while (uploadFile.exists()) {
                    fileIndex++;
                    uniqueFileName = baseName + "(" + fileIndex + ")" + extension;
                    uploadFile = new File(uploadDirFile, uniqueFileName);
                }

                part.write(uploadFile.getAbsolutePath());
                uploadedFiles.add(uploadFile);
                System.out.println("File uploaded: " + uniqueFileName);
            }
        }
        return uploadedFiles;
    }

    public String getFileName(Part part) {
        String contentDisposition = part.getHeader("Content-Disposition");
        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public int getUserId(HttpServletRequest req) {
        return (int) req.getSession().getAttribute("userId");
    }
}