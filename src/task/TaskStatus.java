package task;

public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    DONE;

    public static TaskStatus fromString(String taskStatus) {
        for (TaskStatus status : TaskStatus.values()) {
            if (taskStatus.equals(status.toString())) {
                return status;
            }
        }
        throw new IllegalArgumentException();
    }
}
