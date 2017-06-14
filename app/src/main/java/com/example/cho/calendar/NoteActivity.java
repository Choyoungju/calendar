package com.example.cho.calendar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class NoteActivity extends Activity {

    EditText editText;
    TextView dateText;
    int autoSaveSpeed = 60000;;
    Thread thread;
    Boolean threadRunning = true;

    void saveFile(String fn, String text){
        try{
            FileOutputStream fos = openFileOutput(fn, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();

        }catch (IOException e){
            Log.e("ERROR", e.getMessage());
        }
    }

    void loadFile(String fn){
        editText.setText("");

        try{
            FileInputStream fis = openFileInput(fn);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            editText.setText(new String(buffer));
            fis.close();

        }catch (IOException e){
            Log.e("ERROR", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);

        Button loadButton = (Button) findViewById(R.id.loadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = dateText.getText().toString();
                loadFile(fn);
            }
        });

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String fn = dateText.getText().toString();
                saveFile(fn, editText.getText().toString());
            }
        });

        editText = (EditText) findViewById(R.id.editText);
        loadFile("tempFile");
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                saveFile("tempFile", editText.getText().toString());
                return false;
            }
        });

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final String date = "NOTE_" + year + "-" + (month +1)+ "-" + day;

        dateText = (TextView) findViewById(R.id.dateTextView);
        dateText.setText(date);
        dateText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DateDialog dateDialog  = new DateDialog();
                dateDialog.show(getFragmentManager(), "DateDialog");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(threadRunning){
                  try{
                      Thread.sleep(autoSaveSpeed);
                  }catch(InterruptedException e){
                      Log.d("THREAD", e.getMessage());
                  }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String fn = dateText.getText().toString();
                            saveFile(fn, editText.getText().toString());
                            Toast.makeText(NoteActivity.this,"Auto Saved" + fn ,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        thread.start();
        threadRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        threadRunning = false;
    }


}
