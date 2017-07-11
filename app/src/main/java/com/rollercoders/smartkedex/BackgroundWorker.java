package com.rollercoders.smartkedex;

import android.app.ProgressDialog;
import android.content.Context;
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
    private String page, table, name, type, post_data, username, password, email, attack, ulti;
    private int flag = 0, id, appversion, iv;
    private Context context;

    BackgroundWorker (String page, String table, Context context) {this.page=page; this.table=table; this.context=context; flag=1;} //getRows, getOwner, getSmartkedex, getPokemonGO
    BackgroundWorker (String page, String name, String type, Context context) {this.page=page; this.name=name; this.type=type; this.context=context; flag=2;} //getMovesType, getAttacksStuff and setInitialData
    BackgroundWorker (String page, int id, Context context) {this.page=page; this.id=id; this.context=context; flag=3;} //getPokeTypes, getStrengths, getWeakness, getCopyName, getIdsFromPokeID , getPokeAttacks, getAppVersion and getPokeName
    BackgroundWorker (String page, int id, int iv, String attack, String ulti, String name, String username, Context context) {this.context=context; this.page=page; this.id=id; this.iv=iv; this.attack=attack; this.ulti=ulti; this.name=name; this.username=username; flag=4;}
    BackgroundWorker (String page, int id, String table, Context context) {this.page=page; this.id=id; this.table=table; this.context=context; flag=5;} //getMoves, insertCatched, getCatched, getIdsFromPokeID and all the updaters
    BackgroundWorker (String page, String email, String username, String password, int appversion, Context context) {this.page=page; this.context=context; this.username=username; this.password=password; this.email=email; this.appversion=appversion; flag=6;}

    @Override
    protected void onPreExecute () {
    }


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
                    case "erase":
                    case "getTotalCopies":
                    case "getAllCatched":
                        try {
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
                    case "getConditionedRows":
                        try {
                            post_data = URLEncoder.encode("table", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+
                                        URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(type, "UTF-8");
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
                    case "getAppVersion":
                        try {
                            post_data = URLEncoder.encode("localAppVersion", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
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

            case 4:
                switch (page) {
                    case "insertCopy":
                        try {
                            post_data = URLEncoder.encode("attack", "UTF-8")+"="+URLEncoder.encode(attack, "UTF-8")+"&"+
                                        URLEncoder.encode("ulti", "UTF-8")+"="+URLEncoder.encode(ulti, "UTF-8")+"&"+
                                        URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+
                                        URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"+
                                        URLEncoder.encode("iv", "UTF-8")+"="+URLEncoder.encode(String.valueOf(iv), "UTF-8")+"&"+
                                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;

            case 5:
                switch (page) {
                    case "getCatched":
                    case "insertCatches":
                    case "remove":
                        try {
                            post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "getIdsFromPokeID":
                        try {
                            post_data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                                        URLEncoder.encode("pokemonID", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "updatePokeAttack":
                        try {
                            post_data = URLEncoder.encode("attack", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "updatePokeUlti":
                        try {
                            post_data = URLEncoder.encode("ulti", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
                                        URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(id), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "updatePokeName":
                        try {
                            post_data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(table, "UTF-8")+"&"+
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
        ///if (progressDialog.isShowing())
            //progressDialog.dismiss();
    }
}
