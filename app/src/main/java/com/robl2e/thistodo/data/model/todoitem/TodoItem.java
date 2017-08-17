package com.robl2e.thistodo.data.model.todoitem;

/**
 * Created by robl2e on 8/12/2017.
 */

public class TodoItem {
    private String name;
    private Boolean isDone;
    private Long dueDate;

    public TodoItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Long getDueDate() {
        return dueDate;
    }
}
