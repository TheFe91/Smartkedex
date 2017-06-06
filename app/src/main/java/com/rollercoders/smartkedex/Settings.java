package com.rollercoders.smartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheFe on 17/10/2016.
 */

public class Settings extends AppCompatActivity {

    public String lingua = "ITA";
    private int setpokeGO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(getApplicationContext());
        getActionBar();

        //final TextView language = (TextView)findViewById(R.id.lingua);
        TextView name = (TextView)findViewById(R.id.nomeSmartkedex);
        TextView proprietario = (TextView)findViewById(R.id.proprietario);
        final EditText inputNome = (EditText)findViewById(R.id.inputNomeSmartkedex);
        final EditText inputProprietario = (EditText)findViewById(R.id.inputProprietario);
        Button presentazione = (Button)findViewById(R.id.presentazione);
        final Switch playPokemonGO = (Switch)findViewById(R.id.playPokemonGO);
        final Button reset = (Button)findViewById(R.id.reset);
        Button conferma = (Button)findViewById(R.id.conferma);

        inputNome.setText(pokemonHelper.getSmartkedex());
        inputProprietario.setText(pokemonHelper.getOwner());
        final int dbPokemonGO = pokemonHelper.getPokemonGO();

        if (dbPokemonGO == 0)
            playPokemonGO.setChecked(false);
        else {
            playPokemonGO.setChecked(true);
            setpokeGO = 1;
        }

        name.setText(getResources().getString(getResources().getIdentifier("editsmartkedexname", "string", getPackageName())));
        proprietario.setText(getResources().getString(getResources().getIdentifier("owner", "string", getPackageName())));
        presentazione.setText(getResources().getString(getResources().getIdentifier("presentazione", "string", getPackageName())));
        reset.setText(getResources().getString(getResources().getIdentifier("erase", "string", getPackageName())));
        conferma.setText(getResources().getString(getResources().getIdentifier("apply", "string", getPackageName())));
        playPokemonGO.setText(getResources().getString(getResources().getIdentifier("inside_playpokego", "string", getPackageName())));

        presentazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Speaking speaking = new Speaking(getApplicationContext());
                speaking.presentati();
            }
        });

        playPokemonGO.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (playPokemonGO.isChecked())
                    setpokeGO = 1;
                else
                    setpokeGO = 0;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset.setText(getResources().getString(getResources().getIdentifier("erase_confirm", "string", getPackageName())));
                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pokemonHelper.erase(pokemonHelper.getLocalUsername());
                        pokemonHelper.localErase();
                        Intent i = new Intent(getApplicationContext(), InitialLogin.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smartkedex = inputNome.getText().toString();
                String owner = inputProprietario.getText().toString();

                if (owner.equals("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(getResources().getIdentifier("editname_error", "string", getPackageName())), Toast.LENGTH_SHORT).show();
                    finish();
                }

                int rows = pokemonHelper.getRows("Settings");
                String dbSmartkedex = pokemonHelper.getSmartkedex();
                String dbOwner = pokemonHelper.getOwner();

                if (rows == 0) {
                    pokemonHelper.insertSettingsData(owner, smartkedex, setpokeGO);
                }
                else {
                    if (!owner.equals("") && !dbOwner.equals(owner))
                        pokemonHelper.updateOwner(owner, dbOwner);
                    if (!smartkedex.equals("") && !dbSmartkedex.equals(smartkedex))
                        pokemonHelper.updateSmartkedex(smartkedex, dbSmartkedex);
                    if (dbPokemonGO != setpokeGO)
                        pokemonHelper.updatePokemonGO(setpokeGO, dbPokemonGO);
                }

                finish();
            }
        });
    }

}
