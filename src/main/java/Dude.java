import java.util.Scanner;

/**
 * Main class for the Dude chatbot.
 * Handles task management including adding, listing, and marking tasks.
 */
public class Dude {
    public static final int MAX_TASKS = 100;
    private static Task[] taskList = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        printGreeting();
        respondToMessage();
    }

    /**
     * Processes user input in a loop until the 'bye' command is received.
     */
    public static void respondToMessage() {
        Scanner in = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            String line = in.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                isRunning = false;
            } else {
                handleLineCommand(line);
            }
        }
        exitMessage();
    }

    /**
     * Processes the command and handles errors, such as exceeding the 100-task limit.
     *
     * @param line The user input to be executed.
     */
    private static void handleLineCommand(String line) {
        try {
            processMessage(line);
        } catch (DudeException e) {
            printHorizontalLine();
            System.out.println(e.getMessage());
            printHorizontalLine();
        }
    }

    /**
     * Interprets the user's input, produces the appropriate task management command and
     * checks if the task limit is reached.
     *
     * @param line The raw string input from the user.
     * @throws DudeException If the list is full.
     */
    private static void processMessage(String line) throws DudeException {
        if (line.equalsIgnoreCase("list")) {
            listTasks();
        } else if (line.startsWith("unmark")) {
            handleMarking(line, false);
        } else if (line.startsWith("mark")) {
            handleMarking(line, true);
        } else {
            taskExceeded();
            addTaskByType(line);
            taskCreatedMessage();
        }
    }

    /**
     * Ensures the task list does not exceed the maximum capacity of MAX_TASKS.
     *
     * @throws DudeException If the task count has reached MAX_TASKS.
     */
    private static void taskExceeded() throws DudeException {
        if (taskCount >= MAX_TASKS) {
            throw new DudeException("this list is already full.");
        }
    }

    /**
     * Updates the completion status of a task and provides feedback to the user.
     *
     * @param line   The raw user input containing the task index.
     * @param isDone The new status to set (true for marked, false for unmarked).
     */
    private static void handleMarking(String line, boolean isDone) {
        int index = getTaskNumber(line) - 1;
        taskList[index].setDone(isDone);
        String feedback = isDone
                ? "Dude OKAY. I've marked this task as done:\n "
                : "Dude really? I've marked this task as not done yet:\n";
        printHorizontalLine();
        System.out.println(feedback + taskList[index]);
        printHorizontalLine();
    }

    /**
     * Prints a confirmation message after a task is successfully added to the list.
     */
    private static void taskCreatedMessage() {
        printHorizontalLine();
        System.out.println(
                "Dude I got it. I've added this task:\n" + taskList[taskCount - 1] + "\nNow you have "
                        + taskCount + " tasks in the list.");
        printHorizontalLine();
    }

    /**
     * Adds the corresponding task type to the task list.
     *
     * @param line The raw user input containing the task type and details.
     */
    private static void addTaskByType(String line) {
        switch (getTaskType(line)) {
        case "todo":
            taskList[taskCount] = new Todo(getTaskDescription(line));
            break;
        case "deadline":
            taskList[taskCount] = new Deadline(getTaskDescription(line), getDeadlineDate(line));
            break;
        case "event":
            taskList[taskCount] =
                    new Event(getTaskDescription(line), getEventFromTime(line), getEventToTime(line));
            break;
        default:
            break;
        }
        taskCount += 1;
    }

    /**
     * Prints the closing message when the user exits the chatbot.
     */
    private static void exitMessage() {
        System.out.println("Dude that's it? Okay Bye. See you again soon I hope.");
        printHorizontalLine();
    }

    /**
     * Iterates through the task list and prints each task with its index number.
     */
    private static void listTasks() {
        printHorizontalLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + taskList[i]);
        }
        printHorizontalLine();
    }

    /**
     * Displays initial welcome message.
     */
    private static void printGreeting() {
        String logo =
                """
                         ____        _____
                        |  _ \\ _   _|  _ \\   ___
                        | | | | | | | | | |/  _ \\
                        | |_| | |_| | |_| |\\  __/
                        |____/ \\__,_|____/  \\___|
                        """;
        printHorizontalLine();
        System.out.println(logo + "Hello! I'm Dude\nWhat can I do for you?");
        printHorizontalLine();
    }

    /**
     * Prints a horizontal separator line to the console.
     */
    public static void printHorizontalLine() {
        System.out.println("____________________________________");
    }

    /**
     * Extracts the task index number from a command string.
     *
     * @param message The raw user input.
     * @return The integer task number.
     */
    public static int getTaskNumber(String message) {
        String[] messageParts = message.split(" ", 2);
        return Integer.parseInt(messageParts[1]);
    }

    /**
     * Extracts the first word from the message to determine the command task type.
     */
    public static String getTaskType(String message) {
        String[] commandParts = message.split(" ", 2);
        return commandParts[0];
    }

    /**
     * Extracts the description by splitting at the first slash (/).
     */
    public static String getTaskDescription(String message) {
        String type = getTaskType(message);
        String details = message.replaceFirst(type, "").trim();
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
