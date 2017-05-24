package com.rollercoders.smartkedex;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by TheFe on 20/05/17.
 */

class BackgroundWorker extends AsyncTask<Void, Void, String> {

    WebServicesAsyncResponse delegate = null;
    private String page, table, name, type, post_data, username;
    private int flag = 0, id;

    public BackgroundWorker (String page, String table) {this.page=page; this.table=table; flag=1;} //getRows
    public BackgroundWorker (String page, String name, String type) {this.page=page; this.name=name; this.type=type; flag=2;} //getMovesType and getAttacksStuff
    public BackgroundWorker (String page, int id) {this.page=page; this.id=id; flag=3;} //getPokeTypes, getStrengths and getWeakness
    public BackgroundWorker (String page, String username, boolean settings) {this.page=page; this.username=username; flag=4;} //getOwner, getSmartkedex and getPokemonGO
    public BackgroundWorker (String page, int id, String table) {this.page=page; this.id=id; this.table=table; flag=5;}


    @Override
    protected String doInBackground(Void... aVoid) {
        switch (flag) {
            case 1:
                try {
                    post_data = URLEncoder.encode("table", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8"); //il primo è il nome della casella POST, il secondo è il suo valore
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                switch (page) {
                    case "getMovesType":
                        try {
                            post_data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+
                                        URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "getAttacksStuff":
                        try {
                            post_data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+
                                        URLEncoder.encode("table", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                break;
            case 3:
                try {
                    post_data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    post_data = URLEncoder.encode("table", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                            URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
        String link = "http://smartkedexwebservices.altervista.org/"+page+".php";
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line+"\n";
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
