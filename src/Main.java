import management.*;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

    //Создание задач
        System.out.println("Создание задач");
        //id: 1
        Task task1 = new Task("Помыть посуду",
                "С Фэри", 
                TaskStatus.IN_PROGRESS);
        //id: 2
        Task task2 = new Task("Выбросить мусор",
                "В мусоропровод", 
                TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
    //Создание эпиков
        //id: 3
        Epic epic1 = new Epic("Уход за животным",
                "За кошкой");
        //id: 4
        Epic epic2 = new Epic("Сготовить яичницу",
                "На сковороде");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
    //Создание подзадач
        //id: 5
        Subtask subtask1 = new Subtask(3,
                "Расчесать шерсть",
                "Жесткой расческой");
        //id: 6
        Subtask subtask2 = new Subtask(3,
                "Смазать подушечки на лапках",
                "Специальной мазью",
                TaskStatus.DONE);
        //id: 7
        Subtask subtask3 = new Subtask(4,
                "Довести до готовности",
                "Желток едва схватился",
                TaskStatus.NEW);
        taskManager.createSubtask(subtask1); //История: [3, 5]
        taskManager.createSubtask(subtask2); //История: [3, 5, 6]
        taskManager.createSubtask(subtask3); //История: [3, 5, 6, 4, 7]
    //Получаем полный список задач
        printAllTasks(taskManager);
    //Изменяем статусы задач
        System.out.println("\r\nИзменяем статусы задач");
        task1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        subtask3.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);
        //Выводим полный список для проверки
        printAllTasks(taskManager);
    //Удаление задач
        System.out.println("\r\nУдаляем задачи по идентификатору");
        //Удаляем самую первую задачу
        taskManager.removeTaskById(1);
        //Удаляем первый эпик
        taskManager.removeEpicById(3);
        //Удаляем последнюю подзадачу
        taskManager.removeSubtaskById(7);
        //Выводим полный список для проверки
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Выводим полный список задач:");
        System.out.println("--------------------------------------------");
        System.out.println("Задачи типа Задача:");
        for (Task task : taskManager.getAllTasksList()) {
            System.out.println(task);
        }
        System.out.println("--------------------------------------------");
        System.out.println("Задачи типа Эпик:");
        //TODO: Исправить утерю задач из истории при выводе эпиков (?)
        for (Task epic : taskManager.getAllEpicsList()) {
            System.out.println(epic);
            for (Task subtask : taskManager.getAllSubtasksFromEpic(epic.getId())) {
                System.out.println("-->" + subtask);
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println("Задачи типа Подзадача:");
        for (Task subtask : taskManager.getAllSubtasksList()) {
            System.out.println(subtask);
        }
        System.out.println("--------------------------------------------");
        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("--------------------------------------------");
    }
}
