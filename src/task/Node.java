package task;

public class Node {
    private Task task;
    private Integer prev;
    private Integer next;

    public Node(Task task, Integer prev) {
        this.task = task;
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task.setName(task.getName());
        this.task.setDescription(task.getDescription());
        this.task.setTaskStatus(task.getTaskStatus());
    }

    public Integer getPrev() {
        return prev;
    }

    public void setPrev(Integer prev) {
        this.prev = prev;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }
}
