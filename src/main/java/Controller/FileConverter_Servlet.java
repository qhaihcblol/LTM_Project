package Controller;

import Model.BO.FileConverter_BO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@MultipartConfig
@WebServlet(name = "FileConverter_Servlet", value = "/FileConverter_Servlet")
public class FileConverter_Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        FileConverter_BO fileConverter_bo = new FileConverter_BO();
        if (action.equals("pdfToWord")) {
            File uploadedFile = getInputFile(req);
            if (uploadedFile != null) {
                String fileName = uploadedFile.getName();
                String filePath = uploadedFile.getAbsolutePath();
                resp.getWriter().println("File uploaded successfully: " + fileName);
                resp.getWriter().println("File is stored at: " + filePath);
                File outputFile = fileConverter_bo.pdfToWord(uploadedFile);
                if (outputFile != null) {
                    resp.getWriter().println("File converted successfully: " + outputFile.getName());
                    resp.getWriter().println("File is stored at: " + outputFile.getAbsolutePath());
                } else {
                    resp.getWriter().println("File conversion failed.");
                }
            } else {
                resp.getWriter().println("No file uploaded.");
            }
        }
    }
    public File getInputFile(HttpServletRequest req) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart != null) {
            String fileName = getFileName(filePart);
            String uploadDir = "uploads";
            File uploadFile = new File(uploadDir, fileName);
            filePart.write(uploadFile.getAbsolutePath());
            return uploadFile;
        }
        return null;
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
}
