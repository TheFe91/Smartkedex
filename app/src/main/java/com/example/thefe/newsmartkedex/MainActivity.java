package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final ReadFileFacade readFileFacade = new ReadFileFacade(getApplicationContext(), FILENAME); //creo un oggetto di classe ReadFileFacade
        final Presentation presentation = new Presentation((Button)findViewById(R.id.button1), (Button)findViewById(R.id.button2), getApplicationContext()); //creo un oggetto di classe Presentation
        presentation.presentati();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //final String[] tmp = readFileFacade.getDescriptionFromId(position+"");
                Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });

    };
}