package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Activity.EditStatus;
import com.example.lue.erachat.R;

import java.util.ArrayList;

/**
 * Created by lue on 25-07-2017.
 */

public class StatusTextAdaptor extends BaseAdapter {
    LayoutInflater mInflater;
    private Activity context;
    ArrayList<String>Status_Text=new ArrayList<>();

    public StatusTextAdaptor(EditStatus editStatus, ArrayList<String> statusUpdateList) {
        this.context=editStatus;
        this.Status_Text=statusUpdateList;
    }


    @Override
    public int getCount() {
        return Status_Text.size();
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
        view = mInflater.inflate(R.layout.adaptor_status_text, null);
        TextView textView=(TextView)view.findViewById(R.id.text_status);
        String chbhb=Status_Text.get(i);
        Log.d("hcbhbcub","jc"+chbhb);
        textView.setText(Status_Text.get(i));
        return view;
    }
}
