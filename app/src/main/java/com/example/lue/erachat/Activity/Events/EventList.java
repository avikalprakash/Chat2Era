package com.example.lue.erachat.Activity.Events;

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
 * Created by lue on 15-06-2017.
 */

public class EventList extends Fragment {
ListView eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventList=(ListView)getActivity().findViewById(R.id.eventList);
        EventListAdaptor eventListAdaptor=new EventListAdaptor(getActivity());
        eventList.setAdapter(eventListAdaptor);
       eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent=new Intent(getActivity(),EventListDeatil.class);
               startActivity(intent);
           }
       });
    }
}
