package management;

import task.Task;
import task.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager{
    //Хеш-таблица идентификаторов задач в истории
    private HashMap<Integer,Node> historyIdsMap = new HashMap<>(); // TODO: Реализовать поля first и last
    //TODO: Интегрировать события с назначением первого и последнего элементов
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

    //TODO: Полный рефакторинг
    //TODO: Если первая задача перемещается вниз, то мы лишаемся первой задачи
    public void linkLast(Task task) {
        Integer currId = task.getId();
        if (firstId.equals(null)) { //Самая первая задача в списке
            firstId = lastId = currId;
        }
        if (!getHistory().contains(task)) { //Новая задача
            //TODO: Пройтись по всему списку, чтобы получить последнюю задачу
            //TODO: Рефакторинг - возможно следует вывести добавление новой задачи в отдельный метод
            //Секция 1: Добавление ссылки на новую задачу последнему элементу
            Node prevNode = historyIdsMap.get(currId);
            prevNode.setNext(currId);
            //Секция 2: Добавление новой задачи в Map
            Node node = new Node(task, lastId);
            //TODO: Вынести добавление в конец в отдельный метод
            historyIdsMap.put(currId, node); //Добавление в историю
            //Секция 3: Изменение ссылки на последний элемент в списке
            lastId = currId;
        } else { //Уже созданная задача
            //подменяем предыдущий, минуя текущий и удаляем ссылку на следующий
            Node currNode = historyIdsMap.get(currId); //Узел текущей задачи
            Node prevNode = historyIdsMap.get(currNode.getPrev()); //Узел предыдущей задачи
            prevNode.setNext(currNode.getNext()); //Связываем предыдущий со следующим
            currNode.setNext(null); //Удаляем ссылку в текущем, выставляя её в конец списка
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = historyIdsMap.get(0); //TODO: Исправить заполнение первого вхождения
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
        //TODO: Неявная связь между узлом и списком задач, в historyIdsMap так и осталась задача
        //Частично исправлено в методе remove
    }

    public int getLastTask() {
        do {
            tasks.add(node.getTask()); //Добавляем задачу в возвращаемый список
            node = historyIdsMap.get(next); //Задаем в качестве центрального узла цикла следующий за текущим
        } while (node != null);
    }
}
