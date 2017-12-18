package com.example.lue.erachat.Activity.Task;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.lue.erachat.R;

public class AddTask extends AppCompatActivity {
EditText Addmember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_imageview_addtask, null);
        actionBar.setCustomView(v);
        Addmember=(EditText)findViewById(R.id.editText7);
        Addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssignTask dialogFragment = new AssignTask();

                FragmentManager fm = getFragmentManager();
                dialogFragment.show(fm, "Sample Fragment");
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
