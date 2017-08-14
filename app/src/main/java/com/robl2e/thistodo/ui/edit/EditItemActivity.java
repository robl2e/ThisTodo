package com.robl2e.thistodo.ui.edit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.robl2e.thistodo.R;
import com.robl2e.thistodo.ui.common.ActivityRequestCodeGenerator;
import com.robl2e.thistodo.ui.util.KeyboardUtil;

public class EditItemActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = ActivityRequestCodeGenerator.getFreshInt();
    public static final String EXTRA_KEY_TODO_NAME = "EXTRA_KEY_TODO_NAME";
    public static final String EXTRA_KEY_TODO_POSITION = "EXTRA_KEY_TODO_POSITION";

    private EditText etItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItemName = (EditText) findViewById(R.id.et_item_name);
        initializeView();
    }

    private void initializeView() {
        if (getIntent() == null) return;
        if (getIntent().hasExtra(EXTRA_KEY_TODO_NAME)) {
            String name = getIntent().getStringExtra(EXTRA_KEY_TODO_NAME);
            etItemName.setText(name);
            if (etItemName.getText().length() > 0) {
                etItemName.setSelection(etItemName.getText().length());
            }
        }
    }

    public static void startForResult(Activity activity, String name, int position) {
        Intent intent = new Intent(activity, EditItemActivity.class);
        intent.putExtra(EXTRA_KEY_TODO_NAME, name);
        intent.putExtra(EXTRA_KEY_TODO_POSITION, position);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public void onSaveItem(View view) {
        Intent data = new Intent();
        data.putExtra(EXTRA_KEY_TODO_NAME, etItemName.getText().toString());

        if (getIntent() != null && getIntent().hasExtra(EXTRA_KEY_TODO_POSITION)) {
            int position = getIntent().getIntExtra(EXTRA_KEY_TODO_POSITION, -1);
            data.putExtra(EXTRA_KEY_TODO_POSITION, position); // ints work too
        }
        this.setResult(RESULT_OK, data);
        KeyboardUtil.hideSoftInput(etItemName);
        this.finish();
    }
}
