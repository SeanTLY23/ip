package dude.task;

/**
 * Represents a simple task without any date/time attached.
 */
public class Todo extends Task {

    /**
     * Initializes a new Task with the given description.
     *
     * @param task The text describing the task.
     */
    public Todo(String task) {
        super(task);
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone() ? "1" : "0") + " | " + getTaskName();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
