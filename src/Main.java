import model.Epic;
import model.Status;
import model.Task;
import service.FileBackedTaskManager;
import service.Managers;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager inMemoryTaskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager();
        inMemoryTaskManager.loadFromFile(); /* эту строчку я оставлю, здесь все равно задействуется те же методы,
        что и при создании такси вручную */
        System.out.println(inMemoryTaskManager.getHistory() + "\n");
        System.out.println(inMemoryTaskManager.getPrioritizedTasks() + "\n");
        inMemoryTaskManager.removeAllTasks();
        System.out.println(inMemoryTaskManager.getPrioritizedTasks() + "\n");
        inMemoryTaskManager.createTask(Status.NEW, new Task("Что угодно1", "09:20 20.10.24",
                "20")); // пересекается
        inMemoryTaskManager.createTask(Status.NEW, new Task("Что угодно2", "10:00 20.10.24",
                "40")); // не пересекается
        inMemoryTaskManager.createEpic(Status.NEW, new Epic("Что угодно3", "09:00 20.10.24",
                "30")); // не пересекается
        System.out.println(inMemoryTaskManager.getPrioritizedTasks() + "\n");
        inMemoryTaskManager.getTaskById(9);
        inMemoryTaskManager.removeEpicById(8);
        inMemoryTaskManager.removeEpicById(10);
        inMemoryTaskManager.createTask(Status.NEW, new Task("Что угодно1", "09:40 20.10.24",
                "20")); // после удалений уже не пересекается и мы можем добавить эту задачу
        System.out.println(inMemoryTaskManager.getPrioritizedTasks() + "\n");
        System.out.println(inMemoryTaskManager.getHistory() + "\n");
    }
}