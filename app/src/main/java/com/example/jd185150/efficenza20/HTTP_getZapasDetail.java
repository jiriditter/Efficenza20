package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
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
import java.util.List;

/*
    TODO: uprav php skript, aby zobrazoval hvezdu zapasu

*/

public class HTTP_getZapasDetail extends AsyncTask<String, Void, String> {
    String data;
    Activity akt;
    private _config cfg;

    public HTTP_getZapasDetail(Activity akt) {
        cfg = new _config();
        this.akt = akt;
    }

    @Override
    protected String doInBackground(String... strings) {
        String zapasid = strings[0];
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader bufferedReader;
        String result;
        //Log.d("HTTP_getZapasDetail", "Contacting URL... with zapasid=" + zapasid);

        try {
            data = "?zapasid=" + zapasid;
            String link = cfg.getURL_zapaspreview() + data;
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

    @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null) {
                List<hrac_struct> h = new ArrayList<hrac_struct>();

                JSONArray jsonArray = null;
                JSONArray koment = null;
                String effi_goly = "", souper_goly = "", souper = "", misto = "", komentar = "", odehrano = "", kolo = "", datum = "", hvezda_zapasu = "", cas = "", kontumacne = "";
                String post = "", goly = "", asistence = "", ck = "", zk = "", cislo = "", prezdivka = "", odehral = "", sezonaid = "", poradi = "";
                String cervenokaretnici = "", zlutokaretnici = "";
                TextView tSouperi = (TextView) ((Activity) akt).findViewById(R.id.txtSouperi);
                TextView tSestava = (TextView) ((Activity) akt).findViewById(R.id.txtSestava);
                TextView tGoly = (TextView) ((Activity) akt).findViewById(R.id.txtGoly);
                TextView tAsistence = (TextView) ((Activity) akt).findViewById(R.id.txtAsist);
                TextView tKomentar = (TextView) ((Activity) akt).findViewById(R.id.txtKomentar);
                TextView tKarty = (TextView) ((Activity) akt).findViewById(R.id.txtKarty);
                TextView tHvezdaZapasu = (TextView) ((Activity) akt).findViewById(R.id.txtMuzZapasu);
                LinearLayout lKarty = (LinearLayout) ((Activity) akt).findViewById(R.id.ll_karty);
                //zap = new zapaspreview();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("JASAN", "Getting details of a match");
                    Log.d("JASAN", s.toString());
                    JSONArray zapasy = jsonObject.getJSONArray("zapasy");
                    if (jsonObject.has("komentar")) {
                        koment = jsonObject.getJSONArray("komentar");
                    }
                    JSONArray zapasyhrac = jsonObject.getJSONArray("zapasyhrac");
                    if(zapasy.length() >0) {
                        for (int i = 0; i < zapasy.length(); i++) {
                            JSONObject c = zapasy.getJSONObject(i);
                            effi_goly = c.getString("effi_goly");
                            souper_goly = c.getString("souper_goly");
                            souper = c.getString("nazev");
                            misto = c.getString("misto");
                            kolo = c.getString("kolo");
                            odehrano = c.getString("odehrano");
                            datum = c.getString("datum");
                            cas = c.getString("cas");
                            kontumacne = c.getString("kontumacne");
                            poradi = c.getString("poradi");
                        }

                        if ((odehrano.equals("1") && (koment != null))) {
                            for (int q = 0; q < koment.length(); q++) {
                                JSONObject c = koment.getJSONObject(q);
                                komentar = c.getString("text");
                            }

                            for (int z = 0; z < zapasyhrac.length(); z++) {
                                JSONObject zh = zapasyhrac.getJSONObject(z);
                                hrac_struct hh = new hrac_struct();
                                hh.setName(zh.getString("prezdivka"));
                                hh.setAsistence(zh.getString("asistence"));
                                hh.setGoly((zh.getString("goly")));
                                hh.setPost((zh.getString("post")));
                                hh.setCislo(zh.getString("cislo"));
                                hh.setCk(Integer.parseInt(zh.getString("ck")));
                                hh.setZk(Integer.parseInt(zh.getString("zk")));
                                h.add(hh);
                            }

                            String str_sestava = bulidSestavaString(h);
                            String str_goly = returnBodyZapasu(h, 0);
                            String str_asistence = returnBodyZapasu(h, 1);
                            String str_zk = returnBodyZapasu(h, 2);
                            String str_ck = returnBodyZapasu(h, 3);

                            tSouperi.setText("AC Efficenza [ " + effi_goly + " : " + souper_goly + " ] " + souper);
                            tSestava.setText(str_sestava);
                            tGoly.setText(Html.fromHtml("<b>Góly:</b> " + str_goly));
                            tAsistence.setText(Html.fromHtml("<b>Asistence:</b> " + str_asistence));
                            tKomentar.setText(komentar);
                            if (str_zk.length() > 20) {
                                tKarty.setText(Html.fromHtml(str_zk));
                                if (str_ck.length() > 22)
                                    tKarty.setText(Html.fromHtml(tKarty.getText() + "<br>" + str_ck));
                            } else lKarty.setVisibility(View.GONE);
                            tHvezdaZapasu.setText("");
                        } else {
                            tSouperi.setText("AC Efficenza vs " + souper + " - ještě neodehráno");
                            tSestava.setText("Neodehráno");
                            tGoly.setText("Neodehráno");
                            tAsistence.setText("Neodehráno");
                            tKomentar.setText("Bez komentáře. Sorry jako");
                            tHvezdaZapasu.setText("");
                            lKarty.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    private String bulidSestavaString(List<hrac_struct> h) {
        String sstwa = "";
        List<hrac_struct> gol = new ArrayList<hrac_struct>();
        List<hrac_struct> def = new ArrayList<hrac_struct>();
        List<hrac_struct> zal = new ArrayList<hrac_struct>();
        List<hrac_struct> att = new ArrayList<hrac_struct>();
        for (int i = 0; i < h.size(); i++) {
            switch (h.get(i).getPost()) {
                case "0":
                    Log.d("PSMF","++Golman found = " + h.get(i).getName());
                    gol.add(h.get(i));
                    break;
                case "1":
                    Log.d("PSMF","++Obrance found = " + h.get(i).getName());
                    def.add(h.get(i));
                    break;
                case "2":
                    Log.d("PSMF","++Zaloznik found = " + h.get(i).getName());
                    zal.add(h.get(i));
                    break;
                case "3":
                    Log.d("PSMF","++Utocnik found = " + h.get(i).getName());
                    att.add(h.get(i));
                    break;
            }
        }

        //golmani
        if(gol.size() >0) {
            for (int i = 0; i < gol.size(); i++) {
                sstwa += gol.get(i).getName() + ", ";
            }
            Log.d("PSMF", "Golmani :: " + sstwa);
            sstwa = sstwa.substring(0, sstwa.length() - 2);
        }
        //obranci
        if(def.size() >0) {
            sstwa += " - ";
            for (int i = 0; i < def.size(); i++) {
                sstwa += def.get(i).getName() + ", ";
            }
            Log.d("PSMF", "obranci :: " + sstwa);
            sstwa = sstwa.substring(0, sstwa.length() - 2);
        }
        //zaloznici
        if(zal.size() >0) {
            sstwa += " - ";
            for (int i = 0; i < zal.size(); i++) {
                sstwa += zal.get(i).getName() + ", ";
            }
            sstwa = sstwa.substring(0, sstwa.length() - 2);
        }
        //utocnici
        if(att.size() >0) {
            sstwa += " - ";
            for (int i = 0; i < att.size(); i++) {
                sstwa += att.get(i).getName() + ", ";
            }
            sstwa = sstwa.substring(0, sstwa.length() - 2);
        }

        Log.d("HTTP_getZapasDetail", "vracim sstwa=" + sstwa);
        return sstwa;
    }

    private String returnBodyZapasu(List<hrac_struct> h, int prepinac) {
        String str_ = "";
        List<hrac_struct> goly = new ArrayList<hrac_struct>();
        List<hrac_struct> asist = new ArrayList<hrac_struct>();

        switch (prepinac) {
            case 2: //Z karty
                    str_ = "<b>Žluté karty: </b>";
                break;
            case 3: //C karty
                    str_ = "<b>Červené karty: </b>";
                break;
        }

        for (int i = 0; i < h.size(); i++) {
            switch (prepinac) {
                case 0: //goly
                    if (h.get(i).getGoly() >0) str_ += h.get(i).getName() + ": " + h.get(i).getGoly() + "<br>";
                    break;
                case 1: //asist
                    if (h.get(i).getAsistence() >0) str_ += h.get(i).getName() + ": " + h.get(i).getAsistence() + "<br>";
                    break;
                case 2: //Z karty
                    if (h.get(i).getZk() >0) str_ += h.get(i).getName() + ": " + h.get(i).getZk() + "<br>";
                    break;
                case 3: //C karty
                    if (h.get(i).getCk() >0) str_ += h.get(i).getName() + ": " + h.get(i).getCk() + "<br>";
                    break;
            }
        }
        return str_;
    }
}