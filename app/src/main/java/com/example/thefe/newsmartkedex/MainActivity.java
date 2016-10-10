package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t2;

    private static final String FILENAME = "descriptions.txt";

    //ovviamente così non funziona, ma non so come farlo andare >.<
    public void goSecond (View view) {
        startActivity(new Intent("android.intent.action.PokemonDetails"));
        //printkind (temp); //String temp passata come argomento
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ReadFileFacade readFileFacade = new ReadFileFacade(getApplicationContext(), FILENAME); //creo un oggetto di classe ReadFileFacade
        final Presentation presentation = new Presentation((Button)findViewById(R.id.button1), (Button)findViewById(R.id.button2), getApplicationContext()); //creo un oggetto di classe Presentation
        presentation.presentati();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                position += 1; //così il numero di Pokémon corrisponde al numero di Pokédex
                //final String[] tmp = readFileFacade.getDescriptionFromId(position+"");
                Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                i.putExtra("id", position);
                startActivity(i);
//                t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                    public void onInit(int status) {
//                        if(status != TextToSpeech.ERROR) {
//                            t2.setLanguage(Locale.ITALIAN);
//                            t2.speak(tmp[1], TextToSpeech.QUEUE_FLUSH, null);
//                        }
//                    }
//                });
            }
        });

    };

}