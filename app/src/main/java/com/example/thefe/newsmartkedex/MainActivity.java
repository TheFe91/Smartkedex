package com.example.thefe.newsmartkedex;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t1, t2;
    Button b1, b2;

    private static final String FILENAME = "descriptions.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ReadFileFacade readFileFacade = new ReadFileFacade(getApplicationContext(), FILENAME);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                position += 1; //così il numero di Pokémon corrisponde al numero di Pokédex
                final String tmp = readFileFacade.getDescriptionFromId(position+"");
                t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            t2.setLanguage(Locale.ITALIAN);
                            t2.speak(tmp, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
            }
        });

    };

}