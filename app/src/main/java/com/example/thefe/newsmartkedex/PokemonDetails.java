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

    }

    public void printKind (String tipo) {
        ImageView imageView = (ImageView) findViewById(R.id.tipo1);
        imageView.setImageResource(getResources().getIdentifier(tipo, "drawable", getPackageName()));
    }

}
