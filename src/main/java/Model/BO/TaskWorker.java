package Model.BO;

import Model.Bean.Task;
import Model.Bean.TaskQueue;
import Model.DAO.FileConverter_DAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskWorker implements Runnable {
    private final List<String> successfulFiles = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            try {
                // Lấy tác vụ tiếp theo từ hàng đợi
                Task task = TaskQueue.getNextTask();
                if (task == null) {
                    // Nếu không có tác vụ nào sau thời gian chờ, thoát khỏi vòng lặp
                    System.out.println("No tasks available. Exiting worker.");
                    break;
                }

                task.setStatus("PROCESSING");
                FileConverter_BO.updateTask(task);

                String outputFilePath = "";
                try {
                    if (task.getInputFilePath().endsWith(".pdf")) {
                        outputFilePath = FileConverter_BO.pdfToWord(task.getInputFilePath());
                    }
                    // Đánh dấu hoàn thành nếu chuyển đổi thành công
                    task.setOutputFilePath(outputFilePath);
                    task.setStatus("DONE");
                    successfulFiles.add(outputFilePath);
                    System.out.println("Task completed for file: " + outputFilePath);
                } catch (Exception e) {
                    // Đánh dấu thất bại nếu xảy ra lỗi
                    task.setStatus("FAILED");
                    System.err.println("Task failed for file: " + task.getInputFilePath());
                    e.printStackTrace();
                }

                FileConverter_BO.updateTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // In danh sách các tệp đã chuyển đổi thành công
        System.out.println("Successfully converted files:");
        for (String filePath : successfulFiles) {
            System.out.println(filePath);
        }
    }
    public List<String> getSuccessfulFiles() {
        return successfulFiles;
    }
}
