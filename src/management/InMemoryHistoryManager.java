package management;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    //Константное значение
    private final int HISTORY_SIZE = 10;
    //Храним историю
    private ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
    //Вспомогательный метод для добавления задачи в историю
    @Override
    public void add(Task task) {
        //Если история заполнена, удаляем самый старый элемент
        if (history.size() == HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(new Task(task.getName(), task.getDescription(), task.getTaskStatus()));
    }
}
