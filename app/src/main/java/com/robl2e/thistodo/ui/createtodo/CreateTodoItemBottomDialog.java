package com.robl2e.thistodo.ui.createtodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.robl2e.thistodo.R;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;
import com.robl2e.thistodo.ui.util.KeyboardUtil;

/**
 * Created by robl2e on 8/15/2017.
 */

public class CreateTodoItemBottomDialog extends BottomDialog {
    private final View customView;
    private EditText etNewItem;
    private Listener listener;

    public interface Listener {
        void onFinishedSaving(TodoItem todoItem);
    }
    public static CreateTodoItemBottomDialog newInstance(Context context, Listener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.dialog_create_todo_item, null);

        BottomDialog.Builder builder = new BottomDialog.Builder(context)
                .setTitle(R.string.create_todo)
                .setNegativeText(R.string.cancel)
                .setPositiveText(R.string.save)
                .setCustomView(customView);

        return new CreateTodoItemBottomDialog(builder, customView, listener);
    }

    protected CreateTodoItemBottomDialog(Builder builder, View customView, Listener listener) {
        super(builder);
        this.customView = customView;
        this.listener = listener;

        this.etNewItem = (EditText) customView.findViewById(R.id.et_new_item);
        this.etNewItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    save();
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        builder.onPositive(new ButtonCallback() {
            @Override
            public void onClick(@NonNull BottomDialog bottomDialog) {
                save();
                dismiss();
            }
        });
        builder.onNegative(new ButtonCallback() {
            @Override
            public void onClick(@NonNull BottomDialog bottomDialog) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        etNewItem.post(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showSoftInput(etNewItem);
            }
        });
    }

    private void save() {
        String itemText = etNewItem.getText().toString();
        if (TextUtils.isEmpty(itemText)) return;
        TodoItem todoItem = new TodoItem(itemText);
        notifyListener(todoItem);
    }

    private void notifyListener(TodoItem todoItem) {
        if (listener != null) {
            listener.onFinishedSaving(todoItem);
        }
    }
}
