package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class InMemoryTaskManagerTest {
    public static TaskManager inMemoryTaskManager;
    public static Task task1;
    public static Task task2;
    public static Epic epic;
    public static Subtask subtask1;
    public static Subtask subtask2;

    @BeforeAll
    public static void beforeAll() {
        task1 = new Task("123");
        task2 = new Task("125");
        epic = new Epic("234");
        subtask1 = new Subtask(epic, "345");
        subtask2 = new Subtask(epic, "345");
    }

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldEqualIfIdTaskAndIdEpicEqual() {
        task1 = new Task("123");
        epic = new Epic("234");
        task1.setId(1);
        epic.setId(1);
        assertEquals(epic, task1, "Задачи с одинаковыми id не равны");
    }

    @Test
    void checkAddNewTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        assertEquals(inMemoryTaskManager.getTaskById(1), task1, "Задачи не совпадают.");
        assertEquals(inMemoryTaskManager.getEpicById(2), epic, "Эпики не совпадают.");
        assertEquals(inMemoryTaskManager.getSubtaskById(3), subtask1, "Подзадачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getListTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldNullByInstalledIdTask() {
        task1.setId(7);
        inMemoryTaskManager.createTask(Status.NEW, task1);
        assertNull(inMemoryTaskManager.getTaskById(7), "Присваивание id происходит неверно");
    }

    @Test
    void shouldTrueWhenRemoveAllTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        inMemoryTaskManager.removeAllTasks();
        assertEquals(inMemoryTaskManager.getListTasks().size(), 0, "Список не очищен");
    }

    @Test
    void statusCheckEpicWhenChangeStatusSubtask() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        inMemoryTaskManager.changeStatusSubtask(Status.IN_PROGRESS, subtask1);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус у эпика не изменился");
        inMemoryTaskManager.changeStatusSubtask(Status.DONE, subtask1);
        inMemoryTaskManager.changeStatusSubtask(Status.DONE, subtask2);
        assertEquals(epic.getStatus(), Status.DONE, "Статус у эпика не изменился");
    }

    @Test
    void checkRemoveEpicById() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        inMemoryTaskManager.removeEpicById(1);
        assertEquals(inMemoryTaskManager.getListEpics().size(), 0, "Эпик не удалился");
        assertNull(inMemoryTaskManager.getListSubtasks(epic), "Подзадачи из этого эпика не удалились");
    }

    @Test
    void checkRemoveTaskById() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        inMemoryTaskManager.removeTaskById(1);
        assertEquals(inMemoryTaskManager.getListTasks().size(), 1, "Задача не удалилась");
        inMemoryTaskManager.removeTaskById(2);
        assertEquals(inMemoryTaskManager.getListTasks().size(), 0, "Задача не удалилась");
    }

    @Test
    void checkRemoveSubtaskById() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        inMemoryTaskManager.removeSubtaskById(3);
        assertEquals(inMemoryTaskManager.getListSubtasks(epic).size(), 1, "Подзадача не удалилась");
    }

    @Test
    void checkRemoveAllTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        inMemoryTaskManager.removeAllTasks();
        assertEquals(inMemoryTaskManager.getListTasks().size(), 0, "Задачи не удалились");
    }

    @Test
    void checkRemoveAllSubtask() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        inMemoryTaskManager.removeAllSubtasks(epic);
        assertEquals(epic.getSubtasks().size(), 0, "Подзадачи удаляются неверно");
    }

    @Test
    void checkUpdateTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.updateTask(1, task2);
        assertEquals(task1.getTask(), task2.getTask(), "Задача не обновилась");
    }

    @Test
    void shouldNotNullHistory() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        assertNotNull(inMemoryTaskManager.getHistory());
        assertEquals(inMemoryTaskManager.getHistory().size(), 2, "История записывается неверно");
    }

    @Test
    void checkingDataIntegrityChangingId() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        Task task = inMemoryTaskManager.getTaskById(task1.getId());
        task.setId(1000);
        assertNotEquals(task.getId(), task1.getId(), "Нарушена целостность данных в InMemoryTaskManager");
    }
}