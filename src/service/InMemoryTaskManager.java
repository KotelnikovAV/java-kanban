package service;

import exceptions.IntersectionIntervalsException;
import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    private static final double REDUCTION_FACTOR = 0.8;

    private final List<Task> listTasks;
    private final List<Epic> listEpics;
    private final List<Subtask> listSubtasks;
    private final Set<Task> tasksByPriority;
    protected Map<LocalDateTime, Boolean> takenInterval;
    protected final HistoryManager inMemoryHistoryManager;
    private int counterId;

    public InMemoryTaskManager() {
        listTasks = new ArrayList<>();
        listEpics = new ArrayList<>();
        listSubtasks = new ArrayList<>();
        takenInterval = new HashMap<>();
        inMemoryHistoryManager = Managers.getHistoryManager();
        counterId = 0;
        tasksByPriority = new TreeSet<>((task1, task2) -> {
            if (LocalDateTime.parse(task1.getStartTime(), DATE_TIME_FORMATTER)
                    .isAfter(LocalDateTime.parse(task2.getStartTime(), DATE_TIME_FORMATTER))) {
                return 1;
            } else if (LocalDateTime.parse(task2.getStartTime(), DATE_TIME_FORMATTER)
                    .isAfter(LocalDateTime.parse(task1.getStartTime(), DATE_TIME_FORMATTER))) {
                return -1;
            } else if (LocalDateTime.parse(task2.getStartTime(), DATE_TIME_FORMATTER)
                    .isEqual(LocalDateTime.parse(task1.getStartTime(), DATE_TIME_FORMATTER))
                    && task1.getId() == task2.getId()) {
                return 0;
            } else {
                return 1;
            }
        });
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    @Override
    public void createTask(Status status, Task task) {
        if ((task.getClass() != Task.class) || (listTasks.contains(task))) {
            return;
        }
        if (!addTakenInterval(task)) {
            return;
        }
        counterId++;
        task.setId(counterId);
        task.setStatus(status);
        listTasks.add(task);
        tasksByPriority.add(task);
    }

    @Override
    public void createEpic(Status status, Epic epic) {
        if (listEpics.contains(epic)) {
            return;
        }
        if (!addTakenInterval(epic)) {
            return;
        }
        counterId++;
        epic.setId(counterId);
        epic.setStatus(status);
        listEpics.add(epic);
        tasksByPriority.add(epic);
    }

    @Override
    public void createSubtask(Epic epic, Status status, Subtask subtask) {
        if (listSubtasks.contains(subtask)) {
            return;
        }
        counterId++;
        subtask.setId(counterId);
        subtask.setStatus(status);
        listSubtasks.add(subtask);
        tasksByPriority.add(subtask);
        epic.addSubtask(subtask);
        updateTimeValuesOfEpic(epic);
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
    public Optional<List<Subtask>> getListSubtasks(Epic epic) {
        if (checkEpic(epic)) {
            return Optional.of(List.copyOf(epic.getSubtasks()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Subtask> getListSubtasks() {
        return List.copyOf(listSubtasks);
    }

    @Override
    public void removeAllTasks() {
        listTasks.forEach(task -> {
            inMemoryHistoryManager.remove(task.getId());
            tasksByPriority.remove(task);
            removeTakenInterval(task);
        });
        listTasks.clear();
    }

    @Override
    public void removeAllEpics() {
        listEpics.forEach(epic -> {
            inMemoryHistoryManager.remove(epic.getId());
            tasksByPriority.remove(epic);
            removeTakenInterval(epic);
        });
        listSubtasks.forEach(subtask -> {
            inMemoryHistoryManager.remove(subtask.getId());
            tasksByPriority.remove(subtask);
        });
        listEpics.clear();
        listSubtasks.clear();
    }

    @Override
    public void removeAllSubtasks(Epic epic) {
        List<Subtask> subtasksInEpic = epic.getSubtasks();
        subtasksInEpic.forEach(subtask -> {
            inMemoryHistoryManager.remove(subtask.getId());
            tasksByPriority.remove(subtask);
            listSubtasks.remove(subtask);
        });
        epic.getSubtasks().clear();
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        Optional<Task> taskById = listTasks.stream().filter(task -> task.getId() == id).findFirst();
        if (taskById.isPresent()) {
            inMemoryHistoryManager.add(taskById.get());
            Task copyTask = new Task(taskById.get().getTask(), taskById.get().getStartTime(),
                    taskById.get().getDurationTask());
            copyTask.setId(taskById.get().getId());
            copyTask.setStatus(taskById.get().getStatus());
            return Optional.of(copyTask);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        Optional<Epic> epicById = listEpics.stream().filter(epic -> epic.getId() == id).findFirst();
        if (epicById.isPresent()) {
            inMemoryHistoryManager.add(epicById.get());
            Epic copyEpic = new Epic(epicById.get().getTask(), epicById.get().getStartTime(),
                    epicById.get().getDurationTask());
            copyEpic.setId(epicById.get().getId());
            copyEpic.setStatus(epicById.get().getStatus());
            copyEpic.getSubtasks().addAll(epicById.get().getSubtasks());
            return Optional.of(copyEpic);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        Optional<Subtask> subtaskById = listSubtasks.stream().filter(subtask -> subtask.getId() == id).findFirst();
        if (subtaskById.isPresent()) {
            inMemoryHistoryManager.add(subtaskById.get());
            Subtask copySubtask = new Subtask(subtaskById.get().getEpic(), subtaskById.get().getTask(),
                    subtaskById.get().getStartTime(), subtaskById.get().getDurationTask());
            copySubtask.setId(subtaskById.get().getId());
            copySubtask.setStatus(subtaskById.get().getStatus());
            return Optional.of(copySubtask);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        Optional<Task> taskById = listTasks.stream().filter(task1 -> task1.getId() == id).findFirst();
        if (taskById.isPresent()) {
            Task taskFromList = taskById.get();
            taskFromList.setTask(task.getTask());
            taskFromList.setStatus(task.getStatus());
            taskFromList.setStartTime(task.getStartTime());
            taskFromList.setDuration(task.getDurationTask());
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        Optional<Epic> epicById = listEpics.stream().filter(epic1 -> epic1.getId() == id).findFirst();
        if (epicById.isPresent()) {
            Epic epicFromList = epicById.get();
            epicFromList.setTask(epic.getTask());
            epicFromList.setStatus(epic.getStatus());
            epicFromList.setStartTime(epic.getStartTime());
            epicFromList.setDuration(epic.getDurationTask());
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        Optional<Subtask> subtaskById = listSubtasks.stream().filter(subtask1 -> subtask1.getId() == id).findFirst();
        if (subtaskById.isPresent()) {
            Subtask subtaskFromList = subtaskById.get();
            subtaskFromList.setTask(subtask.getTask());
            subtaskFromList.setStatus(subtask.getStatus());
            subtaskFromList.setStartTime(subtask.getStartTime());
            subtaskFromList.setDuration(subtask.getDurationTask());
            updateTimeValuesOfEpic(subtaskById.get().getEpic());
        }
    }

    @Override
    public void removeTaskById(int id) {
        Optional<Task> taskById = listTasks.stream().filter(task -> task.getId() == id).findFirst();
        if (taskById.isPresent()) {
            listTasks.remove(taskById.get());
            inMemoryHistoryManager.remove(id);
            tasksByPriority.remove(taskById.get());
            removeTakenInterval(taskById.get());
        }
    }

    @Override
    public void removeEpicById(int id) {
        Optional<Epic> epicById = listEpics.stream().filter(epic -> epic.getId() == id).findFirst();
        if (epicById.isPresent()) {
            listEpics.remove(epicById.get());
            epicById.get().getSubtasks().forEach(listSubtasks::remove);
            tasksByPriority.remove(epicById.get());
            inMemoryHistoryManager.remove(id);
            removeTakenInterval(epicById.get());
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        Optional<Subtask> subtaskById = listSubtasks.stream().filter(subtask -> subtask.getId() == id).findFirst();
        if (subtaskById.isPresent()) {
            subtaskById.get().getEpic().removeSubtask(subtaskById.get());
            listSubtasks.remove(subtaskById.get());
            inMemoryHistoryManager.remove(id);
            tasksByPriority.remove(subtaskById.get());
            updateTimeValuesOfEpic(subtaskById.get().getEpic());
        }
    }

    @Override
    public void changeStatusTask(Status status, Task task) {
        task.setStatus(status);
    }

    @Override
    public void changeStatusSubtask(Status status, Subtask subtask) {
        long counterNew;
        long counterDone;
        Epic epic = subtask.getEpic();

        subtask.setStatus(status);
        List<Subtask> subtasks = epic.getSubtasks();
        counterNew = epic.getSubtasks().stream().filter(subtask1 -> subtask1.getStatus().equals(Status.NEW)).count();
        counterDone = epic.getSubtasks().stream().filter(subtask1 -> subtask1.getStatus().equals(Status.DONE)).count();

        if (counterNew == subtasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDone == subtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void changeStatusEpic(Status status, Epic epic) {
        epic.setStatus(status);
        List<Subtask> subtasks = epic.getSubtasks();
        subtasks.forEach(subtask -> subtask.setStatus(status));
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(inMemoryHistoryManager.getHistory());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(tasksByPriority);
    }

    public boolean addTakenInterval(Task task) {
        LocalDateTime endTime = task.getEndTime();
        LocalDateTime localDateTime = LocalDateTime.parse(task.getStartTime(), DATE_TIME_FORMATTER);
        Map<LocalDateTime, Boolean> copyTakenInterval = new HashMap<>(takenInterval);
        Duration duration = Duration.ofMinutes(Integer.parseInt(task.getDurationTask()));

        try {
            while (localDateTime.isBefore(endTime)) {
                duration = Duration.ofMinutes((long) (duration.toMinutes() * REDUCTION_FACTOR));
                // за счет этой переменной происходит быстрая выборочная проверка на пересечения из средней области
                if (takenInterval.containsKey(localDateTime)
                        || takenInterval.containsKey(endTime.minusMinutes(1))
                        || takenInterval.containsKey(endTime.minus(duration))
                        || takenInterval.containsKey(localDateTime.plus(duration))) {
                    // значительно увеличил вероятность нахождения пересечений во время первых пару-тройку итераций
                    throw new IntersectionIntervalsException("Добавление недоступно. Задача '" + task.getTask()
                            + "' пересекается по времени с другими задачами");
                }

                copyTakenInterval.put(localDateTime, false);
                localDateTime = localDateTime.plusMinutes(1);
                endTime = endTime.minusMinutes(1);
                copyTakenInterval.put(endTime, false);
                // сразу же начинаем заполнять с двух сторон и движемся к центру
            }
        } catch (IntersectionIntervalsException e) {
            System.out.println(e.getMessage());
            return false;
        }

        takenInterval = copyTakenInterval;
        return true;
    }

    public void removeTakenInterval(Task task) {
        LocalDateTime endTime = task.getEndTime();
        LocalDateTime localDateTime = LocalDateTime.parse(task.getStartTime(), DATE_TIME_FORMATTER);

        while (localDateTime.isBefore(endTime)) {
            takenInterval.remove(localDateTime);
            localDateTime = localDateTime.plusMinutes(1);
            endTime = endTime.minusMinutes(1);
            takenInterval.remove(endTime);
        }
    }

    public void updateTimeValuesOfEpic(Epic epic) {
        epic.setStartTime(epic.getStartTime());
        epic.setDuration(epic.getDurationTask());
    }

    public boolean checkEpic(Epic epic) {
        Optional<Epic> epicOptional = listEpics.stream().filter(epic1 -> epic1.equals(epic)).findFirst();
        return epicOptional.isPresent();
    }
}