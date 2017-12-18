package com.example.lue.erachat.Activity.Task;

import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lue.erachat.R;

public class AssignTask extends DialogFragment {
ListView AssignList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_assign_task, container, false);
        AssignList=(ListView)rootView.findViewById(R.id.AssignList);
        AssignTaskAdaptor assignTaskAdaptor=new AssignTaskAdaptor((FragmentActivity) getActivity());
        AssignList.setAdapter(assignTaskAdaptor);
        return rootView;
    }


}
