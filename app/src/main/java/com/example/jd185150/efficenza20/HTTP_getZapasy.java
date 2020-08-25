package com.example.jd185150.efficenza20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jd185150 on 3/27/2018. Ziskej vsechny zapasy v sezone
 * http://efficenza.zlutazimnice.cz/gui/mobile/returnsezonu.php?sezona=jaro 2018
 */

public class HTTP_getZapasy extends AsyncTask<String, Void, String> {
    String SUrl;
    String JSON_STRING;
    Zapasy_creator zbuilder;
    JSONZapasy_handler jAsan;
    private Activity ctx;

    public HTTP_getZapasy (String web, Activity c) {
        this.SUrl = web.toString();
        Log.d("HTTP_getZapasy", "Volam URL = " + web.toString());
        this.ctx = c;
        jAsan = new JSONZapasy_handler(c);
    }

    @Override
    protected String doInBackground(String... strings) {
        BufferedReader in = null;
        HttpURLConnection con = null;
        URL url = null;
        String result=null;
        Log.d("HTTP_getZapasy", "vysledek= " + strings);

        try {
            url = new URL(SUrl);
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
        Log.d("HTTP_getZapasy", s);
        if((s != null) && (!s.isEmpty())) {
            try {
                JSONArray JArda = new JSONArray(s);
                jAsan.setData(JArda); //zapis globalni zapasy
                int count = JArda.length();
                zbuilder = new Zapasy_creator(ctx);

                for (int p = 0; p < count; p++) {
                    ArrayList<String> ar = new ArrayList<String>();
                    JSONObject Jojako = JArda.getJSONObject(p);
                    String kolo = Jojako.getString("kolo");
                    String nazev = Jojako.getString("nazev");
                    String datum = Jojako.getString("datum");
                    String cas = Jojako.getString("cas");
                    String vysledek = Jojako.getString("vysledek");
                    String effigoly = Jojako.getString("effi_goly");
                    String soupergoly = Jojako.getString("souper_goly");
                    String hrite = Jojako.getString("hriste");
                    String id = Jojako.getString("id");
                    String adresa = Jojako.getString("adresa");
                    String odehrano = Jojako.getString("odehrano");

                    ar.add(kolo);
                    ar.add(datum);
                    ar.add(cas);
                    ar.add(nazev);
                    ar.add(vysledek);
                    ar.add(effigoly);
                    ar.add(soupergoly);
                    ar.add(hrite);
                    ar.add(id);
                    ar.add(adresa);
                    ar.add(odehrano);

                    zbuilder.AddZapas(ar);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
