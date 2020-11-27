package com.example.snazzy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView recyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton todofab;
    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        db=new DatabaseHandler(this);
        db.openDatabase();

        taskList=new ArrayList<>();

        recyclerView=findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksAdapter=new ToDoAdapter(db,this);
        recyclerView.setAdapter(tasksAdapter);

        todofab=findViewById(R.id.todo_fab);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);

        todofab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();

    }
}