package util;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskMapper {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public static TaskDto createTaskDto(Task task) {
        return new TaskDto(task.getTask(), task.getId(), task.getStatus(),
                Duration.ofMinutes(Integer.parseInt(task.getDurationTask())),
                LocalDateTime.parse(task.getStartTime(), DATE_TIME_FORMATTER));
    }

    public static EpicDto createEpicDto(Epic epic) {
        try {
            return new EpicDto(epic.getTask(), epic.getId(), epic.getStatus(),
                    Duration.ofMinutes(Integer.parseInt(epic.getDurationTask())),
                    LocalDateTime.parse(epic.getStartTime(), DATE_TIME_FORMATTER));
        } catch (NullPointerException e) {
            return new EpicDto(epic.getTask(), epic.getId(), epic.getStatus(),
                    null, null);
        }
    }

    public static SubtaskDto createSubtaskDto(Subtask subtask) {
        return new SubtaskDto(subtask.getTask(), subtask.getId(), subtask.getStatus(),
                Duration.ofMinutes(Integer.parseInt(subtask.getDurationTask())),
                LocalDateTime.parse(subtask.getStartTime(), DATE_TIME_FORMATTER), subtask.getEpic());
    }

    public static List<TaskDto> createListTasksDto(List<? extends Task> tasks) {
       List<TaskDto> tasksDto = new ArrayList<>();
       for (Task task : tasks) {
           if (task.getClass().equals(Task.class)) {
               tasksDto.add(createTaskDto(task));
           } else if (task.getClass().equals(Epic.class)) {
               tasksDto.add(createEpicDto((Epic) task));
           } else {
               tasksDto.add(createSubtaskDto((Subtask) task));
           }
       }
       return tasksDto;
    }

    public static Task createTask(TaskDto taskDto) {
        return new Task(taskDto.getDescription(), taskDto.getStartTime().format(DATE_TIME_FORMATTER),
                String.valueOf(taskDto.getDuration().toMinutes()));
    }

    public static Epic createEpic(EpicDto taskDto) {
        return new Epic(taskDto.getDescription());
    }

    public static Subtask createSubtask(SubtaskDto subtaskDto, Epic epic) {
        return new Subtask(epic,subtaskDto.getDescription(), subtaskDto.getStartTime().format(DATE_TIME_FORMATTER),
                String.valueOf(subtaskDto.getDuration().toMinutes()));
    }
}