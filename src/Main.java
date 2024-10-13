public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        //Инициализация задач
        Task task1 = new Task("Помыть посуду",
                "С Фэри", 
                TaskStatus.IN_PROGRESS);
        Task task2 = new Task("Выбросить мусор", 
                "В мусоропровод", 
                TaskStatus.NEW);
        Epic epic1 = new Epic("Уход за животным",
                "За кошкой");
        Epic epic2 = new Epic("Сготовить яичницу",
                "На сковороде");
        Subtask subtask1 = new Subtask(epic1.getId(),
                "Расчесать шерсть",
                "Жесткой расческой",
                TaskStatus.NEW);
        Subtask subtask2 = new Subtask(epic1.getId(),
                "Смазать подушечки на лапках",
                "Специальной мазью",
                TaskStatus.DONE);
        Subtask subtask3 = new Subtask(epic2.getId(),
                "Довести до готовности",
                "Желток едва схватился",
                TaskStatus.IN_PROGRESS);
    //Добавление задач
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        taskManager.createTask(subtask3);
        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);
        epic2.addSubtask(subtask3);
    //Получаем полный список задач
        System.out.println("\r\nВыводим полный список задач:");
        System.out.println(taskManager.getAllTasksList());
        System.out.println("Выводим задачи типа Задача:");
        System.out.println(taskManager.getTasksList());
        System.out.println("Выводим задачи типа Эпик:");
        System.out.println(taskManager.getEpicsList());
        System.out.println("Выводим задачи типа Подзадача:");
        System.out.println(taskManager.getSubtasksList());
    //Изменяем статусы задач
        System.out.println("\r\nИзменяем статусы задач");
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtask1.setTaskStatus(TaskStatus.DONE);
        //Доказательство соблюдения условия, что для подзадачи известно, в рамках какого эпика она выполняется
        taskManager.updateTask((Epic) taskManager.getTaskById(subtask1.getEpicId()));
        //Выводим полный список для проверки
        System.out.println("Выводим полный список задач:");
        System.out.println(taskManager.getAllTasksList());
    //Удаление задач
        System.out.println("\r\nУдаляем задачи");
        taskManager.removeTaskById(task1.getId());
        taskManager.removeTaskById(epic1.getId());
        System.out.println("Выводим полный список задач:");
        System.out.println(taskManager.getAllTasksList());
    }
}
