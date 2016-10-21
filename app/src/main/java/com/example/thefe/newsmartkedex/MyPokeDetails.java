package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by TheFe on 20/10/2016.
 */

public class MyPokeDetails extends AppCompatActivity {

    int pokeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_poke_details);

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");

        getActionBar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), PokemonDetails.class);
        i.putExtra("id", pokeID);
        startActivity(i);
        return true;
    }
}
