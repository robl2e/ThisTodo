package com.robl2e.thistodo.data.model.todoitem;

/**
 * Created by robl2e on 8/12/2017.
 */

public class TodoItem {
    private String name;

    public TodoItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
