package dude;

import dude.task.Deadline;
import dude.task.Event;
import dude.task.Task;
import dude.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Attempts to create the parent directory if it does not exist.
     *
     */
    public boolean isParentDirectoryCreated() {
        File f = filePath.toFile();
        File parent = f.getParentFile();

        if (parent != null && !parent.exists()) {
            return parent.mkdirs();
        }
        return false;
    }

    public boolean isFileCreated() throws IOException {
        File f = filePath.toFile();
        return f.createNewFile();
    }

    /**
     * Saves the current task list to the hard disk.
     * * @param tasks The list of tasks to be written to the file.
     *
     * @throws IOException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        try (FileWriter fw = new FileWriter(filePath.toFile(), false)) {
            for (Task task : tasks) {
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
        }
    }

    public String getAbsolutePath() {
        return filePath.toFile().getAbsolutePath();
    }

    /**
     * Loads tasks from the data file and populates an ArrayList of Task objects.
     * The method parses each line using the pipe ("|") delimiter and reconstructs
     * the specific task type (Todo, Deadline, or Event) along with its completion status.
     *
     * @return An ArrayList containing the Task objects read from the file.
     * @throws FileNotFoundException If the save file does not exist at the specified path.
     */
    public ArrayList<Task> load() throws FileNotFoundException {
        ArrayList<Task> loadedTasks = new ArrayList<>();
        File f = filePath.toFile();
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String nextLine = s.nextLine();
                String[] parts = nextLine.split("\\|", 5);
                if (parts.length < 3) continue;

                String type = parts[0].trim();
                boolean isDone = parts[1].trim().equals("1");
                String desc = parts[2].trim();

                Task task = null;
                switch (type) {
                case "T":
                    task = new Todo(desc);
                    break;
                case "D":
                    task = new Deadline(desc, parts[3].trim());
                    break;
                case "E":
                    task = new Event(desc, parts[3].trim(), parts[4].trim());
                    break;
                }
                if (task != null) {
                    if (isDone) task.setDone(true);
                    loadedTasks.add(task);
                }
            }
        }
        return loadedTasks;
    }

    /**
     * Reads the raw lines from the file to display as a preview.
     *
     * @return A list of strings representing each line in the file.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    public ArrayList<String> getRawLines() throws FileNotFoundException {
        ArrayList<String> lines = new ArrayList<>();
        File f = filePath.toFile();
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            lines.add(s.nextLine());
        }
        return lines;
    }
}
