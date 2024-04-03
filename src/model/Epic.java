package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String task, String startTime, String durationTask) {
        super(task, startTime, durationTask);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void setStartTime(String startTime) {
        if (subtasks.isEmpty()) {
            this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        } else {
            this.startTime = subtasks.stream().map(subtask -> subtask.startTime)
                    .min(Comparator.comparingInt(LocalDateTime::getMinute)).get();
        }
    }

    @Override
    public void setDuration(String durationTask) {
        if (subtasks.isEmpty()) {
            this.durationTask = Duration.ofMinutes(Integer.parseInt(durationTask));
        } else {
            this.durationTask = Duration.ofMinutes(subtasks.stream().map(subtask -> subtask.durationTask.toMinutes())
                    .reduce(0L, Long::sum));
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.isEmpty()) {
            return startTime.plus(durationTask);
        } else {
            return subtasks.stream().map(Task::getEndTime).max(((endTime1, endTime2) -> {
                if (endTime1.isAfter(endTime2)) {
                    return 1;
                } else if (endTime1.isBefore(endTime2)) {
                    return -1;
                } else {
                    return 0;
                }
            })).get();
        }
    }

    @Override
    public String toString() {
        return getId() + ",EPIC," + getTask() + "," + getStatus() + "," + startTime.format(DATE_TIME_FORMATTER)
                + "," + durationTask.toMinutes() + "," + getEndTime().format(DATE_TIME_FORMATTER);
    }
}