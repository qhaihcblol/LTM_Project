package Model.BO;

import Model.Bean.Task;
import Model.DAO.FileConverter_DAO;

import java.io.File;

public class TaskWorker implements Runnable {
    private static volatile boolean isRunning = false;
    private static Thread workerThread = null;

    public TaskWorker() {} // Constructor mặc định

    public static synchronized void startWorker() {
        if (workerThread == null || !workerThread.isAlive()) {
            isRunning = true;
            workerThread = new Thread(new TaskWorker());
            workerThread.start();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                // Lấy task mới nhất từ database có status là PENDING
                Task task = FileConverter_DAO.getNextPendingTask(); 

                if (task == null) {
                    // Nếu không có task nào, chờ 1 giây rồi thử lại
                    Thread.sleep(1000);
                    continue;
                }

                System.out.println("Processing task with ID: " + task.getId());

                task.setStatus("PROCESSING");
                FileConverter_BO.updateTask(task);

                String outputFilePath = "";
                try {
                    if (task.getInputFilePath().endsWith(".pdf")) {
                        outputFilePath = FileConverter_BO.pdfToWord(task.getInputFilePath());
                    }

                    if (outputFilePath != null && !outputFilePath.isEmpty()) {
                        task.setOutputFilePath(outputFilePath);
                        task.setStatus("DONE");
                        System.out.println("Task completed for file: " + outputFilePath);

                        // Lưu lịch sử chuyển đổi
                        File inputFile = new File(task.getInputFilePath());
                        File outputFile = new File(outputFilePath);
                        FileConverter_DAO.addHistory(task.getUserId(), inputFile.getName(), outputFile.getName(), new java.sql.Timestamp(System.currentTimeMillis()));
                    } else {
                        task.setStatus("FAILED");
                        System.err.println("Task failed for file: " + task.getInputFilePath() + " (outputFilePath is null or empty)");
                    }

                } catch (Exception e) {
                    task.setStatus("FAILED");
                    System.err.println("Task failed for file: " + task.getInputFilePath());
                    e.printStackTrace();
                }

                FileConverter_BO.updateTask(task);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                System.err.println("TaskWorker interrupted: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("TaskWorker stopped.");
    }

    public static synchronized void stopWorker() {
        isRunning = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }
}