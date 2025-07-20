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
        return new ArrayList<>(history);
    }

    //Вспомогательный метод для добавления задачи в историю
    @Override
    public void add(Task task) {
        if (task.equals(null)){
            System.out.println("Ошибка добавления в историю! Переданная задача равна null");
        } else {
            linkLast(task.getId(), new Task(task.getName(), task.getDescription(), task.getTaskStatus()));
            history = getTasks();
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyIdsMap.get(id));
        history = getTasks();
    }

    //TODO: Рефакторинг (сделать общий функционал для всех трех сценариев)
    public void linkLast(int currId, Task task) {
        //Сценарий: добавление самой первой задачи
        if (firstId == null) {
            firstId = lastId = currId;
        }
        //Сценарий: добавление задачи, которой не было в списке
        if (!historyIdsMap.containsKey(currId)) {
            //Секция 1: Добавление ссылки на новую задачу последнему элементу
            //Если список узлов не пустой - даем последнему узлу ссылку на текущий
            if (!historyIdsMap.isEmpty()) {
                historyIdsMap.get(lastId).setNext(currId);
            }
            //Секция 2: Добавление новой задачи
            Node currNode = new Node(task, lastId); //Добавляем узел со ссылкой на бывшую последнюю задачу в списке
            historyIdsMap.put(currId, currNode); //Добавление в историю
            //Секция 3: Изменение ссылки на последний элемент в списке
            lastId = currId;
        //Сценарий: обновление позиции задачи, уже добавленной в список
        } else if (currId != lastId) {
            //Подменяем предыдущий, минуя текущий и удаляем ссылку на следующий
            //Секция 1: Получаем узел текущей задачи
            Node currNode = historyIdsMap.get(currId);
            //Если задействован первый элемент из списка, следующему за ним присуждается статус первого
            if (currId == firstId && historyIdsMap.size() > 1) {
                Node secondNode = historyIdsMap.get(currNode.getNext());
                //Связывание текущей задачи с последней в списке
                if (secondNode.getNext() == null) {
                    secondNode.setNext(currId);
                }
                else {
                    Node lastNode = historyIdsMap.get(lastId);
                    lastNode.setNext(currId);
                }
                //Назначение второму узлу статуса первого
                firstId = currNode.getNext();
            }
            //Если нет, то предыдущий узел получает ссылку на следующую за текущей задачу
            else {
                Node prevNode = historyIdsMap.get(currNode.getPrev());
                prevNode.setNext(currNode.getNext());
            }
            //Секция 2: Меняем ссылки на узлах
            currNode.setPrev(lastId); //Выставляем ссылку на последний элемент списка как предыдущий для текущего
            currNode.setNext(null); //Удаляем ссылку в текущем, окончательно выставляя её в конец списка
            //Секция 3: Изменение ссылки на последний элемент в списке
            lastId = currId;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = historyIdsMap.get(firstId);
        while (node != null){
            tasks.add(node.getTask());
            node = (node.getNext() != null) ? historyIdsMap.get(node.getNext()) : null;
        }
        return tasks;
    }

    public void removeNode(Node node) {
        Node prevNode = historyIdsMap.get(node.getPrev());
        prevNode.setNext(node.getNext());
    }
}
