package com.example.thefe.newsmartkedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;



/**
 * Created by TheFe on 02/10/2016.
 */

public class PokemonDetails extends AppCompatActivity {
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedetails);

        getActionBar();

        setInitialImage();
    }

    private void setInitialImage () {
        ImageView imageView = (ImageView) findViewById(R.id.tmpPkmn);
        imageView.setImageResource(R.drawable.pkmn);
        imageView = (ImageView) findViewById(R.id.tipo1);
        imageView.setImageResource(R.drawable.tipo);
        imageView = (ImageView) findViewById(R.id.tipo2);
        imageView.setImageResource(R.drawable.tipo);
    }

    public void goMain (View view) {
        finish();
    }
}
