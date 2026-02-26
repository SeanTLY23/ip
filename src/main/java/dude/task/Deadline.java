package dude.task;

/**
 * Represents a task that needs to be done before a specific deadline.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Initializes a new Deadline task with the given description.
     *
     * @param description The text describing the task.
     * @param by          The deadline date or time.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
