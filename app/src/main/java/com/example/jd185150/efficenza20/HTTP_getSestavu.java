package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/* Long Click na konkretnim zapase */
public class HTTP_getSestavu extends AsyncTask<String, String, String> {
    private Activity akt;
    String data;
    String zapasid;
    private AlertDialog myDiag;
    private ProgressDialog pracuju;
    private ArrayList<String> params;
    private _config cfg;

    public HTTP_getSestavu(Activity a, ArrayList<String> aL) {
        this.akt = a;
        this.params = aL;
        this.zapasid = aL.get(8);
        cfg = new _config();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final AlertDialog.Builder aDiag = new AlertDialog.Builder(akt);
        LayoutInflater layInf = akt.getLayoutInflater();
        final View v = layInf.inflate(R.layout.longpressoptions, null);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(50,5,10,50);
        aDiag.setTitle("DETAILY ZÁPASU");
        aDiag.setView(v);
        aDiag.setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDiag.dismiss();
            }
        });
        myDiag = aDiag.create();
        myDiag.show();

        TextView tNazevSoupere = (TextView) myDiag.findViewById(R.id.txtEffivs);
        tNazevSoupere.setText("AC Efficenza vs. " + params.get(3));
        Button vzajemnezapasy = (Button) myDiag.findViewById(R.id.btnVzajemneZapasy);
        vzajemnezapasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HTTP_vzajemnaBilance HVB = new HTTP_vzajemnaBilance(akt, params.get(3));
                myDiag.dismiss();
                HVB.execute();
            }
        });

        Button lokalizujhriste = (Button) myDiag.findViewById(R.id.btnLokalizovatHriste);
        lokalizujhriste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDiag.dismiss();
                HTTP_HristeDetails HHD = new HTTP_HristeDetails(akt, params.get(7));
                HHD.execute();
            }
        });

        Button zapasysoupere = (Button) myDiag.findViewById(R.id.zapasysoupere);
        zapasysoupere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref shp = new sharedPref(akt);
                String rok[] = shp.nactiSezonu();
                String cast = "jaro";
                if(rok[0].equalsIgnoreCase("jaro")) cast = "1"; else cast = "2";
                String url = cfg.getApiHosting_teamdetails() + rok[1] + "&competition=1&season="+cast+"&id=1083";
                Log.d("HTTP_getSestavu","Ukaz Zapasy Soupere [" + params.get(3) + "], url = " + url);
            }
        });

        pracuju = new ProgressDialog(akt);
        pracuju.setIndeterminate(false);
        pracuju.setCancelable(false);
        pracuju.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pracuju.setMessage("Přemýšlím...");
        pracuju.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader bufferedReader;
        String result;
        Log.d("HTTP_getSestavu", "Contacting URL... with zapasid=" + zapasid);

        try {
            data = "?zapasid=" + zapasid;
            String link = cfg.getURL_getSestavu() + data;
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
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        pracuju.dismiss();
        JSONArray jsonArray = null;
        ArrayList<String> ar = new ArrayList<>();
        TextView tvPristiSestava = (TextView)myDiag.findViewById(R.id.txtPredpokladanaSestava);
        Button bBudu = (Button)myDiag.findViewById(R.id.btnBudu);
        Button bNebudu = (Button)myDiag.findViewById(R.id.btnNebudu);
        Button zapasysoupere = (Button)myDiag.findViewById(R.id.zapasysoupere);
        sharedPref shp = new sharedPref(akt);
        String[] x = new String[1];
        x = shp.nacti();
        final String uzivatel = x[0];

        try {
            JSONArray jArda = new JSONArray(aVoid);
            int length = jArda.length();

            for (int i = 0; i < length; i++) {
                JSONObject obj = jArda.getJSONObject(i);
                String kdo = obj.getString("prijde");
                tvPristiSestava.setText(Html.fromHtml("<b>Sestava:</b> " + kdo));
                Log.d("HTTP_getSestavu", "kdoze to ma prijit? " + kdo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bBudu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDiag.dismiss();
                HTTP_BuduNebudu HBN = new HTTP_BuduNebudu(akt, 1, params.get(0), uzivatel);
                HBN.execute();
            }
        });

        bNebudu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDiag.dismiss();
                HTTP_BuduNebudu HBN = new HTTP_BuduNebudu(akt, 2, params.get(0), uzivatel);
                HBN.execute();
            }
        });

        zapasysoupere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDiag.dismiss();
                Intent zInt = new Intent(akt, ukazZapasySoupere.class);
                //Log.d("ukazZapasySoupere", "posilam intent s parametrem: " + params.get(3));
                zInt.putExtra("param", params.get(3));
                akt.startActivity(zInt);
            }
        });
    }
}
