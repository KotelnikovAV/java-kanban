package model;

import java.util.ArrayList;
import java.util.LinkedList;

public interface TaskManager {
    void createTask(Status status, Task task);

    void createEpic(Status status, Epic epic);

    void createSubtask(Epic epic, Status status, Subtask subtask);

    ArrayList<Task> getListTasks();

    ArrayList<Epic> getListEpics();

    ArrayList<Subtask> getListSubtasks(Epic epic);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks(Epic epic);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void changeStatusTask(Status status, Task task);

    void changeStatusSubtask(Status status, Subtask subtask);

    void changeStatusEpic(Status status, Epic epic);

    LinkedList<Task> getHistory();
}