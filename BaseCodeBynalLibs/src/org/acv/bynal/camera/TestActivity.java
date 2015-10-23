package org.acv.bynal.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import app.bynal.woman.R;

public class TestActivity extends Activity{

	private static final int CAMERA_REQUEST = 1888; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_camera);
		findViewById(R.id.btnCamera).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
			}
		});
		
		findViewById(R.id.btnGallery).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            if(photo != null){
            	Uri uri = saveBitmaptoUri(photo);
            	Intent intent = new Intent(this,CropActivity.class);
            	intent.putExtra("imguri", uri.toString());
            	startActivity(intent);
            }
           
        }  
    } 
	
	
	private Uri saveBitmaptoUri(Bitmap bmp) {
	      String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	      OutputStream outStream = null;
	     // String temp = null;
	      File file = new File(extStorageDirectory, "temp.png");
	      
	      if (file.exists()) {
		       file.delete();
		       file = new File(extStorageDirectory, "temp.png");
	      }

	      try {
		       outStream = new FileOutputStream(file);
		       bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
		       outStream.flush();
		       outStream.close();

	      } catch (Exception e) {
		       e.printStackTrace();
		       return null;
	      }
	      return Uri.fromFile(file);
	}
	
}
