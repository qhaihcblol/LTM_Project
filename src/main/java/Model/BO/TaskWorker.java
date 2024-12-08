package Model.BO;

import Model.Bean.Task;
import Model.Bean.TaskQueue;
import Model.DAO.FileConverter_DAO;

import java.io.File;

public class TaskWorker implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                // Processing
                Task task = TaskQueue.getNextTask();
                task.setStatus("PROCESSING");
                FileConverter_BO.updateTask(task);

                String outputFilePath = "";
                if (task.getInputFilePath().endsWith(".pdf")) {
                    outputFilePath = FileConverter_BO.pdfToWord(task.getInputFilePath());
                } else {
                    outputFilePath = FileConverter_BO.wordToPdf(task.getInputFilePath());
                }
                task.setOutputFilePath(outputFilePath);
                // Done
                task.setStatus("DONE");
                System.out.println("Task completed for file: " + outputFilePath);
                FileConverter_BO.updateTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
