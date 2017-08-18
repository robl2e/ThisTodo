package com.robl2e.thistodo.ui.todolist;

import android.app.Activity;
import android.content.Intent;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.robl2e.thistodo.R;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;
import com.robl2e.thistodo.data.model.todoitem.TodoItemRepository;
import com.robl2e.thistodo.ui.common.ItemClickSupport;
import com.robl2e.thistodo.ui.createtodo.CreateTodoItemBottomDialog;
import com.robl2e.thistodo.ui.createtodo.CreateTodoItemDialogFragment;
import com.robl2e.thistodo.ui.edit.EditItemActivity;

import java.util.List;

public class TodoListActivity extends AppCompatActivity implements CreateTodoItemDialogFragment.Listener {
    private static final String TAG = TodoListActivity.class.getSimpleName();
    private RecyclerView lvItems;
    private TodoListAdapter listAdapter;
    private List<TodoItem> items;
    private MultiSelector multiSelector;
    private ActionMode currentActionMode;
    private ModalMultiSelectorCallback actionModeCallback;
    private FloatingActionButton fabNewItem;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TodoListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        bindViews();
        initializeList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditItemActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) return;
            if (!data.hasExtra(EditItemActivity.EXTRA_KEY_TODO_POSITION)) return;

            int position = data.getIntExtra(EditItemActivity.EXTRA_KEY_TODO_POSITION, -1);
            if (position < 0) return;

            TodoItem todoItem = listAdapter.getItem(position);
            if (todoItem == null) return;

            if (!data.hasExtra(EditItemActivity.EXTRA_KEY_TODO_NAME)) return;
            String name = data.getStringExtra(EditItemActivity.EXTRA_KEY_TODO_NAME);
            if (TextUtils.isEmpty(name)) return;

            todoItem.setName(name);
            TodoItemRepository.writeItems(items);
            updateListAdapter();
        }
    }

    private void bindViews() {
        lvItems = (RecyclerView) findViewById(R.id.lv_items);
        fabNewItem = (FloatingActionButton) findViewById(R.id.fab_new_item);
        fabNewItem.setOnClickListener(fabClickListener);
    }

    private void initializeList() {
        items = TodoItemRepository.readItems();
        multiSelector = new MultiSelector();
        actionModeCallback = new ActionModeCallback(multiSelector);
        listAdapter = new TodoListAdapter(items, multiSelector);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
        lvItems.setAdapter(listAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        lvItems.addItemDecoration(itemDecoration);
        ItemClickSupport.addTo(lvItems).setOnItemClickListener(itemClickListener);
        ItemClickSupport.addTo(lvItems).setOnItemLongClickListener(itemLongClickListener);
    }

    private void updateListAdapter() {
        listAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showCreateItemPrompt();
        }
    };

    private void showCreateItemPrompt() {
        CreateTodoItemBottomDialog dialog = CreateTodoItemBottomDialog.newInstance(this, new CreateTodoItemBottomDialog.Listener() {
            @Override
            public void onFinishedSaving(TodoItem todoItem) {
                items.add(todoItem);
                updateListAdapter();
                TodoItemRepository.writeItems(items);
            }
        });
        dialog.show();
    }

    private ItemClickSupport.OnItemClickListener itemClickListener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            TodoListAdapter.ViewHolder viewHolder =
                    (TodoListAdapter.ViewHolder) lvItems.findViewHolderForAdapterPosition(position);
            if (viewHolder == null) return;

            if (!multiSelector.tapSelection(viewHolder)){
                TodoItem todoItem = listAdapter.getItem(position);
                if (todoItem == null) return;
                EditItemActivity.startForResult(TodoListActivity.this, todoItem.getName(), position);
            } else {
                setActionModeTitle();
            }
        }
    };

    private ItemClickSupport.OnItemLongClickListener itemLongClickListener = new ItemClickSupport.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
            if (!multiSelector.isSelectable()) {
                TodoListAdapter.ViewHolder viewHolder =
                        (TodoListAdapter.ViewHolder) lvItems.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) return false;

                startSupportActionMode(actionModeCallback);
                multiSelector.setSelectable(true);
                multiSelector.setSelected(viewHolder, true); // selected
                setActionModeTitle();
                return true;
            }
            return false;
        }
    };

    private void setActionModeTitle() {
        if (currentActionMode != null) {
            int totalSelected = multiSelector.getSelectedPositions().size();
            currentActionMode.setTitle(getString(R.string.item_selected, totalSelected));
        }
    }

    @Override
    public void onFinishedSaving(TodoItem todoItem) {
        items.add(todoItem);
        updateListAdapter();
        TodoItemRepository.writeItems(items);
    }

    private class ActionModeCallback extends ModalMultiSelectorCallback {
        private MultiSelector multiSelector;

        ActionModeCallback(MultiSelector multiSelector) {
            super(multiSelector);
            this.multiSelector = multiSelector;
        }

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            currentActionMode = actionMode;
            getMenuInflater().inflate(R.menu.menu_todolist_action, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menu_delete) {
                for (int i = listAdapter.getItemCount(); i >= 0; i--) {
                    if (multiSelector.isSelected(i, 0)) { // (1)
                        // remove item from list
                        items.remove(i);
                    }
                }
                multiSelector.clearSelections();
                TodoItemRepository.writeItems(items);
                updateListAdapter();
                currentActionMode.finish();
                return true;
            }
            return false;
        }
    }
}

