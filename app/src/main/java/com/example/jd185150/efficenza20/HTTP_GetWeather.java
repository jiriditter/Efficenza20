package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jd185150 on 3/23/2018.
 */

public class HTTP_GetWeather extends AsyncTask<String,String,String> {
    Context ctx;
    JSONObject pocasi;
    String ajkona;
    String deskripsn;
    JSONObject reader;
    JSONObject main;
    JSONArray weather;
    JSONObject list;
    JSONObject L1teplota;
    String teplota;
    JSONObject forecast;
    JSONObject forecastday, min, max;
    JSONObject day, temp;
    int obrazekpocasi, indays;
    String link;
    URL url;
    String JSON_STRING;

    public HTTP_GetWeather(Context ctx, int r) throws MalformedURLException {
        this.ctx = ctx;
        //this.link = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/125594?apikey=LUFSSAFMYnKF3mUpZAwepxN3iIOWEGfo";
        this.link = "https://api.weatherbit.io/v2.0/forecast/daily?city=Prague&key=3889c27bf5fd473faa105f686c6bd416";
        //this.url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Prague&units=metric&cnt=16&APPID=d3b2977e1415c368e8edeb4a7a352f34");
        //this.url = new URL("http://api.apixu.com/v1/forecast.json?key=05124020c2354853b9b80539182903&q=Prague&days=14");
        indays = r;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("HTTP_GetWeather ", "***Starting background process");
        try {
        URL url = new URL(this.link);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        Log.d("HTTP_GetWeather ", "opening connection");
        int responsecode = urlConnection.getResponseCode();
        Log.d("HTTP_GetWeather ", "response code = " + responsecode);
        if (responsecode == 200) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    Log.d("HTTP_GetWeather ", "line = " + line);
                }
                Log.d("HTTP_GetWeather ", "stringbuilder = " + stringBuilder.toString());
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } else {
            return null;
        }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        /*
        valid with OpenWeather
        try {
            reader = new JSONObject(s);
            Log.d("HTTP_GetWeather", "reader: " + reader.toString());
            JSONArray list = reader.getJSONArray("list");
            forecastday = list.getJSONObject(indays);
            temp = forecastday.getJSONObject("temp");
            pocasi = forecastday.getJSONArray("weather").getJSONObject(0);
            deskripsn = pocasi.getString("description");
            teplota = temp.getDouble("day");
            ajkona = pocasi.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
         */
        if (s != null) {
            Log.d("HTTP_GetWeather", "reader != null ");
            try {
                reader = new JSONObject(s);
                Log.d("HTTP_GetWeather", "reader: " + reader.toString());
                Log.d("HTTP_GetWeather", "indays: " + indays);
                    JSONArray list = reader.getJSONArray("data");
                    Log.d("HTTP_GetWeather", "list (array) = " + list.length());
                    for (int i=0; i<list.length(); i++) {
                        JSONObject c = list.getJSONObject(i);
                        Log.d("HTTP_GetWeather", "i = " + i);
                        if(i == indays) {
                            Log.d("HTTP_GetWeather", "i = indays, stepping in");
                            teplota = c.getString("temp");
                            Log.d("HTTP_GetWeather", "teplota: " + teplota);
                            temp = c.getJSONObject("weather");
                            ajkona = temp.getString("icon");
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView tV = (TextView) ((Activity) ctx).findViewById(R.id.txtPocasi);
                tV.setText(teplota + " ℃");

                //String iconUrl = "http://openweathermap.org/img/w/" + ajkona + ".png";
                String iconUrl = "https://www.weatherbit.io/static/img/icons/" + ajkona + ".png";
                Log.d("IMAGER", "ykona = " + iconUrl);
                ImageView iV = (ImageView) ((Activity)ctx).findViewById(R.id.imgPocasicko);
                new HTTP_getImage((ImageView) iV).execute(iconUrl);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iV.getLayoutParams();
                params.width = 120;
                params.height = 120;
                iV.setLayoutParams(params);
        }
    }

}
