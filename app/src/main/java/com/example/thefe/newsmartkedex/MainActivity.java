package com.example.thefe.newsmartkedex;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SoundPool mySound;
    int pkmn1Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        pkmn1Id = mySound.load(this, R.raw.pkmn1, 1);


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                 int position, long id) {
                position += 1; //così il numero di Pokémon corrisponde al numero di Pokédex e Maryel non si prende male
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                mySound.play(pkmn1Id,1,1,1,0,1); //per ora riproduce sempre e solo la prima descrizione
            }
        });



    };
}
