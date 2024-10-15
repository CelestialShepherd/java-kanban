import Management.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
    //Создание задач
        System.out.println("Создание задач");
        //id: 0
        Task task1 = new Task("Помыть посуду",
                "С Фэри", 
                TaskStatus.IN_PROGRESS);
        //id: 1
        Task task2 = new Task("Выбросить мусор",
                "В мусоропровод", 
                TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
    //Создание эпиков
        //id: 2
        Epic epic1 = new Epic("Уход за животным",
                "За кошкой");
        //id: 3
        Epic epic2 = new Epic("Сготовить яичницу",
                "На сковороде");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
    //Создание подзадач
        //id: 4
        Subtask subtask1 = new Subtask(2,
                "Расчесать шерсть",
                "Жесткой расческой");
        //id: 5
        Subtask subtask2 = new Subtask(2,
                "Смазать подушечки на лапках",
                "Специальной мазью",
                TaskStatus.DONE);
        //id: 6
        Subtask subtask3 = new Subtask(3,
                "Довести до готовности",
                "Желток едва схватился",
                TaskStatus.NEW);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
    //Получаем полный список задач
        printAllTasks(taskManager);
    //Изменяем статусы задач
        System.out.println("\r\nИзменяем статусы задач");
        taskManager.updateTask(0, new Task(task1.getName(), task1.getDescription(), TaskStatus.DONE));
        taskManager.updateSubtask(4, new Subtask(subtask1.getEpicId(),
                subtask1.getName(), subtask1.getDescription(), TaskStatus.DONE));
        taskManager.updateSubtask(6, new Subtask(subtask3.getEpicId(),
                subtask3.getName(), subtask3.getDescription(), TaskStatus.IN_PROGRESS));
        //Выводим полный список для проверки
        printAllTasks(taskManager);
    //Удаление задач
        System.out.println("\r\nУдаляем задачи");
        taskManager.removeTaskById(0);
        taskManager.removeEpicById(2);
        taskManager.removeSubtaskById(6);
        //Выводим полный список для проверки
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Выводим полный список задач:");
        System.out.println("Задачи типа Задача:");
        System.out.println(taskManager.getAllTasksList());
        System.out.println("Задачи типа Эпик:");
        System.out.println(taskManager.getAllEpicsList());
        System.out.println("Задачи типа Подзадача:");
        System.out.println(taskManager.getAllSubtasksList());
    }
}
