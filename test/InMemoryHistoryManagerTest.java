import management.HistoryManager;
import management.Managers;

import task.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO: Требуется проверить удаление из разных частей (начало, середина, конец)
//TODO: Требуется проверить порядок элементов в истории после удаления
class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    public void initializeFields() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Test task1","Task1 description");
        task1.setId(0);
        task2 = new Task("Test task2","Task2 description");
        task2.setId(1);
        task3 = new Task("Test task3","Task3 description");
        task3.setId(2);
    }

    //Проверка на получение истории
    @Test
    void shouldGetHistory() {
        assertNotNull(historyManager.getHistory(), "История не была возвращена");
    }

    //Проверка добавления задач в историю
    @Test
    void shouldAddTaskToHistory() {
        historyManager.add(task1);

        assertEquals(task1, historyManager.getHistory().getLast(), "Задача не была добавлена в историю");
    }

    //Проверка удаления из начала связного списка
    @Test
    void ShouldRemoveFirstTaskFromHistoryMap() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());

        assertFalse(historyManager.getHistory().contains(task1));
    }

    //Проверка удаления из середины связного списка
    @Test
    void ShouldRemoveMiddleTaskFromHistoryMap() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());

        assertFalse(historyManager.getHistory().contains(task2));
    }

    //Проверка удаления из конца связного списка
    @Test
    void ShouldRemoveLastTaskFromHistoryMap() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());

        assertFalse(historyManager.getHistory().contains(task3));
    }

    //Проверка, что история передвигает в конец уже существующую задачу, не увеличиваясь в размерах
    @Test
    void ShouldNotHistoryMapAddExistingTask() {
        System.out.println("Добавление задач:");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        System.out.println("Размер истории: " + historyManager.getHistory().size());
        assertEquals(2,
                historyManager.getHistory().size(),
                "История повторно вместила уже существующую задачу и увеличилась в размерах");
        assertEquals(task1,
                historyManager.getHistory().getLast(),
                "История не передвинула в конец уже существующую задачу при повторной попытке добавления");
    }
}