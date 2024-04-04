package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    void createTask(Status status, Task task);

    void createEpic(Status status, Epic epic);

    void createSubtask(Epic epic, Status status, Subtask subtask);

    List<Task> getListTasks();

    List<Epic> getListEpics();

    Optional<List<Subtask>> getListSubtasks(Epic epic);

    List<Subtask> getListSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks(Epic epic);

    Optional<Task> getTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Optional<Subtask> getSubtaskById(int id);

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void changeStatusTask(Status status, Task task);

    void changeStatusSubtask(Status status, Subtask subtask);

    void changeStatusEpic(Status status, Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}