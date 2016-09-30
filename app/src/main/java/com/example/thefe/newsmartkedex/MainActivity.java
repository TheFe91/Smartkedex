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

    TextToSpeech t1;
    Button b1, b2;

    private static final String FILENAME = "descriptions.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ReadFileFacade readFileFacade = new ReadFileFacade(getApplicationContext(), FILENAME);

        b1=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ITALIAN);
                    //t1.setPitch((float) 0.7);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setLanguage(Locale.ITALIAN);
                String toSpeak = "Sono Caterina, e sono uno Smàrtchedex in versione alfa. Ricorda che i pòchemon non funzionano, e causano l'arresto forzato dell'app";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                t1.setLanguage(Locale.ENGLISH);
                String toSpeak = "I'm Lily and I'm a Smartkedex in Alpha version. Be careful: pòkemons are bugged and cause the app crash";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                position += 1; //così il numero di Pokémon corrisponde al numero di Pokédex
                String tmp = readFileFacade.getDescriptionFromId(position+"");
                Toast.makeText(MainActivity.this, "" + tmp, Toast.LENGTH_SHORT).show();
            }
        });

    };

}