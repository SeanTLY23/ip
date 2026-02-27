package dude;

import dude.task.Deadline;
import dude.task.Event;
import dude.task.Task;
import dude.task.Todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Main class for the Dude chatbot.
 * Handles task management including adding, listing, and marking tasks.
 */
public class Dude {

    private static final Path FILE_PATH = Paths.get("data", "dude.txt");
    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(String.valueOf(FILE_PATH.toFile()));
    private static final TaskList taskList = new TaskList();

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
            ArrayList<Task> tasks = storage.load();
            for (Task t : tasks) {
                taskList.addTask(t);
            }
        } catch (FileNotFoundException e) {
            ui.showError("No previous file");
        }
    }

    /**
     * Ensures that the required directory and data file exist on the hard disk.
     * If the parent directory is missing, it is created. If the file is missing,
     * a new empty file is initialized.
     */
    private static void createTextFile() {
        try {
            if (storage.isParentDirectoryCreated()) {
                ui.showDirectoryCreated();
            }
            boolean fileCreatedNow = storage.isFileCreated();
            ui.showFileStatus(fileCreatedNow, storage.getAbsolutePath());
        } catch (IOException e) {
            ui.showError(e.getMessage());
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
            ui.showError(e.getMessage());
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
        ui.showTaskCreated(taskList.getTask(taskList.getSize() - 1), taskList.getSize());
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
            ui.showTaskList(taskList.getAllTasks());
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
        Task removed = taskList.deleteTask(index);
        ui.showTaskDeleted(removed, taskList.getSize());
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
        Task task = taskList.getTask(index);
        task.setDone(isDone);
        ui.showMarkingFeedback(task, isDone);
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
            taskList.addTask(new Todo(description));
            break;
        case "deadline":
            String by = Parser.getDeadlineDate(line);
            if (by.isEmpty()) {
                throw new DudeException("your deadline /by cannot be empty");
            }
            taskList.addTask(new Deadline(description, by));
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
            taskList.addTask(new Event(description, from, to));
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
        try {
            storage.save(taskList.getAllTasks());
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Displays initial welcome message.
     * Attempts to read and print the contents of the save file to provide context for the user
     * before the chatbot accepts new commands.
     */
    private static void printGreeting() {
        ArrayList<String> lines;
        try {
            lines = storage.getRawLines();
        } catch (FileNotFoundException e) {
            lines = new ArrayList<>();
        }
        ui.showGreeting(lines);
    }
}
