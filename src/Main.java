import model.Epic;
import model.Status;
import model.Task;
import service.FileBackedTaskManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getFileBackedTaskManager();
        ((FileBackedTaskManager) inMemoryTaskManager).loadFromFile();
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.createTask(Status.NEW, new Task("Пропылесосить"));
        inMemoryTaskManager.createTask(Status.NEW, new Task("Поспать"));
        inMemoryTaskManager.createEpic(Status.NEW, new Epic("Переехать"));
        System.out.println(inMemoryTaskManager.getTaskById(7));
        System.out.println(inMemoryTaskManager.getSubtaskById(2));
        System.out.println(inMemoryTaskManager.getHistory());
    }
}