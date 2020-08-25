package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
    TODO prirad uzivateli sezonu pokud neni definovana
 */

public class auth extends AsyncTask<String, String, String> {
    private String wURL;
    private String uz, he;
    String JSON_STRING;
    Activity akt;
    sharedPref shp;
    private ProgressDialog pracuju;
    private MainActivity ooS;
    public authResponse delegate = null;

    public auth(Activity a, MainActivity Ma, authResponse delegate) {
        this.ooS = Ma;
        this.akt = a;
        this.delegate = delegate;
    }

    public interface authResponse {
        void processFinish(String output);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*
        pracuju = new ProgressDialog(akt);
        pracuju.setIndeterminate(false);
        pracuju.setCancelable(false);
        pracuju.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pracuju.setMessage("Přemýšlím...");
        pracuju.show();
        */
    }

    @Override
    protected String doInBackground(String... par) {
        BufferedReader in = null;
        HttpURLConnection con = null;
        URL url = null;
        this.wURL = par[0];
        this.uz = par[1];
        this.he = par[2];
        String result=null;
        try {
            url = new URL(wURL);
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inLine = "";
            while((JSON_STRING = in.readLine()) != null) {
                inLine += JSON_STRING;
            }
            result = inLine;
            in.close();

        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage() + "");
        } finally {
            con.disconnect();
            return result;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("auth","HTTP result = " + s);
        //pracuju.dismiss();
        Boolean heureka = false;
        if(s != null) {
            try {
                JSONArray JArda = new JSONArray(s);
                int count = JArda.length();

                for (int p = 0; p < count; p++) {
                    JSONObject Jojako = JArda.getJSONObject(p);
                    String usr = Jojako.getString("login");
                    String pfd = Jojako.getString("heslo");

                    //porovnej uzivatele a heslo
                    if (uz.equals(usr) & he.equals(pfd)) {
                        heureka = true;
                        Log.d("auth", "heureka je TRUE ");
                        break;
                    } else Log.d("auth", "heureka je FALSE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (!heureka) {
                    //uzivatel nenalezen
                    Toast.makeText(akt, "Uzivatel nenalezen!", Toast.LENGTH_SHORT).show();
                    //ooS.Login();
                    delegate.processFinish("NOT OK");
                } else {
                    Log.d("auth", "uz = " + uz);
                    Log.d("auth", "he = " + he);
                    shp = new sharedPref(akt);
                    shp.zapisUsera(uz, he);
                    delegate.processFinish("OK");
                }
            }
        } else Toast.makeText(akt, "Došlo k chybě", Toast.LENGTH_SHORT).show();
    }

}
