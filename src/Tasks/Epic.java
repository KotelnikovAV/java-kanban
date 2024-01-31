package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks;

    public Epic(String task) {
        super(task);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        System.out.println("Эпик: " + getTask() + ". Идентификационный номер эпика: " + getId() + ". Статус задачи: "
                + getStatus() + ". Список подзадач в данном эпике: ");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.getTask());
        }
        return  " ";
    }
}
