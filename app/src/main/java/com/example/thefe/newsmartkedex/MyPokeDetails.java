package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by TheFe on 01/11/2016.
 */

public class MyPokeDetails extends AppCompatActivity {

    int pokeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_poke_details);

        getActionBar();

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");
        final PokemonDetails pokemonDetails = new PokemonDetails();
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonDetails.getName(pokeID);
        String owner = pokemonHelper.getOwner();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText("I "+pokeName+" di "+owner);

        Button button = (Button)findViewById(R.id.edit);
        button.setText("Modifica i Dettagli dei tuoi "+pokeName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditPokeDetails.class);
                i.putExtra("id", pokeID);
                startActivity(i);
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.mpdlayout);
        ImageAdapter imageAdapter = new ImageAdapter(this);

        int catched = pokemonHelper.getCatched(pokeID);

        for (int j = 0; j < catched; j++) {
            TableLayout tableLayout = new TableLayout(getApplicationContext());
            TableRow tableRow = new TableRow(getApplicationContext());
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
            TextView attack = new TextView(getApplicationContext());
            TextView ulti = new TextView(getApplicationContext());

            TableLayout.LayoutParams tlparams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams trparams = new TableRow.LayoutParams(300,300);

            imageView.setLayoutParams(trparams);
            imageView.setId(j);

            trparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            trparams.gravity = Gravity.CENTER_VERTICAL;
            int pokeCopy =
            attack.setText(pokemonHelper.getPokeAttack(pokeCopy));
            attack.setLayoutParams(trparams);



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), PokemonDetails.class);
        i.putExtra("id", pokeID-1);
        startActivity(i);
        return true;
    }
}
