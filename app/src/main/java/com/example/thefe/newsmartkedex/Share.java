package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by TheFe on 17/10/2016.
 */

public class Share extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.titleshare);
        textView.setText("Cosa vuoi condividere?");
        CheckBox checkBox = (CheckBox)findViewById(R.id.pokemons);
        checkBox.setText("Pokémon Posseduti");
        checkBox = (CheckBox)findViewById(R.id.names);
        checkBox.setText("...con i nomi");
        checkBox = (CheckBox)findViewById(R.id.attacks);
        checkBox.setText("...con gli attacchi");
        checkBox = (CheckBox)findViewById(R.id.images);
        checkBox.setText("...con le immagini");
        checkBox = (CheckBox)findViewById(R.id.data);
        checkBox.setText("non so cosa volessi dire con l'id \"data\"");
        checkBox = (CheckBox)findViewById(R.id.pokedex);
        checkBox.setText("Vista del Pokédex");

        Button button = (Button)findViewById(R.id.apply);
        button.setText("Conferma");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShareResult.class);
                startActivity(i);
            }
        });

    }
}
