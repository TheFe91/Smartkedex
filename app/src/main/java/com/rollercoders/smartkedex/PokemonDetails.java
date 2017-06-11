package com.rollercoders.smartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


/**
 * Created by TheFe on 02/10/2016.
 */

public class PokemonDetails extends AppCompatActivity {

    private TextView tv;
    private TextToSpeech t1;
    private String toSpeech = "";
    private int pokeID;
    private HashMap<String, String> parameters;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setters for the correct display configuration
        //saving the device's dpi on an int and assigning the proper layout based on this int
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        if (dpi <= 420) { //Nexus 5X et simila
            setContentView(R.layout.pokedetailsbig);
        }
        else if (dpi == 480) { //Nexus 5 et simila
            setContentView(R.layout.pokedetails);
        }

        tv = (TextView) findViewById(R.id.descriptiontext);

        //get Intent Data
        Intent i = getIntent();

        //Setting up server and database calls
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);

        //selecting Pokémon's ID
        pokeID = i.getExtras().getInt("id");
        final String pokeName = pokemonHelper.getPokeName(pokeID+1);

        final ImageAdapter imageAdapter = new ImageAdapter(this);

        //Setting the Pokémon's Image
        final ImageView[] imageView = {(ImageView) findViewById(R.id.tmppkmn)};
        imageView[0].setImageResource(imageAdapter.mThumbIds[pokeID]);
        imageView[0].setContentDescription("Image of the current Pokémon, "+pokeName);

        pokeID +=1; //this is because the gridview wants the ID to be counted from 0, while every other method wants it to be counted by 1
                    //so after having called the big Image from the GridView, everything else needs the ID incremented by 1

        //setting the Pokémon's name under its image
        TextView pkmnName = (TextView)findViewById(R.id.pkmnName);
        if (pokeID < 10)
            pkmnName.setText("#00"+pokeID+" - "+pokeName);
        else if (pokeID > 9 && pokeID < 100) {
            switch (pokeName) {
                case "Nidoran_femmina":
                    pkmnName.setText(getResources().getString(getResources().getIdentifier("nidoran_female_adjust", "string", getPackageName())));
                    break;
                case "Nidoran_maschio":
                    pkmnName.setText(getResources().getString(getResources().getIdentifier("nidoran_male_adjust", "string", getPackageName())));
                    break;
                default:
                    pkmnName.setText("#0" + pokeID + " - " + pokeName);
                    break;
            }
        }
        else
            if (pokeID == 122) {
                pkmnName.setText(getResources().getString(getResources().getIdentifier("mrmime_adjust", "string", getPackageName())));
            }
            else
                pkmnName.setText("#"+pokeID+" - "+pokeName);

        final Switch pokeSwitch = (Switch) findViewById(R.id.dettagli);
        final Button pokeDetails = (Button) findViewById(R.id.catturato);
        pokeSwitch.setText(getResources().getString(getResources().getIdentifier("caught", "string", getPackageName())));

        if (pokemonHelper.getPokemonGO() == 1) { //if my user plays PokémonGO
            pokeDetails.setText(getResources().getString(getResources().getIdentifier("details", "string", getPackageName())));
            int catched = pokemonHelper.getCatched(pokeID);
            if (catched == 0) {
                pokeDetails.setEnabled(false);
                pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (pokeSwitch.isChecked()) {
                            pokeDetails.setEnabled(true);
                            pokemonHelper.insertCatches(pokeID); //storing into Catches that my user caught that Pokémon

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float)1.0);

                            //getting Pokémon's type(s)
                            List<String> types = pokemonHelper.getPokeTypes(pokeID);
                            int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above
                            TextView tipiScritta = (TextView)findViewById(R.id.tipiScritta);

                            if (numberOfTypes == 1) { //it can be 1 or 2
                                tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipo", "string", getPackageName())));
                                for (String element:types) {
                                    element = element.toLowerCase();
                                    imageView[0] = (ImageView) findViewById(R.id.tipo1);
                                    imageView[0].setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));

                                    //removing the second type
                                    imageView[0] = (ImageView) findViewById(R.id.tipo2);
                                    ViewGroup type2 = (ViewGroup) imageView[0].getParent();
                                    type2.removeView(imageView[0]);
                                }
                            }
                            else {
                                tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipi", "string", getPackageName())));
                                int j = 1;
                                for (String element:types) {
                                    element = element.toLowerCase();
                                    String id = "tipo"+j; //creating a variable to set my imageview resource: it will be "tipo1" at the first iteration and "tipo2" at the second iteration
                                    imageView[0] = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                                    imageView[0].setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));
                                    j++;
                                }
                            }

                            //setting the Pokémon's strenghts
                            List<String> strenghts = pokemonHelper.getStrenghts(pokeID);
                            if (strenghts.size() == 0) {
                                TextView tv = (TextView)findViewById(R.id.forteContro);
                                tv.setText(getResources().getString(getResources().getIdentifier("not_strong", "string", getPackageName())));
                            }
                            else {
                                int counter = 1;
                                for (String strenght:strenghts) {
                                    String id = "tsf"+counter;
                                    strenght = strenght.toLowerCase();
                                    imageView[0] = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                                    imageView[0].setImageResource(getResources().getIdentifier(strenght, "drawable", getPackageName()));
                                    counter++;
                                }
                            }

                            //setting the Pokémon's weaknesses
                            List<String> weaknesses = pokemonHelper.getWeaknesses(pokeID);
                            int counter = 1;
                            for (String weakness:weaknesses) {
                                String id = "tsd"+counter;
                                weakness = weakness.toLowerCase();
                                imageView[0] = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                                imageView[0].setImageResource(getResources().getIdentifier(weakness, "drawable", getPackageName()));
                                counter++;
                            }

                        }
                        else
                            pokeDetails.setEnabled(false);

                            pokemonHelper.delete(pokeID);

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float)0.1);

                            imageView[0] = (ImageView) findViewById(R.id.tipo1);
                            imageView[0].setImageResource(R.drawable.unknown);
                            imageView[0] = (ImageView) findViewById(R.id.tipo2);
                            imageView[0].setImageResource(R.drawable.unknown);

                            for (int i=1; i<=4; i++) {
                                imageView[0] = (ImageView)findViewById(getResources().getIdentifier("tsf"+i, "id", getPackageName()));
                                imageView[0].setImageResource(R.drawable.unknown);
                            }

                            for (int i=1; i<=4; i++) {
                                imageView[0] = (ImageView)findViewById(getResources().getIdentifier("tsd"+i, "id", getPackageName()));
                                imageView[0].setImageResource(R.drawable.unknown);
                            }
                    }
                });
            }
            else {
                pokeDetails.setEnabled(true);

                imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                imageView[0].setAlpha((float)1.0);

                //getting Pokémon's type(s)
                List<String> types = pokemonHelper.getPokeTypes(pokeID);
                int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above
                TextView tipiScritta = (TextView)findViewById(R.id.tipiScritta);

                if (numberOfTypes == 1) { //it can be 1 or 2
                    tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipo", "string", getPackageName())));
                    for (String element:types) {
                        element = element.toLowerCase();
                        imageView[0] = (ImageView) findViewById(R.id.tipo1);
                        imageView[0].setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));

                        //removing the second type
                        imageView[0] = (ImageView) findViewById(R.id.tipo2);
                        ViewGroup type2 = (ViewGroup) imageView[0].getParent();
                        type2.removeView(imageView[0]);
                    }
                }
                else {
                    tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipi", "string", getPackageName())));
                    int j = 1;
                    for (String element:types) {
                        element = element.toLowerCase();
                        String id = "tipo"+j; //creating a variable to set my imageview resource: it will be "tipo1" at the first iteration and "tipo2" at the second iteration
                        imageView[0] = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                        imageView[0].setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));
                        j++;
                    }
                }

                //setting the Pokémon's strenghts
                List<String> strenghts = pokemonHelper.getStrenghts(pokeID);
                if (strenghts.size() == 0) {
                    TextView tv = (TextView)findViewById(R.id.forteContro);
                    tv.setText(getResources().getString(getResources().getIdentifier("not_strong", "string", getPackageName())));
                }
                else {
                    int counter = 1;
                    for (String strenght:strenghts) {
                        String id = "tsf"+counter;
                        strenght = strenght.toLowerCase();
                        imageView[0] = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                        imageView[0].setImageResource(getResources().getIdentifier(strenght, "drawable", getPackageName()));
                        counter++;
                    }
                }

                //setting the Pokémon's weaknesses
                List<String> weaknesses = pokemonHelper.getWeaknesses(pokeID);
                int counter = 1;
                for (String weakness:weaknesses) {
                    String id = "tsd"+counter;
                    weakness = weakness.toLowerCase();
                    imageView[0] = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                    imageView[0].setImageResource(getResources().getIdentifier(weakness, "drawable", getPackageName()));
                    counter++;
                }

            }

            //listening if the Details button is pressed
            pokeDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MyPokeDetails.class);
                    i.putExtra("id", pokeID);
                    startActivity(i);
                }
            });
        }
        else { //my user doesn't play PokémonGO
            pokeDetails.setAlpha(0);
            int catched = pokemonHelper.getCatched(pokeID);
            if (catched == 0) {
                imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                imageView[0].setAlpha((float) 0.1);

                TextView tipiScritta = (TextView)findViewById(R.id.tipiScritta);

                List<String> types = pokemonHelper.getPokeTypes(pokeID);
                int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above

                if (numberOfTypes == 1)
                    tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipo", "string", getPackageName())));
                else
                    tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipi", "string", getPackageName())));

                imageView[0] = (ImageView) findViewById(R.id.tipo1);
                imageView[0].setImageResource(R.drawable.unknown);
                if (numberOfTypes == 2) {
                    imageView[0] = (ImageView) findViewById(R.id.tipo2);
                    imageView[0].setImageResource(R.drawable.unknown);
                }

                for (int k = 1; k <= 4; k++) {
                    imageView[0] = (ImageView) findViewById(getResources().getIdentifier("tsf" + k, "id", getPackageName()));
                    imageView[0].setImageResource(R.drawable.unknown);
                }

                for (int k = 1; k <= 4; k++) {
                    imageView[0] = (ImageView) findViewById(getResources().getIdentifier("tsd" + k, "id", getPackageName()));
                    imageView[0].setImageResource(R.drawable.unknown);
                }

                pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (pokeSwitch.isChecked()) {
                            pokeDetails.setEnabled(true);
                            pokemonHelper.insertCatches(pokeID); //storing into Catches that my user caught that Pokémon

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float)1.0);

                            //getting Pokémon's type(s)
                            List<String> types = pokemonHelper.getPokeTypes(pokeID);
                            int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above
                            TextView tipiScritta = (TextView)findViewById(R.id.tipiScritta);

                            if (numberOfTypes == 1) { //it can be 1 or 2
                                tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipo", "string", getPackageName())));
                                for (String element:types) {
                                    element = element.toLowerCase();
                                    imageView[0] = (ImageView) findViewById(R.id.tipo1);
                                    imageView[0].setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));

                                    if (findViewById(R.id.tipo2) != null) {
                                        //removing the second type
                                        imageView[0] = (ImageView) findViewById(R.id.tipo2);
                                        ViewGroup type2 = (ViewGroup) imageView[0].getParent();
                                        type2.removeView(imageView[0]);
                                    }
                                }
                            }
                            else {
                                tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipi", "string", getPackageName())));
                                int j = 1;
                                for (String element:types) {
                                    element = element.toLowerCase();
                                    String id = "tipo"+j; //creating a variable to set my imageview resource: it will be "tipo1" at the first iteration and "tipo2" at the second iteration
                                    imageView[0] = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                                    imageView[0].setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));
                                    j++;
                                }
                            }

                            //setting the Pokémon's strenghts
                            List<String> strenghts = pokemonHelper.getStrenghts(pokeID);
                            if (strenghts.size() == 0) {
                                TextView tv = (TextView)findViewById(R.id.forteContro);
                                tv.setText(getResources().getString(getResources().getIdentifier("not_strong", "string", getPackageName())));
                            }
                            else {
                                int counter = 1;
                                for (String strenght:strenghts) {
                                    String id = "tsf"+counter;
                                    strenght = strenght.toLowerCase();
                                    imageView[0] = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                                    imageView[0].setImageResource(getResources().getIdentifier(strenght, "drawable", getPackageName()));
                                    counter++;
                                }
                            }

                            //setting the Pokémon's weaknesses
                            List<String> weaknesses = pokemonHelper.getWeaknesses(pokeID);
                            int counter = 1;
                            for (String weakness:weaknesses) {
                                String id = "tsd"+counter;
                                weakness = weakness.toLowerCase();
                                imageView[0] = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                                imageView[0].setImageResource(getResources().getIdentifier(weakness, "drawable", getPackageName()));
                                counter++;
                            }

                        }
                        else {
                            pokemonHelper.delete(pokeID);

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float) 0.1);

                            List<String> types = pokemonHelper.getPokeTypes(pokeID);
                            int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above

                            imageView[0] = (ImageView) findViewById(R.id.tipo1);
                            imageView[0].setImageResource(R.drawable.unknown);
                            if (numberOfTypes == 2) {
                                imageView[0] = (ImageView) findViewById(R.id.tipo2);
                                imageView[0].setImageResource(R.drawable.unknown);
                            }

                            for (int i = 1; i <= 4; i++) {
                                imageView[0] = (ImageView) findViewById(getResources().getIdentifier("tsf" + i, "id", getPackageName()));
                                imageView[0].setImageResource(R.drawable.unknown);
                            }

                            for (int i = 1; i <= 4; i++) {
                                imageView[0] = (ImageView) findViewById(getResources().getIdentifier("tsd" + i, "id", getPackageName()));
                                imageView[0].setImageResource(R.drawable.unknown);
                            }
                        }
                    }
                });
            }
        }

        getActionBar();

        //this button controls the text and the "read" button next to it, making it appear and disappear
        Button showhide = (Button) findViewById(R.id.showhidedescr);
        final Button leggi = (Button)findViewById(R.id.leggidescrizione);
        tv.setText(getResources().getString(getResources().getIdentifier("pkmn"+pokeID, "string", getPackageName())));
        toSpeech = getResources().getString(getResources().getIdentifier("pkmn"+pokeID, "string", getPackageName()));
        tv.setVisibility(View.GONE);
        leggi.setVisibility(View.GONE);
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getVisibility() == View.VISIBLE) {
                    tv.setVisibility(View.GONE);
                    leggi.setVisibility(View.GONE);
                }
                else if (tv.getVisibility() == View.GONE) {
                    tv.setVisibility(View.VISIBLE);
                    if (!leggi.isEnabled())
                        leggi.setEnabled(true);
                    leggi.setVisibility(View.VISIBLE);
                }
            }
        });

        leggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                                @Override
                                public void onStart(String utteranceId) {
                                    leggi.setEnabled(false);
                                }

                                @Override
                                public void onDone(String utteranceId) {
                                    leggi.setEnabled(true);
                                }

                                @Override
                                public void onError(String utteranceId) {

                                }
                            });
                            parameters = new HashMap<>();
                            parameters.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "fine");
                            t1.speak(toSpeech, TextToSpeech.QUEUE_FLUSH, parameters);
                        }
                    }
                });
            }
        });
    }

    private void fadeOutAndHideSwitch (final Switch aSwitch) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                aSwitch.setVisibility(View.GONE);
            }

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}

        });

        aSwitch.startAnimation(fadeOut);
    }

}