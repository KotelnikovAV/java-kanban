package server;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.EpicDto;
import dto.SubtaskDto;
import service.TaskManager;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;
import util.TaskMapper;
import dto.TaskDto;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskManagerHandler implements HttpHandler {
    private static final int INDEX_OF_EPIC_ID = 1;
    private static final int INDEX_OF_TYPE = 2;
    private static final int INDEX_OF_ID = 3;
    private static final int INDEX_OF_REQUEST_PARAMETERS = 4;

    private TaskManager taskManager;
    Gson gson = new GsonBuilder().serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private int rCode;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "POST":
                postTasks(httpExchange);
                break;
            case "GET":
                List<TaskDto> getResult = getTasks(httpExchange);
                response = formingResponseBody(getResult);
                break;
            case "DELETE":
                deleteTasks(httpExchange);
                break;
            default:
                rCode = 404;
        }

        httpExchange.sendResponseHeaders(rCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public String formingResponseBody(List<TaskDto> getResult) {
        if (getResult == null) {
            return "";
        }

        if (getResult.size() == 1) {
            return gson.toJson(getResult.getFirst());
        } else {
            return gson.toJson(getResult);
        }
    }

    public void postTasks(HttpExchange httpExchange) throws IOException {
        int id = 0;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String[] uri = httpExchange.getRequestURI().getPath().split("/");
        String typeOfTask = uri[INDEX_OF_TYPE];

        if (uri.length == 4) {
            String stringId = uri[INDEX_OF_ID];
            try {
                id = Integer.parseInt(stringId);
            } catch (NumberFormatException e) {
                rCode = 404;
                return;
            }
        }

        switch (typeOfTask) {
            case "tasks":
                TaskDto taskDto = gson.fromJson(body, TaskDto.class);
                Task task = TaskMapper.createTask(taskDto);
                if (id == 0) {
                    taskManager.createTask(Status.NEW, task);
                } else {
                    taskManager.updateTask(id, task);
                }

                if (taskManager.getCompletionStatus()) {
                    rCode = 201;
                } else {
                    rCode = 406;
                }
                break;
            case "subtasks":
                SubtaskDto subtaskDto = gson.fromJson(body, SubtaskDto.class);
                Subtask subtask = TaskMapper.createSubtask(subtaskDto, taskManager.getEpic(subtaskDto.getEpicId()));
                if (id == 0) {
                    taskManager.createSubtask(subtask.getEpic(), Status.NEW, subtask);
                } else {
                    taskManager.updateSubtask(id, subtask);
                }

                if (taskManager.getCompletionStatus()) {
                    rCode = 201;
                } else {
                    rCode = 406;
                }
                break;
            case "epics":
                EpicDto epicDto = gson.fromJson(body, EpicDto.class);
                Epic epic = TaskMapper.createEpic(epicDto);
                if (id == 0) {
                    taskManager.createEpic(Status.NEW, epic);
                } else {
                    taskManager.updateEpic(id, epic);
                }

                if (taskManager.getCompletionStatus()) {
                    rCode = 201;
                } else {
                    rCode = 406;
                }
                break;
            default:
                rCode = 404;
        }
    }

    public void deleteTasks(HttpExchange httpExchange) {
        int id = 0;
        int idEpic = 0;
        String[] uri = httpExchange.getRequestURI().getPath().split("/");
        String typeOfTask = uri[INDEX_OF_TYPE];

        if (uri.length == 4) {
            String stringId = uri[INDEX_OF_ID];
            try {
                id = Integer.parseInt(stringId);
            } catch (NumberFormatException e) {
                id = -1;
                String fullPath = httpExchange.getRequestURI().toString().split("/")[INDEX_OF_REQUEST_PARAMETERS];
                idEpic = Integer.parseInt(fullPath.split("=")[INDEX_OF_EPIC_ID]);
            }
        }

        switch (typeOfTask) {
            case "tasks":
                if (id > 0) {
                    taskManager.removeTaskById(id);
                } else if (id == 0) {
                    taskManager.removeAllTasks();
                } else {
                    rCode = 404;
                    break;
                }

                if (taskManager.getCompletionStatus()) {
                    rCode = 201;
                } else {
                    rCode = 404;
                }
                break;
            case "subtasks":
                if (id > 0) {
                    taskManager.removeSubtaskById(id);
                } else {
                    Epic epic = taskManager.getEpic(idEpic);

                    if (epic != null) {
                        taskManager.removeAllSubtasks(epic);
                    } else {
                        rCode = 404;
                        break;
                    }
                }

                if (taskManager.getCompletionStatus()) {
                    rCode = 201;
                } else {
                    rCode = 404;
                }
                break;
            case "epics":
                if (id > 0) {
                    taskManager.removeEpicById(id);
                } else if (id == 0) {
                    taskManager.removeAllEpics();
                } else {
                    rCode = 404;
                    break;
                }

                if (taskManager.getCompletionStatus()) {
                    rCode = 201;
                } else {
                    rCode = 404;
                }
                break;
            default:
                rCode = 404;
        }
    }

    public List<TaskDto> getTasks(HttpExchange httpExchange) {
        int id = 0;
        int idEpic = 0;
        String[] uri = httpExchange.getRequestURI().getPath().split("/");
        String typeOfTask = uri[INDEX_OF_TYPE];
        String stringId = "";

        if (uri.length == 4) {
            stringId = uri[INDEX_OF_ID];
            try {
                id = Integer.parseInt(stringId);
            } catch (NumberFormatException e) {
                id = -1;
                String fullPath = httpExchange.getRequestURI().toString().split("/")[INDEX_OF_REQUEST_PARAMETERS];
                idEpic = Integer.parseInt(fullPath.split("=")[INDEX_OF_EPIC_ID]);
            }
        }
        switch (typeOfTask) {
            case "tasks":
                if (stringId.isBlank()) {
                    rCode = 200;
                    return TaskMapper.createListTasksDto(taskManager.getListTasks());
                } else {
                    Optional<Task> task = taskManager.getTaskById(id);

                    if (task.isPresent()) {
                        rCode = 200;
                        return List.of(TaskMapper.createTaskDto(task.get()));
                    } else {
                        rCode = 404;
                        return null;
                    }
                }
            case "subtasks":
                if (id < 0) {
                    Epic epic = taskManager.getEpic(idEpic);
                    Optional<List<Subtask>> subtasks = taskManager.getListSubtasks(epic);

                    if (subtasks.isPresent()) {
                        rCode = 200;
                        return TaskMapper.createListTasksDto(subtasks.get());
                    } else {
                        rCode = 404;
                        return null;
                    }

                } else {
                    Optional<Subtask> subtask = taskManager.getSubtaskById(id);

                    if (subtask.isPresent()) {
                        rCode = 200;
                        return List.of(TaskMapper.createSubtaskDto(subtask.get()));
                    } else {
                        rCode = 404;
                        return null;
                    }
                }
            case "epics":
                if (stringId.isBlank()) {
                    rCode = 200;
                    return TaskMapper.createListTasksDto(taskManager.getListEpics());
                } else {
                    Optional<Epic> epic = taskManager.getEpicById(id);

                    if (epic.isPresent()) {
                        rCode = 200;
                        return List.of(TaskMapper.createEpicDto(epic.get()));
                    } else {
                        rCode = 404;
                        return null;
                    }
                }
            case "history":
                rCode = 200;
                return TaskMapper.createListTasksDto(taskManager.getHistory());
            case "prioritized":
                rCode = 200;
                return TaskMapper.createListTasksDto(taskManager.getPrioritizedTasks());
            default:
                rCode = 404;
                return null;
        }
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
}