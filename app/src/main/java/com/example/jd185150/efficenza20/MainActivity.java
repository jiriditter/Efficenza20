package com.example.jd185150.efficenza20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private permissions prava;
    private AsyncTask<String, String, String> overeni;
    public Activity akt;
    private EditText tUziv, tHsl;
    private String[] user = new String[2];
    private AlertDialog.Builder dialog;
    private AlertDialog aDiag;
    HTTP_fetchSezonaList sezona;
    HTTP_getNextMatch dalsiZapas;
    HTTP_getZapasy prehledZapasu;
    ImageView _menu, _forum, _stats, _standings, _refresh;
    TextView tv_aktsezona;
    sharedPref shp;
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;
    private String s1[] = new String[1];
    private logger log;
    private _config cfg;
    //scroll menu: 1. options, 2. tabulka poradi, 3 forum, 4. statistiky


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        akt = this;
        cfg = new _config();
        //log = new logger(akt);

        //log.Zapis("Nacitam preference");
        shp = new sharedPref(this);
        s1 = shp.nacti();
        if((s1[0] == null) || (s1[1] == null)) {
            //log.Zapis("Uzivatel nenalezen, LOGIN");
            setContentView(R.layout.activity_main);
            Login();
        } else {
            //prava = new permissions(this);
            //prava.hasPermission(Manifest.permission.READ_CALENDAR);
            //log.Zapis("Uzivatel nalezen, pokracuji");
            VseJeOK();
        }
    }

    public void VseJeOK() {
        setContentView(R.layout.mainscreen);
        Init();
        NadchazejiciZapas();
        Update();
        Broadcast();
    }

    @SuppressLint("RestrictedApi")
    public void Login() {
        dialog = new AlertDialog.Builder(this);
        //log.Zapis("Inflatuju R.layout.login");
        LayoutInflater inf = this.getLayoutInflater();
        final View v = inf.inflate(R.layout.login, null);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(50,5,10,50);

        /* mezitim nacti posledni sezonu */
        Log.d("MainActivity","Volam prihlasovaci dialog");
        Log.d("AFTERLOGIN","zadne shared prefs nenalezeny. going to zjistit details");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int rok = calendar.get(Calendar.YEAR);
        HTTP_getTeamDetails hAt = new HTTP_getTeamDetails(this, 0, 0, false);
        Log.d("MainActivity",cfg.getURL_Lookupteam() + rok);
        hAt.execute(cfg.getURL_Lookupteam() + rok);

        /* vyvolej prihlasovaci dialog */
        dialog.setMessage("Prosím zadej své přihlašovací údaje na web Efficenzy");
        dialog.setTitle("Požadováno přihlášení");
        dialog.setPositiveButton(R.string.prihlasitse, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                aDiag.dismiss();
                String usr = String.valueOf(tUziv.getText());
                String hleslo = String.valueOf(tHsl.getText());
                //log.Zapis("Uzivatel zmacknul PRIHLASENI");
                if((usr != null) && (hleslo != null)) {
                    String[] parametry = {cfg.getURL_Auth(), usr, hleslo};
                    overeni = new auth((MainActivity) akt, MainActivity.this, new auth.authResponse() {
                        @Override
                        public void processFinish(String output){
                            if(output.equals("OK")) VseJeOK(); else Login();
                        }
                    }).execute(cfg.getURL_Auth(), usr, hleslo);
                } else {
                    VseJeOK();
                }
            }
        });

        dialog.setView(v, 20, 5, 20, 5);

        tUziv = (EditText) v.findViewById(R.id.txtUziv);
        tHsl = (EditText) v.findViewById(R.id.txtHleslo);
        tUziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tUziv.getText().toString().equals("Uživatel")) {
                    tUziv.setText("");
                }
            }
        });
        tUziv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (tUziv.getText().toString().equals("")) {
                        tUziv.setText("Uživatel");
                    }
                }
            }
        });
        tHsl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (tHsl.getText().toString().equals("")) {
                    tHsl.setText("Heslo");
                }
            }
        });
        tHsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tHsl.getText().toString().equals("Heslo")) {
                    tHsl.setText("");
                }
            }
        });

        aDiag = dialog.create();
        aDiag.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Prava uz jsou prirazeny", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, "hm, bohuzel zadna prava dnes...", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    private void Init() {
        _menu = akt.findViewById(R.id.btn_menu);
        _forum = akt.findViewById(R.id.btn_forum);
        _stats = akt.findViewById(R.id.btn_stats);
        _standings = akt.findViewById(R.id.btn_standing);
        _refresh = akt.findViewById(R.id.btn_refresh);
        tv_aktsezona = (TextView) akt.findViewById(R.id.tv_akt_sezona);
        LinearLayout lin_hriste = (LinearLayout) akt.findViewById(R.id.lay_hriste);
        final TextView tvHriste = (TextView) akt.findViewById(R.id.PZHriste);
        shp = new sharedPref(akt);
        s1 = shp.nactiSezonu();

        //ziskej team_id ostatnich tymu - zavolej vsechny zapasy Efficenzy a vypreparuj tym id jednotlivych tymu
        String cast = "jaro";
        //pokus se nacist shared prefs
        if(s1[0] != null) {
            if (s1[0].equalsIgnoreCase("jaro")) cast = "1";
            else cast = "2";
            String url = cfg.getURL_Teamdetails() + s1[1] + "&competition=" + s1[2] + "&season=" + s1[0] + "&id=1083";
            Log.d("AFTERLOGIN","zvlastni, nacetl jsem sharedprefs. Volam :: " + url);
            HTTP_getZapasyIstatnich hZO = new HTTP_getZapasyIstatnich(akt);
            hZO.execute(url);
            tv_aktsezona.setText(cast + " " + s1[1]);
        } else {
            //pokud neexistuji sharedprefs, jde o prvni prihlaseni. ziskej udaje o tymu
            Log.d("AFTERLOGIN","zadne shared prefs nenalezeny. going to zjistit details");
            //HTTP_getTeamDetails hAt = new HTTP_getTeamDetails(this);
            //hAt.execute();
        }
        _menu.setImageResource(R.drawable.settings);
        _forum.setImageResource(R.drawable.forum);
        _standings.setImageResource(R.drawable.standings);
        _stats.setImageResource(R.drawable.statistics);
        _refresh.setImageResource(R.drawable.refresh);

        _refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });
        _menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //invoking OPTIONS
                sezona = new HTTP_fetchSezonaList(akt,1, MainActivity.this); //chci vsechny sezony
                sezona.execute();
            }
        });
        lin_hriste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HTTP_HristeDetails","Spoustim Async HTTP_HristeDetails: " + tvHriste.getText().toString());
                HTTP_HristeDetails hDetails = new HTTP_HristeDetails(akt, tvHriste.getText().toString());
                hDetails.execute();
            }
        });

        _forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u[] = new String[2];
                u = shp.nacti();
                if((u[0] != null) & (!u[0].isEmpty())) {
                    Intent i = new Intent(akt, forum.class);
                    i.putExtra("user", u[0]);
                    i.putExtra("pawd", u[1]);
                    akt.startActivity(i);
                } else Toast.makeText(akt, "Musíš se nejprve přihlásit", Toast.LENGTH_SHORT).show();
            }
        });

        _standings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(akt, teamStandingsTablePSMF.class);
                akt.startActivity(i);
            }
        });
    }

    public void Update() {
        if ((s1[0] == null) & (s1[1] == null) ){
            //uzivatel nema nastavene preference sezony, nacti posledni
            Log.d("UVODNISTRANKA", "uzivatel nema nastavene preference sezony, nacti posledni");
            sezona = new HTTP_fetchSezonaList(this,0, MainActivity.this);
            sezona.execute();
        } else {
            Log.d("UVODNISTRANKA", "spoustim nacteni preferencni sezony");
        }
        s1 = shp.nactiSezonu();
        Log.d("UVODNISTRANKA", "nacetl jsem " + s1[0] + " " + s1[1]);
        String cast = "";
        if(s1[0] != null) {
            Log.d("UVODNISTRANKA", "s1[0] neni null, ale je >> " + s1[0] + " " + s1[1]);
            if (s1[0].equals("1")) cast = "jaro";
            else cast = "podzim";
            tv_aktsezona.setText(cast + " " + s1[1]);
            PrehledZapasu(cast, s1[1]);
            NadchazejiciZapas();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void NadchazejiciZapas() {
        String c = shp.nactiSezonu()[0];
        String r = shp.nactiSezonu()[1];
        String p = cfg.getURL_NEXT_MATCH();
        if(c != null) {
            if (c.equals("1")) p += "jaro " + r;
            else p += "podzim " + r;
            dalsiZapas = new HTTP_getNextMatch(p, akt);
            dalsiZapas.execute();
        }
    }

    private void PrehledZapasu(String j, String r) {
        prehledZapasu = new HTTP_getZapasy(cfg.getURL_PREHLED_ZAPASU() + j + " " + r, akt);
        prehledZapasu.execute();
    }

    public void Broadcast() {
        Intent intent = new Intent(this, ForumReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        //Toast.makeText(this, "Alarm will vibrate at time specified", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (aDiag!=null && aDiag.isShowing() ){
            aDiag.cancel();
        }
    }
}
