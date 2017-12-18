package com.example.lue.erachat.Activity;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.R;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean pausing = false;
    String VideoData;
    ArrayList<Messages> item=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoData=getIntent().getStringExtra("videoData");
        getWindow().setFormat(PixelFormat.UNKNOWN);

        //Displays a video file.
        VideoView mVideoView = (VideoView)findViewById(R.id.videoview);

        String uriPath = "android.resource://com.android.AndroidVideoPlayer/";
        Uri uri = Uri.parse(VideoData);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
}
