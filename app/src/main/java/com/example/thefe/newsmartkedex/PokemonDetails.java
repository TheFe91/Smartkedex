package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


/**
 * Created by TheFe on 02/10/2016.
 */

public class PokemonDetails extends AppCompatActivity {

    private TextView tv;
    private TextToSpeech t1;
    private String toSpeech = "";
    private int pokeID;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setters for the correct display configuration
        //saving the device's dpi on an int and assigning the proper layout based on this int
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        if (dpi == 420) { //Nexus 5X et simila
            setContentView(R.layout.pokedetailsbig);
        }
        else if (dpi == 480) { //Nexus 5 et simila
            setContentView(R.layout.pokedetails);
        }

        tv = (TextView) findViewById(R.id.descriptiontext);

        //get Intent Data
        Intent i = getIntent();

        //selecting Pokémon's ID
        pokeID = i.getExtras().getInt("id");
        String pokeName = getName(pokeID+1);

        //Setting up server and database calls
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);

        ImageAdapter imageAdapter = new ImageAdapter(this);

        //Setting the Pokémon's Image
        ImageView imageView = (ImageView) findViewById(R.id.tmppkmn);
        imageView.setImageResource(imageAdapter.mThumbIds[pokeID]);
        imageView.setContentDescription("Image of the current Pokémon, "+pokeName);

        pokeID +=1; //this is because the gridview wants the ID to be counted from 0, while every other method wants it to be counted by 1
                    //so after having called the big Image from the GridView, everything else needs the ID incremented by 1

        //getting Pokémon's type(s)
        List<String> types = pokemonHelper.getPokeTypes(pokeID);
        int numberOfTypes = types.size(); //checking if it has 1 or 2 types, as written above
        TextView tipiScritta = (TextView)findViewById(R.id.tipiScritta);

        if (numberOfTypes == 1) { //it can be 1 or 2
            tipiScritta.setText("Tipo:");
            for (String element:types) {
                element = element.toLowerCase();
                imageView = (ImageView) findViewById(R.id.tipo1);
                imageView.setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));

                //removing the second type
                imageView = (ImageView) findViewById(R.id.tipo2);
                ViewGroup type2 = (ViewGroup) imageView.getParent();
                type2.removeView(imageView);
            }
        }
        else {
            tipiScritta.setText("Tipi:");
            int j = 1;
            for (String element:types) {
                element = element.toLowerCase();
                String id = "tipo"+j; //creating a variable to set my imageview resource: it will be "tipo1" at the first iteration and "tipo2" at the second iteration
                imageView = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                imageView.setImageResource(getResources().getIdentifier(element, "drawable", getPackageName()));
                j++;
            }
        }

        //setting the Pokémon's name under its image
        TextView pkmnName = (TextView)findViewById(R.id.pkmnName);
        if (pokeID < 10)
            pkmnName.setText("#00"+pokeID+" - "+pokeName);
        else if (pokeID > 9 && pokeID < 100) {
            switch (pokeName) {
                case "Nidoran_femmina":
                    pkmnName.setText("#029 - Nidoran Femmina");
                    break;
                case "Nidoran_maschio":
                    pkmnName.setText("#032 - Nidoran Maschio");
                    break;
                default:
                    pkmnName.setText("#0" + pokeID + " - " + pokeName);
                    break;
            }
        }
        else
            pkmnName.setText("#"+pokeID+" - "+pokeName);

        final Switch pokeSwitch = (Switch) findViewById(R.id.dettagli);
        final Button pokeDetails = (Button) findViewById(R.id.catturato);
        pokeSwitch.setText("Posseduto  ");

        //setting the Pokémon's weaknesses
        List<String> weaknesses = pokemonHelper.getWeaknesses(pokeID);
        int counter = 1;
        for (String weakness:weaknesses) {
            String id = "tsd"+counter;
            weakness = weakness.toLowerCase();
            imageView = (ImageView)findViewById(getResources().getIdentifier(id, "id", getPackageName()));
            imageView.setImageResource(getResources().getIdentifier(weakness, "drawable", getPackageName()));
            counter++;
        }

        if (pokemonHelper.getPokemonGO() == 1) { //if my user plays PokémonGO
            pokeDetails.setText("Dettagli");
            int catched = pokemonHelper.getCatched(pokeID);
            if (catched == 0)
                pokeDetails.setEnabled(false);
            else {
                pokeDetails.setEnabled(true);
                pokeSwitch.setChecked(true);
            }

            pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (pokeSwitch.isChecked()) {
                        pokeDetails.setEnabled(true);
                        pokemonHelper.insertCatches(pokeID, pokemonHelper.getOwner()); //storing into Catches that my user caught that Pokémon
                    }
                    else
                        pokeDetails.setEnabled(false);
                }
            });

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
        else { //removing switch and button, since my user doesn't play PokémonGO
            pokeDetails.setAlpha(0);
            pokeSwitch.setAlpha(0);
        }

        getActionBar();

        //this button controls the text and the "read" button under it, making it appear and disappear
        Button showhide = (Button) findViewById(R.id.showhidedescr);
        final Button leggi = (Button)findViewById(R.id.leggidescrizione);
        tv.setText(pokemonHelper.getDescription(pokeID));
        toSpeech = pokemonHelper.getDescription(pokeID);
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
                    leggi.setVisibility(View.VISIBLE);
                }
            }
        });

        leggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            t1.setLanguage(Locale.ITALIAN);
                            t1.speak(toSpeech, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
            }
        });
    }

    public String getName (int pokeID) {
        String pokeName = "";

        switch (pokeID) {
            case 1: pokeName = "Bulbasaur"; break;
            case 2: pokeName = "Ivysaur"; break;
            case 3: pokeName = "Venusaur"; break;
            case 4: pokeName = "Charmander"; break;
            case 5: pokeName = "Charmeleon"; break;
            case 6: pokeName = "Charizard"; break;
            case 7: pokeName = "Squirtle"; break;
            case 8: pokeName = "Wartortle"; break;
            case 9: pokeName = "Blastoise"; break;
            case 10: pokeName = "Caterpie"; break;
            case 11: pokeName = "Metapod"; break;
            case 12: pokeName = "Butterfree"; break;
            case 13: pokeName = "Weedle"; break;
            case 14: pokeName = "Kakuna"; break;
            case 15: pokeName = "Beedrill"; break;
            case 16: pokeName = "Pidgey"; break;
            case 17: pokeName = "Pidgeotto"; break;
            case 18: pokeName = "Pidgeot"; break;
            case 19: pokeName = "Rattata"; break;
            case 20: pokeName = "Raticate"; break;
            case 21: pokeName = "Spearow"; break;
            case 22: pokeName = "Fearow"; break;
            case 23: pokeName = "Ekans"; break;
            case 24: pokeName = "Arbok"; break;
            case 25: pokeName = "Pikachu"; break;
            case 26: pokeName = "Raichu"; break;
            case 27: pokeName = "Sandshrew"; break;
            case 28: pokeName = "Sandslash"; break;
            case 29: pokeName = "Nidoran_femmina"; break;
            case 30: pokeName = "Nidorina"; break;
            case 31: pokeName = "Nidoqueen"; break;
            case 32: pokeName = "Nidoran_maschio"; break;
            case 33: pokeName = "Nidorino"; break;
            case 34: pokeName = "Nidoking"; break;
            case 35: pokeName = "Clefairy"; break;
            case 36: pokeName = "Clefable"; break;
            case 37: pokeName = "Vulpix"; break;
            case 38: pokeName = "Ninetales"; break;
            case 39: pokeName = "Jigglypuff"; break;
            case 40: pokeName = "Wigglytuff"; break;
            case 41: pokeName = "Zubat"; break;
            case 42: pokeName = "Golbat"; break;
            case 43: pokeName = "Oddish"; break;
            case 44: pokeName = "Gloom"; break;
            case 45: pokeName = "Vileplume"; break;
            case 46: pokeName = "Paras"; break;
            case 47: pokeName = "Parasect"; break;
            case 48: pokeName = "Venonat"; break;
            case 49: pokeName = "Venomoth"; break;
            case 50: pokeName = "Diglett"; break;
            case 51: pokeName = "Dugtrio"; break;
            case 52: pokeName = "Meowth"; break;
            case 53: pokeName = "Persian"; break;
            case 54: pokeName = "Psyduck"; break;
            case 55: pokeName = "Golduck"; break;
            case 56: pokeName = "Mankey"; break;
            case 57: pokeName = "Primeape"; break;
            case 58: pokeName = "Growlithe"; break;
            case 59: pokeName = "Arcanine"; break;
            case 60: pokeName = "Poliwag"; break;
            case 61: pokeName = "Poliwhirl"; break;
            case 62: pokeName = "Poliwrath"; break;
            case 63: pokeName = "Abra"; break;
            case 64: pokeName = "Kadabra"; break;
            case 65: pokeName = "Alakazam"; break;
            case 66: pokeName = "Machop"; break;
            case 67: pokeName = "Machoke"; break;
            case 68: pokeName = "Machamp"; break;
            case 69: pokeName = "Bellsprout"; break;
            case 70: pokeName = "Weepinbell"; break;
            case 71: pokeName = "Victreebel"; break;
            case 72: pokeName = "Tentacool"; break;
            case 73: pokeName = "Tentacruel"; break;
            case 74: pokeName = "Geodude"; break;
            case 75: pokeName = "Graveler"; break;
            case 76: pokeName = "Golem"; break;
            case 77: pokeName = "Ponyta"; break;
            case 78: pokeName = "Rapidash"; break;
            case 79: pokeName = "Slowpoke"; break;
            case 80: pokeName = "Slowbro"; break;
            case 81: pokeName = "Magnemite"; break;
            case 82: pokeName = "Magneton"; break;
            case 83: pokeName = "Farfetchd"; break;
            case 84: pokeName = "Doduo"; break;
            case 85: pokeName = "Dodrio"; break;
            case 86: pokeName = "Seel"; break;
            case 87: pokeName = "Dewgong"; break;
            case 88: pokeName = "Grimer"; break;
            case 89: pokeName = "Muk"; break;
            case 90: pokeName = "Shellder"; break;
            case 91: pokeName = "Cloyster"; break;
            case 92: pokeName = "Gastly"; break;
            case 93: pokeName = "Haunter"; break;
            case 94: pokeName = "Gengar"; break;
            case 95: pokeName = "Onix"; break;
            case 96: pokeName = "Drowzee"; break;
            case 97: pokeName = "Hypno"; break;
            case 98: pokeName = "Krabby"; break;
            case 99: pokeName = "Kingler"; break;
            case 100: pokeName = "Voltorb"; break;
            case 101: pokeName = "Electrode"; break;
            case 102: pokeName = "Exeggcute"; break;
            case 103: pokeName = "Exeggutor"; break;
            case 104: pokeName = "Cubone"; break;
            case 105: pokeName = "Marowak"; break;
            case 106: pokeName = "Hitmonlee"; break;
            case 107: pokeName = "Hitmonchan"; break;
            case 108: pokeName = "Lickitung"; break;
            case 109: pokeName = "Koffing"; break;
            case 110: pokeName = "Weezing"; break;
            case 111: pokeName = "Rhyhorn"; break;
            case 112: pokeName = "Rhydon"; break;
            case 113: pokeName = "Chansea"; break;
            case 114: pokeName = "Tangela"; break;
            case 115: pokeName = "Kangaskhan"; break;
            case 116: pokeName = "Horsea"; break;
            case 117: pokeName = "Seadra"; break;
            case 118: pokeName = "Goldeen"; break;
            case 119: pokeName = "Seaking"; break;
            case 120: pokeName = "Staryu"; break;
            case 121: pokeName = "Starmie"; break;
            case 122: pokeName = "Mr._Mime"; break;
            case 123: pokeName = "Scyter"; break;
            case 124: pokeName = "Jinx"; break;
            case 125: pokeName = "Electabuzz"; break;
            case 126: pokeName = "Magmar"; break;
            case 127: pokeName = "Pinsir"; break;
            case 128: pokeName = "Tauros"; break;
            case 129: pokeName = "Magikarp"; break;
            case 130: pokeName = "Gyarados"; break;
            case 131: pokeName = "Lapras"; break;
            case 132: pokeName = "Ditto"; break;
            case 133: pokeName = "Eevee"; break;
            case 134: pokeName = "Vaporeon"; break;
            case 135: pokeName = "Jolteon"; break;
            case 136: pokeName = "Flareon"; break;
            case 137: pokeName = "Porygon"; break;
            case 138: pokeName = "Omanite"; break;
            case 139: pokeName = "Omastar"; break;
            case 140: pokeName = "Kabuto"; break;
            case 141: pokeName = "Kabutops"; break;
            case 142: pokeName = "Aerodactyl"; break;
            case 143: pokeName = "Snorlax"; break;
            case 144: pokeName = "Articuno"; break;
            case 145: pokeName = "Zapdos"; break;
            case 146: pokeName = "Moltres"; break;
            case 147: pokeName = "Dratini"; break;
            case 148: pokeName = "Dragonair"; break;
            case 149: pokeName = "Dragonite"; break;
            case 150: pokeName = "Mewtwo"; break;
            case 151: pokeName = "Mew"; break;
        }

        return pokeName;
    }
}