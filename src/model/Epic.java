package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String task) {
        super(task, null, null);
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

    public void setStartTime() {
        Optional<LocalDateTime> startTimeOfEpic = subtasks.stream()
                .map(subtask -> subtask.startTime)
                .min(Comparator.comparingInt(LocalDateTime::getMinute));
        startTimeOfEpic.ifPresent(localDateTime -> this.startTime = localDateTime);
    }

    public void setDuration() {
        this.durationTask = Duration.ofMinutes(subtasks.stream()
                .map(subtask -> subtask.durationTask.toMinutes())
                .reduce(0L, Long::sum));
    }

    @Override
    public LocalDateTime getEndTime() {
        if (!subtasks.isEmpty()) {
            Optional<LocalDateTime> endTime = subtasks.stream().map(Task::getEndTime).max(((endTime1, endTime2) -> {
                if (endTime1.isAfter(endTime2)) {
                    return 1;
                } else if (endTime1.isBefore(endTime2)) {
                    return -1;
                } else {
                    return 0;
                }
            }));
            return endTime.get();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (startTime == null) {
            return getId() + ",EPIC," + getTask() + "," + getStatus() + "," + null
                    + "," + null + "," + null;
        } else {
            return getId() + ",EPIC," + getTask() + "," + getStatus() + "," + startTime.format(DATE_TIME_FORMATTER)
                    + "," + durationTask.toMinutes() + "," + getEndTime().format(DATE_TIME_FORMATTER);
        }
    }
}