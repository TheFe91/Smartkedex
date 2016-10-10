package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by TheFe on 27/09/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    public Integer[] mThumbIds = {
            R.drawable.pkmn1, R.drawable.pkmn2,
            R.drawable.pkmn3, R.drawable.pkmn4,
            R.drawable.pkmn5, R.drawable.pkmn6,
            R.drawable.pkmn7, R.drawable.pkmn8,
            R.drawable.pkmn9, R.drawable.pkmn10,
            R.drawable.pkmn11, R.drawable.pkmn12,
            R.drawable.pkmn13, R.drawable.pkmn14,
            R.drawable.pkmn15, R.drawable.pkmn16,
            R.drawable.pkmn17, R.drawable.pkmn18,
            R.drawable.pkmn19, R.drawable.pkmn20,
            R.drawable.pkmn21, R.drawable.pkmn22,
            R.drawable.pkmn23, R.drawable.pkmn24,
            R.drawable.pkmn25, R.drawable.pkmn26,
            R.drawable.pkmn27, R.drawable.pkmn28,
            R.drawable.pkmn29, R.drawable.pkmn30,
            R.drawable.pkmn31, R.drawable.pkmn32,
            R.drawable.pkmn33, R.drawable.pkmn34,
            R.drawable.pkmn35, R.drawable.pkmn36,
            R.drawable.pkmn37, R.drawable.pkmn38,
            R.drawable.pkmn39, R.drawable.pkmn40,
            R.drawable.pkmn41, R.drawable.pkmn42,
            R.drawable.pkmn43, R.drawable.pkmn44,
            R.drawable.pkmn45, R.drawable.pkmn46,
            R.drawable.pkmn47, R.drawable.pkmn48,
            R.drawable.pkmn49, R.drawable.pkmn50,
            R.drawable.pkmn51, R.drawable.pkmn52,
            R.drawable.pkmn53, R.drawable.pkmn54,
            R.drawable.pkmn55, R.drawable.pkmn56,
            R.drawable.pkmn57, R.drawable.pkmn58,
            R.drawable.pkmn59, R.drawable.pkmn60,
            R.drawable.pkmn61, R.drawable.pkmn62,
            R.drawable.pkmn63, R.drawable.pkmn64,
            R.drawable.pkmn65, R.drawable.pkmn66,
            R.drawable.pkmn67, R.drawable.pkmn68,
            R.drawable.pkmn69, R.drawable.pkmn70,
            R.drawable.pkmn71, R.drawable.pkmn72,
            R.drawable.pkmn73, R.drawable.pkmn74,
            R.drawable.pkmn75, R.drawable.pkmn76,
            R.drawable.pkmn77, R.drawable.pkmn78,
            R.drawable.pkmn79, R.drawable.pkmn80,
            R.drawable.pkmn81, R.drawable.pkmn82,
            R.drawable.pkmn83, R.drawable.pkmn84,
            R.drawable.pkmn85, R.drawable.pkmn86,
            R.drawable.pkmn87, R.drawable.pkmn88,
            R.drawable.pkmn89, R.drawable.pkmn90,
            R.drawable.pkmn91, R.drawable.pkmn92,
            R.drawable.pkmn93, R.drawable.pkmn94,
            R.drawable.pkmn95, R.drawable.pkmn96,
            R.drawable.pkmn97, R.drawable.pkmn98,
            R.drawable.pkmn99, R.drawable.pkmn100,
            R.drawable.pkmn101, R.drawable.pkmn102,
            R.drawable.pkmn103, R.drawable.pkmn104,
            R.drawable.pkmn105, R.drawable.pkmn106,
            R.drawable.pkmn107, R.drawable.pkmn108,
            R.drawable.pkmn109, R.drawable.pkmn110,
            R.drawable.pkmn111, R.drawable.pkmn112,
            R.drawable.pkmn113, R.drawable.pkmn114,
            R.drawable.pkmn115, R.drawable.pkmn116,
            R.drawable.pkmn117, R.drawable.pkmn118,
            R.drawable.pkmn119, R.drawable.pkmn120,
            R.drawable.pkmn121, R.drawable.pkmn122,
            R.drawable.pkmn123, R.drawable.pkmn124,
            R.drawable.pkmn125, R.drawable.pkmn126,
            R.drawable.pkmn127, R.drawable.pkmn128,
            R.drawable.pkmn129, R.drawable.pkmn130,
            R.drawable.pkmn131, R.drawable.pkmn132,
            R.drawable.pkmn133, R.drawable.pkmn134,
            R.drawable.pkmn135, R.drawable.pkmn136,
            R.drawable.pkmn137, R.drawable.pkmn138,
            R.drawable.pkmn139, R.drawable.pkmn140,
            R.drawable.pkmn141, R.drawable.pkmn142,
            R.drawable.pkmn143, R.drawable.pkmn144,
            R.drawable.pkmn145, R.drawable.pkmn146,
            R.drawable.pkmn147, R.drawable.pkmn148,
            R.drawable.pkmn149, R.drawable.pkmn150,
            R.drawable.pkmn151
    };
}