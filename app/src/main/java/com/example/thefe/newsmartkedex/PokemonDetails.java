package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;


/**
 * Created by TheFe on 02/10/2016.
 */

public class PokemonDetails extends AppCompatActivity implements WebServicesAsyncResponse {

    private TextView tv;
    private TextToSpeech t1;
    private String toSpeech = "";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedetails);

        tv = (TextView) findViewById(R.id.descriptiontext);

        //Prendo i dati Intent
        Intent i = getIntent();

        //Seleziono l'ID del Pokémon che mi servirà per prendere tutti i dati dal database e dai drawable
        int pokeID = i.getExtras().getInt("id");
        String pokeName = getName(pokeID+1);
        ResponseFromWebService responseFromWebService = new ResponseFromWebService();

        WebServicesAsyncResponse ar = this;

        responseFromWebService.getPokeData(pokeName, ar);

        ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.tmppkmn);
        imageView.setImageResource(imageAdapter.mThumbIds[pokeID]);
        imageView.setContentDescription("Image of the current Pokémon, "+pokeName);

        imageView = (ImageView) findViewById(R.id.tipo1);
        imageView.setImageResource(R.drawable.erba);

        imageView = (ImageView) findViewById(R.id.tipo2);
        imageView.setImageResource(R.drawable.veleno);

        TextView pkmnName = (TextView)findViewById(R.id.pkmnName);
        pkmnName.setText(pokeName);

        final Switch pokeSwitch = (Switch) findViewById(R.id.dettagli);
        final Button pokeDetails = (Button) findViewById(R.id.catturato);
        pokeSwitch.setText("Catturato  ");
        pokeDetails.setText("Aggiungi\nDettagli");

        //prendendo i dati dal database, pokeDetails dev'essere enabled o disabled
        pokeDetails.setEnabled(false);

        pokeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pokeSwitch.isChecked())
                    pokeDetails.setEnabled(true);
                else
                    pokeDetails.setEnabled(false);
            }
        });

        pokeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MyPokeDetails.class);
                startActivity(i);
            }
        });

        //                    |
        //                    |
        //                    |
        //                    |
        //                    |
        //                 \  |  /
        //                  \ | /
        //                   \|/
        //                    |


        /*if (secondo_tipo != NULL) {
            imageView = (ImageView) findViewById(R.id.tipo2);
            imageView.setImageResource(R.drawable.secondo_tipo);
        }
        else {
            imageView = (ImageView) findViewById(R.id.tipo2);
            imageView.setImageResource(R.drawable.transparent);
        }*/

        getActionBar();

        Button showhide = (Button) findViewById(R.id.showhidedescr);
        final Button leggi = (Button)findViewById(R.id.leggidescrizione);
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

    @Override
    public void processFinish(String output){
        tv.setText(output);
        toSpeech = output;
    }

    private String getName (int pokeID) {
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
            case 83: pokeName = "Farfetch'd"; break;
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
