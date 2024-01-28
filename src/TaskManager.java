import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TaskManager {
    private HashMap<Integer, ArrayList<Integer>> linkEpicWithSubtask;
    private ArrayList<HashMap<Integer, Task>> listTasks;
    private ArrayList<HashMap<Integer, Epic>> listEpics;
    private ArrayList<HashMap<Integer, Subtask>> listSubtasks;

    public TaskManager() {
        linkEpicWithSubtask = new HashMap<>();
        listTasks = new ArrayList<>();
        listEpics = new ArrayList<>();
        listSubtasks = new ArrayList<>();
    }

    public void createTask(Status status, Task task) {
        for (HashMap<Integer, Task> taskWithId : listTasks) {
            if (taskWithId.containsKey(task.getId())) {
                return;
            }
        }
        HashMap<Integer, Task> taskWithId = new HashMap<>();
        task.setStatus(status);
        taskWithId.put(task.getId(), task);
        listTasks.add(taskWithId);
    }

    public void createEpic(Status status, Epic epic) {
        for (HashMap<Integer, Epic> epicWithId : listEpics) {
            if (epicWithId.containsKey(epic.getId())) {
                return;
            }
        }
        HashMap<Integer, Epic> epicWithId = new HashMap<>();
        epic.setStatus(status);
        epicWithId.put(epic.getId(), epic);
        listEpics.add(epicWithId);
        linkEpicWithSubtask.put(epic.getId(), new ArrayList<>());
    }

    public void createSubtask(Epic epic, Status status, Subtask subtask) {
        for (HashMap<Integer, Subtask> subtaskWithId : listSubtasks) {
            if (subtaskWithId.containsKey(subtask.getId())) {
                return;
            }
        }
        HashMap<Integer, Subtask> subtaskWithId = new HashMap<>();
        int id = epic.getId();
        subtask.setStatus(status);
        subtaskWithId.put(subtask.getId(), subtask);
        listSubtasks.add(subtaskWithId);
        for (int key : linkEpicWithSubtask.keySet()) {
            if (key == id) {
                ArrayList<Integer> idSubtask = linkEpicWithSubtask.get(key);
                idSubtask.add(subtask.getId());
            }
        }
    }

    public ArrayList<Task> getListTasks() {
        ArrayList<Task> listTasks = new ArrayList<>();
        for (HashMap<Integer, Task> taskWithId : this.listTasks) {
            for (int id : taskWithId.keySet()) {
                Task task = taskWithId.get(id);
                listTasks.add(task);
            }
        }
        return listTasks;
    }

    public ArrayList<Epic> getListEpics() {
        ArrayList<Epic> listEpics = new ArrayList<>();
        for (HashMap<Integer, Epic> epicWithId : this.listEpics) {
            for (int id : epicWithId.keySet()) {
                Epic epic = epicWithId.get(id);
                listEpics.add(epic);
            }
        }
        return listEpics;
    }

    public ArrayList<Subtask> getListSubtasks(Epic epic) {
        int idEpic = epic.getId();

        if (linkEpicWithSubtask.containsKey(idEpic)) {
            ArrayList<Subtask> listSubtasks = new ArrayList<>();
            ArrayList<HashMap<Integer, Subtask>> listSubtasksWithId = new ArrayList<>();
            ArrayList<Integer> listIdSubtasks = linkEpicWithSubtask.get(idEpic);
            for (HashMap<Integer, Subtask> subtaskWithId : this.listSubtasks) {
                for (int idSubtask : listIdSubtasks) {
                    if (subtaskWithId.containsKey(idSubtask)) {
                        listSubtasksWithId.add(subtaskWithId);
                    }
                }
            }
            for (HashMap<Integer, Subtask> subtaskWithId : listSubtasksWithId) {
                for (int id : subtaskWithId.keySet()) {
                    Subtask subtask = subtaskWithId.get(id);
                    listSubtasks.add(subtask);
                }
            }
            return listSubtasks;
        } else {
            return null;
        }
    }

    public void removeAllTasks() {
        listTasks.clear();
    }

    public void removeAllEpics() {
        listEpics.clear();
        listSubtasks.clear();
    }

    public void removeAllSubtasks(Epic epic) {
        int idEpic = epic.getId();
        Iterator<HashMap<Integer, Subtask>> subtaskIterator = listSubtasks.iterator();
        ArrayList<Integer> listIdSubtasks = linkEpicWithSubtask.get(idEpic);

        while (subtaskIterator.hasNext()) {
            HashMap<Integer, Subtask> subtaskWithId = subtaskIterator.next();
            for (Integer idSubtask : listIdSubtasks) {
                if (subtaskWithId.containsKey(idSubtask)) {
                    subtaskIterator.remove();
                }
            }
        }
    }

    public Task getTaskById(int id) {
        for (HashMap<Integer, Task> taskWithId : listTasks) {
            for (Integer idTask : taskWithId.keySet()) {
                if (taskWithId.containsKey(id)) {
                    return taskWithId.get(idTask);
                }
            }
        }
        return null;
    }

    public Epic getEpicById(int id) {
        for (HashMap<Integer, Epic> epicWithId : listEpics) {
            for (Integer idTask : epicWithId.keySet()) {
                if (epicWithId.containsKey(id)) {
                    return epicWithId.get(idTask);
                }
            }
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        for (HashMap<Integer, Subtask> subtaskWithId : listSubtasks) {
            for (Integer idTask : subtaskWithId.keySet()) {
                if (subtaskWithId.containsKey(id)) {
                    return subtaskWithId.get(idTask);
                }
            }
        }
        return null;
    }

    public void updateTask(int id, Task task) {
        for (HashMap<Integer, Task> taskWithId : listTasks) {
            for (int idTask : taskWithId.keySet()) {
                if (id == idTask) {
                    Task oldTask = taskWithId.get(id);
                    taskWithId.put(id, task);
                    task.setId(id);
                    task.setStatus(oldTask.getStatus());
                }
            }
        }
    }

    public void updateEpic(int id, Epic epic) {
        for (HashMap<Integer, Epic> epicWithId : listEpics) {
            for (int idEpic : epicWithId.keySet()) {
                if (id == idEpic) {
                    Epic oldEpic = epicWithId.get(id);
                    epicWithId.put(id, epic);
                    epic.setId(id);
                    epic.setStatus(oldEpic.getStatus());
                }
            }
        }
    }

    public void updateSubtask(int id, Subtask subtask) {
        for (HashMap<Integer, Subtask> subtaskWithId : listSubtasks) {
            for (int idSubtask : subtaskWithId.keySet()) {
                if (id == idSubtask) {
                    Subtask oldSubtask = subtaskWithId.get(id);
                    subtaskWithId.put(id, subtask);
                    subtask.setId(id);
                    subtask.setStatus(oldSubtask.getStatus());
                }
            }
        }
    }

    public void removeTaskById(int id) {
        listTasks.removeIf(taskWithId -> taskWithId.containsKey(id));
    }

    public void removeEpicById(int id) {
        if (linkEpicWithSubtask.containsKey(id)) {
            listEpics.removeIf(epicWithId -> epicWithId.containsKey(id));
            ArrayList<Integer> idSubtasks = linkEpicWithSubtask.get(id);
            for (int idSubtask : idSubtasks) {
                listSubtasks.removeIf(subtaskWithId -> subtaskWithId.containsKey(idSubtask));
            }
        }
        linkEpicWithSubtask.remove(id);
    }

    public void removeSubtaskById(int id) {
        listSubtasks.removeIf(subtaskWithId -> subtaskWithId.containsKey(id));
        for (int idEpic : linkEpicWithSubtask.keySet()) {
            ArrayList<Integer> idSubtask = linkEpicWithSubtask.get(idEpic);
            idSubtask.removeIf(subtaskId -> subtaskId.equals(id));
        }
    }

    public void changeStatusTask(Status status, Task task) {
        task.setStatus(status);
    }

    public void changeStatusSubtask(Status newStatus, Subtask task) {
        int counterNEW = 0;
        int counterDONE = 0;
        int idEpic = 0;
        Epic epic = null;

        task.setStatus(newStatus);
        for (int id : linkEpicWithSubtask.keySet()) {
            ArrayList<Integer> listIdSubtask = linkEpicWithSubtask.get(id);
            if (listIdSubtask.contains(task.getId())) {
                idEpic = id;
            }
        }
        ArrayList<Integer> listIdSubtask = linkEpicWithSubtask.get(idEpic);
        for (HashMap<Integer, Subtask> subtaskWithId : listSubtasks) {
            for (int idSubtask : subtaskWithId.keySet()) {
                if (listIdSubtask.contains(idSubtask)) {
                    Subtask subtask = subtaskWithId.get(idSubtask);
                    if (subtask.getStatus().equals(Status.NEW)) {
                        counterNEW++;
                    } else if (subtask.getStatus().equals(Status.DONE)) {
                        counterDONE++;
                    }
                }
            }
        }
        for (HashMap<Integer, Epic> epicWithId : listEpics) {
            if (epicWithId.containsKey(idEpic)) {
                epic = epicWithId.get(idEpic);
            }
        }
        if (counterNEW == listIdSubtask.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDONE == listIdSubtask.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRES);
        }
    }

    public void changeStatusEpic(Status status, Epic epic) {
        ArrayList<Integer> listIdSubtasks = linkEpicWithSubtask.get(epic.getId());
        ArrayList<Subtask> listSubtask = new ArrayList<>();

        epic.setStatus(status);
        for (HashMap<Integer, Subtask> subtaskWithId : listSubtasks) {
            for (int idSubtask : subtaskWithId.keySet()) {
                if (listIdSubtasks.contains(idSubtask)) {
                    listSubtask.add(subtaskWithId.get(idSubtask));
                }
            }
        }
        for (Subtask subtask : listSubtask) {
            if (epic.getStatus().equals(Status.DONE)) {
                subtask.setStatus(Status.DONE);
            } else if (epic.getStatus().equals(Status.NEW)) {
                subtask.setStatus(Status.NEW);
            }
        }
    }
}
