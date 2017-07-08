package com.rollercoders.smartkedex;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by TheFe on 04/07/2017.
 */

public class AsyncClass extends AsyncTask <Void, String, Void> {

    private Context context;
    ProgressDialog dialog;

    public AsyncClass(Context ctx) {
        context = ctx;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Please wait");
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //SystemClock.sleep(5000);
        return (null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //dialog.dismiss();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
