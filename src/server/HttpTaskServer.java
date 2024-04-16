package server;

import com.sun.net.httpserver.HttpServer;
import service.FileBackedTaskManager;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager inMemoryTaskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager();
        inMemoryTaskManager.loadFromFile();
        final HttpServer httpServer = HttpServer.create();
        final int PORT = 8080;
        httpServer.bind(new InetSocketAddress(PORT), 0);
        TaskManagerHandler taskManagerHandler = new TaskManagerHandler();
        taskManagerHandler.setTaskManager(inMemoryTaskManager);
        httpServer.createContext("/taskManager", taskManagerHandler);
        httpServer.start();
    }
}