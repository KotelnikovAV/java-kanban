package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> browsingHistory;

    public InMemoryHistoryManager() {
        browsingHistory = new ArrayList<>(10);
    }

    @Override
    public void add(Task task) {
        if (browsingHistory.size() == 10) {
            browsingHistory.remove(0);
        }
        browsingHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return browsingHistory;
    }
}