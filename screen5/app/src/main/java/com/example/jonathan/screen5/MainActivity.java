package com.example.jonathan.screen5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import com.itextpdf.text.DocumentException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "yolo";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Preview preview;
    private Context ctx;
    private boolean scanFront = false;
    private boolean scanBack = false;
    Button button0;
    Button button1;
    ImageView img0;
    ImageView img1;
    File outFileFront;
    File outFileBack;
    Button emailButton;
    FileOutputStream outStream = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},1);
        String matchID = "XXX";
        setTitle("Register Result: " + matchID);
        setContentView(R.layout.activity_main);

        final PDFGenerator pdfGenerator = new PDFGenerator();

        ctx = this;
        preview = new Preview(this, (SurfaceView) findViewById(R.id.surfaceView2));
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ConstraintLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);



        preview.setCamera(mCamera);

        button0 = findViewById(R.id.button);
        button1 = findViewById(R.id.button2);
        emailButton = findViewById(R.id.buttonEmail);


        /** FINALIZING REPORT, GENERATING PDF AND EMAILING THAT PDF*/
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pdfGenerator.createPDF(outFileFront.toString() , outFileBack.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch(NullPointerException e){
                    e.printStackTrace();
                    Toast.makeText(ctx, "You forgot the picture(s)", Toast.LENGTH_LONG).show();

                } catch (DocumentException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        //FRONT
        button0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mCamera.takePicture(null, null, rawCallback0);
                scanFront = true;

            }
        });

        //BACK
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mCamera.takePicture(null, null, rawCallback1);
                scanBack = true;

            }
        });

    }
    /** USED FOR TAKING PHOTO OF THE FRONT OF REPORT*/
    private PictureCallback rawCallback0 = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if(data != null){

                Drawable img = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
                Bitmap bitmap = ((BitmapDrawable)img).getBitmap();
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                img0 = findViewById(R.id.imageView4);
                //img0.setRotation(90);
                img0.setImageBitmap(b);

                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MATCH_PICTURES");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                try{

                    String fileFront = "front";
                    outFileFront = new File(dir, fileFront);
                    if(!outFileFront.exists()){
                        outFileFront.createNewFile();
                    }
                    outStream = new FileOutputStream(outFileFront);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outStream);
                    outStream.flush();
                    outStream.close();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
            camera.startPreview();
        }
    };


    /** USED FOR TAKING PHOTO OF THE BACK OF REPORT*/
    private PictureCallback rawCallback1 = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if(data != null){

                Drawable img = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
                Bitmap bitmap = ((BitmapDrawable)img).getBitmap();
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                img0 = findViewById(R.id.imageView3);
                //img0.setRotation(90);
                img0.setImageBitmap(b);


                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MATCH_PICTURES");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                try{

                    String fileFront = "back";
                    outFileBack = new File(dir, fileFront);
                    if(!outFileBack.exists()){
                        outFileBack.createNewFile();
                    }
                    outStream = new FileOutputStream(outFileBack);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
            camera.startPreview();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                mCamera = Camera.open(0);
                mCamera.startPreview();
                preview.setCamera(mCamera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx, getString(R.string.camera_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        if(mCamera != null) {
            mCamera.stopPreview();
            preview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        mCamera.startPreview();
        preview.setCamera(mCamera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }



    /** NOT USED*/
    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };


    /** NOT USED */
    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                if (scanFront){

                    String fileFront = "front";
                    outFileFront = new File(dir, fileFront);
                    if(!outFileFront.exists()){
                        outFileFront.createNewFile();
                    }

                    outStream = new FileOutputStream(outFileFront);
                    outStream.write(data[0]);
                    outStream.flush();
                    outStream.close();
                    Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFileFront.getAbsolutePath());

                    refreshGallery(outFileFront);
                    System.out.println(outFileFront);
                    System.out.println(scanFront);
                }
                else if(scanBack){

                    String fileBack = "back";
                    outFileBack = new File(dir, fileBack);
                    if(!outFileBack.exists()){
                        outFileBack.createNewFile();
                    }
                    outStream = new FileOutputStream(outFileBack);
                    outStream.write(data[0]);
                    outStream.flush();
                    outStream.close();
                    Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFileBack.getAbsolutePath());

                    refreshGallery(outFileBack);
                    System.out.println(outFileBack);
                    System.out.println(scanBack);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            scanFront = false;
            scanBack = false;
            return null;
        }

    }

}
