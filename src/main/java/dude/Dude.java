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
import java.util.ArrayList;

/**
 * Main class for the Dude chatbot.
 * Handles task management including adding, listing, and marking tasks.
 */
public class Dude {
    private static final ArrayList<Task> taskList = new ArrayList<>();
    private static final Path FILE_PATH = Paths.get("data", "dude.txt");
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        createTextFile();
        loadTasksFromFile();
        printGreeting();
        respondToMessage();
    }

    /**
     * Starts the data loading process. This method attempts to read
     * the saved file and populate the task list. If no file is
     * found, it notifies the user that a new list is being started.
     */
    private static void loadTasksFromFile() {
        try {
            convertSavedFileToCurrentList(String.valueOf(FILE_PATH.toFile()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
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
     * Reads the data file line by line and converts text entries into Task objects.
     * Each line is split at the "|" symbol to identify the task type:
     * Todo (T), Deadline (D), or Event (E). Based on this type, the method
     * creates the specific object and adds it to the list.
     *
     * @param filePath The location of the file to be read.
     * @throws FileNotFoundException If the file at the specified path cannot be found.
     */
    private static void printFileContents(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            System.out.println(s.nextLine());
        }
    }

    /**
     * Reads the specified file line-by-line and parses each entry into Task objects.
     * * The expected file format uses a pipe-delimited structure:
     * Type | Status | Description | [Date/Time info]
     * * @param filePath The string path to the saved data file.
     *
     * @throws FileNotFoundException If no file exists at the provided path.
     */

    private static void convertSavedFileToCurrentList(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String nextLine = s.nextLine();
            String[] parts = nextLine.split("\\|", 5);
            switch (parts[0].trim()) {
            case "T":
                taskList.add(taskList.size(), new Todo(parts[2].trim()));
                break;
            case "D":
                taskList.add(taskList.size(), new Deadline(parts[2].trim(), parts[3].trim()));
                break;
            case "E":
                taskList.add(taskList.size(),
                        new Event(parts[2].trim(), parts[3].trim(), parts[4].trim()));
                break;
            default:
                break;
            }

        }
    }

    /**
     * Processes user input in a loop until the 'bye' command is received.
     */
    public static void respondToMessage() {
        boolean isRunning = true;
        while (isRunning) {
            String line = ui.readCommand();
            if (Parser.isExit(line)) {
                isRunning = false;
            } else {
                handleLineCommand(line);
            }
        }
        ui.showExit();
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
            ui.showLine();
            System.out.println(e.getMessage());
            ui.showLine();
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
        addTaskByType(line);
        ui.showTaskCreated(taskList.get(taskList.size() - 1), taskList.size());
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
        String command = Parser.getTaskType(line).toLowerCase();
        switch (command) {
        case "list":
            ui.showTaskList(taskList);
            return true;
        case "unmark":
            handleMarking(line, false);
            saveAllTasks();
            return true;
        case "mark":
            handleMarking(line, true);
            saveAllTasks();
            return true;
        case "delete":
            handleDeletion(line);
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
     * Removes a task from the list based on the user-provided index.
     * Validates the index to ensure it falls within the current list range,
     * and updates the total task count.
     *
     * @param line The raw user input containing the index of the task to be deleted.
     * @throws DudeException If the provided task number is invalid or out of bounds.
     */
    private static void handleDeletion(String line) throws DudeException {
        int index = Parser.getTaskNumber(line) - 1;
        if (index >= taskList.size() || index < 0) {
            throw new DudeException("this task number is not valid");
        }
        Task removed = taskList.remove(index);
        ui.showTaskDeleted(removed, taskList.size());
    }

    /**
     * Updates the completion status of a task and provides feedback to the user.
     *
     * @param line   The raw user input containing the task index.
     * @param isDone The new status to set (true for marked, false for unmarked).
     * @throws DudeException If the task number is out of the valid range of the current list.
     */
    private static void handleMarking(String line, boolean isDone) throws DudeException {
        int index = Parser.getTaskNumber(line) - 1;
        if (index >= taskList.size() || index < 0) {
            throw new DudeException("this task number is not valid");
        }
        taskList.get(index).setDone(isDone);
        String feedback = isDone
                ? "Dude OKAY. I've marked this task as done:\n "
                : "Dude really? I've marked this task as not done yet:\n";
        ui.showLine();
        System.out.println(feedback + taskList.get(index));
        ui.showLine();
    }


    /**
     * Validates and adds the corresponding task type to the task list.
     *
     * @param line The raw user input containing the task type and details.
     * @throws DudeException If any required part of the task is missing.
     */
    private static void addTaskByType(String line) throws DudeException {
        String command = Parser.getTaskType(line).toLowerCase();
        String description = Parser.getTaskDescription(line);
        if (description.isEmpty()) {
            throw new DudeException("your " + command + " task cannot be empty");
        }
        switch (command) {
        case "todo":
            taskList.add(taskList.size(), new Todo(description));
            break;
        case "deadline":
            String by = Parser.getDeadlineDate(line);
            if (by.isEmpty()) {
                throw new DudeException("your deadline /by cannot be empty");
            }
            taskList.add(new Deadline(description, by));
            break;
        case "event":
            String from = Parser.getEventFromTime(line);
            String to = Parser.getEventToTime(line);
            if (from.isEmpty() || to.isEmpty()) {
                throw new DudeException("your event /from or /to cannot be empty");
            }
            if (to.contains("/from")) {
                throw new DudeException("your /from must be before /to");
            }
            taskList.add(new Event(description, from, to));
            break;
        default:
            break;
        }
        saveAllTasks();
    }

    /**
     * Overwrites the save file with the current list of tasks from memory.
     */
    private static void saveAllTasks() {
        try (FileWriter fw = new FileWriter(FILE_PATH.toFile(), false)) {
            for (Task task : taskList) {
                fw.write(formatTaskForFile(task) + System.lineSeparator());
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
            details = t.getTaskName() + " | " + ((Event) t).getFrom() + " | " + ((Event) t).getTo();
        }
        return type + " | " + status + " | " + details;
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
        ui.showLine();
        System.out.println(logo + "Hello! I'm Dude");
        System.out.println("This was your previous saved list of tasks:");
        try {
            printFileContents(String.valueOf(FILE_PATH.toFile()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        System.out.println("What can I do for you?");
        ui.showLine();
    }

}
