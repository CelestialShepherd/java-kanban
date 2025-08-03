import management.FileBackedTaskManager;
import management.ManagerSaveException;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private static FileBackedTaskManager fileBackedtaskManager;
    private static File file;
    private static Task task;
    private static Epic epic;
    private static Subtask subtask;

    //Вспомогательный метод по подготовке файла к тесту
    private static File initializeNewFile(String fileName) {
        try {
            File newFile = File.createTempFile(fileName, "txt");
            fileBackedtaskManager = FileBackedTaskManager.loadFromFile(newFile);
            return newFile;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    //Единожды создаем и заполняем файл
    @BeforeAll
    public static void initializeFields() {
        file = initializeNewFile("default");
        //Создаем задачу
        task = new Task("Test task1","Task1 description", TaskStatus.IN_PROGRESS);
        task.setId(1);
        //Создаем Эпик
        epic = new Epic("Test epic1","Epic1 description");
        epic.setId(2);
        //Создаем подзадачу и связываем её с эпиком
        subtask = new Subtask(2,"Test subtask1","Subtask1 description", TaskStatus.DONE);
        subtask.setId(3);
        //Добавляем задачи в менеджер
        fileBackedtaskManager.createTask(task);
        fileBackedtaskManager.createEpic(epic);
        fileBackedtaskManager.createSubtask(subtask);
    }

    //Инициализируем менеджер
    @BeforeEach
    public void initializeFileBackedTaskManager() {
        fileBackedtaskManager = FileBackedTaskManager.loadFromFile(file);
    }

    //Группа тестов: Восстановление из файла
    @Test
    void shouldLoadFromFile() {
        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
        assertEquals(fileBackedtaskManager.getAllTasksList(),
                fileBackedTaskManager2.getAllTasksList(),
                "Списки задач двух менеджеров отличаются");
        assertEquals(fileBackedtaskManager.getAllEpicsList(),
                fileBackedTaskManager2.getAllEpicsList(),
                "Списки эпиков двух менеджеров отличаются");
        assertEquals(fileBackedtaskManager.getAllSubtasksList(),
                fileBackedTaskManager2.getAllSubtasksList(),
                "Списки подзадач двух менеджеров отличаются");
    }

    @Test
    void shouldLoadFromEmptyFile() {
        initializeNewFile("emptyFile");
        assertTrue(fileBackedtaskManager.getAllTasksList().isEmpty(),
                "Менеджер задач хранит задачи, несмотря на чтение из пустого файла");
        assertTrue(fileBackedtaskManager.getAllEpicsList().isEmpty(),
                "Менеджер задач хранит эпики, несмотря на чтение из пустого файла");
        assertTrue(fileBackedtaskManager.getAllSubtasksList().isEmpty(),
                "Менеджер задач хранит подзадачи, несмотря на чтение из пустого файла");
    }

    @Test
    void shouldLoadIfOnlyOneTaskTypeIsInFile() {
        initializeNewFile("onlyTasks");
        Task task1 = new Task("Test task1","Task1 description", TaskStatus.IN_PROGRESS);
        task1.setId(1);
        Task task2 = new Task("Test task2","Task2 description", TaskStatus.IN_PROGRESS);
        task2.setId(2);
        Task task3 = new Task("Test task3","Task3 description", TaskStatus.IN_PROGRESS);
        task3.setId(3);
        fileBackedtaskManager.createTask(task1);
        fileBackedtaskManager.createTask(task2);
        fileBackedtaskManager.createTask(task3);
        assertEquals(new ArrayList<>(Arrays.asList(task1, task2, task3)),
                fileBackedtaskManager.getAllTasksList(),
                "Из файла с одним типом Задача не были выгружены все задачи этого типа");
        assertTrue(fileBackedtaskManager.getAllEpicsList().isEmpty(),
                "Менеджер содержит Эпик, несмотря на заполнение только типом Задача");
        assertTrue(fileBackedtaskManager.getAllSubtasksList().isEmpty(),
                "Менеджер содержит Подзадачу, несмотря на заполнение только типом Задача");
    }

    @Test
    void shouldLoadOnlyEpicsWithoutSubtasks() {
        initializeNewFile("onlyEpics");
        Epic epic1 = new Epic("Test epic1","Epic1 description");
        epic1.setId(1);
        Epic epic2 = new Epic("Test epic2","Epic2 description");
        epic2.setId(2);
        Epic epic3 = new Epic("Test epic3","Epic3 description");
        epic3.setId(3);
        fileBackedtaskManager.createEpic(epic1);
        fileBackedtaskManager.createEpic(epic2);
        fileBackedtaskManager.createEpic(epic3);
        assertEquals(new ArrayList<>(Arrays.asList(epic1, epic2, epic3)),
                fileBackedtaskManager.getAllEpicsList(),
                "Из файла с одним типом Эпик без подзадач не были выгружены все задачи этого типа");
        assertTrue(fileBackedtaskManager.getAllTasksList().isEmpty(),
                "Менеджер содержит Задачу, несмотря на заполнение только типом Эпик");
        assertTrue(fileBackedtaskManager.getAllSubtasksList().isEmpty(),
                "Менеджер содержит Подзадачу, несмотря на заполнение только типом Эпик");
    }

    //Группа тестов: Удаление задач
    @Test
    void shouldRemoveTaskFromFileIfTaskRemoved() {
        fileBackedtaskManager.removeTaskById(1);
        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertNotEquals(new ArrayList<>(Arrays.asList(task)),
                newFileBackedTaskManager.getAllTasksList(),
                "Задача не была удалена из файла, после удаления менеджером");
    }

    @Test
    void shouldRemoveEpicFromFileIfEpicRemoved() {
        fileBackedtaskManager.removeEpicById(2);
        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertNotEquals(new ArrayList<>(Arrays.asList(epic)),
                newFileBackedTaskManager.getAllEpicsList(),
                "Эпик не был удален из файла, после удаления менеджером");
    }

    @Test
    void shouldRemoveSubtaskFromFileIfSubtaskRemoved() {
        fileBackedtaskManager.removeSubtaskById(3);
        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertNotEquals(new ArrayList<>(Arrays.asList(subtask)),
                newFileBackedTaskManager.getAllTasksList(),
                "Подзадача не была удалена из файла, после удаления менеджером");
    }
}
