package Controller;

import Model.BO.FileConverter_BO;
import Model.BO.TaskWorker;
import Model.Bean.Task;
import Model.Bean.TaskQueue;
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
            for (File uploadedFile : uploadedFiles) {
                Task task = new Task();
                task.setUserId(userId);
                task.setInputFilePath(uploadedFile.getAbsolutePath());
                task.setOutputFilePath(null);
                task.setStatus("PENDING");
                TaskQueue.addTask(task);
                FileConverter_BO.addTask(task);
            }

            // Khởi chạy worker để xử lý tác vụ
            TaskWorker taskWorker = new TaskWorker();
            Thread thread = new Thread(taskWorker);
            thread.start();

            try {
                // Chờ thread kết thúc
                thread.join();

                // Lấy danh sách file đã chuyển đổi thành công
                List<String> successfulFiles = taskWorker.getSuccessfulFiles();
                req.setAttribute("successfulFiles", successfulFiles);
                req.getRequestDispatcher("Download.jsp").forward(req, resp);
            } catch (InterruptedException e) {
                throw new ServletException("Error while waiting for task completion.", e);
            }
        }
    }

    public List<File> getInputFile(HttpServletRequest req) throws IOException, ServletException {
        List<File> uploadedFiles = new ArrayList<>();
        String uploadDirPath = "D:/Nam 3 HK1/Cong nghe Web/Code/LTM_Project/src/main/webapp/uploads";
        File uploadDirFile = new File(uploadDirPath);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        Collection<Part> fileParts = req.getParts();
        for (Part part : fileParts) {
            if ("file".equals(part.getName()) && part.getSize() > 0) {
                String fileName = getFileName(part);
                File uploadFile = new File(uploadDirFile, fileName);
                part.write(uploadFile.getAbsolutePath());
                uploadedFiles.add(uploadFile);
                System.out.println("File uploaded: " + fileName);
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
