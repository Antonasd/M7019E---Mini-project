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
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
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

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_TAKE_PHOTO;
    String mCurrentPhotoPath;
    String fileString;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},1);
        String matchID = "XXX";
        setTitle("Register Result: " + matchID);
        setContentView(R.layout.activity_main);

        final PDFGenerator pdfGenerator = new PDFGenerator();


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
                scanFront = true;
                dispatchTakePictureIntent();
                REQUEST_TAKE_PHOTO = 1;


            }
        });

        //BACK
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                scanBack = true;
                dispatchTakePictureIntent();
                REQUEST_TAKE_PHOTO =0;
            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        /**if (!storageDir.exists()){
            storageDir.mkdirs();
        }*/
        System.out.println(storageDir);

        if(scanFront){
            fileString = "front";
            outFileFront = File.createTempFile(
                    fileString,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = outFileFront.getAbsolutePath();
            return outFileFront;

        }
        else if(scanBack){
            fileString = "back";
            outFileBack = File.createTempFile(
                    fileString,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = outFileBack.getAbsolutePath();
            return outFileBack;
        }
        return outFileBack;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File.
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.FileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Front
        if (requestCode == 1) {
            img0 = findViewById(R.id.imageView4);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img0.setImageBitmap(imageBitmap);
        }
        //Back
        if (requestCode == 0) {
            img1 = findViewById(R.id.imageView3);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img1.setImageBitmap(imageBitmap);

        }
    }


        /** USED FOR TAKING PHOTO OF THE FRONT OF REPORT*
    private PictureCallback rawCallback0 = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if(data != null){
                Drawable img = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
                Bitmap bitmap = ((BitmapDrawable)img).getBitmap();
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                img0 = findViewById(R.id.imageView4);
                img0.setRotation(90);
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

    /**
    /** USED FOR TAKING PHOTO OF THE BACK OF REPORT
    private PictureCallback rawCallback1 = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if(data != null){

                Drawable img = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
                Bitmap bitmap = ((BitmapDrawable)img).getBitmap();
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                img0 = findViewById(R.id.imageView3);
                img0.setRotation(90);
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
    }; */

}
