package dto;

import model.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class EpicDto extends TaskDto {

    public EpicDto(String description, int id, Status status, Duration duration, LocalDateTime startTime) {
        super(description, id, status, duration, startTime);
        this.typeOfTask = "Epic";
    }
}