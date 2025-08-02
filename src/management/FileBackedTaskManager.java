package management;

import task.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String[] taskFields = new String[] {"id", "type", "name", "status", "description", "epic"};
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    //Методы взаимодействия с файлами
    //Сохранение в файл
    public void save() {
        try (Writer fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write(String.join(",", taskFields) + "\r\n");
            List<Task> allTasks = super.getHistory();
            allTasks.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return Integer.compare(o1.getId(),o2.getId());
                }
            });
            for (Task task : allTasks) {
                fw.write(toString(task) + "\r\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    //Загружаемся из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            ArrayList<String> lines;
            if (!(file.exists()))
                file.createNewFile();
            //Чтение всех строк из файла (первая строка опускается)
            String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            if (!fileContent.isEmpty()) {
                lines = new ArrayList<>(Arrays.asList(fileContent.split("\r\n")));
                lines.removeFirst();
                //Создание сущностей с помощью менеджера
                FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(file);
                for (String line : lines) {
                    Task newTask = backedTaskManager.fromString(line);
                    switch (newTask) {
                        case Subtask s -> backedTaskManager.createSubtask((Subtask) newTask);
                        case Epic e -> backedTaskManager.createEpic((Epic) newTask);
                        default -> {
                            backedTaskManager.createTask(newTask);
                            backedTaskManager.getTaskById(newTask.getId());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
            throw new ManagerSaveException();
        }
        return new FileBackedTaskManager(file);
    }

    //Сохранение задачи в строку
    public String toString(Task task) {
        Map<String, String> taskMap = initializeMap();
        //Заполняем хеш-таблицу задачи
        for (String field : taskFields) {
            switch (field) {
                case "id" -> taskMap.put(field, Integer.toString(task.getId()));
                case "name" -> taskMap.put(field, task.getName());
                case "status" -> taskMap.put(field, task.getTaskStatus().toString());
                case "description" -> taskMap.put(field, task.getDescription());
                default -> taskMap.put(field,"");
            }
        }
        //Определяем тип задачи
        String stringTaskType = null;
        switch (task) {
            case Subtask s -> {
                stringTaskType = TaskType.SUBTASK.toString();
                taskMap.put("epic", Integer.toString(s.getEpicId()));
            }
            case Epic e -> stringTaskType = TaskType.EPIC.toString();
            default -> stringTaskType = TaskType.TASK.toString();
        }
        taskMap.put("type", stringTaskType);

        return String.join(",", taskMap.values());
    }

    //Вспомогательный метод по первичной инициализации связной хэш-таблицы
    private LinkedHashMap<String, String> initializeMap() {
        LinkedHashMap<String, String> taskMap = new LinkedHashMap<>();
        for (String key : taskFields) {
            taskMap.put(key, "");
        }
        return taskMap;
    }

    //Создание задачи из строки
    public Task fromString(String value) {
        String[] taskData = value.split(",");
        Map<String, String> taskMap = initializeMap();
        Task task = null;
        //Заполняем хеш-таблицу
        for (int i = 0; i < taskData.length; i++) {
            taskMap.put(taskFields[i], taskData[i]);
        }
        //Создаем объект на её основе
        if (taskMap.get("type").equals(TaskType.SUBTASK.toString())) {
            task = new Subtask(Integer.parseInt(taskMap.get("epic")),
                    taskMap.get("name"),
                    taskMap.get("description"),
                    TaskStatus.fromString(taskMap.get("status")));
        } else if (taskMap.get("type").equals(TaskType.EPIC.toString())) {
            task = new Epic(taskMap.get("name"),
                    taskMap.get("description"));
        } else {
            task = new Task(taskMap.get("name"),
                    taskMap.get("description"),
                    TaskStatus.fromString(taskMap.get("status")));
        }
        //Задаем идентификатор
        task.setId(Integer.parseInt(taskMap.get("id")));
        return task;
    }

    //2.b. Удаление всех задач
    //Удаление задач типа Задача
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    //Удаление задач типа Эпик
    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    //Удаление задач типа Подзадача
    @Override
    public void removeAllSubtasks() {}

    //2.d. Создание. Сам объект должен передаваться в качестве параметра
    //Создание задачи типа Задача
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    //Создание задачи типа Эпик
    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    //Создание задачи типа Подзадача
    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    //2.e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    //Обновление задачи типа Задача
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    //Обновление задачи типа Эпик
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    //Обновление задачи типа Подзадача
    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    //2.f. Удаление по идентификатору
    //Удаление по идентификатору задач типа Задача
    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    //Удаление по идентификатору задач типа Эпик
    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    //Удаление по идентификатору задач типа Подзадача
    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }
}

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {}
}
