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
        Subtask subtask1 = new Subtask("Расчесать шерсть", 
                "Жесткой расческой", 
                TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Смазать подушечки на лапках",
                "Специальной мазью", 
                TaskStatus.DONE);
        Subtask subtask3 = new Subtask("",
                "",
                TaskStatus.IN_PROGRESS);
    //Добавление задач
        System.out.println("Добавляем задачи");
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);
    //Получаем полный список задач
        //TODO: Сделать это через System.out.println
        System.out.println("Выводим полный список задач:");
        taskManager.getTasksList();
        //TODO: Cделать вывод списка эпиков
    //Добавляем подзадачи в эпики
        System.out.println("\r\nДобавляем подзадачи:");
        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);
        taskManager.updateTask(epic1.getId(), epic1);
        epic2.addSubtask(subtask3);
        taskManager.updateTask(epic2.getId(), epic2);
        //Получаем подзадачи
        System.out.println("Epic №1");
        epic1.getSubtasks();
        System.out.println("Epic №2");
        epic2.getSubtasks();
        System.out.println("Epic tasks:");
        System.out.println(taskManager.getTaskById(epic1.getId()).toString());
        System.out.println(taskManager.getTaskById(epic2.getId()).toString());
   //Обновляем статусы подзадачам
        System.out.println("\r\nОбновляем статусы подзадачам:");
        subtask1.setTaskStatus(TaskStatus.DONE);
        subtask3.setTaskStatus(TaskStatus.DONE);
        //Обновляем эпики
        taskManager.updateTask(epic1.getId(), epic1);
        taskManager.updateTask(epic2.getId(), epic2);
        //Выводим эпики повторно
        System.out.println("Epic tasks:");
        System.out.println(taskManager.getTaskById(epic1.getId()).toString());
        System.out.println(taskManager.getTaskById(epic2.getId()).toString());
    //Удаляем задачу и один из эпиков
        System.out.println("\r\nУдаляем задачи по идентификатору:");
        taskManager.removeTaskById(task2.getId());
        //TODO: Добавить удаление подзадач вместе с эпиком
        taskManager.removeTaskById(epic1.getId());
        //Выводим полный список задач
        taskManager.getTasksList();
    //Удаляем все задачи
        System.out.println("\r\nУдаляем все задачи:");
        taskManager.removeAllTasks();
        taskManager.getTasksList();
    }
}
