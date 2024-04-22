package server;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerDeleteMethodTest extends HttpTaskServerTest {

    static HttpResponse<String> getHttpResponse(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler);
    }

    @Test
    void checkDeleteAllTask() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks"));
        List<Task> tasks = inMemoryTaskManager.getListTasks();
        assertEquals(201, response.statusCode(), "Ошибка удаления списка задач");
        assertEquals(0, tasks.size(), "Ошибка удаления списка задач");
    }

    @Test
    void checkDeleteTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/tasks/1"));
        List<Task> tasks = inMemoryTaskManager.getListTasks();
        assertEquals(201, response.statusCode(), "Ошибка удаления задачи");
        assertEquals(2, tasks.size(), "Ошибка удаления задачи");
    }

    @Test
    void checkDeleteAllSubtask() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI
                .create("http://localhost:8080/taskManager/subtasks/all/?idEpic=4"));
        List<Subtask> subtasks = inMemoryTaskManager.getListSubtasks(epic).get();
        assertEquals(201, response.statusCode(), "Ошибка удаления списка подзадач");
        assertEquals(0, subtasks.size(), "Ошибка удаления списка подзадач");
    }

    @Test
    void checkDeleteSubtaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/subtasks/6"));
        List<Subtask> subtasks = inMemoryTaskManager.getListSubtasks(epic).get();
        assertEquals(201, response.statusCode(), "Ошибка удаления подзадачи");
        assertEquals(2, subtasks.size(), "Ошибка удаления подзадачи");
    }

    @Test
    void checkDeleteAllEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/epics"));
        Optional<List<Subtask>> optionalSubtasks = inMemoryTaskManager.getListSubtasks(epic);
        List<Epic> epics = inMemoryTaskManager.getListEpics();
        assertEquals(201, response.statusCode(), "Ошибка удаления списка задач");
        assertTrue(optionalSubtasks.isEmpty(), "Ошибка удаления списка эпиков");
        assertEquals(0, epics.size(), "Ошибка удаления списка эпиков");
    }

    @Test
    void checkDeleteEpicById() throws IOException, InterruptedException {
        HttpResponse<String> response = getHttpResponse(URI.create("http://localhost:8080/taskManager/epics/4"));
        Optional<List<Subtask>> optionalSubtasks = inMemoryTaskManager.getListSubtasks(epic);
        List<Epic> epics = inMemoryTaskManager.getListEpics();
        assertEquals(201, response.statusCode(), "Ошибка удаления списка задач");
        assertTrue(optionalSubtasks.isEmpty(), "Ошибка удаления списка эпиков");
        assertEquals(0, epics.size(), "Ошибка удаления списка эпиков");
    }
}
