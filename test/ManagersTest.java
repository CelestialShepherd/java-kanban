import management.HistoryManager;
import management.Managers;
import management.TaskManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void shouldReturnInitializedTaskManagerImplementation() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "Возвращен пустой менеджер задач");
    }

    @Test
    public void shouldReturnInitializedHistoryManagerImplementation() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Возвращен пустой менеджер истории");
    }
}