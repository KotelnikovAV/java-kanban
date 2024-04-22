package server;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import org.junit.jupiter.api.*;
import util.ListTaskDtoTypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerGetMethodTest extends HttpTaskServerTest {

    static HttpResponse<String> getHttpResponse(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler);
    }

    @Test
    void checkGetTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks"));
        List<TaskDto> tasksDto = gson.fromJson(response.body(), new ListTaskDtoTypeToken().getType());
        assertEquals(200, response.statusCode(), "Ошибка получения списка задач");
        assertEquals(3, tasksDto.size(), "Ошибка получения списка задач");
    }

    @Test
    void checkGetTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks/2"));
        TaskDto taskDto = gson.fromJson(response.body(), TaskDto.class);
        assertEquals(200, response.statusCode(), "Ошибка получения задачи");
        assertEquals("Поспать", taskDto.getDescription(), "Ошибка получения задачи");
    }

    @Test
    void checkGetEpics() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/epics"));
        EpicDto epicDto = gson.fromJson(response.body(), EpicDto.class);
        assertEquals(200, response.statusCode(), "Ошибка получения списка эпиков");
        assertEquals("Приготовить еду", epicDto.getDescription(), "Ошибка получения списка эпиков");
    }

    @Test
    void checkGetEpicById() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/epics/4"));
        EpicDto epicDto = gson.fromJson(response.body(), EpicDto.class);
        assertEquals(200, response.statusCode(), "Ошибка получения эпика");
        assertEquals("Приготовить еду", epicDto.getDescription(), "Ошибка получения эпика");
    }

    @Test
    void checkGetSubtasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/subtasks/all/?idEpic=4"));
        List<TaskDto> subtasksDto = gson.fromJson(response.body(), new ListTaskDtoTypeToken().getType());
        assertEquals(200, response.statusCode(), "Ошибка получения списка подзадач");
        assertEquals(3, subtasksDto.size(), "Ошибка получения списка подзадач");
    }

    @Test
    void checkGetSubtasksById() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/subtasks/5"));
        SubtaskDto subtaskDto = gson.fromJson(response.body(), SubtaskDto.class);
        assertEquals(200, response.statusCode(), "Ошибка получения подзадачи");
        assertEquals("Купить продукты", subtaskDto.getDescription(), "Ошибка получения подзадачи");
    }

    @Test
    void checkGetNonexistentTask() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks/45"));
        assertEquals(404, response.statusCode(), "Код ошибки должен быть 404");
    }

    @Test
    void checkGetPrioritizationList() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/prioritized"));
        List<TaskDto> prioritizedTasks = gson.fromJson(response.body(), new ListTaskDtoTypeToken().getType());
        assertEquals(200, response.statusCode(), "Ошибка получения задач");
        assertEquals(6, prioritizedTasks.size(), "Ошибка получения задач");
    }

    @Test
    void checkGetHistoryList() throws IOException, InterruptedException {
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(3);
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/history"));
        List<TaskDto> prioritizedTasks = gson.fromJson(response.body(), new ListTaskDtoTypeToken().getType());
        assertEquals(200, response.statusCode(), "Ошибка получения задач");
        assertEquals(3, prioritizedTasks.size(), "Ошибка получения задач");
    }
}