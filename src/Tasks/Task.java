package Tasks;

import Service.Status;
import java.util.Objects;

public class Task {
    private String task;
    private int id;
    private Status status;

    public Task(String task) {
        this.task = task;
        id = 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task1 = (Task) o;
        return id == task1.id && Objects.equals(task, task1.task);
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
        return "Задача: " + getTask() + ". Идентификационный номер задачи: " + getId() + ". Статус задачи: "
                + getStatus() + ".";
    }
}
