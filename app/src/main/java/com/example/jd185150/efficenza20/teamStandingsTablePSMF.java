package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

/*
    WOE MUSIS OPRAWIT LIGU A SKUPINU!!! TED JE TO NATWRRDO ZATTIM
*/
public class teamStandingsTablePSMF extends Activity {
    LinearLayout platno;
    Activity akt;
    private _config cfg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zapasysouperu);
        akt = this;
        cfg = new _config();
        platno = (LinearLayout) findViewById(R.id.platno);

        sharedPref shp = new sharedPref(this);
        String[] f = shp.nactiSezonu();
        String cast = "1";
        //if(f[0].equalsIgnoreCase("jaro")) cast = "1"; else cast ="2";
        String path = cfg.getApiHosting_standings() +f[1]+"&competition="+f[2]+"&season="+f[0]+"&league="+f[4]+"&group="+f[3]+"&type=final";
        Log.d("PSMF", "URL call :: " + path);

        HTTP_teamstandings hTS = new HTTP_teamstandings(akt);
        hTS.execute(path);
    }

}
