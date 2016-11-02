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

        ImageAdapter imageAdapter = new ImageAdapter(this);

        int catched = pokemonHelper.getCopy(pokeID);

        for (int j = 0; j < catched; j++) {
            TableLayout tableLayout = (TableLayout)findViewById(R.id.tableNumber);
            TableRow tableRow = new TableRow(getApplicationContext());
            TableLayout attacchi = new TableLayout(getApplicationContext());
            TableRow attackRow = new TableRow(getApplicationContext());
            TableRow ultiRow = new TableRow(getApplicationContext());
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
            TextView attack = new TextView(getApplicationContext());
            TextView ulti = new TextView(getApplicationContext());

            TableRow.LayoutParams trparams = new TableRow.LayoutParams(200,200);

            imageView.setLayoutParams(trparams);
            imageView.setId(j);
            attack.setText("attack 35");
            ulti.setText("ulti 120");
            attack.setTextColor(getResources().getColor(R.color.colorAccent));
            ulti.setTextColor(getResources().getColor(R.color.colorAccent));

            trparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            attack.setLayoutParams(trparams);
            ulti.setLayoutParams(trparams);

            trparams.gravity = Gravity.CENTER_VERTICAL;
            attacchi.setLayoutParams(trparams);

            tableRow.addView(imageView);
            attackRow.addView(attack);
            ultiRow.addView(ulti);
            attacchi.addView(attackRow);
            attacchi.addView(ultiRow);
            tableRow.addView(attacchi);

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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
