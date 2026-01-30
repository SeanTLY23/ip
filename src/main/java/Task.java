/**
 * Represents a task with a task name and completion status.
 */
public class Task {
    private String taskName;
    private boolean isDone;

    /**
     * Initializes a new Task with the given description.
     *
     * @param taskName The text describing the task.
     */
    public Task(String task) {
        this.taskName = task;
        isDone = false;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }
}