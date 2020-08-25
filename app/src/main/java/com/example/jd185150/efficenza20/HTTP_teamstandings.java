package com.example.jd185150.efficenza20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class HTTP_teamstandings extends AsyncTask<String, Void, String> {
    private Activity akt;
    LinearLayout laya;

    public HTTP_teamstandings(Activity a) {
        this.akt = a;
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
            Log.d("HTTP_getZapasyOstatnich", "response code = " + responseCode);
            return String.valueOf(result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableParams.setMargins(3, 3, 3, 5);
        TableLayout tejbl = new TableLayout(akt);
        tejbl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        laya = ((Activity) akt).findViewById(R.id.platno);
        //laya.addView(tejbl);

        TextView tV = ((Activity) akt).findViewById(R.id.jmenotymu);
        tV.setVisibility(View.GONE);
        //SetHeader();

        Log.d("HTTP_teamStandings", "result = " + s);
        JSONObject tymy = new JSONObject();
        if (s != null) {
            if (!s.isEmpty()) {
                Log.d("HTTP_getZapasyOstatnich", "dostal jsem se pres NULL");
                try {
                    JSONObject jO = new JSONObject(s);
                    JSONObject jTable = jO.getJSONObject("table");
                    JSONArray jContent = jTable.getJSONArray("content");
                    Resources resource = akt.getResources();
                    /* SET HEADER */
                    TableRow row = new TableRow(akt);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    row.setBackgroundColor(Color.rgb(245, 245, 240));
                    lp.setMargins(5, 5, 5, 5);
                    row.setLayoutParams(lp);
                    row.setPadding(0, 10, 0, 10);
                    TextView tPozice = new TextView(akt);
                    tPozice.setLayoutParams(rowParams);
                    TextView tTym = new TextView(akt);
                    tTym.setLayoutParams(rowParams);
                    TextView tVRP = new TextView(akt);
                    tVRP.setLayoutParams(rowParams);
                    TextView tGF = new TextView(akt);
                    tGF.setLayoutParams(rowParams);
                    TextView tGA = new TextView(akt);
                    tGA.setLayoutParams(rowParams);
                    TextView tOdehrano = new TextView(akt);
                    tOdehrano.setLayoutParams(rowParams);
                    TextView tPTS = new TextView(akt);
                    tPTS.setLayoutParams(rowParams);
                    tPozice.setText("#");
                    tTym.setText(" | Tým");
                    tVRP.setText("| V/R/P");
                    tGF.setText("| GF");
                    tGA.setText("| GA");
                    tOdehrano.setText("| Odeh.");
                    tPTS.setText("| Bodů");
                    row.addView(tPozice);
                    row.addView(tTym);
                    row.addView(tVRP);
                    row.addView(tGF);
                    row.addView(tGA);
                    row.addView(tOdehrano);
                    row.addView(tPTS);
                    tejbl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    /* END HEADER */
                    for (int u = 0; u < jContent.length(); u++) {
                        JSONObject o = jContent.getJSONObject(u);
                        String pozice = o.getString("position");
                        String tym = o.getString("teamName");
                        String vitezstvi = o.getString("wins");
                        String remiz = o.getString("draws");
                        String proher = o.getString("loses");
                        String gF = o.getString("goalsFor");
                        String gA = o.getString("goalsAgainst");
                        String odehrano = o.getString("numOfGames");
                        String ptz = o.getString("points");

                        row = new TableRow(akt);
                        lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        if (mod(u, 2) == 0) row.setBackgroundColor(Color.rgb(245, 245, 240));
                        else row.setBackgroundColor(Color.rgb(255, 255, 255));
                        lp.setMargins(5, 5, 5, 5);
                        row.setLayoutParams(lp);
                        row.setPadding(0, 10, 0, 10);

                        //TextView tPozice = ((Activity) akt).findViewById(R.id.Pozice);
                        tPozice = new TextView(akt);
                        //tPozice.setBackgroundResource(Color.parseColor("#808080"));
                        tPozice.setLayoutParams(rowParams);
                        //TextView tTym = ((Activity) akt).findViewById(R.id.Tym);
                        tTym = new TextView(akt);
                        tTym.setLayoutParams(rowParams);
                        //TextView tVRP = ((Activity) akt).findViewById(R.id.VRP);
                        tVRP = new TextView(akt);
                        tVRP.setLayoutParams(rowParams);
                        //TextView tGF = ((Activity) akt).findViewById(R.id.GF);
                        tGF = new TextView(akt);
                        tGF.setLayoutParams(rowParams);
                        //TextView tGA = ((Activity) akt).findViewById(R.id.GA);
                        tGA = new TextView(akt);
                        tGA.setLayoutParams(rowParams);
                        //TextView tOdehrano = ((Activity) akt).findViewById(R.id.Odeh);
                        tOdehrano = new TextView(akt);
                        tOdehrano.setLayoutParams(rowParams);
                        //TextView tPTS = ((Activity) akt).findViewById(R.id.PTS);
                        tPTS = new TextView(akt);
                        tPTS.setLayoutParams(rowParams);

                        if ((vitezstvi.equals("")) || (vitezstvi.isEmpty())) vitezstvi = "0";
                        if ((proher.equals("")) || (proher.isEmpty())) proher = "0";
                        if ((remiz.equals("")) || (remiz.isEmpty())) remiz = "0";

                        tPozice.setText(pozice);
                        tTym.setText(" | " + tym);
                        tVRP.setText(" | " + vitezstvi + "/" + remiz + "/" + proher);
                        tGF.setText(" | " + gF);
                        tGA.setText(" | " + gA);
                        tOdehrano.setText(" | " + odehrano);
                        tPTS.setText(" | " + ptz);
                        Log.d("HTTP_teamStandings", "Pozice #" + pozice + ", Tym=" + tym + ", VRP=" + vitezstvi + "/" + remiz + "/" + proher);

                        row.addView(tPozice);
                        row.addView(tTym);
                        row.addView(tVRP);
                        row.addView(tGF);
                        row.addView(tGA);
                        row.addView(tOdehrano);
                        row.addView(tPTS);
                        tejbl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                laya.addView(tejbl);
            }
        }
    }
}
