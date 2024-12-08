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
        File outputFile = new File(downloadDir, "output.docx");
        if (outputFile.exists()) {
            System.out.println("Returning the file: example.docx");
        } else {
            System.out.println("File example.docx not found in Downloads.");
        }
        return outputFile;
    }

}
