package model;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(Epic epic, String task, String startTime, String durationTask) {
        super(task, startTime, durationTask);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return getId() + ",SUBTASK," + getTask() + "," + getStatus() + "," + startTime.format(DATE_TIME_FORMATTER)
                + "," + durationTask.toMinutes() + "," + getEndTime().format(DATE_TIME_FORMATTER) + "," + epic.getId();
    }
}