package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ukazZapasySoupere extends Activity{
    int teamID =0;
    String rok, cast, tym;
    String paramurl ="";
    String[] tmp;
    Activity akt;
    private _config cfg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zapasysouperu);
        akt = this;
        cfg = new _config();

        Intent intent = getIntent();
        String value = intent.getStringExtra("param");
        tym = value;
        Log.d("ukazZapasySoupere", "tym je prej " + value);

        sharedPref shp = new sharedPref(this);
        tmp = shp.nactiSezonu();
        rok = tmp[1];
        cast = tmp[0];
        Log.d("ukazZapasySoupere", "nacitam sezonu: " + cast + " " + rok);

        JSONObject jA = null;
        try {
            jA = shp.NactiTymy();
        } catch (JSONException e) {
        }

        //zjisti ID soupere
        if (jA != null) {
            Iterator<String> iter = jA.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object val = jA.get(key);
                    Log.d("ukazZapasySoupere", "nacetl jsem woe: [" + key + "] >> " + val + " [teamID="+teamID+"]");
                    Log.d("ukazZapasySoupere", key+" = " + value + "?");
                    if (key.equalsIgnoreCase(value)) teamID = (int) val;
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }
        }

        if(teamID > 0 ) {
            //String sezona = "1";
            //if(cast.equalsIgnoreCase("jaro")) sezona="1"; else sezona="2" ;
            String paramurl = cfg.getApiHosting_teamdetails() +rok+"&competition=1&season="+cast+"&id="+teamID;
            //Log.d("ukazZapasySoupere", "volam url = " + paramurl);
            HTTP_ukazZapasySoupere hUZS = new HTTP_ukazZapasySoupere(akt, tym);
            hUZS.execute(paramurl);
        }
    }
}
