package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class InMemoryTaskManagerTest {
    public static TaskManager inMemoryTaskManager;
    public static Task task1;
    public static Task task2;
    public static Epic epic;
    public static Subtask subtask1;
    public static Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("123", "10:30 01.01.01", "30");
        task2 = new Task("125", "11:30 01.01.01", "30");
        epic = new Epic("234");
        subtask1 = new Subtask(epic, "345", "12:30 01.01.01", "30");
        subtask2 = new Subtask(epic, "345", "13:30 01.01.01", "30");
    }

    @Test
    void shouldEqualIfIdTaskAndIdEpicEqual() {
        task1.setId(1);
        epic.setId(1);
        assertEquals(epic, task1, "Задачи с одинаковыми id не равны");
    }

    @Test
    void checkAddNewTask() {
        boolean checkNull = true;
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        Optional<Task> task = inMemoryTaskManager.getTaskById(1);
        Optional<Epic> epic1 = inMemoryTaskManager.getEpicById(2);
        Optional<Subtask> subtask = inMemoryTaskManager.getSubtaskById(3);
        if (task.isPresent() && epic1.isPresent() && subtask.isPresent()) {
            checkNull = false;
        }
        assertFalse(checkNull, "Одна из задач вернулась пустая");
        assertEquals(task.get(), task1, "Задачи не совпадают.");
        assertEquals(epic1.get(), epic, "Эпики не совпадают.");
        assertEquals(subtask.get(), subtask1, "Подзадачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getListTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void shouldNullByInstalledIdTask() {
        boolean checkNull = false;
        task1.setId(7);
        inMemoryTaskManager.createTask(Status.NEW, task1);
        Optional<Task> task = inMemoryTaskManager.getTaskById(7);
        if (task.isEmpty()) {
            checkNull = true;
        }
        assertTrue(checkNull, "Присваивание id происходит неверно");
    }

    @Test
    void shouldTrueWhenRemoveAllTask() {
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        inMemoryTaskManager.removeAllTasks();
        assertEquals(inMemoryTaskManager.getListTasks().size(), 0, "Список не очищен");
    }

    @Test
    void checkStatusEpicWhenChangeStatusSubtask() {
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
    void checkStatusSubtaskWhenChangeStatusEpic() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        inMemoryTaskManager.changeStatusEpic(Status.DONE, epic);
        assertEquals(Status.DONE, subtask1.getStatus(), "Статус входящих подзадач не изменился");
        assertEquals(Status.DONE, subtask2.getStatus(), "Статус входящих подзадач не изменился");
    }

    @Test
    void checkRemoveEpicById() {
        boolean checkNull = false;
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        inMemoryTaskManager.removeEpicById(1);
        assertEquals(inMemoryTaskManager.getListEpics().size(), 0, "Эпик не удалился");
        Optional<List<Subtask>> listSubtasks = inMemoryTaskManager.getListSubtasks(epic);
        if (listSubtasks.isEmpty()) {
            checkNull = true;
        }
        assertTrue(checkNull, "Подзадачи из этого эпика не удалились");
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
        assertEquals(inMemoryTaskManager.getListSubtasks(epic).get().size(), 1, "Подзадача не удалилась");
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
    void checkDataIntegrityChangingId() {
        boolean checkNull = false;
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        Optional<Task> task = inMemoryTaskManager.getTaskById(task1.getId());
        if (task.isEmpty()) {
            checkNull = true;
        }
        assertFalse(checkNull, "Метод ввернул null");
        task.get().setId(1000);
        assertNotEquals(task.get().getId(), task1.getId(), "Нарушена целостность данных в InMemoryTaskManager");
    }

    @Test
    void checkRecordingOfTasksWithOverlappingIntervals() {
        Task task3 = new Task("333", "10:20 01.01.01", "10");
        Task task4 = new Task("444", "12:00 01.01.01", "30");
        Task task5 = new Task("555", "10:00 01.01.01", "25");
        inMemoryTaskManager.createTask(Status.NEW, task1);
        inMemoryTaskManager.createTask(Status.NEW, task2);
        inMemoryTaskManager.createTask(Status.NEW, task3);
        inMemoryTaskManager.createTask(Status.NEW, task4);
        inMemoryTaskManager.createTask(Status.NEW, task5);
        assertEquals(4, inMemoryTaskManager.getListTasks().size(), "Задача с пересекающимся " +
                "временным отрезком была добавлена");
        inMemoryTaskManager.removeTaskById(3);
        inMemoryTaskManager.createTask(Status.NEW, task5);
        assertEquals(4, inMemoryTaskManager.getListTasks().size(), "Временной отрезок удаленной " +
                "задачи не удалился");
    }

    @Test
    void checkUpdateTimeValuesOfEpic() {
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask1);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, subtask2);
        assertEquals("60", epic.getDurationTask(), "Длительность эпика не зависит от длительности " +
                "подзадач");
        inMemoryTaskManager.removeSubtaskById(2);
        assertEquals("30", epic.getDurationTask(), "Длительность эпика не была изменена");
        assertEquals("13:30 01.01.01", epic.getStartTime(), "Время старта не было изменено");
    }
}