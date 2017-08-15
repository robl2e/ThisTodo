package com.robl2e.thistodo.ui.createtodo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.robl2e.thistodo.R;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;
import com.robl2e.thistodo.ui.util.KeyboardUtil;

/**
 * Created by robl2e on 8/14/2017.
 */

public class CreateTodoItemDialogFragment extends DialogFragment {
    private EditText etNewItem;

    public interface Listener {
        void onFinishedSaving(TodoItem todoItem);
    }
    public static CreateTodoItemDialogFragment newInstance() {
        CreateTodoItemDialogFragment fragment = new CreateTodoItemDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.create_todo);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View contentView = inflater.inflate(R.layout.dialog_create_todo_item, null);
        etNewItem = (EditText) contentView.findViewById(R.id.et_new_item);
        builder.setView(contentView);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showKeyboard();
    }

    private void showKeyboard() {
        if (getDialog() == null) return;
        if (getDialog().getWindow() == null) return;
        getDialog().getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void showDialog(AppCompatActivity activity, String tag) {
        FragmentManager fm = activity.getSupportFragmentManager();
        if (!isVisible()) {
            show(fm, tag);
        }
    }

    private void save() {
        String itemText = etNewItem.getText().toString();
        if (TextUtils.isEmpty(itemText)) return;
        TodoItem todoItem = new TodoItem(itemText);
        notifyListener(todoItem);
        KeyboardUtil.hideSoftInput(etNewItem);
    }

    private void notifyListener(TodoItem todoItem) {
        if (getActivity() instanceof Listener) {
            Listener listener = (Listener) getActivity();
            listener.onFinishedSaving(todoItem);
        }
    }
}
