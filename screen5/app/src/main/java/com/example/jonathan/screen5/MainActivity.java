package com.example.jonathan.screen5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import com.itextpdf.text.DocumentException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    private int REQUEST_TAKE_PHOTO;
    String mCurrentPhotoPath;
    String fileString;
    String frontPath;
    String backPath;

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
                    pdfGenerator.createPDF(frontPath, backPath);
                    //Toast.makeText(ctx, "Successfully made a PDF!", Toast.LENGTH_LONG).show();
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

        if(scanFront){
            fileString = "front";
            outFileFront = File.createTempFile(
                    fileString,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = outFileFront.getAbsolutePath();
            System.out.println(mCurrentPhotoPath);
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
            System.out.println(mCurrentPhotoPath);
            return outFileBack;
        }
        scanFront = false;
        scanBack = false;
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
           // startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Handling for the front image
        if (requestCode == 1) {
            /**img0 = findViewById(R.id.imageView4);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img0.setImageBitmap(imageBitmap);*/
            img0 = findViewById(R.id.imageView4);
            ///
            // Get the dimensions of the View
            int targetW = img0.getWidth();
            int targetH = img0.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            System.out.println(mCurrentPhotoPath);
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            img0.setImageBitmap(bitmap);
            frontPath = mCurrentPhotoPath;




        }
        // Handling for back image
        if (requestCode == 0) {
           /** img1 = findViewById(R.id.imageView3);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img1.setImageBitmap(imageBitmap);*/


            img1 = findViewById(R.id.imageView3);
            // Get the dimensions of the View
            int targetW = img1.getWidth();
            int targetH = img1.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            img1.setImageBitmap(bitmap);
            backPath = mCurrentPhotoPath;

        }
    }
}
