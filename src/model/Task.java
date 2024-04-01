package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String task;
    private int id;
    private Status status;
    protected Duration durationTask;
    protected LocalDateTime startTime;
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public Task(String task, String startTime, String durationTask) {
        this.task = task;
        id = 0;
        this.durationTask = Duration.ofMinutes(Integer.parseInt(durationTask));
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
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
        int hash = 17;
        if (task != null) {
            hash = hash + task.hashCode();
        }
        hash = hash * 31;
        if (id != 0) {
            hash = hash + id;
        }
        return hash;
    }

    @Override
    public String toString() {
        return getId() + ",TASK," + getTask() + "," + getStatus() + "," + startTime.format(DATE_TIME_FORMATTER)
                + "," + durationTask.toMinutes();
    }
}