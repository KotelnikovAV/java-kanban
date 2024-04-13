package service;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest {

    public static File file;
    @BeforeAll
    public static void beforeAll() {
        try {
            file = File.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void checkReadFromEmptyFile() {
        FileBackedTaskManager inMemoryTaskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager();
        inMemoryTaskManager.setNameFile(file.getName());
        inMemoryTaskManager.loadFromFile();
        assertEquals(0, inMemoryTaskManager.getListTasks().size(), "Список не пустой");
        assertEquals(0, inMemoryTaskManager.getListEpics().size(), "Список не пустой");
        assertEquals(0, inMemoryTaskManager.getListSubtasks().size(), "Список не пустой");
        assertEquals(0, inMemoryTaskManager.getHistory().size(), "Список не пустой");
    }

    @Test
    void checkWriteInEmptyFileAndLoadingTasks () {
        FileBackedTaskManager inMemoryTaskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager();
        inMemoryTaskManager.setNameFile(file.getName());
        inMemoryTaskManager.createTask(Status.NEW, new Task("Пропылесосить", "10:30 01.01.02",
                "30"));
        inMemoryTaskManager.createTask(Status.NEW, new Task("Поспать", "11:30 01.01.02",
                "30"));
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.loadFromFile();
        assertEquals(2, inMemoryTaskManager.getListTasks().size(), "Запись прошла некорректно");
        assertEquals(2, inMemoryTaskManager.getHistory().size(), "Запись прошла некорректно");
    }
}