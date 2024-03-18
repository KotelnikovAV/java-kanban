package service;

public class Managers {
    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(String file) {
        return new FileBackedTaskManager(file);
    }
}