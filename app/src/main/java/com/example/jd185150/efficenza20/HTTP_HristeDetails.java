package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTP_HristeDetails extends AsyncTask<String, String, String> {
    MapsActivity map;
    private Activity akt;
    String hriste, data;
    private _config cfg;

    public HTTP_HristeDetails(Activity a, String ihrisko) {
        this.akt = a;
        this.hriste = ihrisko;
        cfg = new _config();
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader bufferedReader;
        String result;
        Log.d("HTTP_HristeDetails", "Contacting URL... with hriste=" + hriste);

        try {
            data = "?ihrisko=" + hriste;
            String link = cfg.getURL_gethriste() + data;
            Log.d("HTTP_HristeDetails", link);

            url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestMethod("GET");

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        map = new MapsActivity();
        Log.d("HTTP_HristeDetails", "adresa = " + s);
        Intent y = new Intent(akt, MapsActivity.class);
        y.putExtra("adresa", s);
        akt.startActivity(y);
    }
}
