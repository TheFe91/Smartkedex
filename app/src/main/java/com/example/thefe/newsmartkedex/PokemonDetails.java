package com.example.thefe.newsmartkedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by TheFe on 02/10/2016.
 */

    public class PokemonDetails extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedetails);

        getActionBar();

        final TextView tv = (TextView) findViewById(R.id.descriptiontext);
        tv.setVisibility(View.GONE);
        Button showhide = (Button) findViewById(R.id.showhidedescr);
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getVisibility() == View.VISIBLE)
                    tv.setVisibility(View.GONE);
                else if (tv.getVisibility() == View.GONE)
                    tv.setVisibility(View.VISIBLE);
            }
        });

    }

    public void printDescription () {

    }
    //momentarily disabled
//    public void printKind (String tipo) {
//        ImageView imageView = (ImageView) findViewById(R.id.tipo1);
//        imageView.setImageResource(getResources().getIdentifier(tipo, "drawable", getPackageName()));
//    }

}
