package com.example.jd185150.efficenza20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jd185150 on 3/26/2018.
 */

//NASTAVENI DIALOG
public class HTTP_fetchSezonaList extends AsyncTask<String, Void, String> {
    Activity akt;
    String JSON_STRING;
    sharedPref shp;
    private ProgressDialog pracuju;
    private AlertDialog aDiag;
    private int prep;
    EditText eUziv, ePwd;
    Button zapasydokalend;
    Spinner szList;
    private permissions prava;
    private myCalendar kalendar;
    MainActivity uS;
    int yndex =0;
    private _config cfg;

    public HTTP_fetchSezonaList(Activity a, int prepinac, MainActivity UU) {
        this.akt = a;
        this.prep = prepinac;
        shp = new sharedPref(akt);
        this.uS = UU;
        cfg = new _config();
    }

    @Override
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
    protected String doInBackground(String... strings) {
        BufferedReader in = null;
        HttpURLConnection con = null;
        URL url = null;
        String result=null;

        try {
            url = new URL(cfg.getURL_fetchsezona());
            Log.d("HTTP_fetchSezonaList", "URL = " + cfg.getURL_fetchsezona());
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
        JSONArray jsonArray = null;
        pracuju.dismiss();

        if(result != null) {
            try {
                jsonArray = new JSONArray(result);
                switch (prep) {
                    case 0:
                        //chci jenom posledni sezonu
                        Log.d("HTTP_fetchSezonaList", "result = " + result);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String rok = obj.getString("rok");
                        String cast = obj.getString("cast");
                        String liga = obj.getString("liga");
                        String skupina = obj.getString("skupina");
                        Log.d("HTTP_fetchSezonaList", "wratilo se mi: " + cast + " " + rok);
                        shp = new sharedPref(akt);
                        Log.d("HTTP_fetchSezonaList", "Zapisuju sharedprefs: " + cast + " " + rok);
                        //"https://api.psmf.zlutazimnice.cz/api/v1/teams-by-name?name=efficenza%20&token&year=" + rok;
                        switch(cast) {
                            case "jaro": shp.zapisSezonu("1", rok, "1", skupina, liga);
                            break;
                            case "podzim": shp.zapisSezonu("2", rok, "1", skupina, liga);
                            break;
                        }

                        int qa;
                        if (cast.equals("jaro")) qa =1; else qa =2;
                        HTTP_getTeamDetails HgTD = new HTTP_getTeamDetails(akt, qa, Integer.parseInt(rok), true);
                        HgTD.execute(cfg.getApiHosting_teamsbyname() + rok);
                        break;
                    case 1: //chci vsechny sezony do spinneru
                        final List<String> szn = new ArrayList<String>();
                        int count = jsonArray.length();

                        for (int p = 0; p < count; p++) {
                            JSONObject Jojako = jsonArray.getJSONObject(p);
                            String syzn = Jojako.getString("cast") + " " + Jojako.getString("rok");
                            szn.add(syzn);
                        }
                        UkazOptions(szn);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void UkazOptions(List<String> lst) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(akt);
        LayoutInflater inf = akt.getLayoutInflater();
        final View v = inf.inflate(R.layout.options, null);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(50,5,10,50);
        dialog.setTitle("NASTAVENÍ");
        dialog.setView(v, 20, 5, 20, 5);
        dialog.setPositiveButton(R.string.txtSave, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //saving preferences
                String sezona = szList.getSelectedItem().toString();
                String[] sezonasplitter = sezona.split(" ");
                String uzivatel = eUziv.getText().toString();
                String heslo = ePwd.getText().toString();
                int cast = 0;
                if (sezonasplitter[0].equals("jaro")) cast = 1; else cast = 2;
                HTTP_getTeamDetails HgTD = new HTTP_getTeamDetails(akt, cast, Integer.parseInt(sezonasplitter[1]), true);
                HgTD.execute(cfg.getApiHosting_teamsbyname() + sezonasplitter[1]);
            }
        });
        dialog.setNegativeButton(R.string.txtCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                aDiag.dismiss();
            }
        });

        aDiag = dialog.create();
        aDiag.show();

        eUziv = (EditText)aDiag.findViewById(R.id.edtUzivatel);
        ePwd = (EditText)aDiag.findViewById(R.id.edtHeslo);
        zapasydokalend = (Button)aDiag.findViewById(R.id.pridejzapasydokalendare);
        String[] u = shp.nacti();
        if((u[0] != null) && (u[1] != null)) {
            eUziv.setText(u[0]);
            ePwd.setText(u[1]);
        }

        BuildSezonaSpinner(aDiag, lst);
        zapasydokalend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prava = new permissions(akt);
                if ((prava.hasPermission(Manifest.permission.READ_CALENDAR)) & (prava.hasPermission(Manifest.permission.WRITE_CALENDAR))) {
                    kalendar = new myCalendar(akt);
                }
            }
        });
    }

    private void BuildSezonaSpinner(AlertDialog diag, final List<String> lst) {
        szList =  (Spinner)diag.findViewById(R.id.sezonalist);
        ArrayAdapter<String> arSezona = new ArrayAdapter<String>(akt, android.R.layout.simple_spinner_dropdown_item , lst);
        szList.setAdapter(arSezona);

        shp = new sharedPref(akt);
        String[] sz = new String[5];
        sz = shp.nactiSezonu();
        yndex =0;
        if(sz[0] != null) {
            String tmp;
            if(sz[0].equals("1")) tmp = "jaro " + sz[1]; else tmp = "podzim " + sz[1];
            int q = 0;
            for(String s : lst) {
                if (s.equals(tmp)) yndex = q;
                q++;
            }
        }
        szList.setSelection(yndex);

        szList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(akt, lst.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
