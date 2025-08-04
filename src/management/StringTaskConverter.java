package management;

import task.*;

public class StringTaskConverter {
    //Создание задачи из строки
    public static Task fromString(String value) {
        //0-id, 1-type, 2-name, 3-status, 4-description, 5-epic
        String[] taskData = value.split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        String name = taskData[2];
        TaskStatus taskStatus = TaskStatus.valueOf(taskData[3]);
        String description = taskData[4];
        //Создаем объект на её основе
        Task task = null;
        if (taskType == TaskType.SUBTASK) {
            int epicId = Integer.parseInt(taskData[5]);
            task = new Subtask(epicId, name, description, taskStatus);
        } else if (taskType == TaskType.EPIC) {
            task = new Epic(name, description);
        } else {
            task = new Task(name, description, taskStatus);
        }
        //Задаем идентификатор
        task.setId(Integer.parseInt(taskData[0]));
        return task;
    }

    //Сохранение задачи в строку
    public static String toString(Task task) {
        //Получение типа задачи (и идентификатора эпика, если присутствует)
        String epicId = "";
        String taskType = "";
        switch (task) {
            case Subtask s -> {
                taskType = TaskType.SUBTASK.toString();
                epicId = Integer.toString(s.getEpicId());
            }
            case Epic e -> taskType = TaskType.EPIC.toString();
            default -> taskType = TaskType.TASK.toString();
        }
        return String.join(",",
                Integer.toString(task.getId()),
                taskType,
                task.getName(),
                task.getTaskStatus().toString(),
                task.getDescription(),
                epicId);
    }
}
