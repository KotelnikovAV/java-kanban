package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> browsingHistory;
    private ArrayList<Task> copyBrowsingHistory;
    private static final int SIZE_OF_HISTORY = 10;

    public InMemoryHistoryManager() {
        browsingHistory = new ArrayList<>(SIZE_OF_HISTORY);
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
        copyBrowsingHistory = new ArrayList<>(browsingHistory);
        return copyBrowsingHistory;
    }
}