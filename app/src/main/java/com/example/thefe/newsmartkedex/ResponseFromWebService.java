package com.example.thefe.newsmartkedex;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TheFe on 16/10/2016.
 */

public class ResponseFromWebService {

    public void getPokeData (String pokeName, WebServicesAsyncResponse ar) {
        new JSONTask(ar).execute("https://pkmn-web-services.herokuapp.com/desc?pkmn=" + pokeName);
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        public JSONTask(WebServicesAsyncResponse delegate) {
            this.delegate = delegate;
        }

        WebServicesAsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject finalObject = new JSONObject(finalJson);

                String pkmnName = finalObject.getString("name");
                String pkmnDesc = finalObject.getString("desc");

                /*JSONArray parentArray = parentObject.getJSONArray("movies");

                JSONObject finalObject = parentArray.getJSONObject(0);

                String movieName = finalObject.getString("movie");
                int movieYear = finalObject.getInt("year");*/

                return pkmnDesc;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) { //questa String s è la return di doInBackground
            super.onPostExecute(s);
            delegate.processFinish(s);
        }
    }
}