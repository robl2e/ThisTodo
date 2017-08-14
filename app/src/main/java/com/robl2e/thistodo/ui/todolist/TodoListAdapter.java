package com.robl2e.thistodo.ui.todolist;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robl2e on 8/12/2017.
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>{
    private List<TodoItem> items = new ArrayList<>();
    private MultiSelector multiSelector;

    public TodoListAdapter(List<TodoItem> items, MultiSelector multiSelector) {
        this.items = items;
        this.multiSelector = multiSelector;
    }

    static class ViewHolder extends SwappingHolder{
        private TextView tvItemName;
        ViewHolder(View itemView, MultiSelector multiSelector) {
            super(itemView, multiSelector);
            tvItemName = (TextView) itemView.findViewById(android.R.id.text1);
        }

        void setTodoName(String name) {
            tvItemName.setText(name);
        }
    }

    @Nullable
    public TodoItem getItem(int position) {
        try {
            return items.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(itemView, multiSelector);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem viewModel = items.get(position);
        holder.setTodoName(viewModel.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
