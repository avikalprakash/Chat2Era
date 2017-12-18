package com.example.lue.erachat.Activity.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.lue.erachat.R;

import java.io.IOException;

public class AudioAppActivity extends Activity {

	private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

	private static String audioFilePath;
	private static Button stopButton;
	private static Button playButton;
	private static Button recordButton;
	
	private boolean isRecording = false;

	@Override
	protected void onStart() {
		super.onStart();

	
		/*if (!hasMicrophone())
		{
			stopButton.setEnabled(false);
			playButton.setEnabled(false);
			recordButton.setEnabled(false);
		} else {
			playButton.setEnabled(false);
			stopButton.setEnabled(false);
		}*/
		
		audioFilePath =
       Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
	}

	public void recordAudio (View view) throws IOException
	{
		isRecording = true;
		/*stopButton.setEnabled(true);
		playButton.setEnabled(false);
		recordButton.setEnabled(false);*/

		try {
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mediaRecorder.setOutputFile(audioFilePath);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mediaRecorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mediaRecorder.start();
	}

	public void stopClicked (View view)
	{

		stopButton.setEnabled(false);
		playButton.setEnabled(true);

		if (isRecording)
		{
			recordButton.setEnabled(false);
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			isRecording = false;
		} else {
			mediaPlayer.release();
			mediaPlayer = null;
			recordButton.setEnabled(true);
		}
	}

	public void playAudio (View view) throws IOException
	{
		playButton.setEnabled(false);
		recordButton.setEnabled(false);
		stopButton.setEnabled(true);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(audioFilePath);
		mediaPlayer.prepare();
		mediaPlayer.start();
	}
}