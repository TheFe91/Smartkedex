package com.rollercoders.smartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rollercoders.smartkedex.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        //AppRater appRater = new AppRater(this);
        //appRater.setLaunchesBeforePrompt(3);
        //appRater.setPhrases("Dacci un Feedback", "Ti è piaciuta la nostra app? Dedicaci un paio di minuti e dacci un feedback sul Play Store", "FEEDBACK", "DOPO", "MAI");
        //appRater.demo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        MenuInflater mf = getMenuInflater();
        if (pokemonHelper.getLanguage().equals("ENG"))
            mf.inflate(R.menu.eng_main_menu, menu);
        else if (pokemonHelper.getLanguage().equals("ITA"))
            mf.inflate(R.menu.ita_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(this, Settings.class);
                this.startActivity(i);
                break;
            case R.id.about_us:
                i = new Intent(this, AboutUs.class);
                this.startActivity(i);
                break;
            /*case R.id.share:
                i = new Intent(this, Share.class);
                this.startActivity(i);
                break;*/
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}