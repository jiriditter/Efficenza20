package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONZapasy_handler {
    JSONArray jArda;
    sharedPref shp;

    public JSONZapasy_handler(Activity kx) {
        shp = new sharedPref(kx);
    }

    public void setData(JSONArray jA) throws JSONException {
        jArda = null;
        jArda = jA;
        for (int i = 0; i < jArda.length(); i++) {
            JSONObject obj = jArda.getJSONObject(i);
            Log.d("JSON_data", "Adding ..." + obj.getString("nazev"));
        }
        shp.zapisJASAN(jArda);
    }

    public JSONArray getData() throws JSONException {
        for (int i = 0; i < jArda.length(); i++) {
            JSONObject obj = jArda.getJSONObject(i);
            Log.d("JSON_data", "CONTAINED " + obj.getString("nazev"));
        }
        return jArda;
    }

    public void ClearData() {
        jArda = new JSONArray();
    }

}
