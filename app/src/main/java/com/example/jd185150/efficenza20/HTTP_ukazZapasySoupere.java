package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTP_ukazZapasySoupere extends AsyncTask<String, Void, String> {
    private Activity ctx;
    LinearLayout laya;
    String tymname;

    public HTTP_ukazZapasySoupere(Activity a, String tym) {
        this.ctx = a;
        this.laya = ((Activity) ctx).findViewById(R.id.platno);
        this.tymname = tym;
        laya.removeAllViews();
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;

        try {
            String link = strings[0];
            //Log.d("HTTP_getZapasyOstatnich", link);

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
            Log.d("HTTP_getZapasyOstatnich", "response code = " + responseCode);
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
        //Log.d("UkazZapasySoupere", "result = " +s);
        TextView t = (TextView) ctx.findViewById(R.id.jmenotymu);
        t.setText(tymname);
        if(s != null) {
            try {
                JSONObject jO = new JSONObject(s);
                JSONArray jArr = jO.getJSONArray("games");
                int dylka = jArr.length();
                int koleso =1;

                while (koleso <12) {
                    for(int x=0; x<dylka; x++) {
                        JSONObject o = jArr.getJSONObject(x);
                        String kolo = o.getString("order");
                        if(String.valueOf(koleso).equals((kolo))) {
                            String teamHome = o.getString("teamHome");
                            String teamAway = o.getString("teamAway");
                            String datum = o.getString("date");
                            String g1 = "";
                            String g2 = "";
                            if(o.isNull("goalsHome")) g1 = "x"; else g1 = o.getString("goalsHome");
                            if(o.isNull("goalsAway")) g2 = "x"; else g2 = o.getString("goalsAway");
                            //String kolo = o.getString("order");

                            View hlavniUdaje= View.inflate(ctx,R.layout.zapas,null);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(5, 10,5, 10);
                            hlavniUdaje.setLayoutParams(layoutParams);
                            hlavniUdaje.setBackgroundResource(R.drawable.zapas_neodehrano);
                            laya.setPadding(0,10,0,10);
                            laya.addView(hlavniUdaje);

                            TextView Zcas = hlavniUdaje.findViewById(R.id.ZAPCas);
                            TextView ZDatum = hlavniUdaje.findViewById(R.id.ZAPdatum);
                            TextView Zapsoup = hlavniUdaje.findViewById(R.id.ZAPSouper);
                            TextView Zaphrist = hlavniUdaje.findViewById(R.id.ZAPHriste);
                            Zaphrist.setPadding(50,0,0,0);
                            Zapsoup.setPadding(50,0,0,10);
                            Zcas.setPadding(0,0,0,10);

                            StringBuilder sbuild = new StringBuilder(99);
                            sbuild.append(teamHome + " [ " + g1 + " : " + g2 + " ] " + teamAway);
                            String dat[] = datum.split("-");
                            String dat2[] = dat[2].split("T");

                            Zapsoup.setText("");
                            Zaphrist.setText("");
                            ZDatum.setText(sbuild.toString());
                            Zcas.setText(kolo + ". kolo, " + dat2[0]+"."+dat[1]+".");
                        }
                    }
                    koleso++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
