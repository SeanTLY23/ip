package dude.task;

/**
 * Represents a task that starts and ends at specific times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Initializes a new Event task with the given description.
     *
     * @param task The text describing the event.
     * @param from The start time/date.
     * @param to   The end time/date.
     */
    public Event(String task, String from, String to) {
        super(task);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone() ? "1" : "0") + " | " + getTaskName() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
