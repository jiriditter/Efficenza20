package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jd185150 on 3/19/2018.
 */

public class sharedPref {
    private Activity ctx;
    public String usr;
    public String pwd;
    JSONArray jArda;
    JSONObject jO;

    public sharedPref(Activity c) {
        this.ctx = c;
    }

    public String[] nacti() {
        String[] s = new String[2];
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        s[0] = sharedpreferences.getString("user", null);
        s[1] = sharedpreferences.getString("password", null);

        return s;
    }

    public void zapisUsera(String usr, String pwd) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user", usr);
        editor.putString("password", pwd);
        editor.commit();
    }

    public String[] nactiSezonu() {
        String[] s = new String[5];
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        s[0] = sharedpreferences.getString("cast", null);
        s[1] = sharedpreferences.getString("rok", null);
        s[2] = sharedpreferences.getString("competition", null);
        s[3] = sharedpreferences.getString("group", null);
        s[4] = sharedpreferences.getString("league", null);
        Log.d("SharedPrefs", "s[0]=" + s[0] + ", s[1]=" + s[1] + ", s[2]=" + s[2] + ", s[3]=" + s[3] + ", s[4]=" + s[4]);
        return s;
    }

    public void zapisSezonu(String cast, String rok, String competition, String skupina, String liga) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("cast", cast);
        editor.putString("rok", rok);
        editor.putString("competition", competition);
        editor.putString("group", skupina);
        editor.putString("league", liga);
        editor.commit();
    }

    public void zapisJASAN(JSONArray jAjA) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("jsondata", jAjA.toString());
        editor.commit();
    }

    public JSONArray nactiJASAN() throws JSONException {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        String strJson = sharedpreferences.getString("jsondata","0");//second parameter is necessary ie.,Value to return if this preference does not exist.
        jArda = new JSONArray(strJson);

        return jArda;
    }

    public void UlozTymy(String tm) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("tymy", tm);
        editor.commit();
    }

    public JSONObject NactiTymy() throws JSONException{
        SharedPreferences sharedpreferences = ctx.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        String strJson = sharedpreferences.getString("tymy","0");
        Log.d("sharedPrefs","shp nacteno: " + strJson);
        jO = new JSONObject(strJson);
        return jO;
    }
}
