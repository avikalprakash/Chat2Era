package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.lue.erachat.Activity.AddToContact;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;

import java.util.ArrayList;



/**
 * Created by lue on 21-06-2017.
 */

public class ContactListAdaptor extends BaseAdapter implements Filterable {
    LayoutInflater mInflater;
    private Activity context;
    DatabaseHandler databaseHandler;
  ArrayList<User>Contact=new ArrayList<>();
     ArrayList<User> mOriginalValues=new ArrayList<>(); // Original Values
     ArrayList<User> mDisplayedValues=new ArrayList<>();
    //ImageView imageView;
    String num="";
    String id="";



    private class MyViewHolder {
        TextView name,number;
          Button add;
          int position;

            public MyViewHolder(View item) {
                name=(TextView)item.findViewById(R.id.cont_name);
                number=(TextView)item.findViewById(R.id.cont_number);
                add=(Button)item.findViewById(R.id.button14);
            }
        }


    ArrayList<User> ContactList =new ArrayList<>();
    public ContactListAdaptor(FragmentActivity activity, ArrayList<User> storeContacts) {
        this.context=activity;
        this.Contact=storeContacts;
        this.mOriginalValues=storeContacts;
        this.mDisplayedValues=storeContacts;

        mInflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return mDisplayedValues.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
         final MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contactlist_adaptor,null, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        databaseHandler=new DatabaseHandler(context);
        ContactList= databaseHandler.getContact();
       mViewHolder.add.setVisibility(View.GONE);
        mViewHolder.position = i;
       mViewHolder. add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               Intent intent=new Intent(context,AddToContact.class);
                intent.putExtra("name",mViewHolder.number.getText().toString());
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });
        mViewHolder.name.setText(mDisplayedValues.get(i).getContactName());
       mViewHolder. number.setText(mDisplayedValues.get(i).getContactNumber());

        if(!mDisplayedValues.get(i).getContactName().equals("")) {

            for (int z = 0; z < ContactList.size(); z++) {

                if (mDisplayedValues.get(i).getContactNumber().equals(ContactList.get(z).getContactNumber())) {

                   // ContactList.get(z).getContactStatus()
                    Log.d("visibilitystatus",""+"true"+ ContactList.get(z).getContactStatus());
                      if(ContactList.get(z).getContactStatus().equals("true")){
                          mViewHolder.add.setVisibility(View.VISIBLE);
                          mViewHolder.add.setText("Added");
                          mViewHolder.add.setBackgroundResource(R.color.grey);

                      }else if(ContactList.get(z).getContactStatus().equals("false")){
                          mViewHolder.add.setVisibility(View.VISIBLE);
                      }
                    num=ContactList.get(z).getContactNumber();
                    id=ContactList.get(z).getContactId();
                  // mViewHolder.add.setVisibility(View.VISIBLE);

                }

            }
        }


        return convertView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<User>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<User> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<User>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getContactName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new User(mOriginalValues.get(i).getContactName(),mOriginalValues.get(i).getContactNumber()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}


