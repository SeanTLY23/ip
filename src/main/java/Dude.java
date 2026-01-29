import java.util.Scanner;

public class Dude {
    public static void printHorizontalLine() {
        System.out.println("____________________________________");
    }

    public static void echoMessage(){
        Scanner in = new Scanner(System.in);
        String line;
        while (true) {
            line = in.nextLine();
            if (line.equals("bye")){
                break;
            }
            printHorizontalLine();
            System.out.println("Dude " + line);
            printHorizontalLine();
        }
        System.out.println("Dude that's it? Okay Bye. See you again soon I hope");
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
        echoMessage();
    }
}
