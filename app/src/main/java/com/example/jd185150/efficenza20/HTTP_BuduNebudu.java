package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTP_BuduNebudu extends AsyncTask<String, String, String> {
    private Activity akt;
    int prepinac;
    String zapasid, uziv;
    byte[] outputBytes;
    private _config cfg;

    public HTTP_BuduNebudu(Activity a, int prep, String z, String uz) {
        this.akt = a;
        this.prepinac = prep;
        this.zapasid = z;
        this.uziv = uz;
        cfg = new _config();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        if(!uziv.isEmpty()) {
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader bufferedReader;
            String result;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("uziv", uziv)
                    .appendQueryParameter("zapasid", zapasid)
                    .appendQueryParameter("budunebudu", String.valueOf(prepinac));

            String query = builder.build().getEncodedQuery();

            try {
                String link = cfg.getURL_budunebudu();
                Log.d("HTTP_BuduNebudu", link);

                url = new URL(link);
                con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestMethod("POST");

                Log.d("HTTP_BuduNebudu", "zapisuju POST data = " + builder.toString());
                outputBytes = query.getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write(outputBytes);
                os.close();
                int statusCode = con.getResponseCode();
                Log.d("HTTP_BuduNebudu", "dostal jsem response = " + statusCode);

                /* 200 represents HTTP OK */
                if (statusCode == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    result = bufferedReader.readLine();
                    Log.d("HTTP_BuduNebudu", "mam output a vracim uspesne result = " + result);
                    return "OK";
                }
                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null)
                    con.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String g;
        if (prepinac==1) g = "budu"; else g = "nebudu";
        if (s.equals("OK")) Toast.makeText(akt, "Zapsal jsem <" + g + "> na " + zapasid + ". kolo", Toast.LENGTH_SHORT).show();
    }
}
