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
    private static final String FILE_HEADER = "id,type,name,status,description,epic";
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    //Методы взаимодействия с файлами
    //Сохранение в файл
    private void save() {
        try (Writer fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write(FILE_HEADER + "\r\n");
            //Заполняем полный список задач
            List<Task> allTasks = getAllTasksList();
            allTasks.addAll(getAllEpicsList());
            allTasks.addAll(getAllSubtasksList());
            //Сортируем список по Id
            allTasks.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return Integer.compare(o1.getId(),o2.getId());
                }
            });
            //Пишем в файл
            for (Task task : allTasks) {
                fw.write(StringTaskConverter.toString(task) + "\r\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    //Загружаемся из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            if (!(file.exists()))
                file.createNewFile();
            FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(file);
            //Чтение всех строк из файла (первая строка опускается)
            String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            if (!fileContent.isEmpty()) {
                ArrayList<String> lines = new ArrayList<>(Arrays.asList(fileContent.split("\r\n")));
                lines.removeFirst();
                for (String line : lines) {
                    Task newTask = StringTaskConverter.fromString(line);
                    switch (newTask) {
                        case Subtask s -> {
                            backedTaskManager.subtasks.put(s.getId(), s);
                            //Добавляем подзадачу внутрь эпика
                            backedTaskManager.getEpicById(s.getEpicId()).addToSubtasksIds(s.getId());
                        }
                        case Epic e -> backedTaskManager.epics.put(e.getId(), e);
                        default -> backedTaskManager.tasks.put(newTask.getId(), newTask);
                    }
                    //Синхронизация счетчиков taskId
                    if (newTask.getId() >= backedTaskManager.taskId) {
                        backedTaskManager.setTaskId(newTask.getId() + 1);
                    }
                }
            }
            return backedTaskManager;
        } catch (IOException e) {
            e.getStackTrace();
            throw new ManagerSaveException();
        }
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
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

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

