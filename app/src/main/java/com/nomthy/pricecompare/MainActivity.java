package com.nomthy.pricecompare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private static final String TAG = "pc: ";
    private final String myurl = "https://pricecompareapp.000webhostapp.com/upload.php";
    //ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
        cameraKitView = (CameraKitView)findViewById(R.id.camera);
    }

    public void fabCheck(View view){
        captureImage();
        afterImageCapture();
    }

    void captureImage(){
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                File savedPhoto = new File(Environment.getExternalStorageDirectory(), "pchk.jpg");
                try{
                    FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                    outputStream.write(photo);
                    outputStream.close();
                }catch (java.io.IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    void afterImageCapture(){
       // Toasty.success(MainActivity.this, "Captured Successfully", Toast.LENGTH_SHORT, true).show();
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("flag", "1");
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Delete last image and reset everything
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }



    //Request for permission to use device camera from user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    //Acquire permissions for read write access to external storage

    public  boolean isStoragePermissionGranted() {
        boolean result = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted");
                    result = true;
                } else {

                    Log.v(TAG, "Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    result = false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG, "Permission is granted");
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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

}
