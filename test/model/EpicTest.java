package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    public static Epic epic;
    public static Subtask subtask1;
    public static Subtask subtask2;
    public static ArrayList<Subtask> subtasks;

    @BeforeAll
    public static void beforeAll() {
        epic = new Epic("Переезд");
        subtask1 = new Subtask(epic, "Собрать вещи");
        subtask2 = new Subtask(epic, "Сказать пока");
        subtask1.setId(1);
        subtask2.setId(2);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
    }

    @Test
    void shouldEqualsListSubtask() {
        Subtask subtask3 = new Subtask(epic, "Выйти");
        subtask3.setId(3);
        subtasks.add(subtask3);
        epic.addSubtask(subtask3);
        assertEquals(epic.getSubtasks(), subtasks, "Списки неравны");
    }
}