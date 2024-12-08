package Model.BO;

import Model.Bean.Task;
import Model.DAO.FileConverter_DAO;


public class FileConverter_BO {

    public static String pdfToWord(String inputFilePath) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return FileConverter_DAO.pdfToWord(inputFilePath);
    }
    public static String wordToPdf(String inputFilePath){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return FileConverter_DAO.wordToPdf(inputFilePath);
    }

    public static void addTask(Task task){
        FileConverter_DAO.addTask(task);
    }

    public static void updateTask(Task task){
        FileConverter_DAO.updateTask(task);
    }

}
