package com.robl2e.thistodo.ui.createtodo;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.robl2e.thistodo.R;
import com.robl2e.thistodo.data.model.todoitem.Priority;
import com.robl2e.thistodo.data.model.todoitem.TodoItem;
import com.robl2e.thistodo.ui.util.KeyboardUtil;

import java.util.Calendar;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by robl2e on 8/15/2017.
 */

public class CreateTodoItemBottomDialog extends BottomDialog {
    private static final String TAG = CreateTodoItemBottomDialog.class.getSimpleName();
    private static final String TAG_DATE_PICKER = TAG + "_TAG_DATE_PICKER";
    private final View customView;
    private EditText etNewItem;
    private Button btnDatePicker;
    private Spinner spPriorityPicker;

    private DateTime dateTime;
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

    protected CreateTodoItemBottomDialog(Builder builder, final View customView, Listener listener) {
        super(builder);
        this.customView = customView;
        this.listener = listener;

        etNewItem = (EditText) customView.findViewById(R.id.et_new_item);
        btnDatePicker = (Button) customView.findViewById(R.id.btn_date_picker);
        spPriorityPicker = (AppCompatSpinner) customView.findViewById(R.id.spinner_priority_picker);
        setNewItemNameView();
        setDatePickerView();
        setPriorityPickerView();

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

    private void setNewItemNameView() {
        this.etNewItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    boolean successful = save();
                    if (successful) {
                        dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setDatePickerView() {
        dateTime = DateTime.now(TimeZone.getDefault());
        btnDatePicker.setText(getFormattedDateTime(dateTime));
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) customView.getContext();
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setPreselectedDate(dateTime.getYear(), getCompatibleMonth(), dateTime.getDay())
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                int compatibleMonth = monthOfYear + 1;
                                dateTime = DateTime.forDateOnly(year, compatibleMonth, dayOfMonth);
                                btnDatePicker.setText(getFormattedDateTime(dateTime));
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText(v.getResources().getString(R.string.done))
                        .setCancelText(v.getResources().getString(R.string.cancel));
                cdp.show(activity.getSupportFragmentManager(), TAG_DATE_PICKER);
            }
        });
    }

    private void setPriorityPickerView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spPriorityPicker.getContext()
                , R.array.priority_array, R.layout.item_priority_spinner);
        spPriorityPicker.setAdapter(adapter);
        spPriorityPicker.setSelection(1); // default to normal
    }

    @Override
    public void show() {
        super.show();
        etNewItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showSoftInput(etNewItem);
            }
        }, etNewItem.getResources()
                .getInteger(android.R.integer.config_shortAnimTime));
    }

    private int getCompatibleMonth() {
        if (dateTime == null) return 1;
        return dateTime.getMonth() - 1;
    }

    private String getInputName() {
        return etNewItem.getText().toString();
    }

    private Long getInputDateTime() {
        TimeZone timeZone = TimeZone.getDefault();
        if (dateTime == null) {
            dateTime = DateTime.now(timeZone);
        }
        return dateTime.getMilliseconds(timeZone);
    }
    private String getFormattedDateTime(DateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format("MM-DD-YYYY");
    }

    private String getPriority() {
        String priorityStr = (String) spPriorityPicker.getSelectedItem();
        return Priority.fromValue(priorityStr).getValue();
    }

    private boolean save() {
        String itemText = getInputName();
        if (TextUtils.isEmpty(itemText)) return false;

        TodoItem todoItem = new TodoItem(itemText);
        todoItem.setDueDate(getInputDateTime());
        todoItem.setPriority(getPriority());
        notifyListener(todoItem);
        return true;
    }

    private void notifyListener(TodoItem todoItem) {
        if (listener != null) {
            listener.onFinishedSaving(todoItem);
        }
    }
}
