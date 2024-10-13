import java.util.Objects;

public class Task {
    private final int id;
    private final String name;
    private final String description;
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

    @Override
    public String toString() {
        String stringTaskStatus = null;
        switch (taskStatus) {
            case NEW -> stringTaskStatus = "To do";
            case IN_PROGRESS -> stringTaskStatus = "In Progress";
            case DONE -> stringTaskStatus = "Done";
        }

        return "Task{id=" + id
                + ", name=" + name
                + ", description=" + description
                + ", taskStatus=" + stringTaskStatus
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
