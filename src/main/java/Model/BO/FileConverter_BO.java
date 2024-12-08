package Model.BO;

import Model.Bean.Task;
import Model.DAO.FileConverter_DAO;

import java.io.File;


public class FileConverter_BO {

    public File pdfToWord(File inputFile) {
        FileConverter_DAO fileConverter_dao = new FileConverter_DAO();
        return fileConverter_dao.pdfToWord(inputFile);
    }
    public File wordToPdf(File inputFile) {
        FileConverter_DAO fileConverter_dao = new FileConverter_DAO();
        return fileConverter_dao.wordToPdf(inputFile);
    }

    public void addTask(Task task){
        FileConverter_DAO fileConverter_dao = new FileConverter_DAO();
        fileConverter_dao.addTask(task);
    }

    public void updateTask(Task task){
        FileConverter_DAO fileConverter_dao = new FileConverter_DAO();
        fileConverter_dao.updateTask(task);
    }

}
