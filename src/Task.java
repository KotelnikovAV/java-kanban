import java.util.Objects;

public class Task {
    private final String task;
    private int id;
    private Status status;

    public Task(String task) {
        this.task = task;
        id = (int) (Math.random() * 1_000_000);
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
}
