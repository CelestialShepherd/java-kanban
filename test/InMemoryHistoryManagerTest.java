import management.HistoryManager;
import management.Managers;

import task.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    public void initializeFields() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("Test task","Task description");
    }

    //Проверка на получение истории
    @Test
    void shouldGetHistory() {
        assertNotNull(historyManager.getHistory(), "История не была возвращена");
    }

    //Проверка добавления задач в историю
    @Test
    void shouldAddTaskToHistory() {
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "Задача не была добавлена в историю");
    }

    //Проверка, что история вмещает только 10 задач
    @Test
    void shouldHistoryStoreOnly10Tasks() {
        final int HISTORY_SIZE = 10;
        System.out.println("Добавление задач:");
        for (int i = 1; i <= HISTORY_SIZE + 1; i++) {
            System.out.println(i + ".Добавлена задача: " + task);
            historyManager.add(task);
        }
        System.out.println("Размер истории: " + historyManager.getHistory().size());

        assertEquals(HISTORY_SIZE,
                historyManager.getHistory().size(),
                "История вместила более " + HISTORY_SIZE + " задач");
    }
}