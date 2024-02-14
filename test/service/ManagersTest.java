package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldNotNullTaskManager() {
        assertNotNull(Managers.getTaskManager());
    }

    @Test
    void shouldNotNullHistoryManager() {
        assertNotNull(Managers.getHistoryManager());
    }
}