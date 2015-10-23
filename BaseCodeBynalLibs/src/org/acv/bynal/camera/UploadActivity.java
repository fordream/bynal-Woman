package org.acv.bynal.camera;

import org.json.JSONException;
import org.json.JSONObject;

import a.com.acv.crash.CrashExceptionHandler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import app.bynal.woman.R;

public class UploadActivity extends Activity{
	
	private Handler handler = new Handler();
	private Bitmap croppedImage;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		CrashExceptionHandler.onCreate(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_upload);
		
		byte[] byteArray = getIntent().getByteArrayExtra("image");
		croppedImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		
		if(croppedImage == null) finish();
		
		ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
		croppedImageView.setImageBitmap(croppedImage);

		
		final Button uploadButton = (Button) findViewById(R.id.Button_upload);
		uploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

//					FilePost.postImage(croppedImage,
//							JavascripInterfaceCameraManager
//									.getToken(UploadActivity.this),
//							new CallbackContext("", null) {
//								@Override
//								public void success(String message) {
//									_success(message);
//								}
//	
//								public void error(String message) {
//									_error(message);
//								}
//							});
				}
			});
		
		}

		private void _success(final String message) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					finishCropper(message);
				}
			});
		}
		
		private void _error(final String message) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT)
							.show();
				}
			});
		}
		
		private String getString(JSONObject jsonObject, String string) {
			try {
				return jsonObject.getString(string);
			} catch (JSONException e) {
			}
			return null;
		}
		
		
		public void finishCropper(String response) {
			try {
				
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject.getString("status").equals("1")) {
				String url = getString(jsonObject, "img");
				if (url == null)
					url = getString(jsonObject, "image_url");
				Intent data = new Intent();
				data.putExtra("url", url);
					setResult(Activity.RESULT_OK, data);
					finish();
				}
			} catch (Exception ex) {
		
			}
		}

}
