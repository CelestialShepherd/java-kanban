import task.Epic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EpicTest {
    //Проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void shouldNotAddEpicAsItselfSubtask() {
        Epic epic1 = new Epic("Test task","Test description");
        epic1.setId(0);

        epic1.addToSubtasksIds(epic1.getId());

        assertNotEquals(1,
                epic1.getSubtasksIds().size(),
                "Экземпляр класса Epic возможно добавить " +
                        "в качестве собственной подзадачи");
    }
}