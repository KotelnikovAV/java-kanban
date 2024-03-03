package model;

public class Node<Task> {
    public Task task;
    public Node<Task> nextTask;
    public Node<Task> prevTask;

    public Node(Node<Task> prevTask, Task task, Node<Task> nextTask) {
        this.task = task;
        this.nextTask = nextTask;
        this.prevTask = prevTask;
    }
}
