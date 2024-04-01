import service.FileBackedTaskManager;
import service.Managers;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager inMemoryTaskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager();
        inMemoryTaskManager.loadFromFile();
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getPrioritizedTasks());
    }
}