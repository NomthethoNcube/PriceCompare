package com.nomthy.pricecompare;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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

    private final String myurl = "url for server";
    ProgressDialog progressDialog;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    String product(String url, String imageName){
        String nameOfProduct = "";


        //Code to send request goes here

        return nameOfProduct;
    }

    public void uploaduserimage(){
        progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage("Processing, please wait...");
        progressDialog.show();
        bitmap = getImageBitMap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Mysmart",""+error);
                Toast.makeText(ResultActivity.this, "Cannot retrieve data"+error, Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();


                String imagePath = Environment.getExternalStorageState()+"/pchk.jpg";

                param.put("image",imageString);
                return param;
            }
        };

        requestQueue.add(stringRequest);
    }

    Bitmap getImageBitMap(){
        File sd = Environment.getExternalStorageDirectory();
        File imageFile = new File(sd+"/pchk.jpg");
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),bmOptions);
        return image;
    }
}
