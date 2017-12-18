package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lue on 23-06-2017.
 */

public class ContactListMeoBookAdaptor extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity context;
   CircleImageView circleImageView;
    ArrayList<User> Contact=new ArrayList<>();
    ArrayList<User> compareList=new ArrayList<User>();
    TextView name,number;

    public ContactListMeoBookAdaptor(FragmentActivity activity, ArrayList<User> storeContacts) {
        this.context=activity;
        this.Contact=storeContacts;
    }
    @Override
    public int getCount() {
        return Contact.size();
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
        view = mInflater.inflate(R.layout.contactlist_meobook_adaptor, null);
        name=(TextView)view.findViewById(R.id.cont_name);
        number=(TextView)view.findViewById(R.id.cont_number);
        circleImageView=(CircleImageView)view.findViewById(R.id.circleImageView);
        number.setText(Contact.get(i).getContactNumber());
        if(Contact.get(i).getContactName()!=null){
            name.setText(Contact.get(i).getContactName());
        }else {
            name.setText(" ");
        }
        Picasso.with(context).load(Contact.get(i).getContactImage()).into(circleImageView);
        return view;
    }
}
