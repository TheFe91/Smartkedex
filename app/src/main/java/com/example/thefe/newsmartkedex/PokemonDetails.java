package com.example.thefe.newsmartkedex;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;



/**
 * Created by TheFe on 02/10/2016.
 */

public class PokemonDetails extends Activity {
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedetails);

        setInitialImage();
    }

    private void setInitialImage () {
        ImageView imageView = (ImageView) findViewById(R.id.prova);
        imageView.setImageResource(R.drawable.pkmn102);
    }

    public void goMain (View view) {
        finish();
    }
}
