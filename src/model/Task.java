package model;

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
        return getId() + ",TASK," + getTask() + "," + getStatus();
    }
}
