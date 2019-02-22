package com.nomthy.pricecompare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private final String myurl = "https://pricecompareapp.000webhostapp.com/upload.php";
    private ProgressDialog progressDialog;
    private static final String TAG = "pc: ";
    private RequestQueue requestQueue;
    TextView txtView;

    Bitmap image;
    String imageString;
    String imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtView = (TextView)findViewById(R.id.txtTest);
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        if (flag.equals("1")) {
            image = null;
            imageString = null;
            imageData = null;
            try {
                uploaduserimage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Clear all variables to stop resending image to server.
        image = null;
        imageString = null;
        imageData = null;
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Clear all variables to stop resending image to server.
        image = null;
        imageString = null;
        imageData = null;
    }


    public void uploaduserimage(){
        requestQueue = Volley.newRequestQueue(ResultActivity.this);
        progressDialog = ProgressDialog.show(this,"Please Wait...","Processing image...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                txtView.setText(response);
                Log.v(TAG, response);
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("priceCompare",""+error);
                txtView.setText("Cannot retrieve data: "+error);
                Log.v(TAG, error.toString());
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                imageData =  imageToString(getImageBitMap());
                if (imageData == null){
                    Log.v(TAG, "No image data: imageData is null");
                }
                param.put("image",imageData);
                return param;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                if (progressDialog !=  null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    //Get bitmap of image from memory
    Bitmap getImageBitMap(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File imageFile = new File(sd + "/pchk.jpg");
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
            if(image == null) {
                Log.v(TAG, "image object empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    //Image can be efficiently sent as a Base64 byte string which will then converted back to jpg on the server
    private String imageToString(Bitmap bitmap){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(imageString == null){
            Log.v(TAG, "imageString is null");
        }
        return imageString;
    }

    //Acquire permissions for read write access to external storage

    public  boolean isStorageAccessPermissionGranted() {
        boolean result = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");
                    result = true;
                } else {

                    Log.v(TAG, "Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    result = false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted");
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
