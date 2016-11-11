package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheFe on 20/10/2016.
 */

public class EditPokeDetails extends AppCompatActivity {

    int pokeID;
    Spinner attackSpinner;
    Spinner ultiSpinner;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_poke_details);

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");
        final PokemonDetails pokemonDetails = new PokemonDetails();
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonDetails.getName(pokeID);

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText("Modifica i tuoi "+pokeName);
        textView = (TextView)findViewById(R.id.number);
        textView.setText("Numero di "+pokeName);


        //defining and setting up the SpinnerNumber
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerNumber);
        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < 51; j++) {
            list.add(j);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        final List<Integer> pokeIds = pokemonHelper.getIdsFromPokeID(pokeID);

        final ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());

        for (int pokeId:pokeIds) {
            // get a reference for the TableLayout
            TableLayout table = (TableLayout)findViewById(R.id.copies);

            //create a new TableLayout
            TableLayout internaltable = new TableLayout(getApplicationContext());

            // create a new TableRow
            TableRow row = new TableRow(getApplicationContext());
            TableRow attackRow = new TableRow(getApplicationContext());
            TableRow ultiRow = new TableRow(getApplicationContext());

            ImageView iv = new ImageView(getApplicationContext());
            iv.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
            TableRow.LayoutParams params = new TableRow.LayoutParams(250,250);
            iv.setLayoutParams(params);

            attackSpinner = new Spinner(getApplicationContext());
            ultiSpinner = new Spinner(getApplicationContext());
            params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            attackSpinner.setLayoutParams(params);
            ultiSpinner.setLayoutParams(params);
            attackSpinner.setId(pokeId);
            ultiSpinner.setId(pokeId*10);

            String[] moves = pokemonHelper.getPokeAttacks(pokeId); //0 is attack, 1 is ulti

            List<String> attacks = pokemonHelper.getMoves(pokeID, "HasAttack");

            ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, attacks);
            attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            attackSpinner.setAdapter(attacksAdapter);
            attackSpinner.setSelection(attacksAdapter.getPosition(moves[0]));
            attackSpinner.setBackgroundColor(getResources().getColor(getResources().getIdentifier(pokemonHelper.getMovesType(moves[0], "Attack").toLowerCase(), "color", getPackageName())));

            List<String> ultis = pokemonHelper.getMoves(pokeID, "HasUlti");

            ArrayAdapter<String>ultiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ultis);
            attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ultiSpinner.setAdapter(ultiAdapter);
            ultiSpinner.setSelection(ultiAdapter.getPosition(moves[1]));
            ultiSpinner.setBackgroundColor(getResources().getColor(getResources().getIdentifier(pokemonHelper.getMovesType(moves[1], "Ulti").toLowerCase(), "color", getPackageName())));

            editText = new EditText(getApplicationContext());
            editText.setWidth(300);
            editText.setHint("Nome");
            editText.setId(pokeId*11);

            // add the TextView  to the new TableRow
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0,10,0,0);
            row.addView(iv);
            editText.setLayoutParams(params);
            row.addView(editText);
            attackRow.addView(attackSpinner);
            ultiRow.addView(ultiSpinner);
            internaltable.addView(attackRow);
            internaltable.addView(ultiRow);
            internaltable.setLayoutParams(params);
            row.addView(internaltable);

            // add the TableRow to the TableLayout
            table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int j = 1+pokeIds.size(); j <= (int)spinner.getSelectedItem()+pokeIds.size(); j++) {
                    // get a reference for the TableLayout
                    TableLayout table = (TableLayout)findViewById(R.id.copies);

                    //create a new TableLayout
                    TableLayout internaltable = new TableLayout(getApplicationContext());

                    // create a new TableRow
                    TableRow row = new TableRow(getApplicationContext());
                    TableRow attackRow = new TableRow(getApplicationContext());
                    TableRow ultiRow = new TableRow(getApplicationContext());

                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(200,200);
                    iv.setLayoutParams(params);

                    attackSpinner = new Spinner(getApplicationContext());
                    ultiSpinner = new Spinner(getApplicationContext());
                    attackSpinner.setBackgroundColor(getResources().getColor(R.color.erba));
                    ultiSpinner.setBackgroundColor(getResources().getColor(R.color.erba));
                    params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    attackSpinner.setLayoutParams(params);
                    ultiSpinner.setLayoutParams(params);
                    attackSpinner.setId(j);
                    ultiSpinner.setId(j*10);

                    editText = new EditText(getApplicationContext());
                    editText.setWidth(300);
                    editText.setHint("Nome");
                    editText.setId(j*11);

                    List<String> attacks = pokemonHelper.getMoves(pokeID, "HasAttack");

                    ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, attacks);
                    attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    attackSpinner.setAdapter(attacksAdapter);

                    List<String> ultis = pokemonHelper.getMoves(pokeID, "HasUlti");

                    ArrayAdapter<String>ultiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ultis);
                    attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ultiSpinner.setAdapter(ultiAdapter);

                    // add the TextView  to the new TableRow
                    params.gravity = Gravity.CENTER_VERTICAL;
                    row.addView(iv);
                    editText.setLayoutParams(params);
                    row.addView(editText);
                    attackRow.addView(attackSpinner);
                    ultiRow.addView(ultiSpinner);
                    internaltable.addView(attackRow);
                    internaltable.addView(ultiRow);
                    internaltable.setLayoutParams(params);
                    row.addView(internaltable);

                    // add the TableRow to the TableLayout
                    table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        Button apply = (Button)findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int pokeId:pokeIds) {
                    attackSpinner = (Spinner)findViewById(pokeId);
                    String attack = (String) attackSpinner.getSelectedItem();
                    ultiSpinner = (Spinner)findViewById(pokeId*10);
                    String ulti = (String) ultiSpinner.getSelectedItem();
                    editText = (EditText)findViewById(pokeId*11);
                    String copyName = editText.getText().toString();
                    pokemonHelper.updatePokeAttacks(attack, ulti, copyName, pokeId);
                }

                for (int j = 1+pokeIds.size(); j <= (int)spinner.getSelectedItem()+pokeIds.size(); j++) {
                    attackSpinner = (Spinner)findViewById(j);
                    String attack = (String) attackSpinner.getSelectedItem();
                    ultiSpinner = (Spinner)findViewById(j*10);
                    String ulti = (String) ultiSpinner.getSelectedItem();
                    editText = (EditText)findViewById(j*11);
                    String copyName = editText.getText().toString();
                    System.err.println(attack + " - " + ulti + " - " + copyName);
                    pokemonHelper.insertCopy(attack, ulti, copyName, pokeID);
                }

                Intent i = new Intent (getApplicationContext(), MyPokeDetails.class);
                i.putExtra("id", pokeID);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), MyPokeDetails.class);
        i.putExtra("id", pokeID);
        startActivity(i);
        return true;
    }
}