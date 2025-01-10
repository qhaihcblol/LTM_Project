package Model.BO;

import Model.Bean.Task;
import Model.DAO.FileConverter_DAO;

import java.util.List;

public class FileConverter_BO {

    public static String pdfToWord(String inputFilePath) {
        return FileConverter_DAO.pdfToWord(inputFilePath);
    }

    public static void addTask(Task task) {
        FileConverter_DAO.addTask(task);
    }

    public static void updateTask(Task task) {
        FileConverter_DAO.updateTask(task);
    }

    public static Task getTask(int id) {
        return FileConverter_DAO.getTask(id);
    }

    public static List<Task> getTasksByUserId(int userId) {
        return FileConverter_DAO.getTasksByUserId(userId);
    }

}