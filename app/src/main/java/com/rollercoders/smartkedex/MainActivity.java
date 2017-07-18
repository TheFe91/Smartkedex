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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    private PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);
    GridView grid;
    String[] names = new String[151], allCatchedNames;
    int[] imageId = new int[151], ids;
    float[] alphas = new float[151];
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-9807330777593753~2280250826");

        /*InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9807330777593753/2216958029");
        adRequest = new AdRequest.Builder()
                .addTestDevice("01658E55E3FE332C522AEF747FD402FF")
                .build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.show();*/

        if (!pokemonDatabaseAdapter.getNumberOfTables()) {
            pokemonDatabaseAdapter.doBackupAndUpdateDB(this);
        }

        AdView mAdView = (AdView) findViewById(R.id.AdView);
        adRequest = new AdRequest.Builder()
                .addTestDevice("01658E55E3FE332C522AEF747FD402FF")
                .build();
        mAdView.loadAd(adRequest);

        ids = pokemonDatabaseAdapter.getAllCatched(this);
        allCatchedNames = pokemonDatabaseAdapter.getAllCatchedNames(this);

        for (int k = 0; k < 151; k++) {
            if (k+1 < 10) {
                names[k] = "#00"+String.valueOf(k+1)+" - ???";
            }
            else if (k+1 > 9 && k+1 < 100) {
                names[k] = "#0"+String.valueOf(k+1)+" - ???";
            }
            else {
                names[k] = "#"+String.valueOf(k+1)+" - ???";
            }

        }

        for (int k = 0, j=0; k < 151; k++) {
            imageId[k] = k + 1;
            if (contains(ids, k+1)) {
                alphas[k] = (float)1.0;
                if (k+1 < 10) {
                    names[k] = "#00"+String.valueOf(k+1)+" - "+allCatchedNames[j];
                    j++;
                }
                else if (k+1 > 9 && k+1 < 100) {
                    names[k] = "#0"+String.valueOf(k+1)+" - "+allCatchedNames[j];
                    j++;
                }
                else {
                    names[k] = "#"+String.valueOf(k+1)+" - "+allCatchedNames[j];
                    j++;
                }
            }
            else
                alphas[k] = (float)0.1;
        }

        for (int k=0; k<151; k++) {
            String id = "pkmn"+imageId[k];
            imageId[k] = getResources().getIdentifier(id, "drawable", getPackageName());
        }

        CustomAdapter adapter = new CustomAdapter(this, names, imageId, alphas);
        grid = (GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        //grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                i.putExtra("id", position);
                startActivity(i);
                finish();
            }
        });

        /*grid.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                int selectCount = grid.getCheckedItemCount();
                switch (selectCount) {
                    case 1:
                        mode.setSubtitle("One item selected");
                        break;
                    default:
                        mode.setSubtitle("" + selectCount + " items selected");
                        break;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Select Items");
                mode.setSubtitle("One item selected");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });*/

        AppRater.app_launched(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = getMenuInflater();
        mf.inflate(R.menu.main_menu, menu);
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

    public boolean contains(final int[] array, final int key) {
        for (int n : array) {
            if (key == n)
                return true;
        }
        return false;
    }
}