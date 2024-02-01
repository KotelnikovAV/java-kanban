import model.Status;
import service.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int id;
        String task;
        Task task1 = new Task("Убрать посуду");
        Task task2 = new Task("Пропылесосить");
        Epic epic1 = new Epic("Переезд");
        Epic epic2 = new Epic("Поменять лампочку");
        Subtask subtask1 = new Subtask(epic1, "Собрать вещи");
        Subtask subtask2 = new Subtask(epic1, "Сказать пока");
        Subtask subtask3 = new Subtask(epic2, "Принести стремянку");
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        taskManager.createTask(Status.NEW, task1);
        taskManager.createTask(Status.NEW, task2);
        taskManager.createEpic(Status.NEW, epic1);
        taskManager.createEpic(Status.NEW, epic2);
        taskManager.createSubtask(epic1, Status.NEW, subtask1);
        taskManager.createSubtask(epic1, Status.NEW, subtask2);
        taskManager.createSubtask(epic2, Status.NEW, subtask3);
        print(taskManager.getListEpics(), taskManager.getListSubtasks(epic1), taskManager.getListSubtasks(epic2),
                taskManager.getListTasks(), taskManager, epic1, epic2);
        taskManager.changeStatusEpic(Status.DONE, epic1);
        taskManager.changeStatusTask(Status.IN_PROGRESS, task2);
        taskManager.changeStatusSubtask(Status.IN_PROGRESS, subtask3);
        print(taskManager.getListEpics(), taskManager.getListSubtasks(epic1), taskManager.getListSubtasks(epic2),
                taskManager.getListTasks(), taskManager, epic1, epic2);
        System.out.println("Какой эпик получить (введите id)?");
        id = scanner.nextInt();
        if (taskManager.getEpicById(id) != null) {
            System.out.println(taskManager.getEpicById(id));
        }
        System.out.println("Какую задачу получить (введите id)?");
        id = scanner.nextInt();
        if (taskManager.getTaskById(id) != null) {
            System.out.println(taskManager.getTaskById(id));
        }
        System.out.println("Какую подзадачу получить (введите id)?");
        id = scanner.nextInt();
        if (taskManager.getSubtaskById(id) != null) {
        System.out.println(taskManager.getSubtaskById(id));
        }
        System.out.println("Какую задачу обновить (введите id)?");
        id = scanner.nextInt();
        System.out.println("Введите задачу:");
        scanner.nextLine();
        task = scanner.nextLine();
        taskManager.updateTask(id, new Task(task));
        System.out.println("Какой эпик обновить (введите id)?");
        id = scanner.nextInt();
        System.out.println("Введите эпик:");
        scanner.nextLine();
        task = scanner.nextLine();
        taskManager.updateEpic(id, new Epic(task));
        System.out.println("Какую подзадачу обновить (введите id)?");
        id = scanner.nextInt();
        System.out.println("Введите подзадачу:");
        scanner.nextLine();
        task = scanner.nextLine();
        taskManager.updateSubtask(id, new Subtask(null, task));
        print(taskManager.getListEpics(), taskManager.getListSubtasks(epic1), taskManager.getListSubtasks(epic2),
                taskManager.getListTasks(), taskManager, epic1, epic2);
        System.out.println("Какую задачу удалить (введите id)?");
        id = scanner.nextInt();
        taskManager.removeTaskById(id);
        System.out.println("Какой эпик удалить (введите id)?");
        id = scanner.nextInt();
        taskManager.removeEpicById(id);
        System.out.println("Какую подзадачу удалить (введите id)?");
        id = scanner.nextInt();
        taskManager.removeSubtaskById(id);
        print(taskManager.getListEpics(), taskManager.getListSubtasks(epic1), taskManager.getListSubtasks(epic2),
                taskManager.getListTasks(), taskManager, epic1, epic2);
        taskManager.removeAllTasks();
        taskManager.removeAllSubtasks(epic1);
        taskManager.removeAllEpics();
        print(taskManager.getListEpics(), taskManager.getListSubtasks(epic1), taskManager.getListSubtasks(epic2),
                taskManager.getListTasks(), taskManager, epic1, epic2);
    }

    public static void print(ArrayList<Epic> epics, ArrayList<Subtask> subtasks1, ArrayList<Subtask> subtasks2,
                             ArrayList<Task> tasks, TaskManager taskManager, Epic epic1, Epic epic2) {
        tasks = taskManager.getListTasks();
        for (Task task : tasks) {
            System.out.println(task.toString());
        }
        System.out.println();
        epics = taskManager.getListEpics();
        for (Epic epic : epics) {
            System.out.println(epic.toString());
        }
        subtasks1 = taskManager.getListSubtasks(epic1);
        if (subtasks1 != null) {
            for (Subtask subtask : subtasks1) {
                System.out.println(subtask.toString());
            }
        }
        System.out.println();
        subtasks2 = taskManager.getListSubtasks(epic2);
        if (subtasks2 != null) {
            for (Subtask subtask : subtasks2) {
                System.out.println(subtask.toString());
            }
        }
        System.out.println("______________________________________________________________________________________");
        System.out.println();

    }
}
