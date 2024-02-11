package service;

import model.HistoryManager;
import model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> browsingHistory;
    // сделал реализацию через LinkedList, так как он лучше работает с добавлением элементов в начало списка

    public InMemoryHistoryManager() {
        browsingHistory = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        browsingHistory.addFirst(task);
        if (browsingHistory.size() > 10) {
            browsingHistory.remove(10);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return browsingHistory;
    }
}