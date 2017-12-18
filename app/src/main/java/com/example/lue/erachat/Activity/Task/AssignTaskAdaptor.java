package com.example.lue.erachat.Activity.Task;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lue.erachat.R;

/**
 * Created by lue on 14-06-2017.
 */

public class AssignTaskAdaptor extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity context;
    int[]itmes={1,2,3,4,5,6,7,8,9,10,11};
    public AssignTaskAdaptor(FragmentActivity activity) {
        this.context=activity;
    }
    @Override
    public int getCount() {
        return itmes.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.adaptor_assign_task, null);
        return view;
    }
}
