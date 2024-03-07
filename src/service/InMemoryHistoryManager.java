package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList listHistory;
    private final Map<Integer, Node> browsingHistory;

    public InMemoryHistoryManager() {
        listHistory = new CustomLinkedList();
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

    public void removeNode(Node node) {
        if (node.getPrevTask() != null && node.getNextTask() != null) {
            node.getPrevTask().setNextTask(node.getNextTask());
            node.getNextTask().setPrevTask(node.getPrevTask());
        } else if (node.getPrevTask() == null) {
            node.getNextTask().setPrevTask(null);
            listHistory.head = node.getNextTask();
        } else {
            node.getPrevTask().setNextTask(null);
            listHistory.tail = node.getPrevTask();
        }
    }

    private class CustomLinkedList {
        private Node head;
        private Node tail;

        public void linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNextTask(newNode);
            }
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node taskNode = tail;
            while (taskNode != null) {
                tasks.add(taskNode.getTask());
                taskNode = taskNode.getPrevTask();

            }
            return tasks;
        }
    }
}