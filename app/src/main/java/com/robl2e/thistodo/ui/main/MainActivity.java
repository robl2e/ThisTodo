package com.robl2e.thistodo.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robl2e.thistodo.ui.todolist.TodoListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TodoListActivity.start(this);
        finish();
    }
}
