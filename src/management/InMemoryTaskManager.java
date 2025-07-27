package management;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 1;
    //1. Храним задачи всех типов
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    private void generateId(Task task) {
        //Проверка на наличие сгенерированного ранее идентификатора, т.к
        //сгенерированные идентификаторы начинаются с 1, а идентификатор по умолчанию равен 0
        if (task.getId() == 0) {
            task.setId(taskId++);
        } else {
            System.out.println("Ошибка генерации идентификатора! " +
                    "Переданная задача уже имеет идентификатор");
        }
    }

    //2.a. Получение списка всех задач
    //Получить список задач типа Задача
    @Override
    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(tasks.values());
    }

    //Получить список задач типа Эпик
    @Override
    public ArrayList<Task> getAllEpicsList() {
        return new ArrayList<>(epics.values());
    }

    //Получить список задач типа Подзадача
    @Override
    public ArrayList<Task> getAllSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //2.b. Удаление всех задач
    //Удаление задач типа Задача
    @Override
    public void removeAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    //Удаление задач типа Эпик
    @Override
    public void removeAllEpics() {
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
    }

    //Удаление задач типа Подзадача
    @Override
    public void removeAllSubtasks() {
        //Удаляем все идентификаторы подзадач и обновляем статус у каждого эпика
        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            calculateEpicStatus(epic.getId());
        }
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
    }

    //2.c. Получение по идентификатору
    //Получение задачи типа Задача по идентификатору
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    //Получение задачи типа Эпик по идентификатору
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    //Получение задачи типа Подзадача по идентификатору
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    //2.d. Создание. Сам объект должен передаваться в качестве параметра
    //Создание задачи типа Задача
    @Override
    public void createTask(Task task) {
        //Проверка на присутствие элемента происходит с помощью переопределенного метода equals()
        if (tasks.containsValue(task)) {
            System.out.println("Ошибка создания! Задача с идентичными параметрами уже была создана.");
        } else {
            Task newTask = new Task(task.getName(), task.getDescription(), task.getTaskStatus());
            generateId(newTask);
            task.setId(newTask.getId());
            tasks.put(newTask.getId(), newTask);
        }
    }

    //Создание задачи типа Эпик
    @Override
    public void createEpic(Epic epic) {
        if (epics.containsValue(epic)) {
            System.out.println("Ошибка создания! Эпик с идентичными параметрами уже был создан.");
        } else {
            Epic newEpic = new Epic(epic.getName(), epic.getDescription());
            generateId(newEpic);
            epic.setId(newEpic.getId());
            epics.put(newEpic.getId(), newEpic);
        }
    }

    //Создание задачи типа Подзадача
    //4.a,b. Управление статусами задач
    @Override
    public void createSubtask(Subtask subtask) {
        if (subtasks.containsValue(subtask)) {
            System.out.println("Ошибка создания! Подзадача с идентичными параметрами уже была создана.");
        } else if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("Ошибка создания! Эпика с которым связывается" +
                    " подзадача не существует");
        } else {
            Subtask newSubtask = new Subtask(subtask.getEpicId(),
                    subtask.getName(),
                    subtask.getDescription(),
                    subtask.getTaskStatus());
            generateId(newSubtask);
            if (newSubtask.getId() != newSubtask.getEpicId()) {
                subtask.setId(newSubtask.getId());
                subtasks.put(newSubtask.getId(), newSubtask);
                //Получаем эпик, связанный с подзадачей
                Epic epic = getEpicById(subtask.getEpicId());
                //Связываем эпик с подзадачей
                epic.addToSubtasksIds(subtask.getId());
                //Вычисляем статус Эпика, связанного с подзадачей
                calculateEpicStatus(epic.getId());
            }
        }
    }

    //2.e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    //Обновление задачи типа Задача
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Ошибка обновления! Переданная задача содержит некорректный идентификатор.");
        }
    }

    //Обновление задачи типа Эпик
    //4.a,b. Управление статусами задач
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            //Обновляем статус Эпика после его обновления
            calculateEpicStatus(epic.getId());
        } else {
            System.out.println("Ошибка обновления! Переданный эпик содержит некорректный идентификатор.");
        }
    }

    //Обновление задачи типа Подзадача
    //4.a,b. Управление статусами задач
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            //После изменения подзадачи вычисляем статус Эпика, связанного с ней
            calculateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Ошибка обновления! Подзадачи с указанным идентификатором не существует!");
        }
    }

    //2.f. Удаление по идентификатору
    //Удаление по идентификатору задач типа Задача
    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует задачи с указанным идентификатором.");
        }
    }

    //Удаление по идентификатору задач типа Эпик
    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            //Удаляем все связанные подзадачи, если удален Эпик
            Epic epic = getEpicById(id);
            for (int subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            //Удаление эпика
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует эпика с указанным идентификатором.");
        }
    }

    //Удаление по идентификатору задач типа Подзадача
    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            //Удаляем подзадачу из связанного с ним эпика
            Subtask subtask = getSubtaskById(id);
            getEpicById(subtask.getEpicId()).removeFromSubtasksIds(id);
            //Обновляем статус эпика
            calculateEpicStatus(subtask.getEpicId());
            //Удаление подзадачи
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует подзадачи с указанным идентификатором.");
        }
    }

    //3.a. Получение списка всех подзадач определённого эпика
    @Override
    public ArrayList<Subtask> getAllSubtasksFromEpic(int id) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        if (!getEpicById(id).getSubtasksIds().isEmpty()) {
            for (int subtaskId : getEpicById(id).getSubtasksIds()) {
                epicSubtasks.add(getSubtaskById(subtaskId));
            }
        }
        return epicSubtasks;
    }

    //Получение истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
