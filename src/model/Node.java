package model;

public class Node {
    private Task task;
    private Node nextTask;
    private Node prevTask;

    public Node(Node prevTask, Task task, Node nextTask) {
        this.task = task;
        this.nextTask = nextTask;
        this.prevTask = prevTask;
    }

    public Node getNextTask() {
        return nextTask;
    }

    public void setNextTask(Node nextTask) {
        this.nextTask = nextTask;
    }

    public Node getPrevTask() {
        return prevTask;
    }

    public void setPrevTask(Node prevTask) {
        this.prevTask = prevTask;
    }

    public Task getTask() {
        return task;
    }
}