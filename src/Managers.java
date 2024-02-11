import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import service.InMemoryTaskManager;
import model.TaskManager;

public class Managers { // не уверен, что правильно понял задачу по созданию утилитарного класса Managers

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = getDefault();
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefault() {
        return getInMemoryTaskManager();
    }
}

class ManagersTest {

    @Test
    void shouldNotNull() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        assertNotNull(inMemoryTaskManager.getListEpics(), "Объект не был создан");
    }
}
