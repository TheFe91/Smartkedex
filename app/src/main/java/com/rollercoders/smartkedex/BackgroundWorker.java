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
    private String page, table, name, type, post_data, username, password, email;
    private int flag = 0, id, appversion;

    public BackgroundWorker (String page, String table) {this.page=page; this.table=table; flag=1;} //getRows, getOwner, getSmartkedex, getPokemonGO and getAppVersion
    public BackgroundWorker (String page, String name, String type) {this.page=page; this.name=name; this.type=type; flag=2;} //getMovesType, getAttacksStuff and setInitialData
    public BackgroundWorker (String page, int id) {this.page=page; this.id=id; flag=3;} //getPokeTypes, getStrengths, getWeakness, getCopyName, getIdsFromPokeID and getPokeAttacks
    //public BackgroundWorker (String page, String username, boolean settings) {this.page=page; this.username=username; flag=4;} //
    public BackgroundWorker (String page, int id, String table) {this.page=page; this.id=id; this.table=table; flag=5;} //getMoves and getCatched
    public BackgroundWorker (String page, String email, String username, String password, int appversion) {this.page=page; this.username=username; this.password=password; this.email=email; this.appversion=appversion; flag=6;}


    @Override
    protected String doInBackground(Void... aVoid) {
        switch (flag) {
            case 1:
                switch (page) {
                    case "getRows":
                        try {
                            post_data = URLEncoder.encode("table", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8"); //il primo è il nome della casella POST, il secondo è il suo valore
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "getOwner":
                    case "getSmartkedex":
                    case "getPokemonGO":
                    case "getAppVersion":
                        try {
                            System.err.println(page + " " + table);
                            post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }
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
                    case "setInitialData":
                    case "login":
                        try {
                            post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+
                                        URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                break;
            case 3:
                switch (page) {
                    case "getIdsFromPokeID":
                        try {
                            post_data = URLEncoder.encode("pokemonID", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "getPokeAttacks":
                        try {
                            post_data = URLEncoder.encode("pokeCopy", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        try {
                            post_data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            case 4: break;

            case 5:
                switch (page) {
                    case "getCatched":
                        try {
                            post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                                    URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        try {
                            post_data = URLEncoder.encode("table", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                                    URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case 6:
                try {
                    post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"+
                                URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"+
                                URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+
                                URLEncoder.encode("appversion", "UTF-8")+"="+URLEncoder.encode(String.valueOf(appversion), "UTF-8");
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
