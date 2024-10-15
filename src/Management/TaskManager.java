package Management;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskId = 0;
    //1. Храним задачи всех типов
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private int generateId() {
        return taskId++;
    }

    //2.a. Получение списка всех задач
    //Получить список задач типа Задача
    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(tasks.values());
    }
    //Получить список задач типа Эпик
    public ArrayList<Task> getAllEpicsList() {
        return new ArrayList<>(epics.values());
    }
    //Получить список задач типа Подзадача
    public ArrayList<Task> getAllSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //2.b. Удаление всех задач
    //Удаление задач типа Задача
    public void RemoveAllTasks() {
        tasks.clear();
    }
    //Удаление задач типа Эпик
    public void RemoveAllEpics() {
        epics.clear();
        subtasks.clear();
    }
    //Удаление задач типа Подзадача
    public void RemoveAllSubtasks() {
        for (Epic epic : epics.values()) {
            for (int subtaskId : subtasks.keySet()) {
                if (epic.getSubtasksIds().contains(subtaskId)) {
                    epic.removeFromSubtasksIds(subtaskId);
                }
            }
        }
        subtasks.clear();
    }

    //2.c. Получение по идентификатору
    //Получение задачи типа Задача по идентификатору
    private Task getTaskById(int id) {
        return tasks.get(id);
    }
    //Получение задачи типа Эпик по идентификатору
    private Epic getEpicById(int id) {
        return epics.get(id);
    }
    //Получение задачи типа Подзадача по идентификатору
    private Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    //2.d. Создание. Сам объект должен передаваться в качестве параметра
    //Создание задачи типа Задача
    public void createTask(Task newTask) {
        //Проверка на присутствие элемента происходит с помощью переопределенного метода equals()
        if (tasks.containsValue(newTask)) {
            System.out.println("Ошибка создания! Задача с идентичными параметрами уже была создана.");
        } else {
            tasks.put(generateId(), newTask);
        }
    }
    //Создание задачи типа Эпик
    public void createEpic(Epic newEpic) {
        if (epics.containsValue(newEpic)) {
            System.out.println("Ошибка создания! Эпик с идентичными параметрами уже был создан.");
        } else {
            epics.put(generateId(), newEpic);
        }
    }
    //Создание задачи типа Подзадача
    //4.a,b. Управление статусами задач
    public void createSubtask(Subtask newSubtask) {
        if (subtasks.containsValue(newSubtask)) {
            System.out.println("Ошибка создания! Подзадача с идентичными параметрами уже была создана.");
        } else if (!epics.containsKey(newSubtask.getEpicId())) {
            System.out.println("Ошибка создания! Эпика с которым связывается" +
                    " подзадача не существует");
        } else {
            int newSubtaskId = generateId();
            subtasks.put(newSubtaskId, newSubtask);
            Epic epic = getEpicById(newSubtask.getEpicId());
            //Связываем эпик с подзадачей
            epic.getSubtasksIds().add(newSubtaskId);
            //Вычисляем статус Эпика, связанного с подзадачей
            calculateEpicStatus(newSubtask.getEpicId());
        }
    }

    //2.e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    //Обновление задачи типа Задача
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        } else {
            System.out.println("Ошибка обновления! Задачи с указанным идентификатором не существует!");
        }
    }
    //Обновление задачи типа Эпик
    //4.a,b. Управление статусами задач
    public void updateEpic(int id, Epic epic) {
        if (epics.containsKey(id)) {
            epics.put(id, epic);
            //Обновляем статус Эпика после его обновления
            calculateEpicStatus(id);
        } else {
            System.out.println("Ошибка обновления! Эпика с указанным идентификатором не существует!");
        }
    }
    //Обновление задачи типа Подзадача
    //4.a,b. Управление статусами задач
    public void updateSubtask(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            //Удаляем подзадачу из первичного эпика, если произошла привязка к другому эпику
            if (subtask.getEpicId() != getSubtaskById(id).getEpicId()) {
                getEpicById(getSubtaskById(id).getEpicId()).getSubtasksIds().remove(id);
            }
            subtasks.put(id, subtask);
            //После изменения подзадачи вычисляем статус Эпика, связанного с ней
            calculateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Ошибка обновления! Подзадачи с указанным идентификатором не существует!");
        }
    }

    //2.f. Удаление по идентификатору
    //Удаление по идентификатору задач типа Задача
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует задачи с указанным идентификатором.");
        }
    }
    //Удаление по идентификатору задач типа Эпик
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            //Удаляем все связанные подзадачи, если удален Эпик
            Epic epic = getEpicById(id);
            for (int subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
            //Удаление эпика
            epics.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует эпика с указанным идентификатором.");
        }
    }
    //Удаление по идентификатору задач типа Подзадача
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            //Удаляем подзадачу из связанного с ним эпика
            getEpicById(getSubtaskById(id).getEpicId()).removeFromSubtasksIds(id);
            //Обновляем статус эпика
            calculateEpicStatus(getSubtaskById(id).getEpicId());
            //Удаление подзадачи
            subtasks.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует подзадачи с указанным идентификатором.");
        }
    }

    //3.a. Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getAllSubtasksFromEpic(int id) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (!getEpicById(id).getSubtasksIds().isEmpty()) {
            for (int subtaskId : getEpicById(id).getSubtasksIds()) {
                epicSubtasks.add(getSubtaskById(subtaskId));
            }
        }
        return epicSubtasks;
    }

    //Подсчет статуса Эпика
    private void calculateEpicStatus(int id) {
        Epic epic = getEpicById(id);
        //Проверка на наличие подзадач, если их нет возвращаем в качестве статуса Эпика NEW
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            TaskStatus taskStatusTemp = null;
            //Проходим по списку подзадач*
            //* - для оптимизации прохождение по списку будет реализовано только единожды
            for (int subtaskId : epic.getSubtasksIds()) {
                //Если находим подзадачу со статусом IN_PROGRESS, то сразу возвращаем данный статус для всего Эпика
                if (getSubtaskById(subtaskId).getTaskStatus() == TaskStatus.IN_PROGRESS) {
                    taskStatusTemp = TaskStatus.IN_PROGRESS;
                    break;
                } else {
                    //Проверка на первый элемент списка, присуждаем нашей вспомогательной переменной его статус
                    if (taskStatusTemp == null) {
                        taskStatusTemp = getSubtaskById(subtaskId).getTaskStatus();
                        //Если элемент списка не первый, то совершаем проверку
                        //на равенство статусов первого элемента и текущего:
                        //различаются - сразу возвращаем IN_PROGRESS, нет - проверяем следующий элемент из списка
                    } else if (taskStatusTemp != getSubtaskById(subtaskId).getTaskStatus()) {
                        taskStatusTemp = TaskStatus.IN_PROGRESS;
                        break;
                    }
                }
            }
            //Выставляем окончательно значение статуса
            epic.setTaskStatus(taskStatusTemp);
        }
    }
}
