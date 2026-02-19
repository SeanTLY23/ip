package dude;

import dude.task.Deadline;
import dude.task.Event;
import dude.task.Task;
import dude.task.Todo;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;
import java.io.File;

/**
 * Main class for the Dude chatbot.
 * Handles task management including adding, listing, and marking tasks.
 */
public class Dude {
    public static final int MAX_TASKS = 100;
    private static Task[] taskList = new Task[MAX_TASKS];
    private static int taskCount = 0;
    private static final Path FILE_PATH = Paths.get("data", "dude.txt");

    public static void main(String[] args) {
        createTextFile();
        printGreeting();
        respondToMessage();
    }

    /**
     * Ensures that the required directory and data file exist on the hard disk.
     * If the parent directory is missing, it is created. If the file is missing,
     * a new empty file is initialized.
     */
    private static void createTextFile() {
        File f = FILE_PATH.toFile();
        File parent = f.getParentFile();
        try {
            if (parent != null && !parent.exists()) {
                boolean success = parent.mkdirs();
                if (success) {
                    System.out.println("Dude I created a data directory");
                }
            }
            if (f.createNewFile()) {
                System.out.println("File created at: " + f.getAbsolutePath());
            } else {
                System.out.println("File already exists at: " + f.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This is used to display the raw saved task data to the user during startup.
     *
     * @param filePath The string path of the file to be read.
     * @throws FileNotFoundException If the file at the specified path does not exist.
     */
    private static void printFileContents(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            System.out.println(s.nextLine());
        }
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
     * Manages command flow by handling terminal actions and coordinates the addition of new tasks
     *
     * @param line The raw string input from the user.
     * @throws DudeException If the command is invalid or the task limit is reached.
     */
    private static void processMessage(String line) throws DudeException {
        if (isTerminalCommand(line)) return;
        taskExceeded();
        addTaskByType(line);
        taskCreatedMessage();
    }

    /**
     * Validates the command syntax and executes non-task actions like list or mark.
     *
     * @param line The raw user input string.
     * @return true if the command was a non-task action (terminal);
     * false if it is a valid task that needs to be added to the list.
     * @throws DudeException If the input is empty, the command is unknown,
     *                       or specific keywords (like /by) are missing.
     */
    private static boolean isTerminalCommand(String line) throws DudeException {
        String filteredMessage = line.trim();
        if (filteredMessage.isEmpty()) {
            throw new DudeException("your message cannot be empty.");
        }
        String command = getTaskType(line).toLowerCase();
        switch (command) {
        case "list":
            listTasks();
            return true;
        case "unmark":
            handleMarking(line, false);
            saveAllTasks();
            return true;
        case "mark":
            handleMarking(line, true);
            saveAllTasks();
            return true;
        case "deadline":
            if (!filteredMessage.contains("/by")) {
                throw new DudeException("deadline task must have a /by.");
            }
            break;
        case "event":
            if (!filteredMessage.contains("/from") || !filteredMessage.contains("/to")) {
                throw new DudeException("event task must have a /from and a /to.");
            }
            break;
        case "todo":
            if (!filteredMessage.contains(" ")) {
                throw new DudeException("your todo task cannot be empty.");
            }
            break;
        default:
            throw new DudeException("only the following commands are valid: list,mark,unmark,deadline,event or todo.");
        }
        return false;
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
     * @throws DudeException If the task number is out of the valid range of the current list.
     */
    private static void handleMarking(String line, boolean isDone) throws DudeException {
        int index = getTaskNumber(line) - 1;
        if (index >= taskCount || index < 0) {
            throw new DudeException("this task number is not valid");
        }
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
     * Validates and adds the corresponding task type to the task list.
     *
     * @param line The raw user input containing the task type and details.
     * @throws DudeException If any required part of the task is missing.
     */
    private static void addTaskByType(String line) throws DudeException {
        switch (getTaskType(line).toLowerCase()) {
        case "todo":
            taskList[taskCount] = new Todo(getTaskDescription(line));
            break;
        case "deadline":
            if (getTaskDescription(line).isEmpty()) {
                throw new DudeException("your deadline task cannot be empty");
            }
            if (getDeadlineDate(line).isEmpty()) {
                throw new DudeException("your deadline /by cannot be empty");
            }
            taskList[taskCount] = new Deadline(getTaskDescription(line), getDeadlineDate(line));
            break;
        case "event":
            if (getTaskDescription(line).isEmpty()) {
                throw new DudeException("your event task cannot be empty");
            }
            if (getEventFromTime(line).isEmpty() || getEventToTime(line).isEmpty()) {
                throw new DudeException("your event /from or /to cannot be empty");
            }
            if (getEventToTime(line).contains("/from")) {
                throw new DudeException("your /from must be before /to");
            }
            taskList[taskCount] =
                    new Event(getTaskDescription(line), getEventFromTime(line), getEventToTime(line));
            break;
        default:
            break;
        }
        taskCount += 1;
        saveAllTasks();
    }

    /**
     * Overwrites the save file with the current list of tasks from memory.
     */
    private static void saveAllTasks() {
        try (FileWriter fw = new FileWriter(FILE_PATH.toFile(), false)) {
            for (int i = 0; i < taskCount; i++) {
                fw.write(formatTaskForFile(taskList[i]) + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Dude, I couldn't save the changes: " + e.getMessage());
        }
    }

    /**
     * Converts a Task object into a formatted string for file storage.
     * The format used is "Type | Status | Description | [Optional Dates]".
     *
     * @param t The task to be formatted.
     * @return A string representation of the task to be displayed in the text file.
     */
    private static String formatTaskForFile(Task t) {
        String status = t.isDone() ? "1" : "0";
        String type = "";
        String details = "";
        if (t instanceof Todo) {
            type = "T";
            details = t.getTaskName();
        } else if (t instanceof Deadline) {
            type = "D";
            details = t.getTaskName() + " | " + ((Deadline) t).getBy();
        } else if (t instanceof Event) {
            type = "E";
            details = t.getTaskName() + " | " + ((Event) t).getFrom() + "-" + ((Event) t).getTo();
        }
        return type + " | " + status + " | " + details;
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
     * Attempts to read and print the contents of the save file to provide context for the user
     * before the chatbot accepts new commands.
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
        System.out.println(logo + "Hello! I'm Dude");
        System.out.println("This was your previous saved list of tasks:");
        try {
            printFileContents(String.valueOf(FILE_PATH.toFile()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        System.out.println("What can I do for you?");
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
