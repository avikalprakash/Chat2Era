package com.example.lue.erachat.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Other.VideoViewActivity;
import com.example.lue.erachat.Activity.Settings.Profile;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewContactActivity extends AppCompatActivity {
    String ContactId="";
    String ContactNo="";
    String ContactImage="";
    String name="";
    String number="";
    CircleImageView ProfilePhoto;
    TextView Name,Mobile;
    ImageView call;
    Button Add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);


        //Intent
        Intent intent=getIntent();
        ContactId= intent.getStringExtra("ContactId");
        ContactNo=intent.getStringExtra("ContactNo");
        name=intent.getStringExtra("ContactImage");
        Add=(Button)findViewById(R.id.button11);

        //Intialtion
        ProfilePhoto=(CircleImageView)findViewById(R.id.imageView46);
        Name=(TextView)findViewById(R.id.name);
        Mobile=(TextView)findViewById(R.id.mobile);
        call=(ImageView)findViewById(R.id.imageView45);

        //Listner
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewContactActivity.this, CallActivity.class);
                startActivity(intent);
            }
        });
        if (ContactNo==null) {
            getNameUsingContactId(ContactId);
        }else {
            Name.setText(name);
            Mobile.setText(ContactNo);
            ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.man));
          /*  if(ContactImage==null){
                ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.man));

            }else {
                Picasso.with(getApplicationContext()).load(ContactImage).into(ProfilePhoto);
            }*/
        }
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE,Mobile.getText().toString());
                startActivity(addContactIntent);
            }
        });

    }

    public void getNameUsingContactId(String contactId){

        String cContactIdString = ContactsContract.Contacts._ID;
        Uri cCONTACT_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String cDisplayNameColumn = ContactsContract.Contacts.DISPLAY_NAME;
        String cPhoto= ContactsContract.Contacts.PHOTO_URI;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        String selection = cContactIdString + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(contactId)};

        Cursor cursor = getApplicationContext().getContentResolver().query(cCONTACT_CONTENT_URI, null, selection, selectionArgs, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            while ((cursor != null) && (cursor.isAfterLast() == false)) {
                if (cursor.getColumnIndex(cContactIdString) >= 0) {
                    if (contactId.equals(cursor.getString(cursor.getColumnIndex(cContactIdString)))) {
                        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                        if (hasPhoneNumber > 0) {

                            Log.d("outcjjsuicb", " " + name);
                            //This is to read multiple phone numbers associated with the same contact
                            Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{ContactId}, null);
                            while (phoneCursor.moveToNext()) {
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                                Log.d("hbvhn",""+phoneNumber+"*");
                                //  output.append("\n Phone number:" + phoneNumber);
                                Mobile.setText(phoneNumber);
                            }
                            phoneCursor.close();
                        }

                         name = cursor.getString(cursor.getColumnIndex(cDisplayNameColumn));
                          if(cursor.getString(cursor.getColumnIndex(cPhoto))!=null) {
                            Uri photo = Uri.parse(cursor.getString(cursor.getColumnIndex(cPhoto)));
                            Log.d("hbvhn", name + photo + "*");

                            try {
                                Bitmap bitmapContact = MediaStore.Images.Media.getBitmap(getContentResolver(), photo);
                                Log.d("hbvhn", name + photo + " ## " + bitmapContact);
                                ProfilePhoto.setImageBitmap(bitmapContact);
                                Log.d("yegycgy"," "+name+" "+photo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                              ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.man));
                          }
                        Name.setText(name);
                         //Mobile.setText(number);

                        break;
                    }
                }
                cursor.moveToNext();
            }
        }
        if (cursor != null)
            cursor.close();
    }

}
//bitmapContact = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);