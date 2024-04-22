package server;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import util.TaskMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerPostMethodTest extends HttpTaskServerTest {
    static String taskDto;

    static HttpResponse<String> getHttpResponse(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(taskDto))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler);
    }

    @Test
    void checkPostTask() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createTaskDto(new Task("Поиграть", "14:00 25.10.24",
                "30")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks"));
        assertEquals(201, response.statusCode(), "Ошибка публикации задачи");
        Task task = inMemoryTaskManager.getTaskById(8).get();
        assertEquals("Поиграть", task.getTask(), "Ошибка публикации задачи");
    }

    @Test
    void checkUpdateTask() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createTaskDto(new Task("Поиграть", "14:00 25.10.24",
                "30")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks/1"));
        assertEquals(201, response.statusCode(), "Ошибка обновления задачи");
        Task task = inMemoryTaskManager.getTaskById(1).get();
        assertEquals("Поиграть", task.getTask(), "Ошибка обновления задачи");
    }

    @Test
    void checkPostIntersectingTask() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createTaskDto(new Task("Поиграть", "14:00 20.10.24",
                "30")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks"));
        assertEquals(406, response.statusCode(), "Ошибка публикации задачи");
    }

    @Test
    void checkPostEpic() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createEpicDto(new Epic("Поиграть")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/epics"));
        assertEquals(201, response.statusCode(), "Ошибка публикации эпика");
        Epic epic = inMemoryTaskManager.getEpicById(8).get();
        assertEquals("Поиграть", epic.getTask(), "Ошибка публикации эпика");
    }

    @Test
    void checkUpdateEpic() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createEpicDto(new Epic("Поиграть")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/epics/4"));
        assertEquals(201, response.statusCode(), "Ошибка обновления эпика");
        Epic epic = inMemoryTaskManager.getEpicById(4).get();
        assertEquals("Поиграть", epic.getTask(), "Ошибка обновления эпика");
    }

    @Test
    void checkPostSubtask() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createSubtaskDto(new Subtask(epic,"Поиграть", "14:00 27.10.24",
                "30")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/subtasks"));
        assertEquals(201, response.statusCode(), "Ошибка публикации подзадачи");
        Subtask subtask = inMemoryTaskManager.getSubtaskById(8).get();
        assertEquals("Поиграть", subtask.getTask(), "Ошибка публикации подзадачи");
    }

    @Test
    void checkUpdateSubtask() throws IOException, InterruptedException {
        taskDto = gson.toJson(TaskMapper.createSubtaskDto(new Subtask(epic,"Поиграть", "14:00 27.10.24",
                "30")));
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/subtasks/6"));
        assertEquals(201, response.statusCode(), "Ошибка обновления подзадачи");
        Subtask subtask = inMemoryTaskManager.getSubtaskById(6).get();
        assertEquals("Поиграть", subtask.getTask(), "Ошибка обновления подзадачи");
    }
}
