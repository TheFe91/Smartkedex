package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonDetails.getName(pokeID);
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
                pokemonHelper.updateCatches(pokeID, (int)spinner.getSelectedItem());
                System.err.println("Inserimento avvenuto");

                int numberOfCopies = pokemonHelper.getNumberOfCopies(pokeID);
                TableLayout tl = (TableLayout)findViewById(R.id.copies);

                ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());

                for (int j = 0; j < numberOfCopies; j++) {
                    // get a reference for the TableLayout
                    TableLayout table = (TableLayout)findViewById(R.id.copies);

                    // create a new TableRow
                    TableRow row = new TableRow(getApplicationContext());

                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(300,300);
                    iv.setLayoutParams(params);
                    iv.setId(j);

                    Spinner internalSpinner = new Spinner(getApplicationContext());
                    params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_VERTICAL;
                    internalSpinner.setLayoutParams(params);
                    internalSpinner.setId(j);

                    String[] attacks = pokemonHelper.getAttacks(pokeID);

                    //setting up the InternalSpinner
                    List<String> attacchi = new ArrayList<>();
                    for (String element:attacks) {
                        if (!element.equals(""))
                            attacchi.add(element);
                    }
                    ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, attacchi);
                    attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    internalSpinner.setAdapter(attacksAdapter);

                    // add the TextView  to the new TableRow
                    row.addView(iv);
                    row.addView(internalSpinner);

                    // add the TableRow to the TableLayout
                    table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), PokemonDetails.class);
        i.putExtra("id", pokeID-1);
        startActivity(i);
        return true;
    }
}
