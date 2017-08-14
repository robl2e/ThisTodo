package com.robl2e.thistodo.data.model.todoitem;

import android.content.Context;
import android.support.annotation.NonNull;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robl2e on 8/13/2017.
 */

public class TodoItemPersistence {
    private static final String TODO_FILE_NAME = "todo.txt";

    @NonNull
    public static List<TodoItem> readItems(Context context) {
        List<String> todoItems = null;
        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE_NAME);
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            todoItems = new ArrayList<>();
        }
        return convertToTodoItem(todoItems);
    }

    public static void writeItems(Context context, List<TodoItem> todoItems) {
        if (todoItems == null) return;
        if (todoItems.isEmpty()) return;

        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, convertToString(todoItems));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private static List<TodoItem> convertToTodoItem(List<String> fromList) {
        List<TodoItem> todoItems = new ArrayList<>();

        if (fromList == null) return todoItems;

        for (String name : fromList) {
            todoItems.add(new TodoItem(name));
        }
        return todoItems;
    }

    @NonNull
    private static List<String> convertToString(List<TodoItem> fromList) {
        List<String> todoStrings = new ArrayList<>();

        if (fromList == null) return todoStrings;

        for (TodoItem todoItem : fromList) {
            todoStrings.add(todoItem.getName());
        }
        return todoStrings;
    }
}
