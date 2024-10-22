import management.HistoryManager;
import management.Managers;
import management.TaskManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void shouldReturnInitializedManagerImplementations() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager, "Возвращен пустой менеджер задач");
        assertNotNull(historyManager, "Возвращен пустой менеджер истории");
    }
}