import task.Epic;
import task.Subtask;
import task.Task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    //Проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void shouldConfirmEqualityOfTaskInstancesWithEqualIdAndParameters() {
        Task task1 = new Task("Test task","Test description");
        task1.setId(0);
        Task task2 = new Task("Test task","Test description");
        task2.setId(task1.getId());

        assertEquals(task1, task2, "Экземпляры Task с одинаковыми id и параметрами не равны");
    }

    //Проверьте, что наследники класса Task равны друг другу, если равен их id;
    //Проверка Epic
    @Test
    public void shouldConfirmEqualityOfEpicInstancesWithEqualIdAndParameters() {
        Epic epic1 = new Epic("Test task","Test description");
        epic1.setId(0);
        Epic epic2 = new Epic("Test task","Test description");
        epic2.setId(epic1.getId());

        assertEquals(epic1, epic2, "Экземпляры Epic с одинаковыми id и параметрами не равны");
    }
    //Проверка Subtask
    @Test
    public void shouldConfirmEqualityOfSubtaskInstancesWithEqualIdAndParameters() {
        Subtask subtask1 = new Subtask(0, "Test task", "Test description");
        subtask1.setId(0);
        Subtask subtask2 = new Subtask(0,"Test task", "Test description");
        subtask2.setId(subtask1.getId());

        assertEquals(subtask1, subtask2, "Экземпляры Subtask с одинаковыми id и параметрами не равны");
    }
}