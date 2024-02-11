package test;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import service.InMemoryTaskManager;

import java.util.ArrayList;

class InMemoryTaskManagerTest {
    // сделал покрытие тестами не всех методов, так как многие методы реализуют похожую логику
    public static InMemoryTaskManager inMemoryTaskManager;
    public static Task task1;
    public static Task task2;
    public static Epic epic;
    public static Subtask subtask;

    @BeforeAll
    public static void beforeAll(){
        task1 = new Task("123");
        task2 = new Task("125");
        epic = new Epic("234");
        subtask = new Subtask(epic, "345");
    }

    @BeforeEach
    public void beforeEach(){
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldEqualIfIdTasksEqual() {
        task1 = new Task("123");
        task2 = new Task("234");
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи с одинаковыми id не равны");
    }

    @Test
    void shouldEqualIfIdTaskAndIdEpicEqual() {
        task1 = new Task("123");
        epic = new Epic("234");
        task1.setId(1);
        epic.setId(1);
        assertEquals(epic, task1, "Задачи с одинаковыми id не равны");
    }

    /* В ТЗ написано, что необходимо проверить, что объект Epic нельзя добавить в самого себя в виде подзадачи.
    В моей реализации этого не позволит сделать сам компилятор. То же самое касается и пункта: проверьте,
    что объект Subtask нельзя сделать своим же эпиком */

    @Test
    void checkAddNewTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask);

        assertEquals(inMemoryTaskManager.getTaskById(1), task1, "Задачи не совпадают.");
        assertEquals(inMemoryTaskManager.getEpicById(2), epic, "Эпики не совпадают.");
        assertEquals(inMemoryTaskManager.getSubtaskById(3), subtask, "Подзадачи не совпадают.");

        final ArrayList<Task> tasks = inMemoryTaskManager.getListTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkHistoryManager() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        Object o = inMemoryTaskManager.getTaskById(1);
        assertEquals(inMemoryTaskManager.getHistory().size(), 1, "История работает некорректно");
        for (int i = 0; i < 9; i++) {
            o = inMemoryTaskManager.getTaskById(1);
        }
        assertEquals(inMemoryTaskManager.getHistory().size(), 10, "Размер должен быть равен 10");
        o = inMemoryTaskManager.getTaskById(1);
        assertEquals(inMemoryTaskManager.getHistory().size(), 10, "Размер должен быть равен 10");
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
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask);
        inMemoryTaskManager.changeStatusSubtask(Status.IN_PROGRESS, subtask);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус у эпика не изменился");
    }

    @Test
    void statusCheckEpicWhenChangeStatusSubtask1() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask);
        inMemoryTaskManager.changeStatusSubtask(Status.IN_PROGRESS, subtask);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус у эпика не изменился");
    }

    @Test
    void CheckRemoveEpicById() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask);
        inMemoryTaskManager.removeEpicById(1);
        assertEquals(inMemoryTaskManager.getListEpics().size(), 0, "Эпик не удалился");
        assertNull(inMemoryTaskManager.getListSubtasks(epic), "Подзадачи из этого эпика не удалились");
    }

    @Test
    void CheckUpdateTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.updateTask(1, task2);
        assertEquals(task1.getTask(), task2.getTask(), "Задача не обновилась");
    }

    @Test
    void CheckRemoveAllTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.removeAllTasks();
        assertEquals(inMemoryTaskManager.getListTasks().size(), 0, "Задачи не удалились");
    }
}