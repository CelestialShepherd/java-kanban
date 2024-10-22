import management.Managers;
import management.TaskManager;

import task.Epic;
import task.Subtask;
import task.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void initializeManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldCreateTaskAndGetItById() {
        taskManager.createTask(new Task("Test task","Task description"));
        assertNotEquals(0,
                taskManager.getAllTasksList().size(),
                "Задача типа Task не была создана");
        assertNotNull(taskManager.getTaskById(1), "Задача типа Task не была возвращена");
    }

    @Test
    void shouldCreateEpicAndGetItById() {
        taskManager.createEpic(new Epic("Test epic","Epic description"));
        assertNotEquals(0,
                taskManager.getAllEpicsList().size(),
                "Задача типа Epic не была создана");
        assertNotNull(taskManager.getEpicById(1), "Задача типа Epic не была возвращена");
    }

    @Test
    void shouldCreateSubtaskAndGetItById() {
        taskManager.createEpic(new Epic("Test epic","Epic description"));
        taskManager.createSubtask(new Subtask(1,"Test subtask","Subtask description"));
        assertNotEquals(0,
                taskManager.getAllSubtasksList().size(),
                "Задача типа Subtask не была создана");
        assertNotNull(taskManager.getSubtaskById(2), "Задача типа Subtask не была возвращена");
    }

    @Test
    void shouldNotTwoTasksWithEqualIdCauseErrorInsideManager() {
        //Задача со сгенерированным id
        Task task1 = new Task("Test task1","Task1 description");
        taskManager.createTask(task1);
        //Задача с заданными id
        Task task2 = new Task("Test task2","Task2 description");
        task2.setId(1);
        taskManager.createTask(task2);

        assertNotNull(taskManager.getTaskById(1), "Не удалось вернуть задачу со сгенерированным id, " +
                        "произошел конфликт внутри менеджера");
        assertEquals(task1, taskManager.getTaskById(1), "Произошла потеря объекта с заданным id " +
                        "его заменил объект с заданным id");
    }

    @Test
    void shouldNotChangeTaskInsideManagerIfTaskTemplateIsChanged() {
        Task taskTemplate = new Task("Test task","Task description");
        taskManager.createTask(taskTemplate);
        //Создание резервной копии шаблона задачи
        Task taskTemplateArchive = new Task(taskTemplate.getName(),
                taskTemplate.getDescription(),
                taskTemplate.getTaskStatus());
        taskTemplateArchive.setId(1);
        //Стартовая архива проверка для легитимности последующих проверок
        assertEquals(taskTemplateArchive,
                taskManager.getTaskById(1),
                "Архив шаблона задачи и задача в менеджере не равны");

        //Изменяем id шаблона
        taskTemplate.setId(2);
        assertNotEquals(taskTemplate,
                taskManager.getAllTasksList().getFirst(),
                "Задача в менеджере стала равна шаблону после изменения id шаблона");

        //Возвращаем шаблонное значение
        taskTemplate = returnToTaskTemplateTask(taskTemplateArchive);
        //Изменяем имя шаблона
        taskTemplate.setName("New name");
        assertNotEquals(taskTemplate,
                taskManager.getTaskById(1),
                "Задача в менеджере стала равна шаблону после изменения имени шаблона");

        //Возвращаем шаблонное значение
        taskTemplate = returnToTaskTemplateTask(taskTemplateArchive);
        //Изменяем описание шаблона
        taskTemplate.setDescription("New description");
        assertNotEquals(taskTemplate,
                taskManager.getTaskById(1),
                "Задача в менеджере стала равна шаблону после изменения описания шаблона");

        //Возвращаем шаблонное значение
        taskTemplate = returnToTaskTemplateTask(taskTemplateArchive);
        //Изменяем статус шаблона
        taskTemplate.setTaskStatus(TaskStatus.IN_PROGRESS);
        assertNotEquals(taskTemplate,
                taskManager.getTaskById(1),
                "Задача в менеджере стала равна шаблону после изменения статуса шаблона");
    }
    private Task returnToTaskTemplateTask(Task taskTemplate) {
        Task task = new Task(taskTemplate.getName(), taskTemplate.getDescription(), taskTemplate.getTaskStatus());
        task.setId(taskTemplate.getId());

        return task;
    }

    @Test
    void shouldNotHistoryManagerUpdatePreviousVersionsOfTask() {
        //Создаем задачу
        Task task = new Task("Test1","Test description");
        taskManager.createTask(task);
        //Вызываем задачу после создания для занесения в историю
        taskManager.getTaskById(task.getId());
        //Вносим изменения в задачу
        task.setName("Test2");
        taskManager.updateTask(task);
        //Вызываем задачу после изменения для занесения в историю
        taskManager.getTaskById(task.getId());
        //Печатаем историю
        System.out.println("История:\r\n" + taskManager.getHistory());

        assertNotEquals(taskManager.getHistory().getFirst(),
                taskManager.getHistory().getLast());
    }
}