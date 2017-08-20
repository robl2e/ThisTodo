package com.robl2e.thistodo.ui.todolist;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.loopeer.itemtouchhelperextension.Extension;
import com.robl2e.thistodo.R;
import com.robl2e.thistodo.data.model.todoitem.Priority;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

import static android.R.attr.button;
import static android.R.attr.direction;
import static com.robl2e.thistodo.R.string.done;

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

    static class ViewHolder extends SwappingHolder implements Extension {
        private View backgroundView;
        private View contentView;
        private TextView tvItemName;
        private TextView tvItemSubText;
        private TextView tvPriorityBadge;
        private ImageView ivStatusView;
        private TextView tvBackgroundText;

        ViewHolder(View itemView, MultiSelector multiSelector) {
            super(itemView, multiSelector);
            contentView = itemView.findViewById(R.id.content_view);
            backgroundView = itemView.findViewById(R.id.background_view);
            tvBackgroundText = (TextView) backgroundView.findViewById(R.id.text_background_view);
            tvItemName = (TextView) itemView.findViewById(R.id.tv_name);
            tvItemSubText = (TextView) itemView.findViewById(R.id.tv_subtext);
            tvPriorityBadge = (TextView) itemView.findViewById(R.id.tv_priority_badge);
            ivStatusView = (ImageView) itemView.findViewById(R.id.iv_status);
        }

        void bindView(TodoItem viewModel) {
            setItemNameView(viewModel.getName());
            setDueDateView(viewModel.getDueDate());
            setPriorityView(viewModel.getPriority());
            setItemStatusView(viewModel.getDone());
        }

        public View getForegroundView() {
            return contentView;
        }

        private void setItemNameView(String name) {
            tvItemName.setText(name);
        }

        private void setDueDateView(Long dueDate) {
            if (dueDate != null) {
                DateTime dateTime = DateTime.forInstant(dueDate, TimeZone.getDefault());
                String dateString = dateTime.format("MMM D, YYYY", Locale.getDefault());
                String displayStr = tvItemSubText.getResources().
                        getString(R.string.item_due_on, dateString);
                tvItemSubText.setText(displayStr);
            }
            tvItemSubText.setVisibility(dueDate != null ? View.VISIBLE : View.INVISIBLE);
        }

        private void setPriorityView(String priorityStr) {
            tvPriorityBadge.setVisibility(TextUtils.isEmpty(priorityStr) ? View.GONE : View.VISIBLE);

            Priority priority = Priority.fromValue(priorityStr);
            int badgeBackgroundColor = 0;
            switch (priority) {
                case HIGH:
                    badgeBackgroundColor = tvPriorityBadge
                            .getResources().getColor(R.color.red);
                    break;
                case NORMAL:
                    badgeBackgroundColor = tvPriorityBadge
                            .getResources().getColor(R.color.orange);
                    break;
                case LOW:
                    badgeBackgroundColor = tvPriorityBadge
                            .getResources().getColor(R.color.yellow);
                    break;
            }

            if (tvPriorityBadge.getBackground() != null) {
                Drawable drawable = tvPriorityBadge.getBackground();
                DrawableCompat.setTint(drawable.mutate(), badgeBackgroundColor);
            }
            tvPriorityBadge.setText(priority.getValue());
        }

        private void setItemStatusView(Boolean done) {
            if (done != null && done) {
                ivStatusView.setVisibility(View.VISIBLE);
                tvPriorityBadge.setVisibility(View.INVISIBLE);
            } else {
                ivStatusView.setVisibility(View.INVISIBLE);
                tvPriorityBadge.setVisibility(View.VISIBLE);
            }
        }

        void setBackgroundView(boolean isRightToLeft) {
            if (isRightToLeft) {
                backgroundView.setBackgroundColor(backgroundView.getResources()
                        .getColor(R.color.red));
                tvBackgroundText.setText(R.string.mark_not_done);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tvBackgroundText.getLayoutParams();
                params.gravity = Gravity.RIGHT|Gravity.END;
                tvBackgroundText.setLayoutParams(params);
            } else {
                backgroundView.setBackgroundColor(backgroundView.getResources()
                        .getColor(R.color.colorAccent));
                tvBackgroundText.setText(R.string.mark_done);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tvBackgroundText.getLayoutParams();
                params.gravity = Gravity.LEFT|Gravity.START;
                tvBackgroundText.setLayoutParams(params);
            }
        }

        @Override
        public float getActionWidth() {
            return backgroundView.getWidth();
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
        View itemView = inflater.inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(itemView, multiSelector);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem viewModel = items.get(position);
        holder.bindView(viewModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
