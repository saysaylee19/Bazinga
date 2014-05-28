package com.example.frag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Tab5Fragment extends Fragment{

	EditText mEditText;
	Button mButton;
	TextView mTextView1, mTextView2;
	ImageView mImageView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		Log.v("ALERT","In TAB 5");
		View v= inflater.inflate(R.layout.tab_frag5_layout, container, false);
		
//setContentView(R.layout.activity_main);
		
		mEditText = (EditText) v.findViewById(R.id.editText1);
		mButton = (Button) v.findViewById(R.id.button1);
		mTextView1 = (TextView) v.findViewById(R.id.textView1);
		mTextView2 = (TextView) v.findViewById(R.id.textView2);
		mImageView = (ImageView) v.findViewById(R.id.imageView1);
		
		
		mButton.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
				ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
				if(networkInfo != null && networkInfo.isConnected()){
					new FetchWeatherTask().execute(mEditText.getText().toString());
					//mEditText.clearFocus();
					InputMethodManager inputManager = 
					        (InputMethodManager) getActivity().getApplicationContext().
					            getSystemService(Context.INPUT_METHOD_SERVICE); 
					inputManager.hideSoftInputFromWindow(
					        mEditText.getWindowToken(),
					        InputMethodManager.HIDE_NOT_ALWAYS);
				} else{
					Toast.makeText(getActivity().getApplicationContext(), "Device not connected", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return v;
	}

		
@SuppressLint("NewApi")
		private class FetchWeatherTask extends AsyncTask<String, Void, Object[]> {

		int responseCode;
		@Override
		protected Object[] doInBackground(String... arg0) {
			
			InputStream is = null;
			Object[] result = null;
			
			String urlString = buildQueryURLString(arg0[0]);
			Log.d(this.getClass().getSimpleName(), urlString);
			try {
				URL url = new URL(urlString);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.setReadTimeout(10000);
				connection.setConnectTimeout(15000);
				
				connection.connect();
				
				responseCode = connection.getResponseCode();
				if(responseCode == HttpURLConnection.HTTP_OK){
					is = connection.getInputStream();
					System.out.println("response code is "+ responseCode);
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String in=br.readLine();
					System.out.println(in);
					int j=0;
					is.close();
					JSONObject obj = new JSONObject(in).getJSONObject("data");
					//System.out.println("->"+obj.toString(4));
					JSONArray weatherArr = obj.getJSONArray("weather");
					String imageUrl = new String();
					for(int i=0;i<weatherArr.length();i++){
						JSONObject o = weatherArr.getJSONObject(i);
						System.out.println("i="+i);
						result = new Object[4];
						result[j++] = new String(o.getString("tempMaxC"));
						result[j++] = new String(o.getString("tempMinC"));
						JSONArray weatherDesc = o.getJSONArray("weatherDesc");
						for(int k=0;k<weatherDesc.length();k++){
							JSONObject o1 = weatherDesc.getJSONObject(k);
							result[j] = o1.getString("value");
						}
						j++;
						imageUrl = o.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
						HttpURLConnection imageFetchConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
						imageFetchConnection.setDoInput(true);
						imageFetchConnection.setReadTimeout(10000);
						imageFetchConnection.setConnectTimeout(15000);
						imageFetchConnection.connect();
						if(imageFetchConnection.getResponseCode()== HttpURLConnection.HTTP_OK){
							System.out.println("imageFetchConnection responseCode" + imageFetchConnection.getResponseCode());
							is = imageFetchConnection.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							System.out.println("j="+j);
							result[j++] = bitmap;							
						}
					}
					
					
					
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Object[] result) {
			if(result == null){
				Toast.makeText(getActivity().getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
				
			} else{
				mTextView1.setText((CharSequence)result[0]+"/"+(CharSequence)result[1]);
				mTextView2.setText((CharSequence) result[2]);
				mImageView.setImageBitmap((Bitmap)result[3]);
//				mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				mImageView.setVisibility(View.VISIBLE);
			}
		}
		/*private String getTempMaxC(String in) {
			String tempMaxC = null;
			try {
				 
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return tempMaxC;
			
		}
*/
		private String buildQueryURLString(String string) {
			StringBuffer mStringBuffer = new StringBuffer("http://api.worldweatheronline.com/free/v1/weather.ashx?");
			try {
				mStringBuffer.append("key=tzddbbmkxjzqf4rxy3g6vt3u")
							.append("&format=json")
							.append("&q=")
							.append(URLEncoder.encode(string, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mStringBuffer.toString();
			
			
		}

	}



}
