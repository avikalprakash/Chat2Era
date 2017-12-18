package com.example.lue.erachat.Activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ItemListAdaptor;
import com.example.lue.erachat.Activity.AddFriend.FragmentContact;
import com.example.lue.erachat.Activity.AddFriend.FragmentQRCode;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.Settings.Profile;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.R.attr.width;
import static android.app.Activity.RESULT_OK;
import static android.support.v7.mediarouter.R.attr.height;

/**
 * Created by lue on 15-06-2017.
 */

public class FragmentProfile extends Fragment {

    ImageView EditName, EditStatus;
    TextView name, status;
    DatabaseHandler databaseHandler;
    String myuser_id="";
    TextView number,email;
    CircleImageView imageView;
    ImageView profile_takepic, profile_choosepic;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    PopupWindow popupWindow;
    MyAsync myAsync;
    LoadProfile loadProfile;
    DeleteAccount deleteAccount;
    Dialog dialog;
    String myBase64Image="";
    Uri imageuri;
    public static int flag=0;
    String myuser_name ="";
    String myuser_status="";
    String myuser_profilePIc="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    Button QRcode,DeleteAccount;
    File compressedImage;
    Uri uriCompress;
    //Button
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //database
        databaseHandler=new DatabaseHandler(getActivity());
        //asynktask
        myAsync=new MyAsync();
        loadProfile=new LoadProfile();
        deleteAccount=new DeleteAccount();
        //Dialog
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //sharedprf
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
        String pr0=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("MYPROFILE", "");
       if(pr0.equals("")){
           loadProfile();
       }

        EditName = (ImageView) view.findViewById(R.id.edit_name);
        EditStatus = (ImageView) view.findViewById(R.id.edit_status);
        name = (TextView)view. findViewById(R.id.name);
        status = (TextView)view. findViewById(R.id.status);
        number=(TextView)view.findViewById(R.id.textView3);
        imageView = (CircleImageView) view.findViewById(R.id.profile_image);
        QRcode=(Button)view.findViewById(R.id.button3);
        DeleteAccount=(Button)view.findViewById(R.id.button4);
        EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.example.lue.erachat.Activity.Activity.EditName.class);
                startActivity(intent);
            }
        });
        EditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.example.lue.erachat.Activity.Activity.EditStatus.class);
                startActivity(intent);
            }
        });
        QRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentQRCode fragmentQRCode=new FragmentQRCode();
                FragmentTransaction fragmentTransaction1=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.containerdashbord,fragmentQRCode);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }
        });
        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do You Want to Delete Your Account");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                getActivity().deleteDatabase("databaseEraChat.db");
                                deleteProfile();
                            }
                        });

                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //SharedPrf
        myuser_name=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("MYNAME", "");
        myuser_status= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("MYSTATUS", "");
         myuser_profilePIc = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("MYPROFILE", "");
        String numberprf=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("MYNUMBER", "");
        ArrayList<User> ContactList =databaseHandler.getContact();
        for(int i=0;i<ContactList.size();i++){
            if(ContactList.get(i).getContactId().equals(myuser_id)) {
                String name = ContactList.get(i).getContactEmail();
                String nuamber=ContactList.get(i).getContactNumber();
                number.setText(numberprf);

            }

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);
            }
        });

        if (!numberprf.equals("")) {
            number.setText(numberprf);
        }
        if (!myuser_name.equals("")) {
            name.setText(myuser_name);
        }
        if (!myuser_status.equals("")) {
            status.setText(myuser_status);
        }
       if(!myuser_profilePIc.equals("")){
            Picasso.with(getActivity()).load(myuser_profilePIc).into(imageView);
        }

    }

    public void onShowPopup(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.dialogchoose_profile, null, false);
        // find the ListView in the popup layout
        profile_takepic = (ImageView) inflatedView.findViewById(R.id.imageView40);
        profile_choosepic = (ImageView) inflatedView.findViewById(R.id.imageView42);
        // get device size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        profile_choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
               startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        profile_takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageuri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);*/

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageuri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });
         popupWindow = new PopupWindow(inflatedView, size.x - 0, size.y / 6, true);

        // set a background drawable with rounders corners
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        // make it focusable to show the keyboard to enter in `EditText`
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // make it outside touchable to dismiss the popup window
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
         /*   File myFile = new File(uri.toString());
            new Compressor(getActivity())
                    .compressToFileAsFlowable(myFile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            uriCompress = Uri.fromFile(compressedImage);
                          //  setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                           // showError(throwable.getMessage());
                        }
                    });*/

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                myBase64Image = encodeToBase641(bitmap, Bitmap.CompressFormat.JPEG,5);
                loadData();
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
         //   Bundle extras = data.getExtras();
         //   Bitmap imageBitmap = (Bitmap) extras.get("data");
            try {

                Bitmap imagefromActivity = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageuri);
                myBase64Image = encodeToBase641(imagefromActivity, Bitmap.CompressFormat.JPEG,5);
               // myBase64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadData();
        }

    }
    public static String encodeToBase641(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        Matrix matrix = new Matrix();

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image,image.getWidth(),image.getHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        rotatedBitmap.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d("hchbhn","######");
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:

                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }





    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                    // pb.setVisibility(View.VISIBLE);
                    dialog.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                    //  pb.setVisibility(View.GONE);
                    dialog.hide();
                    break;
            }
            return false;
        }
    });
    class MyAsync extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            Log.d("werftghy","iiiiiiiiiiii");
            String s="";

            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/updatePhoto");
                httpPost.setHeader("Content-type", "application/json");
                Log.d("useridssss"," "+myuser_id);
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);
                jsonObject.accumulate("photo",myBase64Image);

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);

            try {
                JSONObject jsonObject=new JSONObject(s);
                String error=jsonObject.getString("error");
                String message=jsonObject.getString("message");
                if(error.equals("false")){

                    myBase64Image="";
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("MYPROFILE", message).commit();
                    FragmentProfile fragmentProfile=new FragmentProfile();
                    FragmentTransaction fragmentTransaction3=getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.containerdashbord,fragmentProfile);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class LoadProfile extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s="";

            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getUserDetail");
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcute", "" + s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String error=jsonObject.getString("error");
                String message=jsonObject.getString("message");
                if(error.equals("false")){
                    JSONArray jsonArray=new JSONArray(message);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String phtoto=jsonObject1.getString("photo");
                        String user=jsonObject1.getString("user_name");
                        String mobile=jsonObject1.getString("mobile");
                        String status=jsonObject1.getString("status");
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("MYPROFILE", phtoto).commit();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("MYNAME", user).commit();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("MYSTATUS", status).commit();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("MYNUMBER", mobile ).commit();
                    }

                    FragmentProfile fragmentProfile=new FragmentProfile();
                    FragmentTransaction fragmentTransaction3=getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.containerdashbord,fragmentProfile);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // ItemListAdaptor.confmtick.setVisibility(View.VISIBLE);
        }
    }
    class DeleteAccount extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            Log.d("werftghy","iiiiiiiiiiii");
            String s="";

            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/deleteAccount");
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11s"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);

            try {
                JSONObject jsonObject=new JSONObject(s);
                String error=jsonObject.getString("error");
                String message=jsonObject.getString("message");
                if(error.equals("false")){
                    Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("CheckRegister", "false");
                    editor.commit();
                    Intent intent=new Intent(getActivity(),SplashScreen.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(getActivity()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(getActivity(),"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }
    public void  loadProfile(){
        loadProfile=new LoadProfile();
        if (Utils.isNetworkAvailable(getActivity()) && loadProfile.getStatus()!= AsyncTask.Status.RUNNING)
        {
            loadProfile.execute();
        }else{
            Toast.makeText(getActivity(),"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }  public void  deleteProfile(){
        deleteAccount=new DeleteAccount();
        if (Utils.isNetworkAvailable(getActivity()) && deleteAccount.getStatus()!= AsyncTask.Status.RUNNING)
        {
            deleteAccount.execute();
        }else{
            Toast.makeText(getActivity(),"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }

    private String readResponse(HttpResponse httpResponse) {
        InputStream is=null;
        String return_text="";
        try {
            is=httpResponse.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
            Log.d("return_text",""+return_text);
        } catch (Exception e)
        {

        }
        return return_text;


    }

    @Override
    public void onPause() {
        super.onPause();
        if(popupWindow!=null) {
            popupWindow.dismiss();
        }
        dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
}
