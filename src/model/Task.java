package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static util.TaskMapper.DATE_TIME_FORMATTER;

public class Task {
    private String task;
    private int id;
    private Status status;
    protected Duration durationTask;
    protected LocalDateTime startTime;

    public Task(String task, String startTime, String durationTask) {
        this.task = task;
        id = 0;
        if (startTime != null) {
            this.durationTask = Duration.ofMinutes(Integer.parseInt(durationTask));
            this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        } else {
            this.durationTask = null;
            this.startTime = null;
        }
    }

    public String getTask() {
        return task;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(durationTask);
    }

    public String getDurationTask() {
        return String.valueOf(durationTask.toMinutes());
    }

    public String getStartTime() {
        return startTime.format(DATE_TIME_FORMATTER);
    }

    public void setDuration(String durationTask) {
        this.durationTask = Duration.ofMinutes(Integer.parseInt(durationTask));
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Task task;
        try {
            task = (Task) o;
        } catch (ClassCastException err) {
            return false;
        }
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getId() + ",TASK," + getTask() + "," + getStatus() + "," + startTime.format(DATE_TIME_FORMATTER)
                + "," + durationTask.toMinutes() + "," + getEndTime().format(DATE_TIME_FORMATTER);
    }
}