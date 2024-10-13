import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int taskId = 0;
    //1. Храним задачи всех типов
    private final HashMap<Integer, Object> allTasks;

    public TaskManager() {
        allTasks = new HashMap<>();
    }

    public int generateId() {
        return taskId++;
    }

    //2.a. Получение списка всех задач
    //Получить полный список задач
    public ArrayList<Object> getAllTasksList() {
        return new ArrayList<>(allTasks.values());
    }
    //Получить список задач типа Задача
    public ArrayList<Task> getTasksList() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (var task : allTasks.values()) {
            if (task.getClass() == Task.class) {
                tasksList.add((Task) task);
            }
        }
        return tasksList;
    }
    //Получить список задач типа Эпик
    public ArrayList<Epic> getEpicsList() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (var task : allTasks.values()) {
            if (task.getClass() == Epic.class) {
                epicsList.add((Epic) task);
            }
        }
        return epicsList;
    }
    //Получить список задач типа Подзадача
    public ArrayList<Subtask> getSubtasksList() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (var task : allTasks.values()) {
            if (task.getClass() == Subtask.class) {
                subtasksList.add((Subtask) task);
            }
        }
        return subtasksList;
    }

    //2.b. Удаление всех задач
    //Удаление всех задач любого типа
    public void removeAllTasks() {
        allTasks.clear();
    }
    //Удаление задач типа Задача
    public void removeTasks() {
        for (var task : allTasks.entrySet()) {
            if (task.getValue() == Task.class) {
                allTasks.remove(task.getKey());
            }
        }
    }
    //Удаление задач типа Эпик
    public void removeEpics() {
        for (var task : allTasks.entrySet()) {
            if (task.getValue() == Epic.class) {
                allTasks.remove(task.getKey());
            }
        }
    }
    //Удаление задач типа Подзадача
    public void removeSubtasks() {
        for (var task : allTasks.entrySet()) {
            if (task.getValue() == Subtask.class) {
                allTasks.remove(task.getKey());
            }
        }
    }

    //2.c. Получение по идентификатору
    public Object getTaskById(int id) {
        return allTasks.getOrDefault(id, null);
    }

    //2.d. Создание. Сам объект должен передаваться в качестве параметра
    //4.a,b. Управление статусами задач
    private void addTask(Task task) {
        if (task.getClass() == Epic.class) {
            Epic epicTask = (Epic) task;
            task.setTaskStatus(epicTask.calculateTaskStatus());
        }
        allTasks.put(task.getId(), task);
    }
    public void createTask(Task task) {
        addTask(task);
    }
    //2.e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    public void updateTask(Task task) {
        if (allTasks.containsKey(task.getId())) {
            addTask(task);
        } else {
            System.out.println("Ошибка обновления! В списке не существует задачи с указанным идентификатором.");
        }
    }

    //2.f. Удаление по идентификатору
    public void removeTaskById(int id) {
        if (allTasks.containsKey(id)) {
            //Удаляем все связанные подзадачи, если удален Эпик
            if (allTasks.get(id).getClass() == Epic.class) {
                Epic epicTask = (Epic) allTasks.get(id);
                for (Subtask subtask : epicTask.getSubtasks()) {
                    allTasks.remove(subtask.getId());
                }
            }
            allTasks.remove(id);
        } else {
            System.out.println("Ошибка удаления! В списке не существует задачи с указанным идентификатором.");
        }
    }

    //3.a. Получение списка всех подзадач определённого эпика
    public ArrayList<Subtask> getAllSubtasksFromEpic(Epic epic) {
        return epic.getSubtasks();
    }
}
