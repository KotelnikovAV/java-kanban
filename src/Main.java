import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {
    public static Epic epic;

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getTaskManager();
        epic = new Epic("Приготовить еду");
        inMemoryTaskManager.createTask(Status.NEW, new Task("Пропылесосить"));
        inMemoryTaskManager.createTask(Status.NEW, new Task("Погулять"));
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, new Subtask(epic,"Купить продукты"));
        inMemoryTaskManager.createSubtask(epic, Status.NEW, new Subtask(epic,"Помыть продукты"));
        inMemoryTaskManager.createSubtask(epic, Status.NEW, new Subtask(epic,"Поставить все на огонь"));
        inMemoryTaskManager.createEpic(Status.NEW, new Epic("Переехать"));
        System.out.println(inMemoryTaskManager.getTaskById(1));
        System.out.println(inMemoryTaskManager.getTaskById(2));
        System.out.println(inMemoryTaskManager.getTaskById(1));
        System.out.println(inMemoryTaskManager.getEpicById(3));
        System.out.println(inMemoryTaskManager.getSubtaskById(6));
        System.out.println(inMemoryTaskManager.getSubtaskById(4));
        System.out.println(inMemoryTaskManager.getEpicById(3));
        System.out.println(inMemoryTaskManager.getEpicById(7));
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeTaskById(1);
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeEpicById(3);
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
