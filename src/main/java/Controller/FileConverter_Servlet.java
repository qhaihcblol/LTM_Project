package Controller;

import Model.BO.FileConverter_BO;
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
        FileConverter_BO fileConverter_bo = new FileConverter_BO();
        if (action.equals("pdfToWord")) {
            List<File> uploadedFiles = getInputFile(req);
            if (uploadedFiles.isEmpty()) {
                resp.getWriter().write("Không có file nào được tải lên.");
            }
            int userId = getUserId(req);
            for (File uploadedFile : uploadedFiles) {
                Task task = new Task();
                task.setUserId(userId);
                task.setInputFilePath(uploadedFile.getAbsolutePath());
                task.setOutputFilePath(null);
                task.setStatus("PENDING");
                TaskQueue.addTask(task);
                fileConverter_bo.addTask(task);
            }

        }
    }
    public List<File> getInputFile(HttpServletRequest req) throws IOException, ServletException {
        List<File> uploadedFiles = new ArrayList<>();
        Collection<Part> fileParts = req.getParts();
        for (Part part : fileParts) {
            if ("file".equals(part.getName()) && part.getSize() > 0) {
                String fileName = getFileName(part);
                String uploadDir = "uploads";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                File uploadFile = new File(uploadDir, fileName);
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
