import java.util.Scanner;

/**
 * Main class for the Dude chatbot.
 * Handles task management including adding, listing, and marking tasks.
 */
public class Dude {
    private static Task[] taskList = new Task[100];
    private static int taskCount = 0;

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
        message = message.replace(" ", "");
        message = message.replace("unmark", "");
        message = message.replace("mark", "");
        return Integer.parseInt(message);
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

    /**
     * Processes user input in a loop until the 'bye' command is received.
     */
    public static void respondToMessage() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            } else if (line.equalsIgnoreCase("list")) {
                printHorizontalLine();
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + taskList[i]);
                }
                printHorizontalLine();
            } else if (line.startsWith("unmark")) {
                int index = getTaskNumber(line) - 1;
                printHorizontalLine();
                taskList[index].setDone(false);
                System.out.println(
                        "Dude really? I've marked this task as not done yet:\n" + taskList[index]);
                printHorizontalLine();
            } else if (line.startsWith("mark")) {
                int index = getTaskNumber(line) - 1;
                printHorizontalLine();
                taskList[index].setDone(true);
                System.out.println("Dude OKAY. I've marked this task as done:\n " + taskList[index]);
                printHorizontalLine();
            } else {
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
                printHorizontalLine();
                System.out.println(
                        "Dude I got it. I've added this task:\n" + taskList[taskCount - 1] + "\nNow you have "
                                + taskCount + " tasks in the list.");
                printHorizontalLine();
            }
        }
        System.out.println("Dude that's it? Okay Bye. See you again soon I hope.");
        printHorizontalLine();
    }

    public static void main(String[] args) {
        String logo =
                " ____        _____\n" + "|  _ \\ _   _|  _ \\   ___\n" + "| | | | | | | | | |/  _ \\\n"
                        + "| |_| | |_| | |_| |\\  __/\n" + "|____/ \\__,_|____/  \\___|\n";
        printHorizontalLine();
        System.out.println(logo + "Hello! I'm Dude\nWhat can I do for you?");
        printHorizontalLine();
        respondToMessage();
    }
}
