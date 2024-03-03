package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {


    private final CustomLinkedList<Task> listHistory;
    private final Map<Integer, Node<Task>> browsingHistory;

    public InMemoryHistoryManager() {
        listHistory = new CustomLinkedList<>();
        browsingHistory = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (browsingHistory.containsKey(task.getId()) && browsingHistory.get(task.getId()) != null) {
            removeNode(browsingHistory.get(task.getId()));
        }
        listHistory.linkLast(task);
        browsingHistory.put(task.getId(), listHistory.tail);
    }

    @Override
    public List<Task> getHistory() {
        return listHistory.getTasks();
    }

    @Override
    public void remove(int id) {
        if (browsingHistory.get(id) != null) {
            removeNode(browsingHistory.get(id));
            browsingHistory.remove(id);
        }
    }

    public void removeNode(Node<Task> node) {
        if (node.prevTask != null && node.nextTask != null) {
            node.prevTask.nextTask = node.nextTask;
            node.nextTask.prevTask = node.prevTask;
        } else if (node.prevTask == null) {
            node.nextTask.prevTask = null;
            listHistory.head = node.nextTask;
        } else {
            node.prevTask.nextTask = null;
            listHistory.tail = node.prevTask;
        }
    }

    class CustomLinkedList<Task> {
        private Node<Task> head;
        private Node<Task> tail;

        public void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.nextTask = newNode;
            }
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node<Task> taskNode = tail;
            while (taskNode != null) {
                tasks.add(taskNode.task);
                taskNode = taskNode.prevTask;

            }
            return tasks;
        }
    }
}