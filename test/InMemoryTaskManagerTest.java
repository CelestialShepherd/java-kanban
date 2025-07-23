import management.Managers;
import management.TaskManager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void initializeFields() {
        taskManager = Managers.getDefault();
        task = new Task("Test task","Task description");
        taskManager.createTask(task);
        epic = new Epic("Test epic","Epic description");
        taskManager.createEpic(epic);
        subtask = new Subtask(epic.getId(),"Test subtask","Subtask description");
        taskManager.createSubtask(subtask);
    }

    //Проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id*
    //* - данные тесты содержат две проверки, т.к. вторая проверка зависит от уже созданного объекта для первой проверки
    //Проверка Task
    @Test
    void shouldCreateTaskAndGetItById() {
        assertNotEquals(0,
                taskManager.getAllTasksList().size(),
                "Задача типа Task не была создана");
        assertNotNull(taskManager.getTaskById(task.getId()), "Задача типа Task не была возвращена");
    }
    @Test
    void shouldCreateEpicAndGetItById() {
        assertNotEquals(0,
                taskManager.getAllEpicsList().size(),
                "Задача типа Epic не была создана");
        assertNotNull(taskManager.getEpicById(epic.getId()), "Задача типа Epic не была возвращена");
    }
    @Test
    void shouldCreateSubtaskAndGetItById() {
        assertNotEquals(0,
                taskManager.getAllSubtasksList().size(),
                "Задача типа Subtask не была создана");
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Задача типа Subtask не была возвращена");
    }

    //Проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    void shouldNotTwoTasksWithEqualIdCauseErrorInsideManager() {
        //Задача со сгенерированным id уже была создана перед запуском теста
        //Получаем сгенерированный id
        int taskGeneratedId = task.getId();
        //Задача с заданными id
        Task task2 = new Task("Test task2","Task2 description");
        task2.setId(taskGeneratedId);
        taskManager.createTask(task2);

        assertNotNull(taskManager.getTaskById(taskGeneratedId), "Не удалось вернуть задачу со сгенерированным id, " +
                        "произошел конфликт внутри менеджера");
        assertNotEquals(task2, taskManager.getTaskById(taskGeneratedId), "Произошла потеря объекта со сгенерированным id " +
                        "его заменил объект с заданным id");
    }

    //Создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    //Ниже представлена группа тестов с проверками на каждое поле
    @Test
    void shouldNotChangeTaskIdInsideManagerIfTaskTemplateIdIsChanged() {
        //Изменяем id шаблона
        task.setId(2);
        assertNotEquals(task,
                taskManager.getAllTasksList().getFirst(),
                "Задача в менеджере стала равна шаблону после изменения id шаблона");
    }
    @Test
    void shouldNotChangeTaskNameInsideManagerIfTaskTemplateNameIsChanged() {
        //Изменяем имя шаблона
        task.setName("New name");
        assertNotEquals(task,
                taskManager.getTaskById(task.getId()),
                "Задача в менеджере стала равна шаблону после изменения имени шаблона");
    }
    @Test
    void shouldNotChangeTaskDescriptionInsideManagerIfTaskTemplateDescriptionIsChanged() {
        //Изменяем описание шаблона
        task.setDescription("New description");
        assertNotEquals(task,
                taskManager.getTaskById(task.getId()),
                "Задача в менеджере стала равна шаблону после изменения описания шаблона");
    }
    @Test
    void shouldNotChangeTaskStatusInsideManagerIfTaskTemplateStatusIsChanged() {
        //Изменяем статус шаблона
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        assertNotEquals(task,
                taskManager.getTaskById(task.getId()),
                "Задача в менеджере стала равна шаблону после изменения статуса шаблона");
    }

    //Группа тестов на проверку работоспособности обновления задач
    @Test
    void shouldUpdateTask() {
        task.setName("Test task2");
        taskManager.updateTask(task);

        assertEquals(task.getName(),
                taskManager.getTaskById(task.getId()).getName(),
                "Задача не обновилась в менеджере");
    }
    @Test
    void shouldUpdateEpic() {
        epic.setName("Test epic2");
        taskManager.updateEpic(epic);

        assertEquals(epic.getName(),
                taskManager.getEpicById(epic.getId()).getName(),
                "Эпик не обновился в менеджере");
    }
    @Test
    void shouldUpdateSubtask() {
        subtask.setName("Test subtask2");
        taskManager.updateSubtask(subtask);

        assertEquals(subtask.getName(),
                taskManager.getSubtaskById(subtask.getId()).getName(),
                "Подзадача не обновилась в менеджере");
    }

    //Группа тестов на проверку работоспособности удаления задач
    @Test
    void shouldRemoveTask() {
        taskManager.removeTaskById(task.getId());

        assertFalse(taskManager.getAllTasksList().contains(task));
    }
    @Test
    void shouldRemoveEpic() {
        taskManager.removeEpicById(epic.getId());

        assertFalse(taskManager.getAllEpicsList().contains(epic));
    }
    @Test
    void shouldRemoveSubtask() {
        taskManager.removeSubtaskById(subtask.getId());

        assertFalse(taskManager.getAllSubtasksList().contains(subtask));
    }
    //Внутри эпиков не должно оставаться неактуальных id подзадач
    @Test
    void shouldRemoveSubtasksFromEpicSubtaskIds() {
        epic.addToSubtasksIds(subtask.getId());
        taskManager.removeSubtaskById(subtask.getId());

        assertFalse(taskManager.getEpicById(epic.getId()).getSubtasksIds().contains(subtask.getId()));
    }

    //Убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void shouldNotHistoryManagerUpdatePreviousVersionsOfTask() {
        //Задача была создана до исполнения теста
        //Вызываем задачу после создания для занесения в историю
        taskManager.getTaskById(task.getId());
        //Вносим изменения в задачу
        task.setName("Test task2");
        taskManager.updateTask(task);
        //Вызываем задачу после изменения для занесения в историю после изменения
        taskManager.getTaskById(task.getId());
        //Печатаем историю
        List<Task> history = taskManager.getHistory();
        System.out.println("История:");
        for(var task : history) {
            System.out.println(task);
        }
        System.out.println("\r\nПредпоследняя запись: " + history.get(history.size() - 2));
        System.out.println("Последняя запись:     " + history.getLast());

        assertNotEquals(history.get(history.size() - 2),
                history.getLast(),
                "В истории хранятся текущие версии задач вместо предыдущих");
    }
}