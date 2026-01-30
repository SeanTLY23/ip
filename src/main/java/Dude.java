import java.util.Scanner;

public class Dude {
    private static List[] taskList = new List[100];
    private static int taskCount = 0;
    public static void printHorizontalLine() {
        System.out.println("____________________________________");
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
                    System.out.println((i + 1) + ". " + taskList[i].getList());
                }
                printHorizontalLine();
            }
            else {
                taskList[taskCount] = new List(line);
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
