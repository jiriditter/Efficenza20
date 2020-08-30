package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
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

/**
 * Created by jd185150 on 3/26/2018.
 */

public class HTTP_getNextMatch extends AsyncTask<String, Void, String> {
    String SUrl;
    String JSON_STRING;
    HTTP_GetWeather pocasicko;
    private Activity akt;
    private HTTP_vzajemnaBilance bilance;

    public HTTP_getNextMatch(String web, Activity a) {
        this.SUrl = web.toString().replaceAll(" ", "%20");
        this.akt = a;
        Log.d("HTTP_getNextMatch", "moje URL je " + SUrl);
    }

    @Override
    protected String doInBackground(String... strings) {
        BufferedReader in = null;
        HttpURLConnection con = null;
        URL url = null;
        String result=null;

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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("HTTP_getNextMatch", "RESULT je " + result);
        JSONArray jsonArray = null;
        Boolean asponjedenodehranej = false;
        final TextView TVsouper = (TextView) ((Activity) akt).findViewById(R.id.PZSouper);
        TextView TVdatum = (TextView) ((Activity) akt).findViewById(R.id.PZDatum);
        TextView TVCas = (TextView) ((Activity) akt).findViewById(R.id.PZCas);
        TextView TVhriste = (TextView) ((Activity) akt).findViewById(R.id.PZHriste);
        TextView TVzadnu = (TextView) ((Activity) akt).findViewById(R.id.PZzapocetdnu);

        Log.d("HTTP_getNextMatch", "result: " + result);

        if((result != null) && (!result.isEmpty())) {
            try {
                jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                Log.d("HTTP_getNextMatch", "velikost pole je : " + length);

                for (int i = 0; i < length; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //fetch all records for current open season
                    String datum = obj.getString("datum") + "\n";
                    String cas = obj.getString("cas") + "\n";
                    String odehrano = obj.getString("odehrano");
                    String kontumacne = obj.getString("kontumacne");
                    String nazev = obj.getString("nazev");
                    String kolo = obj.getString("kolo");
                    String id = obj.getString("id");
                    String hriste = obj.getString("hriste");

                    Log.d("HTTP_getNextMatch", "[" + i + "] odehrano: " + odehrano + ",nazev: " + nazev);

                    if (odehrano.equals("0")) {
                        asponjedenodehranej = true;
                        //toto je nas spravny zapas, ten chceme
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String[] splitter = new String[3];
                        splitter = datum.split("\\.");
                        String d1, d2, d3;

                        if (splitter[0].length() <= 1) d1 = "0" + splitter[0];
                        else d1 = splitter[0];
                        if (splitter[1].length() <= 1) d2 = "0" + splitter[1];
                        else d2 = splitter[1];
                        d3 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                        Date c = Calendar.getInstance().getTime();
                        String dneska = dateFormat.format(c);

                        String datumzapasu = d1 + "/" + d2 + "/" + d3;
                        int rozdil = Integer.parseInt(getCountOfDays(dneska, datumzapasu));
                        if ((rozdil < 16) & (rozdil >=0)) {
                            try {
                                pocasicko = new HTTP_GetWeather(akt, rozdil);
                                pocasicko.execute();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                        }

                        TVsouper.setText(Html.fromHtml("<b>Soupeř:</b> " + nazev));
                        TVdatum.setText(Html.fromHtml("<b>Kdy:</b> " + datum));
                        TVCas.setText(Html.fromHtml("<b>Cas:</b> " + cas));
                        TVhriste.setText(Html.fromHtml(hriste));
                        TVzadnu.setText(rozdil + " dní do začátku zápasu ");

                        TVsouper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UkazVzajemnouBilanciZapasu(TVsouper.getText().toString());
                            }
                        });
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            TVsouper.setText(Html.fromHtml("<b>Soupeř:</b> - "));
            TVdatum.setText(Html.fromHtml("<b>Kdy:</b>  - "));
            TVCas.setText(Html.fromHtml("<b>Cas:</b> - "));
            TVhriste.setText(Html.fromHtml(" - "));
            TVzadnu.setText("? dní do začátku zápasu ");
            TextView tPocasi = (TextView) ((Activity) akt).findViewById(R.id.txtPocasi);
            tPocasi.setText(" - ");
        }
        Log.d("HTTP_getNextMatch", asponjedenodehranej.toString());
        if(!asponjedenodehranej) {
            TVsouper.setText(Html.fromHtml("<b>Soupeř:</b> - "));
            TVdatum.setText(Html.fromHtml("<b>Kdy:</b>  - "));
            TVCas.setText(Html.fromHtml("<b>Cas:</b> - "));
            TVhriste.setText(Html.fromHtml(" - "));
            TVzadnu.setText("? dní do začátku zápasu ");
            TextView tPocasi = (TextView) ((Activity) akt).findViewById(R.id.txtPocasi);
            tPocasi.setText(" - ");
        }
    }

    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount);
    }

    private void UkazVzajemnouBilanciZapasu(String souper) {
        String team[] = souper.split(":");
        String s = team[1].replaceAll("^\\s+", "");
        bilance = new HTTP_vzajemnaBilance(akt, s);
        bilance.execute();
    }
}
