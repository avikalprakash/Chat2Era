package com.example.lue.erachat.Activity.Task;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.lue.erachat.R;

public class TaskDetail extends AppCompatActivity {
ListView taskDetail;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        taskDetail=(ListView)findViewById(R.id.ListParticipent);
        TaskDeatilAdaptor taskDeatilAdaptor=new TaskDeatilAdaptor(TaskDetail.this);
        taskDetail.setAdapter(taskDeatilAdaptor);
        send=(Button)findViewById(R.id.button10);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TaskDetail.this,AddTask.class);
                startActivity(intent);
            }
        });

    }
}
