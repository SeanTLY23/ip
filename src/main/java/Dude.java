import java.util.Scanner;

public class Dude {
    private static Task[] taskList = new Task[100];
    private static int taskCount = 0;
    public static void printHorizontalLine() {
        System.out.println("____________________________________");
    }
    public static int getTaskNumber(String message) {
        message = message.replace(" ","");
        message = message.replace("unmark","");
        message = message.replace("mark","");
        return Integer.parseInt(message);
    }

    public static void respondToMessage(){
        Scanner in = new Scanner(System.in);
        String line;
        while (true) {
            line = in.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            }
            else if (line.equalsIgnoreCase("list")) {
                printHorizontalLine();
                for (int i = 0; i < taskCount; i++){
                    System.out.println((i + 1) + ".[" + taskList[i].getStatusIcon() + "] " + taskList[i].getTask());
                }
                printHorizontalLine();
            }
            else if (line.startsWith("unmark")) {
                int index = getTaskNumber(line) - 1;
                printHorizontalLine();
                taskList[index].setIsDone(false);
                System.out.println("Dude really? I've marked this task as not done yet:\n  [" + taskList[index].getStatusIcon() + "] " + taskList[index].getTask());
                printHorizontalLine();
            }
            else if (line.startsWith("mark")) {
                int index = getTaskNumber(line) - 1;
                printHorizontalLine();
                taskList[index].setIsDone(true);
                System.out.println("Dude OKAY. I've marked this task as done:\n  [" + taskList[index].getStatusIcon() + "] " + taskList[index].getTask());
                printHorizontalLine();
            }
            else {
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
        String logo = " ____        _____        \n"
                    + "|  _ \\ _   _|  _ \\   ___ \n"
                    + "| | | | | | | | | |/  _ \\\n"
                    + "| |_| | |_| | |_| |\\  __/\n"
                    + "|____/ \\__,_|____/  \\___|\n";
        printHorizontalLine();
        System.out.println(logo + "Hello! I'm Dude\nWhat can I do for you?");
        printHorizontalLine();
        respondToMessage();
    }
}
