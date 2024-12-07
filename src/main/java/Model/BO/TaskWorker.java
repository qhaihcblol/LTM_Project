package Model.BO;

import Model.Bean.Task;
import Model.Bean.TaskQueue;
import Model.DAO.FileConverter_DAO;

public class TaskWorker implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Task task = TaskQueue.getNextTask();
                task.setStatus("PROCESSING");
                // Convert file based on type
                String outputFilePath;
                if (task.getInputFilePath().endsWith(".pdf")) {
//                    outputFilePath = FileConverter_DAO.pdfToWord(task.getInputFilePath());
                } else {
//                    outputFilePath = FileConverter_DAO.wordToPdf(task.getInputFilePath());
                }
//                task.setOutputFilePath(outputFilePath);
                task.setStatus("DONE");
//                System.out.println("Task completed for file: " + outputFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
