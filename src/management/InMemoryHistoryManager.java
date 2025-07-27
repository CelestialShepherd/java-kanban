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
            removeNode(historyIdsMap.get(task.getId()));
            linkLast(task.getId(), task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(historyIdsMap.get(id));
    }

    public void linkLast(int currId, Task task) {
        //Создаем новый узел
        Node newNode = new Node(task, tail);
        //Добавляем новый узел в хеш-таблицу
        historyIdsMap.put(currId, newNode);
        if (head == null) {
            //Сценарий 1: Добавление самой первой задачи
            //Узел становится первым списке
            head = newNode;
        } else {
            //Сценарий 2: Добавление элемента в уже заполненный список
            //Устанавливаем ссылку бывшему последнему элементу на текущий
            tail.next = newNode;
        }
        //Новый узел - последний в списке
        tail = newNode;
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
        if (node != null) {
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
            for (Integer historyId : historyIdsMap.keySet()) {
                if (historyIdsMap.get(historyId) == node) {
                    historyIdsMap.remove(historyId);
                    break;
                }
            }
        }
    }
}
