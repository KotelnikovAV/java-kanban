package service;

import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    public static HistoryManager inMemoryHistoryManager;

    @BeforeAll
    public static void beforeAll() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        for (int i = 0; i < 10; i++) {
            inMemoryHistoryManager.add(new Task("Номер задачи: " + (i + 1)));
        }
    }

    @Test
    void checkAddNewTask() {
        assertEquals(inMemoryHistoryManager.getHistory().size(), 10, "Размер листа неверен");
        inMemoryHistoryManager.add(new Task("Номер задачи: " + 11));
        assertEquals(inMemoryHistoryManager.getHistory().size(), 10, "Размер листа неверен");
        Task task = inMemoryHistoryManager.getHistory().get(0);
        assertFalse(task.getTask().equals("Номер задачи: 1"), "Алгоритм удаления из истории неверен");
    }
}