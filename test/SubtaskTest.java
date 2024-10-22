import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.Test;
import task.Subtask;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubtaskTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    public void shouldNotAddSubtaskAsItselfEpic() {
        Subtask subtask = new Subtask(1, "Test task", "Test description");
        taskManager.createSubtask(subtask);

        assertNotEquals(1,
                taskManager.getAllSubtasksList().size(),
                "Экземпляр класса Subtask возможно добавить " +
                "в качестве собственного эпика");
    }
}