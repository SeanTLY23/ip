public class Dude {
    public static void printHorizontalLine() {
        System.out.println("____________________________________");
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
        System.out.println("Bye. Hope to see you again soon!");
        printHorizontalLine();
    }
}
