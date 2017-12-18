package com.example.lue.erachat.Activity.CreateTeam;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.R;

import java.util.ArrayList;

/**
 * Created by lue on 14-06-2017.
 */

public class SearchTeamAdaptor extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity context;
    int[] itmes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    ImageView done;
    ArrayList<User> item_user = new ArrayList<>();

    TextView user_contactname;
    CheckBox checkBox;

    public SearchTeamAdaptor(FragmentActivity activity, ArrayList<User> contact) {
        this.context = activity;
        this.item_user = contact;
    }

    @Override
    public int getCount() {
        return item_user.size();
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
        ViewHolder holder = null;

        if (view == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.adaptor_searchteam_member, null);
            holder = new ViewHolder();
           holder. user_Contactname = (TextView) view.findViewById(R.id.name);
           holder. checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            view.setTag(holder);
            holder.checkBox.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    User country = (User) cb.getTag();

                    country.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        User country = item_user.get(i);
        holder.user_Contactname.setText(country.getContactNumber());
        holder.checkBox.setText(country.getUserId());
        holder.checkBox.setChecked(country.isSelected());
        holder.checkBox.setTag(country);

        return view;

    }


    private class ViewHolder {
        TextView user_Contactname;
        CheckBox checkBox;
    }
}
