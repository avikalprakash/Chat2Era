package com.example.lue.erachat.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Fragment.FragmentContactListMeoBook;
import com.example.lue.erachat.Activity.Fragment.FramentContactList;
import com.example.lue.erachat.R;

/**
 * Created by lue on 16-06-2017.
 */

public class FragmentContactInfo extends Fragment {
ImageView phonebook,EraBook;
TextView phone_book,era_book;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_info, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phonebook=(ImageView)view.findViewById(R.id.imageView38);
        phone_book=(TextView)view.findViewById(R.id.textView60);
        era_book=(TextView)view.findViewById(R.id.textView61) ;
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
        phonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonebook.setImageResource(R.drawable.phone_book_color);
                phone_book.setTextColor(Color.parseColor("#ff751a"));
                FramentContactList chatList =new FramentContactList();
                FragmentTransaction fragmentTransaction1=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.containerdashbord,chatList);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        });
        EraBook=(ImageView)view.findViewById(R.id.imageView39);
        EraBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EraBook.setImageResource(R.drawable.phone_book_color);
                era_book.setTextColor(Color.parseColor("#ff751a"));
                FragmentContactListMeoBook eralist=new FragmentContactListMeoBook();
                FragmentTransaction fragmentTransaction1=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.containerdashbord,eralist);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        });
    }
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);

        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("shbh"," "+"@@@@@@@");
            return true;
        } else {
            Log.d("shbh"," "+"*******");
            return false;
        }
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.CAMERA }, 101);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    // Toast.makeText(ViewProductPage.this),"Permission is granted",Toast.LENGTH_LONG).show();

                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
