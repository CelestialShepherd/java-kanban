package management;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Managers {
    public static TaskManager getDefault() {
        return new TaskManager() {
            //Генерация идентификатора задачи
            @Override
            public void generateId(Task task) {}
            //Получение списка всех задач
            @Override
            public ArrayList<Task> getAllTasksList() { return null; }
            @Override
            public ArrayList<Task> getAllEpicsList() { return null; }
            @Override
            public ArrayList<Task> getAllSubtasksList() { return null; }
            //Удаление всех задач
            @Override
            public void removeAllTasks() {}
            @Override
            public void removeAllEpics() {}
            @Override
            public void removeAllSubtasks() {}
            //Получение по идентификатору
            @Override
            public Task getTaskById(int id) { return null; }
            @Override
            public Epic getEpicById(int id) { return null; }
            @Override
            public Subtask getSubtaskById(int id) { return null; }
            //Создание. Сам объект должен передаваться в качестве параметра
            @Override
            public void createTask(Task task) {}
            @Override
            public void createEpic(Epic epic) {}
            @Override
            public void createSubtask(Subtask subtask) {}
            //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
            @Override
            public void updateTask(Task task) {}
            @Override
            public void updateEpic(Epic epic) {}
            @Override
            public void updateSubtask(Subtask subtask) {}
            //Удаление по идентификатору
            @Override
            public void removeTaskById(int id) {}
            @Override
            public void removeEpicById(int id) {}
            @Override
            public void removeSubtaskById(int id) {}
            //Получение списка всех подзадач определённого эпик
            @Override
            public ArrayList<Subtask> getAllSubtasksFromEpic(int id) { return null; }
        };
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager() {};
    }
}
