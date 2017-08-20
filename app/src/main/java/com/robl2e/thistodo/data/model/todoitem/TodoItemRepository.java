package com.robl2e.thistodo.data.model.todoitem;

import android.support.annotation.NonNull;

import com.robl2e.thistodo.data.local.LocalPersistence;
import java.util.ArrayList;
import java.util.List;

import static com.robl2e.thistodo.data.local.PersistenceRegistry.TODO_ITEM_LIST;

/**
 * Created by robl2e on 8/13/2017.
 */

public class TodoItemRepository {
    @NonNull
    public static List<TodoItem> readItems() {
        return LocalPersistence.getInstance().read(
                TODO_ITEM_LIST, new ArrayList<TodoItem>());
    }

    public static void writeItems(List<TodoItem> todoItems) {
        if (todoItems == null || todoItems.isEmpty()) {
            LocalPersistence.getInstance().delete(TODO_ITEM_LIST);
            return;
        }
        LocalPersistence.getInstance().write(TODO_ITEM_LIST, todoItems);
    }
}
