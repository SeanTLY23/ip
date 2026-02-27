package dude;

/**
 * Deciphers user input into actionable components.
 * This class handles the logic of splitting strings to identify
 * command types, task descriptions, and dates.
 */
public class Parser {

    /**
     * Checks if the user wants to exit the program.
     */
    public static boolean isExit(String input) {
        return input.trim().equalsIgnoreCase("bye");
    }

    /**
     * Extracts the task index number from a command string.
     *
     * @param message The raw user input.
     * @return The integer task number.
     * @throws DudeException If no number is provided or if the input cannot be parsed as an integer.
     */
    public static int getTaskNumber(String message) throws DudeException {
        String[] messageParts = message.split(" ", 2);
        if (messageParts.length < 2 || messageParts[1].trim().isEmpty()) {
            throw new DudeException("I need a task number to work with.");
        }
        try {
            return Integer.parseInt(messageParts[1].trim());
        } catch (NumberFormatException e) {
            throw new DudeException("That's not a number.");
        }
    }

    /**
     * Extracts the first word from the message to determine the command task type.
     */
    public static String getTaskType(String message) {
        String[] commandParts = message.split(" ", 2);
        return commandParts[0];
    }

    public static String getFindDescription(String message) throws DudeException{
        String description = message.replaceFirst("find", "").trim();
        if (description.isEmpty()) {
            throw new DudeException("your find command cannot be empty");
        }
        return description;
    }

    /**
     * Extracts the description by splitting at the first slash (/).
     */
    public static String getTaskDescription(String message) {
        String type = getTaskType(message);
        String details = message.replaceFirst(type, "").trim();
        if (type.equalsIgnoreCase("todo")) {
            return details;
        }
        String[] parts = details.split("/", 2);
        return parts[0].trim();
    }

    /**
     * Extracts the date/time for a deadline task by splitting at "/by".
     */
    public static String getDeadlineDate(String message) {
        String[] parts = message.split("/by", 2);
        return parts[1].trim();
    }

    /**
     * Extracts the "from" time for an event task by splitting between "/from" and "/to".
     */
    public static String getEventFromTime(String message) {
        String afterFrom = message.split("/from", 2)[1];
        String[] timeParts = afterFrom.split("/to", 2);
        return timeParts[0].trim();
    }

    /**
     * Extracts the "to" time for an event task by taking everything after "/to".
     */
    public static String getEventToTime(String message) {
        String[] parts = message.split("/to", 2);
        return parts[1].trim();
    }

}
