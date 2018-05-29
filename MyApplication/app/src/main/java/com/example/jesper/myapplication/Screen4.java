package com.example.jesper.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Screen4 extends AppCompatActivity {
    public static final int maxPlayers = 11;

    private CheckBox adminButton;
    private Button confirmButton;
    private Button confirmSigButton;
    private TableLayout playersLayout;
    private SignaturePad signaturePad;

    private boolean justCleared = false;
    private View.OnClickListener playerSelectListener;
    private File signatureFile;
    private FileOutputStream signatureOutStream;

    private int teamYear = 0;
    private ArrayList<Player> selectedPlayers;

    Parser parser = null;

    String team = null;
    String admin_override = null;
    String year = null;
    String group = null;
    String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen4);

        playersLayout = findViewById(R.id.player_list);
        signaturePad = findViewById(R.id.signature_pad);
        adminButton = findViewById(R.id.admin_override);
        confirmButton = findViewById(R.id.confirmation_button);
        confirmSigButton = findViewById(R.id.confirm_signature);

        adminButton.setEnabled(false);
        confirmButton.setEnabled(false);
        confirmSigButton.setEnabled(false);

        selectedPlayers = new ArrayList<>();

        setListeners();

        admin_override = getIntent().getStringExtra("ADMIN_OVERRIDE");
        team = getIntent().getStringExtra("Team");
        year = getIntent().getStringExtra("YEAR");
        String year_last2 = year.substring(Math.max(year.length() - 2, 0));
        group = getIntent().getStringExtra("GROUP");
        url = getIntent().getStringExtra("URL");


        if(admin_override.equals("true")){
            adminButton.setEnabled(true);
        }

        parser = new Parser();
        try{teamYear = Integer.parseInt(parser.getAge(year_last2, group));}
        catch(IOException e){e.printStackTrace(); }
        try {
            for (ArrayList<String> player : parser.getPlayers(url)) {
                addPlayer(player.get(0), player.get(1));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
        //      1);
    }

    private void setListeners() {
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(checkValidity()) {
                    Intent data = new Intent();
                    data.putExtra("PlayerList", selectedPlayers);
                    data.putExtra("Team", team);
                    setResult(RESULT_OK, data);
                    finish();
                }
                else {
                    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_NACK, 200);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Screen4.this);
                    builder.setCancelable(false);
                    builder.setMessage(getString(R.string.too_many_old_players));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.create().show();
                }
            }
        });
        confirmSigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSignature(signaturePad.getTransparentSignatureBitmap());
                confirmButton.setEnabled(true);
            }
        });
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                if(justCleared) justCleared = false;
                else confirmSigButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                confirmSigButton.setEnabled(false);
                confirmButton.setEnabled(false);

                justCleared = true;
            }
        });

        playerSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckedTextView checkedText = (CheckedTextView) (((ViewGroup)view).getChildAt(0));
                if(checkedText.isChecked()) {
                    checkedText.setChecked(false);
                    checkedText.setCheckMarkDrawable(0);
                    removeSelectedPlayer(getPlayerRowData((TableRow)view));
                }

                else {
                    checkedText.setChecked(true);
                    checkedText.setCheckMarkDrawable(R.drawable.ic_checkmark);
                    selectedPlayers.add(getPlayerRowData((TableRow)view));
                }
            }
        };
    }

    private void addPlayer(String name, String birthday) {
        TableRow newPlayer = new TableRow(this);
        TextView textBirthday = new TextView(this);
        CheckedTextView textName = new CheckedTextView(this);

        newPlayer.setMinimumHeight(dpToPx(50));
        newPlayer.addView(textName);
        newPlayer.addView(textBirthday);

        textName.setText(name);
        textName.setChecked(false);
        textName.setGravity(Gravity.CENTER_VERTICAL);
        textName.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        newPlayer.setOnClickListener(playerSelectListener);

        textBirthday.setText(birthday);
        textBirthday.setGravity(android.view.Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textBirthday.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT,0.3f));

        playersLayout.addView(newPlayer);
    }


    private boolean checkValidity() {
        int nOveraged = 0;
        if(adminButton.isChecked()){
            return true;
        } else {
            for (Player player : selectedPlayers) {
                Log.v("Player", "Name : " + player.name + ", Birth year : " + player.birthYear + ", Birth month : " + player.birthMonth);
                if (teamYear > player.birthYear) {
                    nOveraged++;
                    if (nOveraged > 2) return false;
                }
            }
        }
        return true;
    }

    private Player getPlayerRowData(TableRow playerRow) {
        CheckedTextView name = (CheckedTextView) playerRow.getChildAt(0);
        TextView birthDay = (TextView) playerRow.getChildAt(1);

        int birthYear = Integer.parseInt(birthDay.getText().toString().substring(0,2));
        int birthMonth = Integer.parseInt(birthDay.getText().toString().substring(2,4));

        return new Player(name.getText().toString(), birthYear, birthMonth);
    }

    private int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void saveSignature(Bitmap signature) {
        String fileName = "Signature_team_"+team+".png";

        signatureFile = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            if(!signatureFile.exists()) {
                signatureFile.createNewFile();
            }
            signatureOutStream = new FileOutputStream(signatureFile);
            signature.compress(Bitmap.CompressFormat.PNG, 100, signatureOutStream);
            signatureOutStream.close();
            //Toast.makeText(this, "Signature saved", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void removeSelectedPlayer(Player playerRemove) {
        for(Player player : selectedPlayers) {
            if(player.name.equals(playerRemove.name) &&
               player.birthMonth == playerRemove.birthMonth &&
               player.birthYear == playerRemove.birthYear) {
                selectedPlayers.remove(player);
                return;
            }
        }
    }
}