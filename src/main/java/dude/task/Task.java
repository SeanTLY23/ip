package dude.task;

/**
 * Represents a task with a task name and completion status.
 */
public abstract class Task {
    private String taskName;
    private boolean isDone;

    /**
     * Initializes a new Task with the given description.
     *
     * @param task The text describing the task.
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

    /**
     * Returns a visual icon representing completion.
     *
     * @return "X" if done, else a blank space.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public abstract String toFileFormat();

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getTaskName();
    }


}