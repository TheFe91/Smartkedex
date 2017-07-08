package com.rollercoders.smartkedex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;

/**
 * Created by TheFe on 05/07/2017.
 */

public class TestMain extends AppCompatActivity {
    GridView grid;
    String[] names = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard"};
    int[] imageId = {R.drawable.pkmn1, R.drawable.pkmn2, R.drawable.pkmn3, R.drawable.pkmn4, R.drawable.pkmn5, R.drawable.pkmn6};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomAdapter adapter = new CustomAdapter(this, names, imageId);
        grid = (GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PokemonDetails.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        grid.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
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
        });
    }
}
