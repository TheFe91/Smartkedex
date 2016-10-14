package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by TheFe on 02/10/2016.
 */

    public class PokemonDetails extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedetails);

        //istanzio un oggetto di classe PokemonDatabaseAdapter
        PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);

        //Prendo i dati Intent
        Intent i = getIntent();

        //Seleziono l'ID del Pokémon che mi servirà per prendere tutti i dati dal database e dai drawable
        int pokeID = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.tmppkmn);
        imageView.setImageResource(imageAdapter.mThumbIds[pokeID]);

        imageView = (ImageView) findViewById(R.id.tipo1);
        imageView.setImageResource(R.drawable.erba);

        imageView = (ImageView) findViewById(R.id.tipo2);
        imageView.setImageResource(R.drawable.acciaio);

        /*List<String> objects = pokemonDatabaseAdapter.getTipo(pokeID+1);

        for (int j=0; j < objects.size(); j++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(getResources().getIdentifier(objects.get(j), "drawable", getPackageName()));
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            /*lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.addRule(RelativeLayout.END_OF, R.id.tmppkmn);
            lp.setMargins(0, 89, 0, 0);
            rl.addView(iv, lp);
        }*/

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
