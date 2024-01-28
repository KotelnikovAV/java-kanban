import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Убрать посуду");
        Task task2 = new Task("Пропылесосить");
        Epic epic1 = new Epic("Переезд");
        Epic epic2 = new Epic("Поменять лампочку");
        Subtask subtask1 = new Subtask("Собрать вещи");
        Subtask subtask2 = new Subtask("Сказать пока");
        Subtask subtask3 = new Subtask("Принести стремянку");
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(Status.NEW, task1);
        taskManager.createTask(Status.NEW, task2);
        taskManager.createEpic(Status.NEW, epic1);
        taskManager.createEpic(Status.NEW, epic2);
        taskManager.createSubtask(epic1, Status.NEW, subtask1);
        taskManager.createSubtask(epic1, Status.NEW, subtask2);
        taskManager.createSubtask(epic2, Status.NEW, subtask3);

        printTasks(taskManager); // новые методы созданы для тестирования программы (в том объеме, что написано в ТЗ)
        printEpics(taskManager);
        printSubtask(epic1, taskManager);
        printSubtask(epic2, taskManager);

        taskManager.changeStatusEpic(Status.DONE, epic1);
        taskManager.changeStatusTask(Status.IN_PROGRES, task2);
        taskManager.changeStatusSubtask(Status.IN_PROGRES, subtask3);

        printTasks(taskManager);
        printEpics(taskManager);
        printSubtask(epic1, taskManager);
        printSubtask(epic2, taskManager);

        taskManager.removeTaskById(1); // нужно через дебагер выставить нужный id
        taskManager.removeEpicById(1); // нужно через дебагер выставить нужный id

        printTasks(taskManager);
        printEpics(taskManager);
        printSubtask(epic1, taskManager);
        printSubtask(epic2, taskManager);
    }

    public static void printTasks(TaskManager taskManager) {
        ArrayList<Task> tasks = taskManager.getListTasks();
        System.out.println("Список задач:");
        for (Task task : tasks) {
            System.out.println("Задача: " + task.getTask());
            System.out.println("Идентификационный номер задачи: " + task.getId());
            System.out.println("Статус задачи: " + task.getStatus());
            System.out.println();
        }
    }

    public static void printEpics(TaskManager taskManager) {
        ArrayList<Epic> epics = taskManager.getListEpics();
        System.out.println("Список эпиков:");
        for (Epic epic : epics) {
            System.out.println("Эпик: " + epic.getTask());
            System.out.println("Идентификационный номер эпика: " + epic.getId());
            System.out.println("Статус задачи: " + epic.getStatus());
            System.out.println();
        }
    }

    public static void printSubtask(Epic epic, TaskManager taskManager) {
        ArrayList<Subtask> subtasks = taskManager.getListSubtasks(epic);
        System.out.println("Список подзадач, относящихся к эпику " + epic.getTask() + ":");
        for (Subtask subtask : subtasks) {
            System.out.println("Подзадача: " + subtask.getTask());
            System.out.println("Идентификационный номер подзадачи: " + subtask.getId());
            System.out.println("Статус подзадачи: " + subtask.getStatus());
            System.out.println();
        }
    }
}
