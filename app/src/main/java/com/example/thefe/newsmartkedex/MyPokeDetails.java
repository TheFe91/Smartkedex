package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        PokemonDetails pokemonDetails = new PokemonDetails();
        PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        String pokeName = pokemonDetails.getName(pokeID);
        String owner = pokemonHelper.getOwner();

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText("I "+pokeName+" di "+owner);
        textView = (TextView)findViewById(R.id.number);
        textView.setText("Numero di "+pokeName);


        //defining and setting up the SpinnerNumber
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerNumber);
        List<Integer> list = new ArrayList<>();
        for (int j = 1; j < 51; j++) {
            list.add(j);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        ImageAdapter imageAdapter = new ImageAdapter(this);
        ImageView imageView = (ImageView) findViewById(R.id.esemplare1);
        imageView.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
        String[] attacks = pokemonHelper.getAttacks(pokeID);

        //defining and setting up the InternalSpinner
        Spinner internalSpinner = (Spinner)findViewById(R.id.esemplare1attacks);
        List<String> attacchi = new ArrayList<>();
        for (String element:attacks) {
            if (!element.equals(""))
                attacchi.add(element);
        }
        ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attacchi);
        attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        internalSpinner.setAdapter(attacksAdapter);

        //TODO: GOTTA ADD RELATION USER --> HAS --> COPY INTO DB; COPY HAS AN ID (INT (10) AUTO_INCREMENT P.K.) AND MAPS EVERY USER COPY OF EVERY PKMN

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), PokemonDetails.class);
        i.putExtra("id", pokeID-1);
        startActivity(i);
        return true;
    }
}
