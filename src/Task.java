import java.util.Objects;

public class Task {
    private final int id;
    private String name;
    private String description;
    private TaskStatus taskStatus;

    public Task(String name, String description) {
        TaskManager taskManager = new TaskManager();
        this.id = taskManager.generateId();
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        TaskManager taskManager = new TaskManager();
        this.id = taskManager.generateId();
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    //TODO: Добавить вызов удобоваримого вывода статуса
    @Override
    public String toString() {
        return "Task{id=" + id
                + ", name=" + name
                + ", description=" + description
                + ", taskStatus=" + taskStatus
                + "}";
    }

    //TODO: Возможно предстоит изменить переопределение методов equals() и hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && taskStatus == task.taskStatus;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, taskStatus);
    }
}
