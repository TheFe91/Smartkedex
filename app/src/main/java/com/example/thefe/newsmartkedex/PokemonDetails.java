package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by TheFe on 02/10/2016.
 */

    public class PokemonDetails extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedetails);

        //Prendo i dati Intent
        Intent i = getIntent();

        //Seleziono l'ID dell'immagine
        int pokeID = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.tmppkmn);
        imageView.setImageResource(imageAdapter.mThumbIds[pokeID-1]);


        getActionBar();

        final TextView tv = (TextView) findViewById(R.id.descriptiontext);
        tv.setVisibility(View.GONE);
        Button showhide = (Button) findViewById(R.id.showhidedescr);
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getVisibility() == View.VISIBLE)
                    tv.setVisibility(View.GONE);
                else if (tv.getVisibility() == View.GONE)
                    tv.setVisibility(View.VISIBLE);
            }
        });

    }
}
