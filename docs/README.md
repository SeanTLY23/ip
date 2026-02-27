# Dude User Guide

Dude is a lightweight, CLI-based task management assistant that helps you track your life without the fluff. 
Whether it's a simple task or a complex event, Dude's got your back.

## Quick Start

1. **Ensure Java is installed**: You must have **Java 17** or above installed in your computer.
    * **Mac users**: Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
2. **Download the App**: Download the latest `dude.jar` file from [here](https://github.com/SEANTLY23/ip/releases).
3. **Setup Folder**: Copy the file to the folder you want to use as the **home folder** for Dude.
4. **Launch Dude**:
    * Open a command terminal.
    * `cd` into the folder where you placed the `.jar` file.
    * Run the command: `java -jar dude.jar`
5. **Start Chatting**: Once the "Dude" logo appears, you are ready to go! Type your command in the terminal and press **Enter** to execute it.

---

### Sample Commands to try:

* `list` : Lists all tasks currently in your list.
* `todo borrow book` : Adds a new Todo task called "borrow book".
* `deadline return book /by Sunday` : Adds a Deadline task with a due date.
* `delete 3` : Deletes the 3rd task shown in the current list.
* `bye` : Exits the app.

> Refer to the [Features](#features) section below for details of each command and advanced syntax.

## Features

### Adding a todo: `todo`
Adds a task to the list without any specific deadline or time.
Format: `todo DESCRIPTION`

Examples:
* `todo read book`
* `todo join library`

### Adding a deadline: `deadline`
Adds a task that needs to be completed by a specific date or time.
Format: `deadline DESCRIPTION /by DATE`

Examples:
* `deadline return book /by Sunday`
* `deadline submit report /by Monday 2pm`

### Adding an event: `event`
Adds a task that occurs within a specific time frame.
Format: `event DESCRIPTION /from START /to END`

Examples:
* `event career fair /from 2pm /to 6pm`
* `event project meeting /from Monday 2pm /to Monday 4pm`

### Listing all tasks : `list`
Shows a list of all tasks currently in your list.
Format: `list`

### Marking a task as done : `mark`
Marks a task as completed.
Format: `mark INDEX`
* Marks the task at the specified `INDEX` as done.
* The index refers to the index number shown in the displayed task list.
* The index **must be a positive integer** 1, 2, 3, …

Example:
* `mark 2` Marks the 2nd task in the list as done.

### Unmarking a task : `unmark`
Marks a task as not done yet.
Format: `unmark INDEX`
* Marks the task at the specified `INDEX` as not done.
* The index refers to the index number shown in the displayed task list.

### Deleting a task : `delete`
Deletes the specified task from the list.
Format: `delete INDEX`
* Deletes the task at the specified `INDEX`.
* The index refers to the index number shown in the displayed task list.

### Locating tasks by keyword: `find`
Finds tasks whose descriptions contain the given keyword.
Format: `find KEYWORD`
* The search is **case-sensitive**. e.g. `book` will **not** match `Book`.
* Only the description is searched.
* Tasks matching the keyword will be returned.

Example:
* `find book` returns tasks like `read book` and `return book`, but not `Book club`.

### Exiting the program : `bye`
Exits the program.
Format: `bye`

### Saving the data
Dude data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file
Dude data are saved automatically as a text file `[JAR file location]/data/dude.txt`. 
