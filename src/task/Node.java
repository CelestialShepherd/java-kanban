package task;

public class Node {
    private Task task;
    private Integer prev;
    private Integer next;

    //TODO: Добавить конструктор для узла
    public Node(Task task, Integer prev) {
        this.task = task;
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getPrev() {
        return prev;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }
}
