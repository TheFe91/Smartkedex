package com.rollercoders.smartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_poke_details);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int dpi = displayMetrics.densityDpi;

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");
        final PokemonDetails pokemonDetails = new PokemonDetails();
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonDetails.getName(pokeID);

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText(getResources().getString(getResources().getIdentifier("edit_your_pokemons", "string", getPackageName()), pokeName));
        textView = (TextView)findViewById(R.id.number);
        textView.setText(getResources().getString(getResources().getIdentifier("number_insert_pokemons", "string", getPackageName()), pokeName));
        textView = (TextView)findViewById(R.id.oldcopies);
        textView.setText(getResources().getString(getResources().getIdentifier("already_inserted", "string", getPackageName())));
        textView = (TextView)findViewById(R.id.newcopies);
        textView.setText(getResources().getString(getResources().getIdentifier("pokemon_to_insert", "string", getPackageName())));


        //defining and setting up the SpinnerNumber
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerNumber);
        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < 11; j++) {
            list.add(j);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        final List<Integer> pokeIds = pokemonHelper.getIdsFromPokeID(pokeID);

        final ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());

        //defining and Setting-up the already in the database Pokémons
        for (int pokeId:pokeIds) {
            //get a reference for the TableLayout
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

            params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            attackSpinner = new Spinner(getApplicationContext());
            attackSpinner.setLayoutParams(params);
            attackSpinner.setId(pokeId);
            if (pokeID != 132) {
                ultiSpinner = new Spinner(getApplicationContext());
                ultiSpinner.setLayoutParams(params);
                ultiSpinner.setId(pokeId*11);
            }

            String[] moves = pokemonHelper.getPokeAttacks(pokeId); //0 is attack, 1 is ulti

            List<String> attacks = pokemonHelper.getMoves(pokeID, "HasAttack");

            ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, attacks);
            attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            attackSpinner.setAdapter(attacksAdapter);
            attackSpinner.setSelection(attacksAdapter.getPosition(moves[0]));
            attackSpinner.setBackgroundColor(getResources().getColor(getResources().getIdentifier(pokemonHelper.getMovesType(moves[0], "Attack").toLowerCase(), "color", getPackageName())));

            //this is necessary because there can be Pokémons like Ditto who don't have a ulti, otherwise the app crashes
            if (pokeID != 132) {
                List<String> ultis = pokemonHelper.getMoves(pokeID, "HasUlti");

                ArrayAdapter<String> ultiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ultis);
                attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ultiSpinner.setAdapter(ultiAdapter);
                ultiSpinner.setSelection(ultiAdapter.getPosition(moves[1]));
                ultiSpinner.setBackgroundColor(getResources().getColor(getResources().getIdentifier(pokemonHelper.getMovesType(moves[1], "Ulti").toLowerCase(), "color", getPackageName())));
            }

            editText = new EditText(getApplicationContext());
            if (dpi == 480) {
                editText.setWidth(250);
                editText.setTextSize(13);
            }
            else if (dpi == 420)
                editText.setWidth(300);

            editText.setHint(getResources().getString(getResources().getIdentifier("hint_color", "string", getPackageName())));
            editText.setHintTextColor(getResources().getColor(R.color.acciaio));
            editText.setTextColor(getResources().getColor(android.R.color.black));
            editText.setId(pokeId*13);
            editText.setText(pokemonHelper.getCopyName(pokeId));

            checkBox = new CheckBox(getApplicationContext());
            checkBox.setButtonDrawable(R.drawable.checkbox_selector);
            checkBox.setId(pokeId*17);
            //Toast.makeText(getApplicationContext(), ""+checkBox.getId(), Toast.LENGTH_SHORT).show();

            // add the TextView  to the new TableRow
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0,10,0,0);
            row.addView(iv);
            editText.setLayoutParams(params);
            row.addView(editText);
            attackRow.addView(attackSpinner);
            if (pokeID != 132)
                ultiRow.addView(ultiSpinner);
            internaltable.addView(attackRow);
            if (pokeID != 132)
                internaltable.addView(ultiRow);
            internaltable.setLayoutParams(params);
            row.addView(internaltable);
            params.setMargins(15,0,0,0);
            checkBox.setLayoutParams(params);
            row.addView(checkBox);

            // add the TableRow to the TableLayout
            table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        //defining and setting-up the new Pokémons
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get a reference for the TableLayout
                TableLayout table = (TableLayout)findViewById(R.id.internalcopies);
                table.removeAllViews(); //this is to remove all previous views created from an eventual prevuious tap

                for (int j = 1+pokeIds.size(); j <= (int)spinner.getSelectedItem()+pokeIds.size(); j++) {
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
                    if (dpi == 480) {
                        editText.setWidth(250);
                        editText.setTextSize(13);
                    }
                    else if (dpi == 420)
                        editText.setWidth(300);
                    editText.setHint(getResources().getString(getResources().getIdentifier("hint_name", "string", getPackageName())));
                    editText.setHintTextColor(getResources().getColor(R.color.acciaio));
                    editText.setTextColor(getResources().getColor(android.R.color.black));
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        Button apply = (Button)findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for loop to enter modifications
                for (int pokeId:pokeIds) {
                    attackSpinner = (Spinner)findViewById(pokeId);
                    String attack = (String) attackSpinner.getSelectedItem();
                    String ulti = "";
                    if (pokeID != 132) {
                        ultiSpinner = (Spinner) findViewById(pokeId * 11);
                        ulti = (String) ultiSpinner.getSelectedItem();
                    }
                    editText = (EditText)findViewById(pokeId*13);
                    String copyName = editText.getText().toString();
                    String[] attacks = pokemonHelper.getPokeAttacks(pokeId); //attacks[0] contains the attack, attack[1] contains the ulti
                    if (!attack.equals(attacks[0])) {
                        pokemonHelper.updatePokeAttack(attack, pokeId);
                    }
                    if (!ulti.equals(attacks[1])) {
                        pokemonHelper.updatePokeUlti(ulti, pokeId);
                    }
                    if (!copyName.equals(pokemonHelper.getCopyName(pokeId))) {
                        pokemonHelper.updatePokeName(copyName, pokeId);
                    }
                    checkBox = (CheckBox)findViewById(pokeId*17);
                    if (checkBox.isChecked()) {
                        pokemonHelper.deleteCopy(pokeId);
                    }
                }

                //for loop to enter new Pokémons
                for (int j = 1+pokeIds.size(); j <= (int)spinner.getSelectedItem()+pokeIds.size(); j++) {
                    attackSpinner = (Spinner)findViewById(j);
                    String attack = (String) attackSpinner.getSelectedItem();
                    ultiSpinner = (Spinner)findViewById(j*10);
                    String ulti = (String) ultiSpinner.getSelectedItem();
                    editText = (EditText)findViewById(j*11);
                    String copyName = editText.getText().toString();
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