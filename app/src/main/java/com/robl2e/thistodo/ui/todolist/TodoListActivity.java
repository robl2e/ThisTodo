package com.robl2e.thistodo.ui.todolist;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.robl2e.thistodo.R;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;
import com.robl2e.thistodo.data.model.todoitem.TodoItemRepository;
import com.robl2e.thistodo.ui.common.ItemClickSupport;
import com.robl2e.thistodo.ui.createtodo.CreateTodoItemBottomDialog;
import com.robl2e.thistodo.ui.edit.EditItemActivity;

import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private static final String TAG = TodoListActivity.class.getSimpleName();
    private RecyclerView lvItems;
    private FloatingActionButton fabNewItem;
    private View emptyView;

    private TodoListAdapter listAdapter;
    private List<TodoItem> items;
    private MultiSelector multiSelector;
    private ActionMode currentActionMode;
    private ModalMultiSelectorCallback actionModeCallback;
    public ItemTouchHelperExtension itemTouchHelperExtension;
    public ItemTouchHelperExtension.Callback callback;

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

    private void displayCurrentStateView() {
        if (items != null && items.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            lvItems.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            lvItems.setVisibility(View.VISIBLE);
        }
    }

    private void bindViews() {
        lvItems = (RecyclerView) findViewById(R.id.lv_items);
        emptyView = findViewById(R.id.empty_view_container);
        fabNewItem = (FloatingActionButton) findViewById(R.id.fab_new_item);
        fabNewItem.setOnClickListener(fabClickListener);
    }

    private void initializeList() {
        items = TodoItemRepository.readItems();
        displayCurrentStateView();
        multiSelector = new MultiSelector();
        actionModeCallback = new ActionModeCallback(multiSelector);
        callback = new ItemTouchHelperCallback();
        itemTouchHelperExtension = new ItemTouchHelperExtension(callback);
        itemTouchHelperExtension.attachToRecyclerView(lvItems);
        listAdapter = new TodoListAdapter(items, multiSelector);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
        lvItems.setAdapter(listAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        lvItems.addItemDecoration(itemDecoration);
        lvItems.setItemAnimator(new AlphaCrossFadeAnimator());
        ItemClickSupport.addTo(lvItems).setOnItemClickListener(itemClickListener);
        ItemClickSupport.addTo(lvItems).setOnItemLongClickListener(itemLongClickListener);
    }

    private void updateListAdapter() {
        listAdapter.notifyDataSetChanged();
        displayCurrentStateView();
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showCreateItemPrompt();
        }
    };

    private void showCreateItemPrompt() {
        showEditItemPrompt(null, RecyclerView.NO_POSITION);
    }

    private void showEditItemPrompt(TodoItem todoItem, Integer position) {
        Pair<Integer, TodoItem> pairItem = null;
        if (todoItem != null) {
            pairItem = new Pair<>(
                    position, todoItem);
        }
        CreateTodoItemBottomDialog dialog = CreateTodoItemBottomDialog.newInstance(this, pairItem, new CreateTodoItemBottomDialog.Listener() {
            @Override
            public void onFinishedSaving(TodoItem todoItem) {
                items.add(todoItem);
                updateListAdapter();
                TodoItemRepository.writeItems(items);
            }

            @Override
            public void onFinishedEditing(TodoItem todoItem, int position) {
                items.remove(position);
                items.add(position, todoItem);
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

            if (!multiSelector.tapSelection(viewHolder)) {
                TodoItem todoItem = listAdapter.getItem(position);
                if (todoItem == null) return;
                showEditItemPrompt(todoItem, position);
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
                        listAdapter.notifyItemRemoved(i);
                    }
                }
                multiSelector.clearSelections();
                displayCurrentStateView();
                TodoItemRepository.writeItems(items);
                currentActionMode.finish();
                return true;
            }
            return false;
        }
    }

    private class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START|ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            TodoItem todoItem = listAdapter.getItem(position);
            if (todoItem != null) {
                if (direction == ItemTouchHelper.END) {
                    todoItem.setDone(true);
                } else if (direction == ItemTouchHelper.START){
                    todoItem.setDone(false);
                }
                listAdapter.notifyItemChanged(position);
                TodoItemRepository.writeItems(items);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (dY != 0 && dX == 0) super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            TodoListAdapter.ViewHolder holder = (TodoListAdapter.ViewHolder) viewHolder;
            holder.getForegroundView().setTranslationX(dX);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                boolean rightToLeft = dX < 0;
                holder.setBackgroundView(rightToLeft);
            }
        }
    }
}

