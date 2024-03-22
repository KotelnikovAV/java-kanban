package service;

public class Managers {

    private static final String NAME_FILE = "TaskManagerMemory.csv";

    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager(NAME_FILE);
    }
}