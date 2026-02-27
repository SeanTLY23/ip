package dude;

import dude.task.Task;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages the user interface and interaction for the application.
 * This class is responsible for reading user input via the scanner and
 * displaying formatted messages, errors, and task information to the
 * console.
 */
public class Ui {
    private static final String HORIZONTAL_LINE = "____________________________________";
    private static final String LOGO =
            """
                     ____        _____
                    |  _ \\ _   _|  _ \\   ___
                    | | | | | | | | | |/  _ \\
                    | |_| | |_| | |_| |\\  __/
                    |____/ \\__,_|____/  \\___|""";
    private final Scanner in;

    public Ui() {
        this.in = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    public void showLogo() {
        System.out.println(LOGO);
    }

    /**
     * Reads the next line of input from the user.
     */
    public String readCommand() {
        return in.nextLine();
    }

    /**
     * Prints a confirmation message after a task is successfully added.
     *
     * @param task The task that was just added.
     * @param size The total number of tasks currently in the list.
     */
    public void showTaskCreated(Task task, int size) {
        showLine();
        System.out.println("Dude I got it. I've added this task:\n" + task);
        System.out.println("Now you have " + size + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a message confirming that a task has been removed.
     *
     * @param removedTask    The task that was deleted.
     * @param remainingCount The number of tasks left in the list.
     */
    public void showTaskDeleted(Task removedTask, int remainingCount) {
        showLine();
        System.out.println("Dude I've removed this task:\n" + removedTask);
        System.out.println("Now you have " + remainingCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays all tasks in the provided list with their index numbers.
     *
     * @param tasks The list of tasks to be displayed.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }


    public void showGreeting(ArrayList<String> oldTasks) {
        showLine();
        showLogo();
        System.out.println("Hello! I'm Dude");
        System.out.println("This was your previous saved list of tasks:");
        for (String line : oldTasks) {
            System.out.println(line);
        }
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays the status of the data file initialization.
     * Notifies the user whether a new file was created or an existing one was found.
     *
     * @param isNew True if a new file was created, false if it already existed.
     * @param path  The absolute file path where the data is stored.
     */
    public void showFileStatus(boolean isNew, String path) {
        if (isNew) {
            System.out.println("File created at: " + path);
        } else {
            System.out.println("File already exists at: " + path);
        }
    }

    /**
     * Displays feedback to the user when a task is marked or unmarked.
     *
     * @param task   The task that was modified.
     * @param isDone The new status of the task.
     */
    public void showMarkingFeedback(Task task, boolean isDone) {
        String feedback = isDone
                ? "Dude OKAY. I've marked this task as done:\n "
                : "Dude really? I've marked this task as not done yet:\n";

        showLine();
        System.out.println(feedback + task);
        showLine();
    }

    /**
     * Displays a message confirming the creation of a data directory.
     */
    public void showDirectoryCreated() {
        showLine();
        System.out.println("Dude I created a data directory");
        showLine();
    }

    /**
     * Displays the results of a keyword search.
     *
     * @param results The list of tasks that matched the search.
     */
    public void showSearchResults(ArrayList<Task> results) {
        showLine();
        if (results.isEmpty()) {
            System.out.println("Dude, I couldn't find any tasks matching that keyword.");
        } else {
            System.out.println("Dude, here are the matching tasks in your list:");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + "." + results.get(i));
            }
        }
        showLine();
    }

    /**
     * Displays an error message to the user in a styled format.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        showLine();
        System.out.println("Dude, " + message);
        showLine();
    }

    public void showExit() {
        System.out.println("Dude that's it? Okay Bye. See you again soon I hope.");
        showLine();
    }
}
