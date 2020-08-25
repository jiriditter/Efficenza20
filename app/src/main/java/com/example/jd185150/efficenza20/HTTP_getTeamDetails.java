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
import java.util.Calendar;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

public class HTTP_getTeamDetails extends AsyncTask<String, Void, String> {
    private sharedPref shp;
    private Activity ctx;
    int rok, mesic, cast;
    boolean refresher;
    private MainActivity MA;
    //private static String MyUrl = "https://api.psmf.zlutazimnice.cz/api/v1/teams-by-name?name=efficenza%20ac&token&year=";

    public HTTP_getTeamDetails(Activity c, int me, int ro, boolean ref) {
        this.ctx = c;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        mesic = calendar.get(Calendar.MONTH) +1;
        cast = me;
        if(ro ==0) rok = calendar.get(Calendar.YEAR); else rok = ro;
        this.refresher = ref;
        this.MA = (MainActivity) c;
        Log.d("HTTP_getTeamDetails", "HTTP_getTeamDetails :: cast=" + me + ", rok=" + ro);
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        if(strings[0] != null) {
            try {
                Log.d("HTTP_getTeamDetails", "dostal jsem parametr : " + strings[0]);
                String link = strings[0];
                url = new URL(link);
                HttpsURLConnection connection = (HttpsURLConnection) new URL(link).openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String text;
                StringBuilder result = new StringBuilder();
                while ((text = in.readLine()) != null)
                    result.append(text);
                in.close();

                int responseCode = connection.getResponseCode();
                Log.d("HTTP_getTeamDetails", "response code = " + responseCode);
                return String.valueOf(result);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else Log.d("HTTP_getTeamDetails", "hele nedostavam zadnej parametr, je to nejaky divny");
        return null;
    }

    private void ZapisIFValid(String competition, String group, String league, String season, String year) {
        //checkni pro prvni sign-in
        String[] str = shp.nactiSezonu();
        if(str[0] != null) {
            Log.d("HTTP_getTeamDetails", "hledam konkretni sezonu: " + cast + " " + rok);
            Log.d("HTTP_getTeamDetails", "--je? " + season + " " + year + " == " + cast + " " + rok + "?");
            if((String.valueOf(cast).equals(season)) && (String.valueOf(rok).equals(year))) {
                Log.d("HTTP_getTeamDetails", "YES! zapisuju...");
                shp.zapisSezonu(season, year, competition, group, league);
            }
        }
        else {
            Log.d("HTTP_getTeamDetails", "zadnej zaznam v shared prefs. Mesic=" + mesic);
            if ((mesic <= 7) & (mesic > 2)) {
                Log.d("HTTP_getTeamDetails", "Mesic <=7 & >2. Season=" + season);
                if (season.equals("1")) {
                    Log.d("HTTP_getTeamDetails", "season equals 1");
                    if (String.valueOf(rok).equals(year)) {
                        Log.d("HTTP_getTeamDetails", "shp write :: season=" + season + ", year=" + year + ", competition=" + competition + ", group=" + group + ", league=" + league);
                        if (str.length > 0)
                            shp.zapisSezonu(season, year, competition, group, league);
                    }
                }
            } else {
                if (season.equals("2")) {
                    Log.d("HTTP_getTeamDetails", "season equals 2");
                    if (mesic < 2) {
                        if (String.valueOf((rok - 1)).equals(year)) {
                            Log.d("HTTP_getTeamDetails", "shp write :: season=" + season + ", year=" + year + ", competition=" + competition + ", group=" + group + ", league=" + league);
                            if (str.length > 0)
                                shp.zapisSezonu(season, year, competition, group, league);
                        } else if (String.valueOf(rok).equals(year)) {
                            Log.d("HTTP_getTeamDetails", "shp write :: season=" + season + ", year=" + year + ", competition=" + competition + ", group=" + group + ", league=" + league);
                            if (str.length > 0)
                                shp.zapisSezonu(season, year, competition, group, league);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null)  {
            Log.d("HTTP_getTeamDetails", "--got result= " + s);
            shp = new sharedPref(ctx);

            JSONObject jO = null;
            JSONArray jArr = null;
            try {
                jO = new JSONObject(s);
                jArr = jO.getJSONArray("teams");
                int dylka = jArr.length();
                for(int x=0; x<dylka; x++) {
                    JSONObject o = jArr.getJSONObject(x);
                    String competition = o.getString("competition");
                    String group = o.getString("group");
                    String league = o.getString("league");
                    String season = o.getString("season");
                    String year = o.getString("year");

                    Log.d("HTTP_getTeamDetails", "--parsing:: competition=" + competition);
                    Log.d("HTTP_getTeamDetails", "--parsing:: group=" + group);
                    Log.d("HTTP_getTeamDetails", "--parsing:: league=" + league);
                    Log.d("HTTP_getTeamDetails", "--parsing:: season=" + season);
                    Log.d("HTTP_getTeamDetails", "--parsing:: year=" + year);

                    Log.d("HTTP_getTeamDetails", "mesic=" + mesic);
                    Log.d("HTTP_getTeamDetails", "season=" + season);

                    ZapisIFValid(competition, group, league, season, year);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(refresher) MA.Update();
        } else Log.d("HTTP_getTeamDetails", "result = null");
    }
}
