package management;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Managers<T extends TaskManager> {
    public TaskManager getDefault() {
        return new TaskManager() {
            @Override
            public void generateId(Task task) {

            }

            @Override
            public ArrayList<Task> getAllTasksList() {
                return null;
            }

            @Override
            public ArrayList<Task> getAllEpicsList() {
                return null;
            }

            @Override
            public ArrayList<Task> getAllSubtasksList() {
                return null;
            }

            @Override
            public void removeAllTasks() {

            }

            @Override
            public void removeAllEpics() {

            }

            @Override
            public void removeAllSubtasks() {

            }

            @Override
            public Task getTaskById(int id) {
                return null;
            }

            @Override
            public Epic getEpicById(int id) {
                return null;
            }

            @Override
            public Subtask getSubtaskById(int id) {
                return null;
            }

            @Override
            public void createTask(Task task) {

            }

            @Override
            public void createEpic(Epic epic) {

            }

            @Override
            public void createSubtask(Subtask subtask) {

            }

            @Override
            public void updateTask(Task task) {

            }

            @Override
            public void updateEpic(Epic epic) {

            }

            @Override
            public void updateSubtask(Subtask subtask) {

            }

            @Override
            public void removeTaskById(int id) {

            }

            @Override
            public void removeEpicById(int id) {

            }

            @Override
            public void removeSubtaskById(int id) {

            }

            @Override
            public ArrayList<Subtask> getAllSubtasksFromEpic(int id) {
                return null;
            }

            @Override
            public ArrayList<Task> getHistory() {
                return null;
            }

            @Override
            public void calculateEpicStatus(int id) {

            }
        };
    }
}
