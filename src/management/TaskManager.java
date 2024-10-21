package management;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //Генерация идентификатора задачи
    void generateId(Task task);

    //2.a. Получение списка всех задач
    //Получить список задач типа Задача
    ArrayList<Task> getAllTasksList();
    //Получить список задач типа Эпик
    ArrayList<Task> getAllEpicsList();
    //Получить список задач типа Подзадача
    ArrayList<Task> getAllSubtasksList();

    //2.b. Удаление всех задач
    //Удаление задач типа Задача
    void removeAllTasks();
    //Удаление задач типа Эпик
    void removeAllEpics();
    //Удаление задач типа Подзадача
    void removeAllSubtasks();

    //2.c. Получение по идентификатору
    //Получение задачи типа Задача по идентификатору
    Task getTaskById(int id);
    //Получение задачи типа Эпик по идентификатору
    Epic getEpicById(int id);
    //Получение задачи типа Подзадача по идентификатору
    Subtask getSubtaskById(int id);

    //2.d. Создание. Сам объект должен передаваться в качестве параметра
    //Создание задачи типа Задача
    void createTask(Task task);
    //Создание задачи типа Эпик
    void createEpic(Epic epic);
    //Создание задачи типа Подзадача
    //4.a,b. Управление статусами задач
    void createSubtask(Subtask subtask);

    //2.e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    //Обновление задачи типа Задача
    void updateTask(Task task);
    //Обновление задачи типа Эпик
    //4.a,b. Управление статусами задач
    void updateEpic(Epic epic);
    //Обновление задачи типа Подзадача
    //4.a,b. Управление статусами задач
    void updateSubtask(Subtask subtask);

    //2.f. Удаление по идентификатору
    //Удаление по идентификатору задач типа Задача
    void removeTaskById(int id);
    //Удаление по идентификатору задач типа Эпик
    void removeEpicById(int id);
    //Удаление по идентификатору задач типа Подзадача
    void removeSubtaskById(int id);

    //3.a. Получение списка всех подзадач определённого эпика
    ArrayList<Subtask> getAllSubtasksFromEpic(int id);
}
