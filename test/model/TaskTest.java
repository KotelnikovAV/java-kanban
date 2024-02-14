package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    public static Task task;

    @BeforeAll
    public static void beforeAll() {
        task = new Task("Отдохнуть");
        task.setId(1);
        task.setStatus(Status.IN_PROGRESS);
    }

    @Test
    void shouldEqualsTask() {
        assertEquals(task.getTask(), "Отдохнуть", "Задача записана неверно");
    }

    @Test
    void shouldEqualsId() {
        assertEquals(task.getId(), 1, "id записан неверно");
    }

    @Test
    void shouldEqualsStatus() {
        assertEquals(task.getStatus(), Status.IN_PROGRESS, "Статус записан неверно");
    }

    @Test
    void shouldEqualsTasks() {
        Task task1 = new Task("Поспать");
        task1.setId(1);
        task1.setStatus(Status.NEW);
        assertEquals(task, task1, "Задачи неравны");
    }
}