package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    public static HistoryManager inMemoryHistoryManager;
    public static Task task1;
    public static Task task2;
    public static Epic epic;
    public static Subtask subtask1;
    public static Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task1 = new Task("123");
        task2 = new Task("125");
        epic = new Epic("234");
        subtask1 = new Subtask(epic, "345");
        subtask2 = new Subtask(epic, "345");
        task1.setId(0);
        task2.setId(1);
        epic.setId(2);
        subtask1.setId(3);
        subtask2.setId(4);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask2);
    }

    @Test
    void checkHistoryManager() {
        assertEquals(5, inMemoryHistoryManager.getHistory().size(), "Количество элементов в истории " +
                "неверно");
    }

    @Test
    void checkAddEqualsTask() {
        inMemoryHistoryManager.add(task1);
        assertEquals(5, inMemoryHistoryManager.getHistory().size(), "В истории не удаляется " +
                "повторяющийся объект");
    }

    @Test
    void checkRemoveById() {
        inMemoryHistoryManager.remove(0);
        inMemoryHistoryManager.remove(2);
        inMemoryHistoryManager.remove(4);
        assertEquals(2, inMemoryHistoryManager.getHistory().size(), "В истории не удалились объекты");
    }
}