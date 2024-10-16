package tasks;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus taskStatus;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        taskStatus = TaskStatus.NEW;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            case TaskStatus.NEW -> stringTaskStatus = "To do";
            case TaskStatus.IN_PROGRESS -> stringTaskStatus = "In Progress";
            case TaskStatus.DONE -> stringTaskStatus = "Done";
        }

        return "Task{name=" + name
                + ", description=" + description
                + ", taskStatus=" + stringTaskStatus
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskStatus);
    }
}
