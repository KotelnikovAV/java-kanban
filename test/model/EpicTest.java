package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public static Epic epic;
    public static Subtask subtask1;
    public static Subtask subtask2;
    public static List<Subtask> subtasks;

    @BeforeAll
    public static void beforeAll() {
        epic = new Epic("Переезд");
        subtask1 = new Subtask(epic, "Собрать вещи", "11:00 01.01.01", "30");
        subtask2 = new Subtask(epic, "Сказать пока", "12:30 01.01.01", "30");
        subtask1.setId(1);
        subtask2.setId(2);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.setStartTime();
        epic.setDuration();
    }

    @Test
    void shouldEqualsListSubtask() {
        Subtask subtask3 = new Subtask(epic, "Выйти", "11:00 01.01.01", "30");
        subtask3.setId(3);
        subtasks.add(subtask3);
        epic.addSubtask(subtask3);
        assertEquals(epic.getSubtasks(), subtasks, "Списки неравны");
    }

    @Test
    void checkStartTime() {
        assertEquals("11:00 01.01.01", epic.getStartTime(), "Расчет начала задачи выполнен неверно");
    }

    @Test
    void checkDuration() {
        assertEquals("60", epic.getDurationTask(), "Расчет длительности задачи выполнен неверно");
    }

    @Test
    void checkEndTime() {
        assertEquals("13:00 01.01.01", epic.getEndTime().format(DATE_TIME_FORMATTER),
                "Расчет окончания задачи выполнен неверно");
    }
}