package com.robl2e.thistodo.data.model.todoitem;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by robl2e on 8/17/2017.
 */

public enum Priority {
    HIGH("high"),
    NORMAL("normal"),
    LOW("low");

    private String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    public static Priority fromValue(String value) {
        if (TextUtils.isEmpty(value)) return NORMAL;

        for(Priority item : Priority.values()) {
            if(item.getValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return NORMAL;
    }
}