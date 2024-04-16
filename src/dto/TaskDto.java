package dto;

import model.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TaskDto {
    protected String typeOfTask;
    private final String description;
    private final int id;
    private final Status status;
    private final Duration duration;
    private final LocalDateTime startTime;

    public TaskDto(String description, int id, Status status, Duration duration, LocalDateTime startTime) {
        this.typeOfTask = "Task";
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return id == taskDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}