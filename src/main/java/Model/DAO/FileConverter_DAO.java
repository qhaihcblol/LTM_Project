package Model.DAO;

import Model.Bean.Task;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FileConverter_DAO {
    public static String pdfToWord(String inputFilePath) {
        return FileConversionUtils.pdfToWord(inputFilePath);
    }
    public static void addTask(Task task) {
        Database database = Database.getInstance();
        String query = "INSERT INTO tasks (user_id, input_file_path, output_file_path, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = database.prepareStatement(query, task.getUserId(), task.getInputFilePath(), task.getOutputFilePath(), task.getStatus());
            statement.executeUpdate();

            // Lấy ID vừa được insert
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1));
                    System.out.println("Inserted task with ID: " + task.getId());
                } else {
                    System.err.println("Failed to retrieve generated ID for task.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTask(Task task) {
        Database database = Database.getInstance();
        String query = "UPDATE tasks SET output_file_path = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement statement = database.prepareStatement(query, task.getOutputFilePath(), task.getStatus(), task.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("WARNING: No rows were updated for task ID: " + task.getId());
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update task with ID: " + task.getId());
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
    public static void addHistory(int userId, String fileUpload, String fileConverted, Timestamp date) {
        Database database = Database.getInstance();
        String query = "INSERT INTO history (user_id, file_upload, file_converted, date) VALUES (?, ?, ?, ?)";
        try {
            database.executeUpdate(query, userId, fileUpload, fileConverted, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        Database database = Database.getInstance();
        String query = "SELECT * FROM tasks WHERE user_id = ? ORDER BY id DESC";
        try (ResultSet rs = database.executeQuery(query, userId)) {
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setUserId(rs.getInt("user_id"));
                task.setInputFilePath(rs.getString("input_file_path"));
                task.setOutputFilePath(rs.getString("output_file_path"));
                task.setStatus(rs.getString("status"));
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }
    public static Task getNextPendingTask() {
        Database database = Database.getInstance();
        String query = "SELECT * FROM tasks WHERE status = 'PENDING' ORDER BY id ASC LIMIT 1";
        try (ResultSet rs = database.executeQuery(query)) {
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