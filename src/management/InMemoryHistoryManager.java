package management;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    //Храним историю
    private ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>(10);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
    //Вспомогательный метод для добавления задачи в историю
    @Override
    public void add(Task task) {
        if (history.size() <= 10) {
            history.add(task);
        } else {
            ArrayList<Task> newHistory = new ArrayList<>(10);
            for (int i = 1; i < 10; i++) {
                newHistory.add(i - 1, history.get(i));
            }
            newHistory.add(task);
            history = newHistory;
        }
    }
}
