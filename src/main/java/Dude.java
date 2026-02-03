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
                System.out.println( "Dude OKAY. I've marked this task as done:\n " + taskList[index]);
                printHorizontalLine();
            } else {
                taskList[taskCount] = new Task(line);
                taskCount += 1;
                printHorizontalLine();
                System.out.println("Dude I added: " + line);
                printHorizontalLine();
            }
        }
        System.out.println("Dude that's it? Okay Bye. See you again soon I hope.");
        printHorizontalLine();
    }

    public static void main(String[] args) {
        String logo =
                " ____        _____        \n" + "|  _ \\ _   _|  _ \\   ___ \n" + "| | | | | | | | | |/  _ \\\n" +
                        "| |_| | |_| | |_| |\\  __/\n" + "|____/ \\__,_|____/  \\___|\n";
        printHorizontalLine();
        System.out.println(logo + "Hello! I'm Dude\nWhat can I do for you?");
        printHorizontalLine();
        respondToMessage();
    }
}
