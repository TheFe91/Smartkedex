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
    SoundPool mySoundPool = new SoundPool (1, AudioManager.STREAM_MUSIC, 0);
    int pkmn;

    private static final String FILENAME = "example.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String textFromFileString;
        ReadFileFacade readFileFacade = new ReadFileFacade(getApplicationContext(), FILENAME);
        textFromFileString = readFileFacade.readFromFile();
        Toast.makeText(getApplicationContext(), "ciao " + textFromFileString, Toast.LENGTH_SHORT).show();

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





        //final ArrayList<Item> items = new ArrayList<Item>();
        /*for(int i = 1; i < 152; i++) {
            String var = "pkmn"+i;
            items.add(new Item(getResources().getIdentifier(var, "drawable", getPackageName()), getResources().getIdentifier(var, "raw", getPackageName())));
        }*/

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                position += 1; //così il numero di Pokémon corrisponde al numero di Pokédex e Maryel non si prende male
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                //Item selectedItem = items.get(position);
                //System.out.println(selectedItem);
                //int soundID = selectedItem.getSoundID();
                mySoundPool.play(pkmn, 1, 1, 1, 0, 1);
            }
        });

    };

}