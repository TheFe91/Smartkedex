package com.rollercoders.smartkedex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by TheFe on 08/04/2017.
 */

class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

       static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        final Dialog dialog = new Dialog(mContext);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(mContext);
        title.setText(mContext.getResources().getString(mContext.getResources().getIdentifier("rate_title", "string", mContext.getPackageName())));
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD_ITALIC);
        title.setTextSize(15);
        ll.addView(title);

        TextView tv = new TextView(mContext);
        tv.setText(mContext.getResources().getString(mContext.getResources().getIdentifier("rate_text", "string", mContext.getPackageName())));
        tv.setGravity(Gravity.CENTER);
        tv.setWidth(1000);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText(mContext.getResources().getString(mContext.getResources().getIdentifier("vota", "string", mContext.getPackageName())));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getResources().getString(mContext.getResources().getIdentifier("packagename", "string", mContext.getPackageName())))));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText(mContext.getResources().getString(mContext.getResources().getIdentifier("dopo", "string", mContext.getPackageName())));
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText(mContext.getResources().getString(mContext.getResources().getIdentifier("mai", "string", mContext.getPackageName())));
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }
}