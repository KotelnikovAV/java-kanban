package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;
import service.Managers;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class HttpTaskServerTest {
    InMemoryTaskManager inMemoryTaskManager;
    static HttpServer httpServer;
    static final int PORT = 8080;
    static TaskManagerHandler taskManagerHandler;
    static Gson gson;
    Epic epic;

    @BeforeAll
    static void beforeAll() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        taskManagerHandler = new TaskManagerHandler();
        httpServer.createContext("/taskManager", taskManagerHandler);
        gson = new GsonBuilder().serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpServer.start();
    }

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getTaskManager();
        inMemoryTaskManager.createTask(Status.NEW, new Task("Погулять", "14:00 20.10.24",
                "30"));
        inMemoryTaskManager.createTask(Status.NEW, new Task("Поспать", "15:00 20.10.24",
                "30"));
        inMemoryTaskManager.createTask(Status.NEW, new Task("Пропылесосить", "12:00 20.10.24",
                "30"));
        epic = new Epic("Приготовить еду");
        inMemoryTaskManager.createEpic(Status.NEW, epic);
        inMemoryTaskManager.createSubtask(epic, Status.NEW, new Subtask(epic, "Купить продукты", "09:00 21.10.24", "60"));
        inMemoryTaskManager.createSubtask(epic, Status.NEW, new Subtask(epic, "Помыть продукты", "10:30 21.10.24", "20"));
        inMemoryTaskManager.createSubtask(epic, Status.NEW, new Subtask(epic, "Поставить все на огонь", "11:00 21.10.24", "30"));
        taskManagerHandler.setTaskManager(inMemoryTaskManager);

    }

    @AfterAll
    static void AfterAll() {
        httpServer.stop(0);
    }
}
