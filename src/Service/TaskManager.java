package Service;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskManager {
    private final ArrayList<Task> listTasks;
    private final ArrayList<Epic> listEpics;
    private final ArrayList<Subtask> listSubtasks;
    private int counterTaskId;
    private int counterEpicId;
    private int counterSubtaskId;

    public TaskManager() {
        listTasks = new ArrayList<>();
        listEpics = new ArrayList<>();
        listSubtasks = new ArrayList<>();
        counterTaskId = 0;
        counterEpicId = 0;
        counterSubtaskId = 0;
    }

    public void createTask(Status status, Task task) {
        for (Task taskFromList : listTasks) {
            if (taskFromList.getId() == task.getId()) {
                return;
            }
        }
        counterTaskId++;
        task.setId(counterTaskId);
        task.setStatus(status);
        listTasks.add(task);
    }

    public void createEpic(Status status, Epic epic) {
        for (Epic epicFromList : listEpics) {
            if (epicFromList.getId() == epic.getId()) {
                return;
            }
        }
        counterEpicId++;
        epic.setId(counterEpicId);
        epic.setStatus(status);
        listEpics.add(epic);
    }

    public void createSubtask(Epic epic, Status status, Subtask subtask) {
        for (Subtask subtaskFromList : listSubtasks) {
            if (subtaskFromList.getId() == subtask.getId()) {
                return;
            }
        }
        counterSubtaskId++;
        subtask.setId(counterSubtaskId);
        subtask.setStatus(status);
        listSubtasks.add(subtask);
        epic.addSubtask(subtask);
    }

    public ArrayList<Task> getListTasks() {
        return listTasks;
    }

    public ArrayList<Epic> getListEpics() {
        return listEpics;
    }

    public ArrayList<Subtask> getListSubtasks(Epic epic) {
        if (checkEpic(epic)) {
            return epic.getSubtasks();
        } else {
            return null;
        }
    }

    public void removeAllTasks() {
        listTasks.clear();
        counterTaskId = 0;
    }

    public void removeAllEpics() {
        listEpics.clear();
        listSubtasks.clear();
        counterEpicId = 0;
        counterSubtaskId = 0;
    }

    public void removeAllSubtasks(Epic epic) {
        ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
        for (Subtask subtask : subtasksInEpic) {
            listSubtasks.removeIf(subtaskInList -> subtaskInList.equals(subtask));
        }
    }

    public Task getTaskById(int id) {
        for (Task task : listTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public Epic getEpicById(int id) {
        for (Epic epic : listEpics) {
            if (epic.getId() == id) {
                return epic;
            }
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        for (Subtask subtask : listSubtasks) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    public void updateTask(int id, Task task) {
        for (Task taskInList : listTasks) {
            if (taskInList.getId() == id) {
                taskInList.setTask(task.getTask());
            }
        }
    }

    public void updateEpic(int id, Epic epic) {
        for (Epic epicInList : listEpics) {
            if (epicInList.getId() == id) {
                epicInList.setTask(epic.getTask());
            }
        }
    }

    public void updateSubtask(int id, Subtask subtask) {
        for (Subtask subtaskInList : listSubtasks) {
            if (subtaskInList.getId() == id) {
                subtaskInList.setTask(subtask.getTask());
            }
        }
    }

    public void removeTaskById(int id) {
        Iterator<Task> taskIterator = listTasks.iterator();
        while (taskIterator.hasNext()) {
            Task task = taskIterator.next();
            if (task.getId() == id) {
                taskIterator.remove();
            }
        }
    }

    public void removeEpicById(int id) {
        Iterator<Epic> epicIterator = listEpics.iterator();
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        while (epicIterator.hasNext()) {
            Epic epic = epicIterator.next();
            if (epic.getId() == id) {
                subtasksInEpic = epic.getSubtasks();
                epicIterator.remove();
            }
        }
        for (Subtask subtask : subtasksInEpic) {
            listSubtasks.removeIf(subtaskInList -> subtaskInList.equals(subtask));
        }
    }

    public void removeSubtaskById(int id) {
        Epic epic;
        ArrayList<Subtask> subtasks ;
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

    }

    public void changeStatusTask(Status status, Task task) {
        task.setStatus(status);
    }

    public void changeStatusSubtask(Status status, Subtask subtask) {
        int counterNEW = 0;
        int counterDONE = 0;
        Epic epic = subtask.getEpic();

        subtask.setStatus(status);
        ArrayList<Subtask> subtasks = epic.getSubtasks();
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

    public void changeStatusEpic(Status status, Epic epic) {
        epic.setStatus(status);
        ArrayList<Subtask> subtasks = epic.getSubtasks();
        for (Subtask subtask : subtasks) {
            subtask.setStatus(status);
        }
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
