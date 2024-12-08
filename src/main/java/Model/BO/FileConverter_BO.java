package Model.BO;

import Model.DAO.FileConverter_DAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

public class FileConverter_BO {

    public File pdfToWord(File inputFile) {
        String downloadDir = "downloads";
        return new File(downloadDir, "output.docx");
    }

}
