package Model.BO;

import Model.Bean.Task;
import Model.DAO.FileConverter_DAO;


public class FileConverter_BO {

    public static String pdfToWord(String inputFilePath) {
        return FileConverter_DAO.pdfToWord(inputFilePath);
    }
    public static void addTask(Task task){
        FileConverter_DAO.addTask(task);
    }

    public static void updateTask(Task task){
        FileConverter_DAO.updateTask(task);
    }

}
