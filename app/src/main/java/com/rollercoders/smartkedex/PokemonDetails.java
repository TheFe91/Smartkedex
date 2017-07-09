package com.rollercoders.smartkedex;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


/**
 * Created by TheFe on 02/10/2016.
 */

public class PokemonDetails extends AppCompatActivity {

    private TextView tv, pkmnName, tipiScritta;
    private TextToSpeech t1;
    private String toSpeech = "", pokeName;
    private int pokeID, effectiveWidth;
    private HashMap<String, String> parameters;
    private Button showhide, leggi;
    private PokemonDatabaseAdapter pokemonHelper;
    private Typeface typeface;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/cmss12.otf");

        //setters for the correct display configuration
        //saving the device's dpi on an int and assigning the proper layout based on this int
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        int totalWidth = displayMetrics.widthPixels;
        effectiveWidth = totalWidth - 128; //128 is because I have 2 64dp margins on the Right and on the Left

        if (dpi <= 420) { //Nexus 5X et simila
            setContentView(R.layout.pokedetailsbig);
        }
        else if (dpi == 480) { //Nexus 5 et simila
            setContentView(R.layout.pokedetails);
        }

        showhide = (Button) findViewById(R.id.showhidedescr);

        tv = (TextView) findViewById(R.id.descriptiontext);

        //get Intent Data
        Intent i = getIntent();

        //Setting up server and database calls
        pokemonHelper = new PokemonDatabaseAdapter(this);

        //selecting Pokémon's ID
        pokeID = i.getExtras().getInt("id");
        pokeName = pokemonHelper.getPokeName(pokeID+1, getApplicationContext());

        final ImageAdapter imageAdapter = new ImageAdapter(this);

        //Setting the Pokémon's Image
        final ImageView[] imageView = {(ImageView) findViewById(R.id.tmppkmn)};
        imageView[0].setImageResource(imageAdapter.mThumbIds[pokeID]);
        imageView[0].setContentDescription("Image of the current Pokémon, "+pokeName);

        pokeID +=1; //this is because the gridview wants the ID to be counted from 0, while every other method wants it to be counted by 1
                    //so after having called the big Image from the GridView, everything else needs the ID incremented by 1

        //setting the Pokémon's name under its image
        pkmnName = (TextView)findViewById(R.id.pkmnName);

        final Switch pokeSwitch = (Switch) findViewById(R.id.dettagli);
        final Button pokeDetails = (Button) findViewById(R.id.catturato);
        pokeSwitch.setText(getResources().getString(getResources().getIdentifier("caught", "string", getPackageName())));

        RelativeLayout overlay = (RelativeLayout) findViewById(R.id.pokedetails);
        overlay.setOnTouchListener(new OnSwipeTouchListener(PokemonDetails.this) {
            public void onSwipeRight() {
                if (pokeID != 1) {
                    Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                    i.putExtra("id", pokeID-2);
                    startActivity(i);
                    finish();
                }
            }
            public void onSwipeLeft() {
                if (pokeID != 151) {
                    Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                    i.putExtra("id", pokeID);
                    startActivity(i);
                    finish();
                }
            }
        });

        if (pokemonHelper.getPokemonGO() == 1) { //if my user plays PokémonGO
            pokeDetails.setText(getResources().getString(getResources().getIdentifier("details", "string", getPackageName())));
            int catched = pokemonHelper.getCatched(pokeID, getApplicationContext());
            if (catched == 0) {
                pokeDetails.setEnabled(false);
                imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                imageView[0].setAlpha((float) 0.1);

                unsetPokeName();
                unsetTipiScritta();
                unsetStrenght();
                unsetWeakness();

                descrButton();

                showhide.setEnabled(false);
                showhide.setAlpha((float)0.0);

                pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (pokeSwitch.isChecked()) {
                            pokemonHelper.insertCatches(pokeID, getApplicationContext()); //storing into Catches that my user caught that Pokémon
                            pokeDetails.setEnabled(true);

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float)1.0);

                            setPokeName();
                            setTipiScritta();
                            setStrenght();
                            setWeakness();

                            descrButton();

                            showhide.setEnabled(true);
                            showhide.setAlpha((float)1.0);

                        }
                        else {
                            pokemonHelper.delete(pokeID, getApplicationContext());

                            pokeDetails.setEnabled(false);

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float) 0.1);

                            unsetPokeName();
                            unsetTipiScritta();

                            TextView tv = (TextView)findViewById(R.id.forteContro);
                            tv.setText(getResources().getString(getResources().getIdentifier("strong", "string", getPackageName())));

                            unsetStrenght();
                            unsetWeakness();

                            descrButton();

                            showhide.setEnabled(false);
                            showhide.setAlpha((float)0.0);
                        }
                    }
                });
            }
            else {
                pokeSwitch.setChecked(true);
                pokeDetails.setEnabled(true);

                imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                imageView[0].setAlpha((float)1.0);

                setPokeName();
                setTipiScritta();

                setStrenght();
                setWeakness();

                descrButton();

                showhide.setEnabled(true);
                showhide.setAlpha((float)1.0);

                pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (pokeSwitch.isChecked()) {
                        pokemonHelper.insertCatches(pokeID, getApplicationContext()); //storing into Catches that my user caught that Pokémon

                        imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                        imageView[0].setAlpha((float)1.0);

                        setPokeName();
                        setTipiScritta();

                        setStrenght();
                        setWeakness();

                        descrButton();

                        showhide.setEnabled(true);
                        showhide.setAlpha((float)1.0);

                    }
                    else {
                        pokemonHelper.delete(pokeID, getApplicationContext());

                        imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                        imageView[0].setAlpha((float) 0.1);

                        unsetPokeName();
                        unsetTipiScritta();

                        tv = (TextView)findViewById(R.id.forteContro);
                        tv.setText(getResources().getString(getResources().getIdentifier("strong", "string", getPackageName())));

                        unsetStrenght();
                        unsetWeakness();

                        descrButton();

                        showhide.setEnabled(false);
                        showhide.setAlpha((float)0.0);
                    }
                    }
                });
            }
        }
        else { //my user doesn't play PokémonGO
            pokeDetails.setAlpha(0);
            pokeDetails.setEnabled(false);
            int catched = pokemonHelper.getCatched(pokeID, getApplicationContext());
            if (catched == 0) {
                imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                imageView[0].setAlpha((float) 0.1);

                unsetPokeName();
                unsetTipiScritta();

                unsetStrenght();
                unsetWeakness();

                descrButton();

                showhide.setEnabled(false);
                showhide.setAlpha((float)0.0);

                pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (pokeSwitch.isChecked()) {
                            pokemonHelper.insertCatches(pokeID, getApplicationContext()); //storing into Catches that my user caught that Pokémon

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float)1.0);

                            setPokeName();
                            setTipiScritta();

                            setStrenght();
                            setWeakness();

                            descrButton();

                            showhide.setEnabled(true);
                            showhide.setAlpha((float)1.0);

                        }
                        else {
                            pokemonHelper.delete(pokeID, getApplicationContext());

                            imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                            imageView[0].setAlpha((float) 0.1);

                            unsetPokeName();
                            unsetTipiScritta();

                            unsetStrenght();
                            unsetWeakness();

                            descrButton();

                            showhide.setEnabled(false);
                            showhide.setAlpha((float)0.0);
                        }
                    }
                });
            }
            else {
                pokeSwitch.setChecked(true);

                imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                imageView[0].setAlpha((float)1.0);

                setPokeName();
                setTipiScritta();

                setStrenght();
                setWeakness();

                descrButton();

                showhide.setEnabled(true);
                showhide.setAlpha((float)1.0);

                pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (pokeSwitch.isChecked()) {
                        pokemonHelper.insertCatches(pokeID, getApplicationContext()); //storing into Catches that my user caught that Pokémon

                        imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                        imageView[0].setAlpha((float)1.0);

                        setPokeName();
                        setTipiScritta();

                        setStrenght();
                        setWeakness();

                        descrButton();

                        showhide.setEnabled(true);
                        showhide.setAlpha((float)1.0);

                    }
                    else {
                        pokemonHelper.delete(pokeID, getApplicationContext());

                        imageView[0] = (ImageView) findViewById(R.id.tmppkmn);
                        imageView[0].setAlpha((float) 0.1);

                        unsetPokeName();
                        unsetTipiScritta();

                        unsetStrenght();
                        unsetWeakness();

                        descrButton();

                        showhide.setEnabled(false);
                        showhide.setAlpha((float)0.0);
                    }
                    }
                });
            }
        }

        getActionBar();

        //this button controls the text and the "read" button next to it, making it appear and disappear
        toSpeech = getResources().getString(getResources().getIdentifier("pkmn"+pokeID, "string", getPackageName()));

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

        pokeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MyPokeDetails.class);
                i.putExtra("id", pokeID);
                startActivity(i);
            }
        });
    }

    private void setPokeName () {
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
    }

    private void unsetPokeName () {
        if (pokeID < 10)
            pkmnName.setText("#00"+pokeID+" - ???");
        else if (pokeID > 9 && pokeID < 100) {
            pkmnName.setText("#0"+pokeID+" - ???");
        }
        else
            pkmnName.setText("#"+pokeID+" - ???");
    }

    private void setTipiScritta () {
        tipiScritta = (TextView)findViewById(R.id.tipiScritta);

        List<String> types = pokemonHelper.getPokeTypes(pokeID, getApplicationContext());
        int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above

        if (numberOfTypes == 1) { //it can be 1 or 2
            tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipo", "string", getPackageName())));
            for (String element:types) {
                element = element.toLowerCase();
                tv = (TextView) findViewById(R.id.tipo1);
                TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
                params.width = effectiveWidth / 4;
                tv.setLayoutParams(params);
                tv.setText(getResources().getString(getResources().getIdentifier(element, "string", getPackageName())));
                tv.setBackgroundResource(R.drawable.rounded_corners);
                tv.setTypeface(typeface);
                tv.setTextColor(getResources().getColor(R.color.white));
                GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
                gradientDrawable.setColor(getResources().getColor(getResources().getIdentifier(element, "color", getPackageName())));

                if (findViewById(R.id.tipo2) != null) {
                    //removing the second type
                    tv = (TextView) findViewById(R.id.tipo2);
                    tv.setAlpha((float)0.0);
                }
            }
        }
        else {
            tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipi", "string", getPackageName())));
            int j = 1;
            for (String element:types) {
                element = element.toLowerCase();
                tv = (TextView) findViewById(getResources().getIdentifier("tipo" + j, "id", getPackageName()));
                TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
                params.width = effectiveWidth / 4;
                tv.setLayoutParams(params);
                System.err.println(element);
                tv.setText(getResources().getString(getResources().getIdentifier(element, "string", getPackageName())));
                tv.setBackgroundResource(R.drawable.rounded_corners);
                tv.setTypeface(typeface);
                tv.setTextColor(getResources().getColor(R.color.white));
                GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
                gradientDrawable.setColor(getResources().getColor(getResources().getIdentifier(element, "color", getPackageName())));
                j++;
            }
        }
    }

    private void unsetTipiScritta () {
        tipiScritta = (TextView)findViewById(R.id.tipiScritta);
        tipiScritta.setText(getResources().getString(getResources().getIdentifier("tipi", "string", getPackageName())));

        for (int k = 1; k <= 2; k++) {
            tv = (TextView) findViewById(getResources().getIdentifier("tipo" + k, "id", getPackageName()));
            tv.setAlpha((float)1.0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
            params.width = effectiveWidth / 4;
            tv.setLayoutParams(params);
            tv.setText("???");
            tv.setBackgroundResource(R.drawable.rounded_corners);
            tv.setTypeface(typeface);
            tv.setTextColor(getResources().getColor(R.color.white));
            GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
            gradientDrawable.setColor(getResources().getColor(R.color.unknown));
        }
    }

    private void setStrenght () {
        List<String> strenghts = pokemonHelper.getStrenghts(pokeID, getApplicationContext());
        if (strenghts.size() == 0) {
            tv = (TextView)findViewById(R.id.forteContro);
            tv.setText(getResources().getString(getResources().getIdentifier("not_strong", "string", getPackageName())));
            for (int j=1; j<=4; j++) {
                tv = (TextView) findViewById(getResources().getIdentifier("tsf" + j, "id", getPackageName()));
                tv.setAlpha((float)0.0);
            }
        }
        else {
            int counter = 1;
            for (String strenght:strenghts) {
                strenght = strenght.toLowerCase();
                tv = (TextView) findViewById(getResources().getIdentifier("tsf" + counter, "id", getPackageName()));
                RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                rlParams.width = effectiveWidth/4;
                tv.setLayoutParams(rlParams);
                tv.setText(getResources().getString(getResources().getIdentifier(strenght, "string", getPackageName())));
                tv.setBackgroundResource(R.drawable.rounded_corners);
                tv.setTypeface(typeface);
                tv.setTextColor(getResources().getColor(R.color.white));
                GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
                gradientDrawable.setColor(getResources().getColor(getResources().getIdentifier(strenght, "color", getPackageName())));
                counter++;
            }
            while (counter <= 4) {
                tv = (TextView) findViewById(getResources().getIdentifier("tsf" + counter, "id", getPackageName()));
                tv.setAlpha((float)0.0);
                counter++;
            }
        }
    }

    private void unsetStrenght () {
        tv = (TextView)findViewById(R.id.forteContro);
        tv.setText(getResources().getString(getResources().getIdentifier("strong", "string", getPackageName())));
        for (int k = 1; k <= 4; k++) {
            tv = (TextView) findViewById(getResources().getIdentifier("tsf" + k, "id", getPackageName()));
            tv.setAlpha((float)1.0);
            RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            rlParams.width = effectiveWidth/4;
            tv.setLayoutParams(rlParams);
            tv.setText("???");
            tv.setBackgroundResource(R.drawable.rounded_corners);
            tv.setTypeface(typeface);
            tv.setTextColor(getResources().getColor(R.color.white));
            GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
            gradientDrawable.setColor(getResources().getColor(R.color.unknown));
        }
    }

    private void setWeakness () {
        List<String> weaknesses = pokemonHelper.getWeaknesses(pokeID, getApplicationContext());
        int counter = 1;
        for (String weakness:weaknesses) {
            weakness = weakness.toLowerCase();
            tv = (TextView) findViewById(getResources().getIdentifier("tsd" + counter, "id", getPackageName()));
            RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            rlParams.width = effectiveWidth/4;
            tv.setLayoutParams(rlParams);
            tv.setText(getResources().getString(getResources().getIdentifier(weakness, "string", getPackageName())));
            tv.setBackgroundResource(R.drawable.rounded_corners);
            tv.setTypeface(typeface);
            tv.setTextColor(getResources().getColor(R.color.white));
            GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
            gradientDrawable.setColor(getResources().getColor(getResources().getIdentifier(weakness, "color", getPackageName())));
            counter++;
        }
        while (counter <= 4) {
            tv = (TextView) findViewById(getResources().getIdentifier("tsd" + counter, "id", getPackageName()));
            tv.setAlpha((float)0.0);
            counter++;
        }
    }

    private void unsetWeakness () {
        for (int k = 1; k <= 4; k++) {
            tv = (TextView) findViewById(getResources().getIdentifier("tsd" + k, "id", getPackageName()));
            tv.setAlpha((float)1.0);
            RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            rlParams.width = effectiveWidth/4;
            tv.setLayoutParams(rlParams);
            tv.setText("???");
            tv.setBackgroundResource(R.drawable.rounded_corners);
            tv.setTypeface(typeface);
            tv.setTextColor(getResources().getColor(R.color.white));
            GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
            gradientDrawable.setColor(getResources().getColor(R.color.unknown));
        }
    }

    private void descrButton () {
        leggi = (Button)findViewById(R.id.leggidescrizione);
        tv = (TextView)findViewById(R.id.descriptiontext);
        tv.setText(getResources().getString(getResources().getIdentifier("pkmn"+pokeID, "string", getPackageName())));
        tv.setVisibility(View.GONE);
        leggi.setVisibility(View.GONE);
    }
}