package com.example.jesper.myapplication;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PDFGenerator{
    String LOG_TAG;


    public String createPDF(String front, String back, String matchcode,
                          String time, String group, String teamname1,
                          String teamname2, String lineup1, String lineup2) throws FileNotFoundException, DocumentException, IOException{

        String[] IMAGES = {front, back};
        String[] SIGNATURES = {"/storage/emulated/0/Signature_team_1.png", "/storage/emulated/0/Signature_team_2.png"};

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String date = df.format(c);

        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Match_Results");
        if(!pdfFolder.exists()){
            pdfFolder.mkdirs();
            Log.i(LOG_TAG, "PDF DIRECTORY CREATED");
        }

        String fileString = "/MATCH_"+matchcode+"-"+date+"-"+time+"-"+group+".pdf";
        File pdfFile = new File(pdfFolder, fileString);
        FileOutputStream outputStream = new FileOutputStream(pdfFile);


        Image img = Image.getInstance(IMAGES[0]);
        Document document = new Document(img);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Paragraph h1 = new Paragraph(teamname1,FontFactory.getFont(FontFactory.HELVETICA_BOLD, 150));
        Paragraph h2 = new Paragraph(teamname2,FontFactory.getFont(FontFactory.HELVETICA_BOLD, 150));
        Paragraph p = new Paragraph(lineup1,FontFactory.getFont(FontFactory.HELVETICA, 75));
        Paragraph p2 = new Paragraph(lineup2,FontFactory.getFont(FontFactory.HELVETICA, 75));
        Image img1 = Image.getInstance(SIGNATURES[0]);
        Image img2 = Image.getInstance(SIGNATURES[1]);

        document.add(h1);
        document.add(p);
        document.add(img1);
        document.add(h2);
        document.add(p2);
        document.add(img2);

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
        return fileString;
    }
}
