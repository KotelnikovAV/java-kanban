package Tasks;

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
        return "Подзадача: " + getTask() + ". Идентификационный номер подзадачи: " + getId() + ". Статус подзадачи: "
                + getStatus() + ". Данная подзадача входит в эпик: " + epic.getTask() + ".";
    }
}
