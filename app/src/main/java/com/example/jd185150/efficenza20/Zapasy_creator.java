package com.example.jd185150.efficenza20;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 Vypis vsechny zapasy zvolene sezony
 ar.add(kolo);
 ar.add(datum);
 ar.add(cas);
 ar.add(nazev);
 ar.add(vysledek);
 ar.add(effigoly);
 ar.add(soupergoly);
 ar.add(hrite);
 ar.add(id);
 */

public class Zapasy_creator {
    LinearLayout laya;
    private Activity ctx;
    private AlertDialog myDiag;

    public Zapasy_creator(Activity c) {
        this.ctx = c;
        this.laya = ((Activity) ctx).findViewById(R.id.LAYprehledZapasu);
        laya.removeAllViews();
    }

    public void AddZapas(final ArrayList<String> params) {
        //view pro inflator
        String vysledek = params.get(4);
        String odehrano = params.get(10);
        Log.d("Zapasy_creator", "Efficenza " + params.get(5) + " : " + params.get(6) + " " + params.get(3) + " [vysledek=" + vysledek + "]");

        View hlavniUdaje= View.inflate(ctx,R.layout.zapas,null);
        LinearLayout koloVju = hlavniUdaje.findViewById(R.id.koloView);
        koloVju.setBackgroundResource(R.drawable.kruh);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 10,5, 10);
        hlavniUdaje.setLayoutParams(layoutParams);
        int draw;
        switch(vysledek) {
            case ("0"): draw = R.drawable.zapasy_prohrano;
            break;
            case ("1"):
                if(odehrano.equalsIgnoreCase("1")) draw = R.drawable.zapasy_remiza;
                    else draw = R.drawable.zapas_neodehrano;
                break;
            case ("2"): draw = R.drawable.zapasy_vyhrano;
                break;
            default: draw = R.drawable.zapas_neodehrano;
            break;
        }
        hlavniUdaje.setBackgroundResource(draw);
        hlavniUdaje.setPadding(10,25,10,25);
        laya.setPadding(0,10,0,10);
        laya.addView(hlavniUdaje);

        TextView Zcas = hlavniUdaje.findViewById(R.id.ZAPCas);
        Zcas.setPadding(0,0,0,10);
        TextView ZDatum = hlavniUdaje.findViewById(R.id.ZAPdatum);
        TextView Zapsoup = hlavniUdaje.findViewById(R.id.ZAPSouper);
        Zapsoup.setPadding(50,0,0,10);
        TextView Zaphrist = hlavniUdaje.findViewById(R.id.ZAPHriste);
        Zaphrist.setPadding(50,0,0,0);

        //Zkolo.setText("#" + params.get(0));
        Zcas.setText(params.get(2));
        ZDatum.setText(params.get(1));
        Zapsoup.setText("Efficenza " + params.get(5) + " : " + params.get(6) + " " + params.get(3));
        Zaphrist.setText(params.get(0) + ". kolo, " + params.get(7));

        hlavniUdaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMeDetails(params.get(8));
            }
        });
        hlavniUdaje.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ukazOptions(params);
                return false;
            }
        });
    }

    private void showMeDetails(String s) {
        Intent i = new Intent(ctx, detail_zapasu.class);
        i.putExtra("zapasid", s);
        ctx.startActivity(i);
    }

    private void ukazOptions(final ArrayList<String> params) {
        HTTP_getSestavu gS = new HTTP_getSestavu(ctx, params);
        gS.execute();
    }
}
