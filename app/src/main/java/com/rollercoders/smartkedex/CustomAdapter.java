package com.rollercoders.smartkedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by TheFe on 05/07/2017.
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private final String[] name;
    private final int[] Imageid;

    public CustomAdapter(Context context, String[] name, int[] Imageid) {
        this.context = context;
        this.name = name;
        this.Imageid = Imageid;
    }


    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.gridview_item, null);
        }
        else {
            grid = convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
        textView.setText(name[position]);
        imageView.setImageResource(Imageid[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return grid;
    }
}
