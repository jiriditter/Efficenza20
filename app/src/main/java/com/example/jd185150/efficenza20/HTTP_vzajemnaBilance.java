package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HTTP_vzajemnaBilance extends AsyncTask<String, String, String> {
    private ProgressDialog pracuju;
    private Activity akt;
    private AlertDialog myDiag;
    String Tym, data;
    private _config cfg;

    public HTTP_vzajemnaBilance(Activity a, String nazev) {
        this.akt = a;
        Tym = nazev;
        cfg = new _config();
        Log.d("VzajemnaBilance", "dostal jsem tutenc tym: " + Tym);
    }

    @Override
    protected String doInBackground(String... strings) {
        String souperid = Tym.replaceAll(" ", "%20");;
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader bufferedReader;
        String result;
        Log.d("HTTP_vzajemnaBilance", "Contacting URL... with souperid=" + souperid);

        try {
            data = "?souper=" + souperid;
            String link = cfg.getURL_vzajemnyzapas() + data;
            Log.d("HTTP_getZapasDetail", link);

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

    protected void onPreExecute() {
        super.onPreExecute();
        pracuju = new ProgressDialog(akt);
        pracuju.setIndeterminate(false);
        pracuju.setCancelable(false);
        pracuju.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pracuju.setMessage("Přemýšlím...");
        pracuju.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pracuju.dismiss();
        JSONArray jsonArray = null;
        String zapasu="", vyher="", remiz="", proher="", skore="";
        ArrayList<String> ar = new ArrayList<>();

        if(s != null) {
            try {
                jsonArray = new JSONArray(s);
                int length = jsonArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //fetch all records for current open season
                    zapasu = obj.getString("zapasu");
                    vyher = obj.getString("vyher");
                    remiz = obj.getString("remiz");
                    proher = obj.getString("proher");
                    skore = obj.getString("skore");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ar.add(zapasu);
            ar.add(vyher);
            ar.add(remiz);
            ar.add(proher);
            ar.add(skore);
            invokeDialog(ar);
        }
    }

    private void invokeDialog(ArrayList<String> aL) {
        final AlertDialog.Builder aDiag = new AlertDialog.Builder(akt);
        LayoutInflater layInf = akt.getLayoutInflater();
        final View v = layInf.inflate(R.layout.vzajemnabilance, null);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(50,5,10,50);
        aDiag.setTitle("BILANCE ZÁPASů");
        aDiag.setView(v);
        aDiag.setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDiag.dismiss();
            }
        });
        myDiag = aDiag.create();
        myDiag.show();

        TextView tNazevSoupere = (TextView) myDiag.findViewById(R.id.txtNazevSoupere);
        tNazevSoupere.setText(Tym);
        TextView tCelkemZapasu = (TextView) myDiag.findViewById(R.id.txtCOZ);
        tCelkemZapasu.setText(aL.get(0));
        TextView tVyher = (TextView) myDiag.findViewById(R.id.txtVitezstvi);
        tVyher.setText(aL.get(1));
        TextView tRemiz = (TextView) myDiag.findViewById(R.id.txtRemiz);
        tRemiz.setText(aL.get(2));
        TextView tProher = (TextView) myDiag.findViewById(R.id.txtProher);
        tProher.setText(aL.get(3));
        TextView tSkore = (TextView) myDiag.findViewById(R.id.txtGolBilance);
        tSkore.setText(aL.get(4));
    }
}
