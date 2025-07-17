package management;

import task.Task;
import task.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager{
    //Хеш-таблица идентификаторов задач в истории
    private HashMap<Integer,Node> historyIdsMap = new HashMap<>();
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
        if (task.equals(null)){
            System.out.println("Ошибка добавления в историю! Переданная задача равна null");
        } else {
            history.add(new Task(task.getName(), task.getDescription(), task.getTaskStatus()));
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyIdsMap.get(id));
        history.remove(id);
    }

    public void linkLast(int id) {
        //TODO: Добавить условие проверки, является ли задача новой
        //TODO: Создать узел через конструктор
        //TODO: Учесть условие самой первой задачи в списке
        //Подменяем предыдущий, минуя текущий и удаляем ссылку на следующий
        Node currNode = historyIdsMap.get(id); //Узел текущей задачи
        Node prevNode = historyIdsMap.get(currNode.getPrev()); //Узел предыдущей задачи
        prevNode.setNext(currNode.getNext()); //Связываем предыдущий со следующим
        currNode.setNext(null); //Удаляем ссылку в текущем, выставляя её в конец списка
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = historyIdsMap.get(0);
        //TODO: Сделать рефакторинг уикла do...while
        do {
            tasks.add(historyIdsMap.get(node.getNext()).getTask());
        } while (node != null);
        return tasks;
    }

    public void removeNode(Node node) {
        Node prevNode = historyIdsMap.get(node.getPrev());
        prevNode.setNext(node.getNext());
        //TODO: Неявная связь между узлом и списком задач, в historyIdsMap так и осталась задача
        //Частично исправлено в методе remove
    }
}
