package dto;

import model.Epic;
import model.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskDto extends TaskDto {
    private String descriptionEpic;
    private int epicId;

    public SubtaskDto(String description, int id, Status status, Duration duration,
                      LocalDateTime startTime, Epic epic) {
        super(description, id, status, duration, startTime);
        this.typeOfTask = "Subtask";
        this.descriptionEpic = epic.getTask();
        this.epicId = epic.getId();
    }

    public int getEpicId() {
        return epicId;
    }
}