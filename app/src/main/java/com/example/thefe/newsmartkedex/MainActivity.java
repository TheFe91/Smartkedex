package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextToSpeech t2;

    //private static final String FILENAME = "descriptions.txt";

    PokemonDatabaseAdapter pokemonDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);

        String data = pokemonDatabaseAdapter.getAllData();
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();

//        long id = pokemonDatabaseAdapter.insertPokemon(1, "Bulbasaur", "È possibile vedere Bulbasàur mentre schiaccia un pisolino sotto il sole. Ha un seme piantato sulla schiena. Grazie ai raggi solari, il seme crescie, ingrandendosi progressivamente.");
//        if (id<0) {
//            System.err.println("Something went wrong");
//        }
//        else {
//            Toast.makeText(this, "Bulbasaur inserito", Toast.LENGTH_SHORT).show();
//        }
//
//        id = pokemonDatabaseAdapter.insertPokemon(2, "Ivysaur", "C'è un germoglio piantato nella schiena di Ivysàur. Per sopportarne il peso, le zampe e il corpo crescono robusti. Quando inizia a passare più tempo esposto al sole, signìfica che il germoglio sboccerà présto in un grande fiore.");
//        if (id<0) {
//            System.err.println("Something went wrong");
//        }
//        else {
//            Toast.makeText(this, "Ivysaur inserito", Toast.LENGTH_SHORT).show();
//        }
//        id = pokemonDatabaseAdapter.insertPokemon(3, "Venusaur", "C'è un grande fiore sulla schiena di Venusàur. Si dice che i colori diventino più vìvidi con il giusto nutrimento e i raggi solari. Il suo profumo calma le reazioni emotive delle persone.");
//        if (id<0) {
//            System.err.println("Something went wrong");
//        }
//        else {
//            Toast.makeText(this, "Venusaur inserito", Toast.LENGTH_SHORT).show();
//        }
//
//        String[] tipi = {"acciaio", "acqua", "coleottero", "drago", "elettro", "erba", "fuoco", "ghiaccio", "lotta", "normale"};
//
//        for (int i = 0; i < 10; i++) {
//            id = pokemonDatabaseAdapter.insertTipo(tipi[i]);
//            if (id<0) {
//                System.err.println("Something went wrong at line " + i + "(" + tipi[i] + ")");
//            }
//            else {
//                Toast.makeText(this, tipi[i] + " inserito", Toast.LENGTH_SHORT).show();
//            }
//        }

        //final ReadFileFacade readFileFacade = new ReadFileFacade(getApplicationContext(), FILENAME); //creo un oggetto di classe ReadFileFacade
        final Presentation presentation = new Presentation((Button)findViewById(R.id.button1), (Button)findViewById(R.id.button2), getApplicationContext()); //creo un oggetto di classe Presentation
        presentation.presentati();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                position += 1; //così il numero di Pokémon corrisponde al numero di Pokédex
                //final String[] tmp = readFileFacade.getDescriptionFromId(position+"");
                Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                i.putExtra("id", position);
                startActivity(i);
//                t2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                    public void onInit(int status) {
//                        if(status != TextToSpeech.ERROR) {
//                            t2.setLanguage(Locale.ITALIAN);
//                            t2.speak(tmp[1], TextToSpeech.QUEUE_FLUSH, null);
//                        }
//                    }
//                });
            }
        });

    };

}