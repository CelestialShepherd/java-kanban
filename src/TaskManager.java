import java.util.HashMap;

public class TaskManager {
    private static int id = 0;
    //1. Храним задачи всех типов
    private HashMap<Integer, Task> tasks;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public int generateId() {
        return id++;
    }

    //2.a. Получение списка всех задач
    public void getTasksList() {
        System.out.println("Tasks List:");
        int counter = 1;
        for (Task value : tasks.values()) {
            System.out.println((counter++) + ". " + value.toString());
        }
    }
    //2.b. Удаление всех задач
    public void removeAllTasks() {
        tasks.clear();
    }
    //TODO: Добавить проверку на наличие объекта под данным идентификатором
    //2.c. Получение по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }
    //TODO: Сделать проверку на идентификатор
    //2.d. Создание. Сам объект должен передаваться в качестве параметра
    //4.a,b. Управление статусами задач
    public void createTask(Task task) {
        //TODO: Избавиться от дублирования кода
        if (task.getClass() == Epic.class) {
            Epic epicTask = (Epic) task;
            task.setTaskStatus(epicTask.calculateTaskStatus());
        }
        tasks.put(task.getId(), task);
    }
    //TODO: Изменить проверку на наличие объекта под данным идентификатором
    //2.e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            //TODO: Избавиться от дублирования кода
            if (task.getClass() == Epic.class) {
                Epic epicTask = (Epic) task;
                task.setTaskStatus(epicTask.calculateTaskStatus());
            }
            tasks.put(id, task);
        } else {
            System.out.println("Ошибка! В списке не существует задачи с указанным идентификатором" +
                    ", попробуйте добавить данную задачу в качестве новой или укажите другой идентификатор");
        }
    }
    //TODO: Добавить проверку на наличие объекта под данным идентификатором
    //2.f. Удаление по идентификатору
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    //TODO: Epic - Добавить проверку на наличие объекта под данным идентификатором
    //3.a. Получение списка всех подзадач определённого эпика
    public void getAllSubtasksFromEpic(int epicId) {
        Epic epic = (Epic) tasks.get(epicId);
        epic.getSubtasks();
    }
}
