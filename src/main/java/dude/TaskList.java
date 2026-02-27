package dude;

import dude.task.Task;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Helper to check if the index is within the bounds of the list.
     */
    private void validateIndex(int index) throws DudeException {
        if (index < 0 || index >= taskList.size()) {
            throw new DudeException("this task number is not valid");
        }
    }

    /**
     * Searches the task list for tasks whose description contains the keyword.
     *
     * @param keyword The string to search for.
     * @return A list of matching tasks.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public Task deleteTask(int index) throws DudeException {
        validateIndex(index);
        return taskList.remove(index);
    }

    public Task getTask(int index) throws DudeException {
        validateIndex(index);
        return taskList.get(index);
    }

    public ArrayList<Task> getAllTasks() {
        return this.taskList;
    }

    public int getSize() {
        return taskList.size();
    }
}
