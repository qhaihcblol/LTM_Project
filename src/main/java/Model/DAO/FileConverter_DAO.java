package Model.DAO;

import Model.Bean.Task;
import java.io.*;
import java.sql.ResultSet;

public class FileConverter_DAO {
    public static String pdfToWord(String inputFilePath) {

        return "output.docx";
    }

    public static String wordToPdf(String inputFilePath) {
        return "output.pdf";
    }

    public static void addTask(Task task) {
        Database database = Database.getInstance();
        String query = "INSERT INTO tasks (user_id, input_file_path, output_file_path, status) VALUES (?, ?, ?, ?)";
        try {
            database.executeUpdate(query, task.getUserId(), task.getInputFilePath(), task.getOutputFilePath(), task.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTask(Task task) {
        Database database = Database.getInstance();
        String query = "UPDATE tasks SET user_id = ?, input_file_path = ?, output_file_path = ?, status = ? WHERE id = ?";
        try {
            database.executeUpdate(query, task.getUserId(), task.getInputFilePath(), task.getOutputFilePath(), task.getStatus(), task.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Task getTask(int id) {
        Database database = Database.getInstance();
        String query = "SELECT * FROM tasks WHERE id = ?";
        try (ResultSet rs = database.executeQuery(query, id)) {
            if (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setUserId(rs.getInt("user_id"));
                task.setInputFilePath(rs.getString("input_file_path"));
                task.setOutputFilePath(rs.getString("output_file_path"));
                task.setStatus(rs.getString("status"));
                return task;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}