package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String task) {
        super(task);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = new ArrayList<>(subtasks);
    }

    @Override
    public String toString() {
        return "Эпик: " + getTask() + ". Идентификационный номер эпика: " + getId() + ". Статус эпика: "
                + getStatus() + ".";
    }
}
