package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    public static Epic epic = new Epic("Переезд");
    public static Subtask subtask = new Subtask(epic, "Собрать вещи");

    @Test
    void shouldEqualsEpic() {
        assertEquals(epic, subtask.getEpic(), "Эпик записан неверно");
    }
}