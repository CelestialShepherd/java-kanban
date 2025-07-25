package management;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    //Хеш-таблица идентификаторов задач в истории
    private HashMap<Integer,Node> historyIdsMap;
    private Node head;
    private Node tail;

    //Вложенный класс узлов
    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task, Node prev) {
            this.task = task;
            this.prev = prev;
        }
    }

    public InMemoryHistoryManager() {
        historyIdsMap = new HashMap<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    //Вспомогательный метод для добавления задачи в историю
    @Override
    public void add(Task task) {
        if (task.equals(null)) {
            System.out.println("Ошибка добавления в историю! Переданная задача равна null");
        } else {
            //Удаляем дубликат при наличии
            if (historyIdsMap.containsKey(task.getId())) {
                removeNode(historyIdsMap.get(task.getId()));
            }
            linkLast(task.getId(), new Task(task.getName(), task.getDescription(), task.getTaskStatus()));
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyIdsMap.get(id));
    }

    public void linkLast(int currId, Task task) {
        if (head == null) {
            //Сценарий 1: Добавление самой первой задачи
            //Создаем новый узел с пустой ссылкой на предыдущий элемент
            Node newNode = new Node(task, null);
            //Добавляем новый узел в хеш-таблицу
            historyIdsMap.put(currId, newNode);
            //Узел становится первым и последним в списке
            head = tail = newNode;
        } else {
            //Сценарий 2: Добавление элемента в уже заполненный список
            //Создаем новый узел
            Node newNode = new Node(task, tail);
            //Добавляем новый узел в хеш-таблицу
            historyIdsMap.put(currId, newNode);
            //Устанавливаем ссылку бывшему последнему элементу на текущий
            tail.next = newNode;
            //Назначаем новый узел - последним
            tail = newNode;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    public void removeNode(Node node) {
        //Назначаем предыдущему узлу ссылку на следующий узел
        if (node == head && node == tail) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = node.next;
            head.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else {
            Node prevNode = node.prev;
            prevNode.next = node.next;
            Node nextNode = node.next;
            nextNode.prev = node.prev;
        }
        //Удаляем узел с задачей из истории
            node.prev = null;
            node.next = null;
    }
}
