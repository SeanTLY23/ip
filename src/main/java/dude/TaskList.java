package dude;

import dude.task.Task;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.taskList = tasks;
    }

    /**
     * Helper to check if the index is within the bounds of the list.
     */
    private void validateIndex(int index) throws DudeException {
        if (index < 0 || index >= taskList.size()) {
            throw new DudeException("this task number is not valid");
        }
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
