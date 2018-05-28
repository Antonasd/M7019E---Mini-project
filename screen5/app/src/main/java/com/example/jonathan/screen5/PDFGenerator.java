package com.example.jonathan.screen5;

import android.Manifest;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;


import com.itextpdf.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;

public class PDFGenerator{
    String $DATE ="1";
    String $TIME="2";
    String $GROUPCODE="3";
    String $MC="4";
    String LOG_TAG;


    public void createPDF(String front, String back) throws FileNotFoundException, DocumentException, IOException{
        String[] IMAGES = {front, back};

        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Match_Results");
        if(!pdfFolder.exists()){
            pdfFolder.mkdirs();
            Log.i(LOG_TAG, "PDF DIRECTORY CREATED");
        }

        //TODO GET DATA FROM SCREEN 3
        File pdfFile = new File(pdfFolder, "/MATCH_"+$MC+"-"+$DATE+"-"+$TIME+"-"+$GROUPCODE+".pdf");
        FileOutputStream outputStream = new FileOutputStream(pdfFile);


        Image img = Image.getInstance(IMAGES[0]);
        Document document = new Document(img);
        PdfWriter.getInstance(document, outputStream);

        document.open();
        //TODO GET REGISTERED PLAYERS AND SIGNATURE
        document.add(new Paragraph("Hejsan, owaodbaiwbdvoawovufvafvbavwfbaowfuvofawvbfabfoawvfuawfvaowfv"));
        for (String image : IMAGES) {
            img = Image.getInstance(image);
            //document.setPageSize(img);
            document.newPage();
            img.setAbsolutePosition(0, 0);
            float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
            img.scaleAbsolute(documentWidth, documentHeight);
            document.add(img);
        }

        document.close();
    }
}
