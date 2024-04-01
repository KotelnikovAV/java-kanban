package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    public static Task task;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    @BeforeEach
    public void beforeAll() {
        task = new Task("Отдохнуть", "10:30 01.01.01", "30");
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
        Task task1 = new Task("Поспать", "10:30 01.01.01", "30");
        task1.setId(1);
        task1.setStatus(Status.NEW);
        assertEquals(task, task1, "Задачи неравны");
    }

    @Test
    void checkStartTime() {
        assertEquals("10:30 01.01.01", task.getStartTime(), "Расчет начала задачи выполнен неверно");
    }

    @Test
    void checkDuration() {
        assertEquals("30", task.getDurationTask(), "Расчет длительности задачи выполнен неверно");
    }

    @Test
    void checkEndTime() {
        assertEquals("11:00 01.01.01", task.getEndTime().format(DATE_TIME_FORMATTER),
                "Расчет окончания задачи выполнен неверно");
    }

    @Test
    void checkSetupStartTime() {
        task.setStartTime("12:30 01.01.12");
        assertEquals("12:30 01.01.12", task.getStartTime(), "Время установилось неверно");
    }

    @Test
    void checkSetupDuration() {
        task.setDuration("115");
        assertEquals("115", task.getDurationTask(), "Длительность установилось неверно");
    }
}