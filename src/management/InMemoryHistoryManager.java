package management;

import task.Task;
import task.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager{
    //Хеш-таблица идентификаторов задач в истории
    private HashMap<Integer,Node> historyIdsMap = new HashMap<>();
    private Integer firstId;
    private Integer lastId;
    //Храним историю
    private ArrayList<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        history = getTasks();
        return new ArrayList<>(history);
    }

    //Вспомогательный метод для добавления задачи в историю
    @Override
    public void add(Task task) {
        if (task.equals(null)){
            System.out.println("Ошибка добавления в историю! Переданная задача равна null");
        } else {
            linkLast(new Task(task.getName(), task.getDescription(), task.getTaskStatus()));
            getHistory(); //Обновление истории
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyIdsMap.get(id));
        history.remove(id);
    }

    //TODO: Рефакторинг
    public void linkLast(Task task) {
        Integer currId = task.getId();
        //Сценарий: добавление самой первой задачи
        if (firstId.equals(null)) {
            firstId = lastId = currId;
        }
        //Сценарий: добавление новой задачи
        if (!getHistory().contains(task)) {
            //Секция 1: Добавление ссылки на новую задачу последнему элементу
            Node prevNode = historyIdsMap.get(lastId);
            prevNode.setNext(currId);
            //Секция 2: Добавление новой задачи
            Node currNode = new Node(task, lastId);
            historyIdsMap.put(currId, currNode); //Добавление в историю
            //Секция 3: Изменение ссылки на последний элемент в списке
            lastId = currId;
        //Сценарий: добавление уже созданной задачи
        } else {
            //Подменяем предыдущий, минуя текущий и удаляем ссылку на следующий
            //Секция 1: Получаем узлы текущей и предыдущей задач
            Node currNode = historyIdsMap.get(currId);
            Node prevNode = historyIdsMap.get(currNode.getPrev());
            //Секция 2: Меняем ссылки на узлах
            prevNode.setNext(currNode.getNext()); //Предыдущий узел получает ссылку на следующую за текущей задачу
            currNode.setPrev(lastId); //Выставляем ссылку на последний элемент списка как предыдущий для текущего
            currNode.setNext(null); //Удаляем ссылку в текущем, окончательно выставляя её в конец списка
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = historyIdsMap.get(firstId);
        int next = node.getNext(); //Получаем идентификатор следующей связанной задачи
        do {
            tasks.add(node.getTask()); //Добавляем задачу в возвращаемый список
            node = historyIdsMap.get(next); //Задаем в качестве центрального узла цикла следующий за текущим
        } while (node != null);

        return tasks;
    }

    public void removeNode(Node node) {
        Node prevNode = historyIdsMap.get(node.getPrev());
        prevNode.setNext(node.getNext());
    }
}
