package com.rollercoders.smartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private int setpokeGO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setters for the correct display configuration
        //saving the device's dpi on an int and assigning the proper layout based on this int
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        if (dpi <= 420) { //Nexus 5X et simila
            setContentView(R.layout.settings_big);
        }
        else if (dpi == 480) { //Nexus 5 et simila
            setContentView(R.layout.settings);
        }

        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(getApplicationContext());
        getActionBar();

        TextView name = (TextView)findViewById(R.id.nomeSmartkedex);
        TextView proprietario = (TextView)findViewById(R.id.proprietario);
        final EditText inputNome = (EditText)findViewById(R.id.inputNomeSmartkedex);
        Button presentazione = (Button)findViewById(R.id.presentazione);
        final Switch playPokemonGO = (Switch)findViewById(R.id.playPokemonGO);
        Button conferma = (Button)findViewById(R.id.conferma);
        Button logout = (Button)findViewById(R.id.logout);

        inputNome.setText(pokemonHelper.getSmartkedex());
        final int dbPokemonGO = pokemonHelper.getPokemonGO();

        if (dbPokemonGO == 0)
            playPokemonGO.setChecked(false);
        else {
            playPokemonGO.setChecked(true);
            setpokeGO = 1;
        }

        name.setText(getResources().getString(getResources().getIdentifier("smartkedexname", "string", getPackageName())));
        proprietario.setText(getResources().getString(getResources().getIdentifier("owner", "string", getPackageName())));
        presentazione.setText(getResources().getString(getResources().getIdentifier("presentazione", "string", getPackageName())));
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

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smartkedex = inputNome.getText().toString();;

                String dbSmartkedex = pokemonHelper.getSmartkedex();

                if (!dbSmartkedex.equals(smartkedex))
                    pokemonHelper.updateSmartkedex(smartkedex, dbSmartkedex);
                if (dbPokemonGO != setpokeGO)
                    pokemonHelper.updatePokemonGO(setpokeGO, dbPokemonGO);

                finish();
            }
        });

        logout.setText(getResources().getString(getResources().getIdentifier("logout", "string", getPackageName())));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pokemonHelper.doLogout();
                Intent i = new Intent(getApplicationContext(), InitialLogin.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = getMenuInflater();
        mf.inflate(R.menu.destroyer, menu);
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_warning_white_18dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.destroyer:
                Intent i = new Intent(getApplicationContext(), Destroyer.class);
                startActivity(i);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
