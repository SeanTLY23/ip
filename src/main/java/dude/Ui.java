package dude;

import dude.task.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String HORIZONTAL_LINE = "____________________________________";
    private static final String LOGO =
            """
                     ____        _____
                    |  _ \\ _   _|  _ \\   ___
                    | | | | | | | | | |/  _ \\
                    | |_| | |_| | |_| |\\  __/
                    |____/ \\__,_|____/  \\___|
                    """;
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


    public void showGreeting() {
        showLine();
        showLogo();
        System.out.println("Hello! I'm Dude");
        System.out.println("What can I do for you?");
        showLine();

    }

    public void showExit() {
        System.out.println("Dude that's it? Okay Bye. See you again soon I hope.");
        showLine();
    }
}
