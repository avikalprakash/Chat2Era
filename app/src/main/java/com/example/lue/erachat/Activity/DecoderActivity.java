package com.example.lue.erachat.Activity;

import android.app.Activity;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.lue.erachat.Activity.AddFriend.FragmentContact;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DecoderActivity extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

   // private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    MyAsync myAsync;
	private static final int SHOW_PROCESS_DIALOG = 1;
	private static final int HIDE_PROCESS_DIALOG = 0;
	String QRText="";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
          myAsync=new MyAsync();
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

    	  // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
	@Override
	public void onQRCodeRead(String text, PointF[] points) {
		//resultTextView.setText(text);
		Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
		QRText=text;
		if(!text.equals("")){
			loadData();
		}
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		qrCodeReaderView.startCamera();
	}
	


	Handler handler=new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message message) {
			switch(message.what){
				case SHOW_PROCESS_DIALOG :
					// pb.setVisibility(View.VISIBLE);

					break;
				case HIDE_PROCESS_DIALOG :
					//  pb.setVisibility(View.GONE);

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
			String s = "";
			String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
			try {

				HttpClient Client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/sendInvitationByQrcode");
				httpPost.setHeader("Content-type", "application/json");
				httpPost.setHeader("Connection", "Keep-Alive");

				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("sender_id", myuser_id);
				if(!QRText.equals("")) {
					jsonObject.accumulate("qr_code", QRText);
				}

				StringEntity stringEntity = new StringEntity(jsonObject.toString());
				httpPost.setEntity(stringEntity);

				HttpResponse httpResponse = Client.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				s = readResponse(httpResponse);

				Log.d("tag11", " " + s);
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
				String md=jsonObject.getString("success");
				Toast.makeText(getApplicationContext(),"Invitation send to the user",Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}try {
				JSONObject jsonObject=new JSONObject(s);
				String md=jsonObject.getString("error");
				if(md.equals(true)){
					Toast.makeText(getApplicationContext(),"Already added",Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			finish();

		}
	}
	public void  loadData(){
		myAsync=new  MyAsync();
		if (Utils.isNetworkAvailable(getApplicationContext()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
		{
			myAsync.execute();
		}else{
			Toast.makeText(getApplicationContext(),"NO net available ",Toast.LENGTH_LONG).show();

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
		qrCodeReaderView.stopCamera();
		if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
		{
			myAsync.cancel(true);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

		if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
		{
			myAsync.cancel(true);
		}
	}

}