package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.http.RequestQueue;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.androidquery.AQuery;
import com.example.lue.erachat.Activity.Activity.DownloadImage;
import com.example.lue.erachat.Activity.Activity.MySingleton;
import com.example.lue.erachat.Activity.Activity.customButtonListener;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.InterFace.PaginationAdapterCallback;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.ZoomActivity;
import com.example.lue.erachat.Activity.VideoActivity;
import com.example.lue.erachat.Activity.ViewContactActivity;
import com.example.lue.erachat.R;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.lue.erachat.R.id.map;

/**
 * Created by lue on 19-06-2017.
 */

public class ItemListAdaptor extends RecyclerView.Adapter<ItemListAdaptor.MyViewHolder> {
    LayoutInflater inflater;
    private Activity context;

    public static ImageView confmtick,confirmtick1;


    String VideoData;
    ArrayList<Messages>item=new ArrayList<>();
    String Currentdate="";
    String SenderID="";
    String MessageText;
    String MessageTime="";
    private AQuery androidQuery;
    MediaController mc;
    String ImageString="";
    String GroupChatId="";
    String ContactId="";
    DownloadImage downloadImage;
    private GoogleMap googleMap;
    Handler seekHandler = new Handler();
    MediaPlayer player;
    customButtonListener customListner;
    String id="";
    View itemView;
    Cursor cursor;
    String result;
    int column_index;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private final HashSet<MapView> mMaps = new HashSet<MapView>();
    String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final int SENT_MESSAGE = 0,
            RECEIVED_MESSAGE = 1,SENT_MESSAGE_GROUP=2,RECEIVED_MESSAGE_GROUP=3;
    public ItemListAdaptor(FragmentChat fragmentChat, ArrayList<Messages> list,String SenderId) {
        this.context=fragmentChat;
        this.item=list;
        this.SenderID=SenderId;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public   RelativeLayout relativeLayout;
        TextView messengerTextView1;
        TextView messageTextView;
        TextView send_file;
        public  ImageView mesaageImageView;
        ImageView videoView;
        LinearLayout contactlayout,mapvielayout, sendFile;
        ImageView ContactInfoPhoto;
        NestedScrollView nestedScrollView;
        String result;
        TextView ContactInfoNumber;
        ImageView imageaudio;
        ImageView pause,resume;
        LinearLayout voicelayout;
        SeekBar seek_bar;
        TextView sendername;
        MapView mapView;

        public MyViewHolder(View convertView) {
            super(convertView);
            relativeLayout=(RelativeLayout)convertView.findViewById(R.id.videoRelativeView);
            confmtick=(ImageView)convertView.findViewById(R.id.conftick);
            confirmtick1=(ImageView)convertView.findViewById(R.id.conftick1);
            messengerTextView1=(TextView)convertView.findViewById(R.id.messengerTextView);
            messageTextView=(TextView)convertView.findViewById(R.id.messageTextView);
            send_file=(TextView)convertView.findViewById(R.id.send_file);
            mesaageImageView=(ImageView)convertView.findViewById(R.id.messageImageView) ;
            videoView=(ImageView)convertView.findViewById(R.id.videoView);
            contactlayout=(LinearLayout)convertView.findViewById(R.id.contactLayout);
            ContactInfoPhoto=(CircleImageView)convertView.findViewById(R.id.profilephoto);
            ContactInfoNumber=(TextView)convertView.findViewById(R.id.contactNumber) ;
            pause=(ImageView)convertView.findViewById(R.id.pause);
            resume=(ImageView)convertView.findViewById(R.id.resume);
            imageaudio=(ImageView)convertView.findViewById(R.id.imageaudio);
            voicelayout=(LinearLayout)convertView.findViewById(R.id.voicelayout) ;
            seek_bar=(SeekBar)convertView.findViewById(R.id.seek_bar);
            sendername=(TextView)convertView.findViewById(R.id.sendername);
            mapvielayout=(LinearLayout)convertView.findViewById(R.id.mapview);
            sendFile=(LinearLayout)convertView.findViewById(R.id.sendFile);
            mapView = (MapView) itemView.findViewById(R.id.map_view);
            downloadImage=new DownloadImage();



          //  confmtick.setImageResource(R.drawable.doubletick_secondary);


        }


    }

    @Override
    public int getItemViewType(int i) {
        if (item.get(i).getChatroom_Message_Senderid()!=null) {
            if(item.get(i).getChatroom_Message_Senderid().equals(FragmentChat.MYID)) {
                return SENT_MESSAGE;
            }else {
                return RECEIVED_MESSAGE;
            }
        }if (item.get(i).getGroupChatMessageSenderId()!=null) {
            if(item.get(i).getGroupChatMessageSenderId().equals(FragmentChat.MYID)) {
                return SENT_MESSAGE_GROUP;
            }else {
                return RECEIVED_MESSAGE_GROUP;
            }
        }

        else {
            return RECEIVED_MESSAGE_GROUP;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case SENT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                break;

            case RECEIVED_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message2, parent, false);
                break;
            case SENT_MESSAGE_GROUP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                break;

            case RECEIVED_MESSAGE_GROUP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message2, parent, false);
                break;
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder1, int i) {
        switch (holder1.getItemViewType()) {
            case SENT_MESSAGE:
                final MyViewHolder holder = (MyViewHolder) holder1;
                if (item.get(i).getMessageId()!=null){
                    if (item.get(i).getMessageId().equals("read")) {
                        confmtick.setImageResource(R.drawable.doubletick_secondary);
                    }
                }
                if (item.get(i).getChatroom_Message_Senderid() != null) {
                    id = item.get(i).getChatroom_Message_Senderid();
                }
                if (!id.equals("")) {

                    if (id.equals(FragmentChat.MYID)) {
                        holder.messengerTextView1.setText(item.get(i).getChatroom_message_inTime());
                        if (!item.get(i).getChatroom_Message_Image().equals("")) {
                            final String ImageString = item.get(i).getChatroom_Message_Image();
                            final Bitmap myBitmapImage = decodeStringToImage(ImageString);

                            holder.mesaageImageView.setImageBitmap(myBitmapImage);
                            holder.mesaageImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ZoomActivity.class);
                                    intent.putExtra("inimage", ImageString);
                                    context.startActivity(intent);
                                }
                            });

                        } else if (!item.get(i).getChatroom_Message_Video().equals("")) {
                            VideoData = item.get(i).getChatroom_Message_Video();
                            Uri uri = Uri.parse(VideoData);
                            String pata = getRealPathFromURI(context, uri);
                            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(pata,
                                    MediaStore.Images.Thumbnails.MINI_KIND);
                            holder.videoView.setImageBitmap(thumb);
                            holder.videoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, VideoActivity.class);
                                    intent.putExtra("videoData", VideoData);
                                    context.startActivity(intent);
                                }
                            });



                        } else if (!item.get(i).getChatroom_Message_ContactOnfo_Number().equals("")) {
                            String Photo = item.get(i).getChatroom_Message_ContactInfo_Image();
                            final String ContactId = item.get(i).getChatroom_Message_ContactInfo_Id();

                            String Number = item.get(i).getChatroom_Message_ContactOnfo_Number();
                            String Name = item.get(i).getChatroom_Message_ContactInfo_Image();
                            holder.ContactInfoNumber.setText(Number);


                            if (!item.get(i).getChatroom_Message_ContactInfo_Image().equals("")) {

                            } else {
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.man);
                                holder.ContactInfoPhoto.setImageBitmap(bitmap);
                            }
                            holder.ContactInfoNumber.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ViewContactActivity.class);

                                    intent.putExtra("ContactId", ContactId);
                                    context.startActivity(intent);
                                }
                            });

                        } else if (!item.get(i).getChatroom_Message_Voice().equals("")) {
                            final String audio = item.get(i).getChatroom_Message_Voice();


                            final MyViewHolder finalHolder = holder;
                            holder.resume.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalHolder.pause.setVisibility(v.VISIBLE);
                                    finalHolder.resume.setVisibility(v.GONE);
                                    finalHolder.pause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            ((ImageView) arg0).setVisibility(arg0.GONE);
                                            finalHolder.resume.setVisibility(arg0.VISIBLE);
                                        }
                                    });

                                    startPlaying(audio);
                                    final Handler handler = new Handler();


                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (player.isPlaying()) {
                                                int mediaPos_new = player.getCurrentPosition();
                                                int mediaMax_new = player.getDuration();
                                                finalHolder.seek_bar.setMax(mediaMax_new);
                                                finalHolder.seek_bar.setProgress(mediaPos_new);

                                                handler.postDelayed(this, 100);
                                            }
                                        }
                                    };

                                    handler.postDelayed(runnable, 100);

                                }
                            });

                        } else if (!item.get(i).getChatroom_Message_Text().equals("")) {
                            holder.messageTextView.setText(item.get(i).getChatroom_Message_Text());
                        }
                        else if (!item.get(i).getChatroom_FileName().equals("")) {
                            holder.send_file.setText(item.get(i).getChatroom_FileName());
                         String a= item.get(i).getChatroom_FileName();
                            Log.d("abcdFile", a);
                        }else if(!item.get(i).getChatroom_Longitude().equals("")){
                            final double lat= Double.parseDouble(item.get(i).getChatroom_Latitude());
                            final double longi= Double.parseDouble(item.get(i).getChatroom_Longitude());
                            holder.mapView.onCreate(null);

                            // Gets to GoogleMap from the MapView and does initialization stuff
                            holder.mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap mMap) {
                                    googleMap = mMap;
                                    LatLng LatLong = new LatLng(lat, longi);

                                    googleMap.addMarker(new MarkerOptions().position(LatLong).title("location").position(LatLong));

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLong));
                                    googleMap.animateCamera( CameraUpdateFactory.zoomTo( 8.0f ) );
                                    googleMap.getMaxZoomLevel();
                                }
                            });
                            holder.mapView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri gmmIntentUri = Uri.parse("geo:"+lat+longi);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                        context.startActivity(mapIntent);
                                    }
                                }
                            });
                        }

                    } /*else {
                        if (!item.get(i).getChatroom_Message_Image().equals("")) {

                            Picasso.with(context).load(item.get(i).getChatroom_Message_Image()).into(holder.mesaageImageView);
                            final String url = item.get(i).getChatroom_Message_Image();
                          //  downloadImage.loadUrlImage(url);
                            holder.mesaageImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ZoomActivity.class);
                                    intent.putExtra("UrlString", url);
                                    context.startActivity(intent);
                                }
                            });
                        } else if (!item.get(i).getChatroom_Message_Video().equals("")) {

                            String url = item.get(i).getChatroom_Message_Video();

                        } else if (!item.get(i).getChatroom_Message_Voice().equals("")) {
                            final String temp = String.valueOf(i);
                            final String urlaudio = item.get(i).getChatroom_Message_Voice();
                            final MyViewHolder finalHolder1 = holder;
                            holder.resume.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    finalHolder1.pause.setVisibility(v.VISIBLE);
                                    finalHolder1.resume.setVisibility(v.GONE);
                                    finalHolder1.pause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            ((ImageView) arg0).setVisibility(arg0.GONE);
                                            finalHolder1.resume.setVisibility(arg0.VISIBLE);
                                        }
                                    });
                                    startPlayingUrl(urlaudio);

                                    final Handler handler = new Handler();


                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (player.isPlaying()) {
                                                int mediaPos_new = player.getCurrentPosition();
                                                int mediaMax_new = player.getDuration();
                                                finalHolder1.seek_bar.setMax(mediaMax_new);
                                                finalHolder1.seek_bar.setProgress(mediaPos_new);

                                                handler.postDelayed(this, 100);
                                            }
                                        }
                                    };

                                    handler.postDelayed(runnable, 100);
                                }
                            });

                        } else if (!item.get(i).getChatroom_Message_ContactInfo_Id().equals("")) {

                            final String contactNo = item.get(i).getChatroom_Message_ContactOnfo_Number();
                            final String contactid = item.get(i).getChatroom_Message_ContactInfo_Id();
                            final String conatctImage = item.get(i).getChatroom_Message_ContactInfo_Image();
                            Log.d("djhckj",""+contactid+contactNo+conatctImage);
                            getImage(conatctImage, holder.ContactInfoPhoto);
                            holder.ContactInfoNumber.setText(contactNo);
                            holder.ContactInfoNumber.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ViewContactActivity.class);
                                    intent.putExtra("ContactId", contactid);
                                    intent.putExtra("ContactNo",contactNo);
                                    intent.putExtra("ContactImage",conatctImage);
                                    context.startActivity(intent);
                                }
                            });

                        } else if (!item.get(i).getChatroom_Message_Text().equals("")) {
                            holder.messageTextView.setText(item.get(i).getChatroom_Message_Text());
                        }*/

                    // }



                    if (!item.get(i).getChatroom_Message_Image().equals("")) {
                        holder.voicelayout.setVisibility(View.GONE);
                        holder.messageTextView.setVisibility(View.GONE);
                        holder.videoView.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.mapvielayout.setVisibility(View.GONE);
                        holder.sendFile.setVisibility(View.GONE);
                    } else if (!item.get(i).getChatroom_Message_Video().equals("")) {
                        holder.voicelayout.setVisibility(View.GONE);
                        holder.messageTextView.setVisibility(View.GONE);
                        holder.mesaageImageView.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.mapvielayout.setVisibility(View.GONE);
                        holder.sendFile.setVisibility(View.GONE);

                    } else if (!item.get(i).getChatroom_Message_Text().equals("")) {
                        holder.mesaageImageView.setVisibility(View.GONE);
                        holder.voicelayout.setVisibility(View.GONE);
                        holder.videoView.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.mapvielayout.setVisibility(View.GONE);
                        holder.sendFile.setVisibility(View.GONE);
                    } else if (!item.get(i).getChatroom_Message_ContactInfo_Id().equals("")) {
                        holder.mesaageImageView.setVisibility(View.GONE);
                        holder.messageTextView.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.voicelayout.setVisibility(View.GONE);
                        holder.mapvielayout.setVisibility(View.GONE);
                        holder.sendFile.setVisibility(View.GONE);

                    } else if (!item.get(i).getChatroom_Message_Voice().equals("")) {
                        holder.mesaageImageView.setVisibility(View.GONE);
                        holder.messageTextView.setVisibility(View.GONE);
                        holder.videoView.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.mapvielayout.setVisibility(View.GONE);
                        holder.sendFile.setVisibility(View.GONE);


                    }else if(!item.get(i).getChatroom_Latitude().equals("")){
                        holder.voicelayout.setVisibility(View.GONE);
                        holder.messageTextView.setVisibility(View.GONE);
                        holder.videoView.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.mesaageImageView.setVisibility(View.GONE);
                        holder.sendFile.setVisibility(View.GONE);
                    }
                    /*else if(!item.get(i).getChatroom_FileName().equals("")){
                        holder.voicelayout.setVisibility(View.GONE);
                        holder.messageTextView.setVisibility(View.GONE);
                        holder.videoView.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.contactlayout.setVisibility(View.GONE);
                        holder.mesaageImageView.setVisibility(View.GONE);
                        holder.mapvielayout.setVisibility(View.GONE);
                    }*/
                }
                break;

            case RECEIVED_MESSAGE:
                MyViewHolder viewHolder2 = (MyViewHolder) holder1;
                viewHolder2.messengerTextView1.setText(item.get(i).getChatroom_message_inTime());
                if (!item.get(i).getChatroom_Message_Image().equals("")) {
                    final String url = item.get(i).getChatroom_Message_Image();
                    Log.d("dhbdhnj",""+url);
                    File imgFile = new  File(url);
                    if(imgFile.exists())
                    {
                        viewHolder2.mesaageImageView.setImageURI(Uri.fromFile(imgFile));

                    }
                    viewHolder2.mesaageImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ZoomActivity.class);
                            intent.putExtra("UrlString", url);
                            context.startActivity(intent);
                        }
                    });
                } else if (!item.get(i).getChatroom_Message_Video().equals("")) {

                    String url = item.get(i).getChatroom_Message_Video();

                } else if (!item.get(i).getChatroom_Message_Voice().equals("")) {
                    final String temp = String.valueOf(i);
                    final String urlaudio = item.get(i).getChatroom_Message_Voice();

                    final MyViewHolder finalHolder1 = viewHolder2;
                    finalHolder1.resume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalHolder1.pause.setVisibility(v.VISIBLE);
                            finalHolder1.resume.setVisibility(v.GONE);
                            finalHolder1.pause.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    ((ImageView) arg0).setVisibility(arg0.GONE);
                                    finalHolder1.resume.setVisibility(arg0.VISIBLE);
                                }
                            });

                            startPlayingUrl(urlaudio);
                            final Handler handler = new Handler();


                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (player.isPlaying()) {
                                        int mediaPos_new = player.getCurrentPosition();
                                        int mediaMax_new = player.getDuration();
                                        finalHolder1.seek_bar.setMax(mediaMax_new);
                                        finalHolder1.seek_bar.setProgress(mediaPos_new);

                                        handler.postDelayed(this, 100);
                                    }
                                }
                            };

                            handler.postDelayed(runnable, 100);
                        }
                    });

                } else if (!item.get(i).getChatroom_Message_ContactInfo_Id().equals("")) {

                    final String contactNo = item.get(i).getChatroom_Message_ContactOnfo_Number();
                    final String contactid = item.get(i).getChatroom_Message_ContactInfo_Id();
                    final String contactImage=item.get(i).getChatroom_Message_ContactInfo_Image();
                    final String conatctImage = item.get(i).getChatroom_Message_ContactInfo_Image();
                    getImage(conatctImage, viewHolder2.ContactInfoPhoto);
                    viewHolder2.ContactInfoNumber.setText(conatctImage);
                    viewHolder2.ContactInfoNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ViewContactActivity.class);
                            intent.putExtra("ContactNo",contactNo);
                            intent.putExtra("ContactImage",conatctImage);
                            intent.putExtra("ContactId", contactid);
                            context.startActivity(intent);
                        }
                    });

                } else if (!item.get(i).getChatroom_Message_Text().equals("")) {
                    viewHolder2.messageTextView.setText(item.get(i).getChatroom_Message_Text());
                }else if(!item.get(i).getChatroom_Longitude().equals("")){
                    final double lat= Double.parseDouble(item.get(i).getChatroom_Latitude());
                    final double longi= Double.parseDouble(item.get(i).getChatroom_Longitude());
                    viewHolder2.mapView.onCreate(null);
                    viewHolder2.mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            googleMap = mMap;
                            LatLng LatLong = new LatLng(lat, longi);

                            googleMap.addMarker(new MarkerOptions().position(LatLong).title("location").position(LatLong));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLong));
                            googleMap.animateCamera( CameraUpdateFactory.zoomTo( 8.0f ) );
                            googleMap.getMaxZoomLevel();
                        }
                    });
                    viewHolder2.mapView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("geo:"+lat+longi);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(mapIntent);
                            }
                        }
                    });
                }
                viewHolder2.sendername.setVisibility(View.GONE);

                if (!item.get(i).getChatroom_Message_Image().equals("")) {
                    viewHolder2.voicelayout.setVisibility(View.GONE);
                    viewHolder2.messageTextView.setVisibility(View.GONE);
                    viewHolder2.videoView.setVisibility(View.GONE);
                    viewHolder2.relativeLayout.setVisibility(View.GONE);
                    viewHolder2.contactlayout.setVisibility(View.GONE);
                    viewHolder2.mapvielayout.setVisibility(View.GONE);
                } else if (!item.get(i).getChatroom_Message_Video().equals("")) {
                    viewHolder2.voicelayout.setVisibility(View.GONE);
                    viewHolder2.messageTextView.setVisibility(View.GONE);
                    viewHolder2.mesaageImageView.setVisibility(View.GONE);
                    viewHolder2.contactlayout.setVisibility(View.GONE);
                    viewHolder2.mapvielayout.setVisibility(View.GONE);
                } else if (!item.get(i).getChatroom_Message_Text().equals("")) {
                    viewHolder2.mesaageImageView.setVisibility(View.GONE);
                    viewHolder2.voicelayout.setVisibility(View.GONE);
                    viewHolder2.videoView.setVisibility(View.GONE);
                    viewHolder2.relativeLayout.setVisibility(View.GONE);
                    viewHolder2.contactlayout.setVisibility(View.GONE);
                    viewHolder2.mapvielayout.setVisibility(View.GONE);
                } else if (!item.get(i).getChatroom_Message_ContactInfo_Id().equals("")) {
                    viewHolder2.videoView.setVisibility(View.GONE);
                    viewHolder2.mesaageImageView.setVisibility(View.GONE);
                    viewHolder2.messageTextView.setVisibility(View.GONE);
                    viewHolder2.relativeLayout.setVisibility(View.GONE);
                    viewHolder2.voicelayout.setVisibility(View.GONE);
                    viewHolder2.mapvielayout.setVisibility(View.GONE);

                }  else if(!item.get(i).getChatroom_Latitude().equals("")){
                    viewHolder2.voicelayout.setVisibility(View.GONE);
                    viewHolder2.messageTextView.setVisibility(View.GONE);
                    viewHolder2.videoView.setVisibility(View.GONE);
                    viewHolder2.relativeLayout.setVisibility(View.GONE);
                    viewHolder2.contactlayout.setVisibility(View.GONE);
                    viewHolder2.mesaageImageView.setVisibility(View.GONE);
                } else if(!item.get(i).getChatroom_Message_Voice().equals("")) {
                    viewHolder2.mesaageImageView.setVisibility(View.GONE);
                    viewHolder2.messageTextView.setVisibility(View.GONE);
                    viewHolder2.videoView.setVisibility(View.GONE);
                    viewHolder2.relativeLayout.setVisibility(View.GONE);
                    viewHolder2.contactlayout.setVisibility(View.GONE);
                    viewHolder2.mapvielayout.setVisibility(View.GONE);

                }break;
            case SENT_MESSAGE_GROUP :
                final MyViewHolder holder2 = (MyViewHolder) holder1;
                holder2.messengerTextView1.setText(item.get(i).getGroupChatMessageIncomingTime());
                if (item.get(i).getGroupChatMessageSenderId() != null) {
                    id = item.get(i).getGroupChatMessageSenderId();
                }

                if (!item.get(i).getGroupChatMessageText().equals("")) {
                    holder2.messageTextView.setText(item.get(i).getGroupChatMessageText());
                }
                else if (!item.get(i).getGroupChatMessageVoice().equals("")) {
                    final String audio = item.get(i).getGroupChatMessageVoice();


                    holder2.resume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder2.pause.setVisibility(v.VISIBLE);
                            holder2.resume.setVisibility(v.GONE);
                            holder2.pause.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    ((ImageView) arg0).setVisibility(arg0.GONE);
                                    holder2.resume.setVisibility(arg0.VISIBLE);
                                }
                            });

                            startPlaying(audio);
                            final Handler handler = new Handler();


                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (player.isPlaying()) {
                                        int mediaPos_new = player.getCurrentPosition();
                                        int mediaMax_new = player.getDuration();
                                        holder2.seek_bar.setMax(mediaMax_new);
                                        holder2.seek_bar.setProgress(mediaPos_new);

                                        handler.postDelayed(this, 100);
                                    }
                                }
                            };

                            handler.postDelayed(runnable, 100);

                        }
                    });

                }else if(!item.get(i).getGroupChatMessageContactInfo_Number().equals("")){
                    final String Photo = item.get(i).getGroupChatMessageContactInfo_Image();
                    final String ContactId = item.get(i).getGroupChatMessageContactInfo_Id();
                    String Number = item.get(i).getGroupChatMessageContactInfo_Number();
                    String Name = item.get(i).getGroupChatMessageContactInfo_Image();
                    holder2.ContactInfoNumber.setText(Name);


                    if (!item.get(i).getGroupChatMessageContactInfo_Image().equals("")) {
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.man);
                        holder2.ContactInfoPhoto.setImageBitmap(bitmap);
                    }
                    holder2.ContactInfoNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ViewContactActivity.class);

                            intent.putExtra("ContactId", ContactId);
                            context.startActivity(intent);
                        }
                    });
                }else if(!item.get(i).getGroupChatMessageLongitude().equals("")){
                    final double lat= Double.parseDouble(item.get(i).getGroupChatMessageLattitude());
                    final double longi= Double.parseDouble(item.get(i).getGroupChatMessageLongitude());
                    holder2.mapView.onCreate(null);

                    // Gets to GoogleMap from the MapView and does initialization stuff
                    holder2.mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            googleMap = mMap;
                            LatLng LatLong = new LatLng(lat, longi);

                            googleMap.addMarker(new MarkerOptions().position(LatLong).title("location").position(LatLong));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLong));
                            googleMap.animateCamera( CameraUpdateFactory.zoomTo( 8.0f ) );
                            googleMap.getMaxZoomLevel();
                        }
                    });
                    holder2.mapView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("geo:"+lat+longi);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(mapIntent);
                            }
                        }
                    });
                }
                if (!item.get(i).getGroupChatMessageImage().equals("")) {
                    final String ImageSsstring = item.get(i).getGroupChatMessageImage();
                    Log.d("djncj",""+ImageSsstring);
                    final Bitmap myBitmapImage1 = decodeStringToImage(ImageSsstring);
                    holder2.mesaageImageView.setImageBitmap(myBitmapImage1);
                   /*
                    holder.mesaageImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ZoomActivity.class);
                            intent.putExtra("inimage", ImageString);
                            context.startActivity(intent);
                        }
                    });*/
                }
                if (!item.get(i).getGroupChatMessageText().equals("")) {
                    holder2.mesaageImageView.setVisibility(View.GONE);
                    holder2.voicelayout.setVisibility(View.GONE);
                    holder2.videoView.setVisibility(View.GONE);
                    holder2.relativeLayout.setVisibility(View.GONE);
                    holder2.contactlayout.setVisibility(View.GONE);
                    holder2.mapvielayout.setVisibility(View.GONE);
                    holder2.sendFile.setVisibility(View.GONE);
                }else if (!item.get(i).getGroupChatMessageVoice().equals("")) {
                    holder2.mesaageImageView.setVisibility(View.GONE);
                    holder2.messageTextView.setVisibility(View.GONE);
                    holder2.videoView.setVisibility(View.GONE);
                    holder2.relativeLayout.setVisibility(View.GONE);
                    holder2.contactlayout.setVisibility(View.GONE);
                    holder2.mapvielayout.setVisibility(View.GONE);
                    holder2.sendFile.setVisibility(View.GONE);
                }else if(!item.get(i).getGroupChatMessageContactInfo_Number().equals("")){
                    holder2.videoView.setVisibility(View.GONE);
                    holder2.mesaageImageView.setVisibility(View.GONE);
                    holder2.messageTextView.setVisibility(View.GONE);
                    holder2.relativeLayout.setVisibility(View.GONE);
                    holder2.voicelayout.setVisibility(View.GONE);
                    holder2.mapvielayout.setVisibility(View.GONE);
                    holder2.sendFile.setVisibility(View.GONE);
                }else if(!item.get(i).getGroupChatMessageLattitude().equals("")){
                    holder2.voicelayout.setVisibility(View.GONE);
                    holder2.messageTextView.setVisibility(View.GONE);
                    holder2.videoView.setVisibility(View.GONE);
                    holder2.relativeLayout.setVisibility(View.GONE);
                    holder2.contactlayout.setVisibility(View.GONE);
                    holder2.mesaageImageView.setVisibility(View.GONE);
                }
                else if (!item.get(i).getGroupChatMessageImage().equals("")) {
                    holder2.voicelayout.setVisibility(View.GONE);
                    holder2.messageTextView.setVisibility(View.GONE);
                    holder2.videoView.setVisibility(View.GONE);
                    holder2.relativeLayout.setVisibility(View.GONE);
                    holder2.contactlayout.setVisibility(View.GONE);
                    holder2.mapvielayout.setVisibility(View.GONE);
                    holder2.sendFile.setVisibility(View.GONE);
                }
                else if (!item.get(i).getChatroom_FileName().equals("")) {
                    holder2.voicelayout.setVisibility(View.GONE);
                    holder2.messageTextView.setVisibility(View.GONE);
                    holder2.videoView.setVisibility(View.GONE);
                    holder2.relativeLayout.setVisibility(View.GONE);
                    holder2.contactlayout.setVisibility(View.GONE);
                    holder2.mapvielayout.setVisibility(View.GONE);
                    holder2.mesaageImageView.setVisibility(View.GONE);
                }
                break;
            case RECEIVED_MESSAGE_GROUP:
                final MyViewHolder holder3 = (MyViewHolder) holder1;
                holder3.messengerTextView1.setText(item.get(i).getGroupChatMessageIncomingTime());
                if (!item.get(i).getGroupChatMessageText().equals("")) {
                    holder3.messageTextView.setText(item.get(i).getGroupChatMessageText());
                } else if (!item.get(i).getGroupChatMessageVoice().equals("")) {
                    final String audio = item.get(i).getGroupChatMessageVoice();


                    holder3.resume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            holder3.pause.setVisibility(v.VISIBLE);
                            holder3.resume.setVisibility(v.GONE);
                            holder3.pause.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    ((ImageView) arg0).setVisibility(arg0.GONE);
                                    holder3.resume.setVisibility(arg0.VISIBLE);
                                }
                            });

                            startPlayingUrl(audio);
                            final Handler handler = new Handler();


                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (player.isPlaying()) {
                                        int mediaPos_new = player.getCurrentPosition();
                                        int mediaMax_new = player.getDuration();
                                        holder3.seek_bar.setMax(mediaMax_new);
                                        holder3.seek_bar.setProgress(mediaPos_new);

                                        handler.postDelayed(this, 100);
                                    }
                                }
                            };

                            handler.postDelayed(runnable, 100);

                        }
                    });

                }else if (!item.get(i).getGroupChatMessageContactInfo_Number().equals("")) {

                    final String contactNo = item.get(i).getGroupChatMessageContactInfo_Number();
                    final String contactid = item.get(i).getGroupChatMessageContactInfo_Id();
                    final String contactImage=item.get(i).getGroupChatMessageContactInfo_Image();

                    getImage(contactImage, holder3.ContactInfoPhoto);
                    holder3.ContactInfoNumber.setText(contactImage);
                    holder3.ContactInfoNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ViewContactActivity.class);
                            intent.putExtra("ContactNo",contactNo);
                            intent.putExtra("ContactImage",contactImage);
                            intent.putExtra("ContactId", contactid);
                            context.startActivity(intent);
                        }
                    });

                }else if(!item.get(i).getGroupChatMessageLongitude().equals("")) {
                    final double lat = Double.parseDouble(item.get(i).getGroupChatMessageLattitude());
                    final double longi = Double.parseDouble(item.get(i).getGroupChatMessageLongitude());
                    holder3.mapView.onCreate(null);

                    // Gets to GoogleMap from the MapView and does initialization stuff
                    holder3.mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            googleMap = mMap;
                            LatLng LatLong = new LatLng(lat, longi);

                            googleMap.addMarker(new MarkerOptions().position(LatLong).title("location").position(LatLong));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLong));
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
                            googleMap.getMaxZoomLevel();
                        }
                    });
                } else if (!item.get(i).getGroupChatMessageImage().equals("")) {
                final String url = item.get(i).getGroupChatMessageImage();
                File imgFile = new  File(url);
                if(imgFile.exists())
                {
                    holder3.mesaageImageView.setImageURI(Uri.fromFile(imgFile));

                }
                    holder3.mesaageImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ZoomActivity.class);
                        intent.putExtra("UrlString", url);
                        context.startActivity(intent);
                    }
                });
            }

                holder3.sendername.setText(item.get(i).getGroupChatMessageSenderName());
                if (!item.get(i).getGroupChatMessageText().equals("")) {
                    holder3.mesaageImageView.setVisibility(View.GONE);
                    holder3.voicelayout.setVisibility(View.GONE);
                    holder3.videoView.setVisibility(View.GONE);
                    holder3.relativeLayout.setVisibility(View.GONE);
                    holder3.contactlayout.setVisibility(View.GONE);
                    holder3.mapvielayout.setVisibility(View.GONE);
                }else if (!item.get(i).getGroupChatMessageVoice().equals("")) {
                    holder3.mesaageImageView.setVisibility(View.GONE);
                    holder3.messageTextView.setVisibility(View.GONE);
                    holder3.videoView.setVisibility(View.GONE);
                    holder3.relativeLayout.setVisibility(View.GONE);
                    holder3.contactlayout.setVisibility(View.GONE);
                    holder3.mapvielayout.setVisibility(View.GONE);
                }else if (!item.get(i).getGroupChatMessageContactInfo_Id().equals("")) {
                    holder3.videoView.setVisibility(View.GONE);
                    holder3.mesaageImageView.setVisibility(View.GONE);
                    holder3.messageTextView.setVisibility(View.GONE);
                    holder3.relativeLayout.setVisibility(View.GONE);
                    holder3.voicelayout.setVisibility(View.GONE);
                    holder3.mapvielayout.setVisibility(View.GONE);
                }else if(!item.get(i).getGroupChatMessageLattitude().equals("")){
                    holder3.voicelayout.setVisibility(View.GONE);
                    holder3.messageTextView.setVisibility(View.GONE);
                    holder3.videoView.setVisibility(View.GONE);
                    holder3.relativeLayout.setVisibility(View.GONE);
                    holder3.contactlayout.setVisibility(View.GONE);
                    holder3.mesaageImageView.setVisibility(View.GONE);
                } else if (!item.get(i).getGroupChatMessageImage().equals("")) {
                    holder3.voicelayout.setVisibility(View.GONE);
                    holder3.messageTextView.setVisibility(View.GONE);
                    holder3.videoView.setVisibility(View.GONE);
                    holder3.relativeLayout.setVisibility(View.GONE);
                    holder3.contactlayout.setVisibility(View.GONE);
                    holder3.mapvielayout.setVisibility(View.GONE);
                }
                break;
        }

        /*  new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                 //   BlueTickHelper b=new BlueTickHelper();
                    String sd= b.getTick();
                    if (sd.equals("Tick")){
                        confmtick.setImageResource(R.drawable.doubletick_secondary);
                    }
                }
            }, 10000);*/


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        Log.d("jn",""+item.size());
        return item.size();

    }






    void getImage(String url, final ImageView imageView)
    {
        RequestQueue requestAdministrator = null;
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if(bitmap!=null) imageView.setBackground(new
                                BitmapDrawable(context.getResources(),bitmap));

                    }
                },0,0,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    //decode base64
    public Bitmap decodeStringToImage(String input){
        byte[] decode= Base64.decode(input ,0 );
        return BitmapFactory.decodeByteArray(decode,0,decode.length);
    }

    private String getRealPathFromURI(Activity context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this.context, contentUri, proj, null, null, null);
        try{
            cursor = loader.loadInBackground();
        }catch (Exception e){}
        try{
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }catch (Exception e){}
        try {
            cursor.moveToFirst();
        }catch (Exception e){}
        try {
            result = cursor.getString(column_index);
        }catch (Exception e){}
        try {
            cursor.close();
        }catch (Exception e){}
        return result;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);

            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startPlaying(String filePath){
        try {

            player = new MediaPlayer();
            if((!(filePath.equals("")))||filePath!=null){
                player.setDataSource(filePath);
            }else {
                Toast.makeText(context,"Cant Open The file",Toast.LENGTH_LONG).show();
            }

            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopPlaying(){

        if(player != null){
            try {
                player.pause();
                player.stop();
                player.reset();

            } catch (Exception e){ }

        }


    }
    private void startPlayingUrl(String filePath){
        try {
            Uri uri = Uri.parse(filePath);
            player=new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(context, uri);
            player.prepare();
            player.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public String downloadFile(String uRl) {
        try
        {

            String extraPath = "/Map-"+".png";
            filepath += extraPath;
            URL url = new URL(uRl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            String filename="downloadedFile.png";
            Log.i("Localfilename:",""+filename);
            File file = new File(SDCardRoot,filename);
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            if(downloadedSize==totalSize) filepath=file.getPath();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            filepath=null;
            e.printStackTrace();
        }
        Log.i("filepath:"," "+filepath) ;
        return filepath;

    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Messages());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = item.size() - 1;
        Messages result = getItem(position);

        if (result != null) {
            item.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void add(Messages r) {
        item.add(r);
        notifyItemInserted(item.size() - 1);
    }
    public void addAll(ArrayList<Messages> moveResults) {
        for (Messages messages : moveResults) {
            add(messages);
        }
    }
    public Messages getItem(int position) {
        return item.get(position);
    }
}
