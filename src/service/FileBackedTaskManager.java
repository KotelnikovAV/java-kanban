package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String nameFile;
    private final Map<Integer, Epic> epicWithId;
    private final Map<Integer, Task> historyIndexWithTask;
    private static final int INDEX_HISTORY = 0;
    private static final int INDEX_ID = 1;
    private static final int INDEX_TYPE = 2;
    private static final int INDEX_DESCRIPTION = 3;
    private static final int INDEX_STATUS = 4;
    private static final int INDEX_EPIC = 7;
    private static final int INDEX_START_TIME = 5;
    private static final int INDEX_DURATION = 6;

    public FileBackedTaskManager(String nameFile) {
        super();
        this.nameFile = nameFile;
        epicWithId = new HashMap<>();
        historyIndexWithTask = new TreeMap<>();
    }

    public void loadFromFile() {
        super.removeAllEpics();
        super.removeAllTasks();
        takenInterval.clear();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nameFile, StandardCharsets.UTF_8))) {
            int id;
            int maxId = 0;

            while (bufferedReader.ready()) {
                String stringTask = bufferedReader.readLine();
                Task task = taskFromString(stringTask);
                findIndexTaskFromHistory(task, stringTask);

                if (task == null) {
                    continue;
                }
                id = task.getId();
                if (task.getClass() == Task.class) {
                    super.createTask(task.getStatus(), task);
                    task.setId(id);
                } else if (task.getClass() == Epic.class) {
                    super.createEpic(task.getStatus(), (Epic) task);
                    task.setId(id);
                    epicWithId.put(id, (Epic) task);
                } else {
                    super.createSubtask(((Subtask) task).getEpic(), task.getStatus(), (Subtask) task);
                    task.setId(id);
                }
                if (maxId < id) {
                    maxId = id;
                }
            }

            setCounterId(maxId);

            for (int i = historyIndexWithTask.size() - 1; i >= 0; i--) {
                inMemoryHistoryManager.add(historyIndexWithTask.get(i));
            }
            historyIndexWithTask.clear();
        } catch (IOException e) {
            RuntimeException ex = new ManagerSaveException("Файла " + nameFile + " не существует", e);
            System.out.println(ex.getMessage());
        }
    }

    public void findIndexTaskFromHistory(Task task, String stringTask) {
        String[] split = stringTask.split(",");
        if (split[INDEX_HISTORY].equals("indexInHistory")) {
            return;
        }
        int index = Integer.parseInt(split[INDEX_HISTORY]);
        if (index >= 0) {
            historyIndexWithTask.put(index, task);
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile, StandardCharsets.UTF_8))) {
            int indexInHistory;
            writer.write("indexInHistory,id,type,description,status,startTime,duration,epic" + "\n");
            for (Task task : getListTasks()) {      // код через стримы в данном случае будет нечитаемым
                indexInHistory = getHistory().indexOf(task);
                writer.write(indexInHistory + "," + task.toString() + "\n");
            }
            for (Epic epic : getListEpics()) {
                indexInHistory = getHistory().indexOf(epic);
                writer.write(indexInHistory + "," + epic.toString() + "\n");
            }
            for (Subtask subtask : getListSubtasks()) {
                indexInHistory = getHistory().indexOf(subtask);
                writer.write(indexInHistory + "," + subtask.toString() + "\n");
            }
        } catch (IOException e) {
            RuntimeException ex = new ManagerSaveException("Файла " + nameFile + " не существует", e);
            System.out.println(ex.getMessage());
        }
    }

    public Task taskFromString(String taskFromFile) {
        String[] splitTask = taskFromFile.split(",");
        switch (splitTask[INDEX_TYPE]) {
            case "TASK" -> {
                Task task = new Task(splitTask[INDEX_DESCRIPTION], splitTask[INDEX_START_TIME], splitTask[INDEX_DURATION]);
                task.setId(Integer.parseInt(splitTask[INDEX_ID]));
                task.setStatus(statusFromString(splitTask[INDEX_STATUS]));
                return task;
            }
            case "EPIC" -> {
                Task epic = new Epic(splitTask[INDEX_DESCRIPTION], splitTask[INDEX_START_TIME], splitTask[INDEX_DURATION]);
                epic.setId(Integer.parseInt(splitTask[INDEX_ID]));
                epic.setStatus(statusFromString(splitTask[INDEX_STATUS]));
                return epic;
            }
            case "SUBTASK" -> {
                Epic epic = epicWithId.get(Integer.parseInt(splitTask[INDEX_EPIC]));
                Task subtask = new Subtask(epic, splitTask[INDEX_DESCRIPTION], splitTask[INDEX_START_TIME],
                        splitTask[INDEX_DURATION]);
                subtask.setId(Integer.parseInt(splitTask[INDEX_ID]));
                subtask.setStatus(statusFromString(splitTask[INDEX_STATUS]));
                return subtask;
            }
            default -> {
                return null;
            }
        }
    }

    public Status statusFromString(String status) {
        return switch (status) {
            case ("NEW") -> Status.NEW;
            case ("IN_PROGRESS") -> Status.IN_PROGRESS;
            case ("DONE") -> Status.DONE;
            default -> null;
        };
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    @Override
    public void createTask(Status status, Task task) {
        super.createTask(status, task);
        save();
    }

    @Override
    public void createEpic(Status status, Epic epic) {
        super.createEpic(status, epic);
        save();
    }

    @Override
    public void createSubtask(Epic epic, Status status, Subtask subtask) {
        super.createSubtask(epic, status, subtask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks(Epic epic) {
        super.removeAllSubtasks(epic);
        save();
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        Optional<Task> task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        Optional<Epic> epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        Optional<Subtask> subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void changeStatusTask(Status status, Task task) {
        super.changeStatusTask(status, task);
        save();
    }

    @Override
    public void changeStatusSubtask(Status status, Subtask subtask) {
        super.changeStatusSubtask(status, subtask);
        save();
    }

    @Override
    public void changeStatusEpic(Status status, Epic epic) {
        super.changeStatusEpic(status, epic);
        save();
    }
}