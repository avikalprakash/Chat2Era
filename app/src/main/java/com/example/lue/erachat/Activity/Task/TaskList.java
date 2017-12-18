package com.example.lue.erachat.Activity.Task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lue.erachat.R;

/**
 * Created by lue on 14-06-2017.
 */

public class TaskList extends Fragment {
ListView Tasklist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmennt_task_list, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tasklist=(ListView)view.findViewById(R.id.TaskList);
        TaskListAdaptor taskListAdaptor=new TaskListAdaptor(getActivity());
        Tasklist.setAdapter(taskListAdaptor);
        Tasklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),TaskDetail.class);
                startActivity(intent);
            }
        });
    }
}
