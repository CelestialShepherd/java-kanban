import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }

    public TaskStatus calculateTaskStatus() {
        //Проверка на наличие подзадач, если их нет возвращаем в качестве статуса Эпика NEW
        if (subtasks.isEmpty()) {
            return TaskStatus.NEW;
        } else {
            TaskStatus taskStatusTemp = null;
            //Проходим по списку подзадач*
            //* - для оптимизации прохождение будет реализовано только единожды
            for (Subtask subtask: subtasks.values()) {
                //Если находим подзадачу со статусом IN_PROGRESS, то сразу возвращаем данный статус для всего Эпика
                if (subtask.getTaskStatus() == TaskStatus.IN_PROGRESS) {
                    return TaskStatus.IN_PROGRESS;
                } else {
                    //Проверка на первый элемент списка, присуждаем нашей вспомогательной переменной его статус
                    if (taskStatusTemp == null) {
                        taskStatusTemp = subtask.getTaskStatus();
                    //Если элемент списка не первый, то совершаем проверку
                    //на равенство статусов первого элемента и текущего:
                    //различаются - сразу возвращаем IN_PROGRESS, нет - проверяем следующий элемент из списка
                    } else if (taskStatusTemp != subtask.getTaskStatus()) {
                        return TaskStatus.IN_PROGRESS;
                    }
                }
            }
            //Возвращаем статус(NEW или DONE) в случае успешной проверки на равенство статуса всех подзадач в списке
            return taskStatusTemp;
        }
    }

    //TODO: Сделать проверку на уже существующий идентификатор
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    //TODO: Добавить проверку на наличие объекта под данным идентификатором
    public void removeSubtask(int id) {
        subtasks.remove(id);
    }

    public void getSubtasks() {
        int counter = 0;
        for (Subtask value : subtasks.values()) {
            System.out.println((counter++) + ". " + value.toString());
        }
    }
}
