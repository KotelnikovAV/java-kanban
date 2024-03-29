package service;

import model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final List<Task> listTasks;
    private final List<Epic> listEpics;
    private final List<Subtask> listSubtasks;
    protected final HistoryManager inMemoryHistoryManager;
    private int counterId;

    public InMemoryTaskManager() {
        listTasks = new ArrayList<>();
        listEpics = new ArrayList<>();
        listSubtasks = new ArrayList<>();
        inMemoryHistoryManager = Managers.getHistoryManager();
        counterId = 0;
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    @Override
    public void createTask(Status status, Task task) {
        if (task.getClass() != Task.class) {
            return;
        }
        for (Task taskFromList : listTasks) {
            if (taskFromList.getId() == task.getId()) {
                return;
            }
        }
        counterId++;
        task.setId(counterId);
        task.setStatus(status);
        listTasks.add(task);
    }

    @Override
    public void createEpic(Status status, Epic epic) {
        for (Epic epicFromList : listEpics) {
            if (epicFromList.getId() == epic.getId()) {
                return;
            }
        }
        counterId++;
        epic.setId(counterId);
        epic.setStatus(status);
        listEpics.add(epic);
    }

    @Override
    public void createSubtask(Epic epic, Status status, Subtask subtask) {
        for (Subtask subtaskFromList : listSubtasks) {
            if (subtaskFromList.getId() == subtask.getId()) {
                return;
            }
        }
        counterId++;
        subtask.setId(counterId);
        subtask.setStatus(status);
        listSubtasks.add(subtask);
        epic.addSubtask(subtask);
    }

    @Override
    public List<Task> getListTasks() {
        return List.copyOf(listTasks);
    }

    @Override
    public List<Epic> getListEpics() {
        return List.copyOf(listEpics);
    }

    @Override
    public List<Subtask> getListSubtasks(Epic epic) {
        if (checkEpic(epic)) {
            return List.copyOf(epic.getSubtasks());
        } else {
            return null;
        }
    }

    @Override
    public List<Subtask> getListSubtasks() {
        return List.copyOf(listSubtasks);
    }

    @Override
    public void removeAllTasks() {
        for (Task task : listTasks) {
            inMemoryHistoryManager.remove(task.getId());
        }
        listTasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : listEpics) {
            inMemoryHistoryManager.remove(epic.getId());
        }
        for (Subtask subtask : listSubtasks) {
            inMemoryHistoryManager.remove(subtask.getId());
        }
        listEpics.clear();
        listSubtasks.clear();
    }

    @Override
    public void removeAllSubtasks(Epic epic) {
        List<Subtask> subtasksInEpic = epic.getSubtasks();
        for (Subtask subtask : subtasksInEpic) {
            inMemoryHistoryManager.remove(subtask.getId());
        }
        for (Subtask subtask : subtasksInEpic) {
            listSubtasks.removeIf(subtaskInList -> subtaskInList.equals(subtask));
        }
        epic.getSubtasks().clear();
    }

    @Override
    public Task getTaskById(int id) {
        for (Task task : listTasks) {
            if (task.getId() == id) {
                inMemoryHistoryManager.add(task);
                Task copyTask = new Task(task.getTask());
                copyTask.setId(task.getId());
                copyTask.setStatus(task.getStatus());
                return copyTask;
            }
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        for (Epic epic : listEpics) {
            if (epic.getId() == id) {
                inMemoryHistoryManager.add(epic);
                Epic copyEpic = new Epic(epic.getTask());
                copyEpic.setId(epic.getId());
                copyEpic.setStatus(epic.getStatus());
                copyEpic.getSubtasks().addAll(epic.getSubtasks());
                return copyEpic;
            }
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        for (Subtask subtask : listSubtasks) {
            if (subtask.getId() == id) {
                inMemoryHistoryManager.add(subtask);
                Subtask copySubtask = new Subtask(subtask.getEpic(), subtask.getTask());
                copySubtask.setId(subtask.getId());
                copySubtask.setStatus(subtask.getStatus());
                return copySubtask;
            }
        }
        return null;
    }

    @Override
    public void updateTask(int id, Task task) {
        for (Task taskInList : listTasks) {
            if (taskInList.getId() == id) {
                taskInList.setTask(task.getTask());
            }
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        for (Epic epicInList : listEpics) {
            if (epicInList.getId() == id) {
                epicInList.setTask(epic.getTask());
            }
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        for (Subtask subtaskInList : listSubtasks) {
            if (subtaskInList.getId() == id) {
                subtaskInList.setTask(subtask.getTask());
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        Iterator<Task> taskIterator = listTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            if (task.getId() == id) {
                taskIterator.remove();
            }
        }
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Iterator<Epic> epicIterator = listEpics.iterator();
        List<Subtask> subtasksInEpic = new ArrayList<>();
        while (epicIterator.hasNext()) {
            Epic epic = epicIterator.next();
            if (epic.getId() == id) {
                subtasksInEpic = epic.getSubtasks();
                epicIterator.remove();
            }
        }
        for (Subtask subtask : subtasksInEpic) {
            listSubtasks.removeIf(subtaskInList -> subtaskInList.equals(subtask));
            inMemoryHistoryManager.remove(subtask.getId());
        }
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Epic epic;
        List<Subtask> subtasks;
        Iterator<Subtask> subtaskIterator = listSubtasks.iterator();
        while (subtaskIterator.hasNext()) {
            Subtask subtask = subtaskIterator.next();
            if (subtask.getId() == id) {
                epic = subtask.getEpic();
                subtaskIterator.remove();
                subtasks = epic.getSubtasks();
                subtasks.removeIf(subtaskInEpic -> subtaskInEpic.equals(subtask));
            }
        }
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void changeStatusTask(Status status, Task task) {
        task.setStatus(status);
    }

    @Override
    public void changeStatusSubtask(Status status, Subtask subtask) {
        int counterNEW = 0;
        int counterDONE = 0;
        Epic epic = subtask.getEpic();

        subtask.setStatus(status);
        List<Subtask> subtasks = epic.getSubtasks();
        for (Subtask subtaskInEpic : subtasks) {
            if (subtaskInEpic.getStatus().equals(Status.NEW)) {
                counterNEW++;
            } else if (subtaskInEpic.getStatus().equals(Status.DONE)) {
                counterDONE++;
            }
        }
        if (counterNEW == subtasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDONE == subtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void changeStatusEpic(Status status, Epic epic) {
        epic.setStatus(status);
        List<Subtask> subtasks = epic.getSubtasks();
        for (Subtask subtask : subtasks) {
            subtask.setStatus(status);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(inMemoryHistoryManager.getHistory());
    }

    public boolean checkEpic(Epic epic) {
        for (Epic epicInList : listEpics) {
            if (epicInList.equals(epic)) {
                return true;
            }
        }
        return false;
    }
}