package model;

public class Node<T> {
    public Task task;
    public Node<T> nextTask;
    public Node<T> prevTask;

    public Node(Node<T> prevTask, Task task, Node<T> nextTask) {
        this.task = task;
        this.nextTask = nextTask;
        this.prevTask = prevTask;
    }
}
