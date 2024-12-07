package Controller;

import Model.BO.FileConverter_BO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "FileConverter_Servlet", value = "/FileConverter_Servlet")
public class FileConverter_Servlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        FileConverter_BO fileConverter_bo = new FileConverter_BO();
        if (action.equals("pdfToWord")) {
            //Thực hiện chuyển file pdf sang word

        }
    }
}
