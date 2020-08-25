package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/* zjisti ID vsech tymu ve skupine a zapise jako string do sharedpref */
public class HTTP_getZapasyIstatnich extends AsyncTask<String, Void, String> {
    Activity akt;

    public HTTP_getZapasyIstatnich(Activity akt) {
        this.akt = akt;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        BufferedReader bufferedReader;
        //String result;
        //Log.d("HTTP_getZapasyOstatnich", "Contacting URL = " + strings[0]);

        try {
            String link = strings[0];
            Log.d("HTTP_getZapasyOstatnich", link);

            url = new URL(link);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(strings[0]).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String text;
            StringBuilder result = new StringBuilder();
            while ((text = in.readLine()) != null)
                result.append(text);
            in.close();

            int responseCode = connection.getResponseCode();
            //Log.d("HTTP_getZapasyOstatnich", "response code = " + responseCode);
            return String.valueOf(result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        JSONObject tymy = new JSONObject();
        //Log.d("HTTP_getZapasyOstatnich", "vysledek = " +s);
        if((s != null) && (!s.isEmpty())) {
            //Log.d("HTTP_getZapasyOstatnich", "dostal jsem se pres NULL");
            try {
                JSONObject jO = new JSONObject(s);
                JSONArray jAsan = jO.getJSONArray("games");
                //Log.d("HTTP_getZapasyOstatnich", "welikost pole: " + jAsan.length());
                for (int i = 0; i < jAsan.length(); i++) {
                    JSONObject c = jAsan.getJSONObject(i);
                    int souperid =0;
                    String souper = c.getString("teamHome");
                    if(!souper.equalsIgnoreCase("Efficenza AC")) {
                        souperid = c.getInt("teamHomeId");
                    }
                    else {
                        souperid = c.getInt("teamAwayId");
                        souper = c.getString("teamAway");
                    }
                    //Log.d("HTTP_getZapasyOstatnich", "souper: " + souper + " | id: " + souperid);
                    try {
                        tymy.put(souper, souperid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Log.d("HTTP_getZapasyOstatnich", "moje sestawa: " + tymy);
                sharedPref shp = new sharedPref(akt);
                shp.UlozTymy(tymy.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
