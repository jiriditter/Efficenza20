package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/*
    odsud se spousti zavolani detailu zapasu HTTP_getZapasDetail
*/

public class detail_zapasu extends Activity {
    private Context ctx;
    private Activity akt;
    HTTP_getZapasDetail zapasdetajl;
    String zapasid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zapaspreview);

        ctx = this;
        akt = this;
        Intent intent = getIntent();
        zapasid = intent.getStringExtra("zapasid");
        zapasdetajl = new HTTP_getZapasDetail(akt);
        zapasdetajl.execute(zapasid);
    }

}
