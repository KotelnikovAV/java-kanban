package model;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(Epic epic, String task) {
        super(task);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return getId() + ",SUBTASK," + getTask() + "," + getStatus() + "," + epic.getId();
    }
}