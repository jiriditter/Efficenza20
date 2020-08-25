package com.example.jd185150.efficenza20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
    TODO: pridej adresu hriste, aby byla vyhledavatelna Googlem
    hriste_address
*/

class mujCalendar {
    public String name;
    public String id;

    public mujCalendar(String _name, String _id) {
        name = _name;
        id = _id;
    }

}

public class myCalendar {
    private Activity akt;
    private Spinner spin;
    private permissions prava;
    private sharedPref shp;
    private String SpinnerItem;
    private JSONArray jArda;
    private AlertDialog.Builder novejdialog;
    private int pridanozapasu;
    View v;
    mujCalendar[] mKal;

    public myCalendar(Activity akt) {
        this.akt = akt;
        BuildDialog();
        pridanozapasu = 0;
    }

    @SuppressLint("RestrictedApi")
    private void BuildDialog() {
        novejdialog = new AlertDialog.Builder(akt);
        LayoutInflater inf = akt.getLayoutInflater();
        final View v = inf.inflate(R.layout.kalendarlist, null);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(50, 5, 10, 50);

        novejdialog.setTitle("Výběr kalendáře");
        novejdialog.setView(v, 20, 5, 20, 5);
        ShowCalendars();
    }

    private List<String> listCalendars() {
        String[] l_projection = new String[]{"_id", "calendar_displayName"};
        List<String> kal = new ArrayList<String>();

        Uri l_calendars;
        if (Build.VERSION.SDK_INT >= 8) {
            l_calendars = Uri.parse("content://com.android.calendar/calendars");
        } else {
            l_calendars = Uri.parse("content://calendar/calendars");
        }
        ContentResolver contentResolver = akt.getContentResolver();
        Cursor managedCursor = contentResolver.query(l_calendars, l_projection, null, null, null);

        if (managedCursor.moveToFirst()) {
            mKal = new mujCalendar[managedCursor.getCount()];
            String calName;
            String calID;
            int cont = 0;
            int nameCol = managedCursor.getColumnIndex(l_projection[1]);
            int idCol = managedCursor.getColumnIndex(l_projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                calID = managedCursor.getString(idCol);
                Log.d("_Calendar", "fetched: " + calName + " [" + calID + "]");
                mKal[cont] = new mujCalendar(calName, calID);
                //m_calendars[cont] = new mujCalendar(calName, calID);
                cont++;
            } while (managedCursor.moveToNext());
            managedCursor.close();
        }
        return kal;
    }

    public void ShowCalendars() {
        listCalendars();

        novejdialog.setNegativeButton(R.string.txtCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface novejdialogInterface, int i) {

            }
        });
        novejdialog.setPositiveButton(R.string.txtPouzij, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface novejdialogInterface, int i) {
                //zapis zapasy do kalendare
                try {
                    pridejZaznamyDokalendare();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        AlertDialog aDiag = novejdialog.create();
        aDiag.show();

        String[] qwe = new String[mKal.length];
        int k = 0;
        for (mujCalendar mC : mKal) {
            qwe[k] = mC.name;
            k++;
        }
        spin = (Spinner) aDiag.findViewById(R.id.spinKalendarList);
        ArrayAdapter<String> aAdapt = new ArrayAdapter<String>(akt, android.R.layout.simple_spinner_dropdown_item, qwe);
        aAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aAdapt);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //SpinnerItemIdx = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String vratIDKalendare(String nejm) {
        int k = 0;
        for (mujCalendar mC : mKal) {
            if (nejm == mC.name) return mC.id;
        }
        return "";
    }

    private void pridejZaznamyDokalendare() throws JSONException {
        shp = new sharedPref(akt);
        jArda = shp.nactiJASAN();
        int count = jArda.length();
        Log.d("_Calendar", "JASAN = " + jArda.toString());

        for (int p = 0; p < count; p++) {
            ArrayList<String> ar = new ArrayList<String>();
            JSONObject Jojako = jArda.getJSONObject(p);
            String datum = Jojako.getString("datum");
            String cas = Jojako.getString("cas");
            String hrite = Jojako.getString("hriste");
            String souper = Jojako.getString("nazev");
            String kolo = Jojako.getString("kolo");
            String adresa = Jojako.getString("adresa");
            ar.add(kolo);
            ar.add(datum);
            ar.add(cas);
            ar.add(souper);
            ar.add(hrite);
            ar.add(adresa);

            pridejSingleZaznam(ar);
        }
        //datum = "7.5.2018 16:00";
    }

    private void pridejSingleZaznam(ArrayList<String> a) {
        try {
            ContentResolver cr = akt.getContentResolver();
            ContentValues values = new ContentValues();

            String thisYear = new SimpleDateFormat("yyyy").format(new Date());
            String souper = "", start = "";
            String datum = a.get(1) + thisYear + " " + a.get(2);
            String kolo = "", hriste = "";

            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String dateStarts = datum;
            String dateEnds = datum;
            Date startDate, endDate;
            startDate = formatter.parse(dateStarts);
            endDate = formatter.parse(dateEnds);
            long begin = startDate.getTime();// starting time in milliseconds
            long end = endDate.getTime() + 60 * 60 * 1000; // ending time in milliseconds

            //check if event already exists
            String[] proj =
                    new String[]{
                            CalendarContract.Instances._ID,
                            CalendarContract.Instances.BEGIN,
                            CalendarContract.Instances.END,
                            CalendarContract.Instances.EVENT_ID};
            Cursor cursor = CalendarContract.Instances.query(akt.getContentResolver(), proj, begin, end, "AC Efficenza, #" + a.get(0) + ". kolo ");
            if (cursor.getCount() > 0) {
                Log.v("_Calendar", "zaznam jiz existuje");
            } else {
                //zaznam jeste neexistuje, zapis
                SpinnerItem = spin.getSelectedItem().toString();
                values.put(CalendarContract.Events.DTSTART, begin);
                values.put(CalendarContract.Events.DTEND, end);
                values.put(CalendarContract.Events.TITLE, a.get(0) + ". kolo, AC Efficenza - " + a.get(3));
                values.put(CalendarContract.Events.DESCRIPTION, "Efficenza vs " + a.get(3) + ", " + a.get(4));
                values.put(CalendarContract.Events.EVENT_LOCATION, a.get(4) + ", " + a.get(5));
                values.put(CalendarContract.Events.HAS_ALARM, 1);
                values.put(CalendarContract.Events.CALENDAR_ID, vratIDKalendare(SpinnerItem));
                values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
                Log.v("_Calendar", "************ " + a.get(0) + ". kolo");
                Log.v("_Calendar", "Start: " + begin);
                Log.v("_Calendar", "Konec: " + end);
                Log.v("_Calendar", "DESCRIPTION: " + "Efficenza vs " + a.get(3));
                Log.v("_Calendar", "EVENT_LOCATION: " + a.get(4));

                if (prava.hasPermission(Manifest.permission.WRITE_CALENDAR)) {
                    //add reminder
                    if (ActivityCompat.checkSelfPermission(akt, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                        long eventId = new Long(uri.getLastPathSegment());
                        values.clear();
                        values.put(CalendarContract.Reminders.MINUTES, 120);
                        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
                        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                        uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
                        Log.v("_Calendar", "Udalost zapsana");
                        pridanozapasu++;
                        return;
                    }
                } else Log.v("_Calendar", "Nemam prava zapisovat do kalendare");
            }
        } catch(Exception e){
                Log.d("KUWA KUWA", "Samsink is wronk");
                e.printStackTrace();
            }
        Toast.makeText(akt, "Přidal jsem celkem " + pridanozapasu + " zápasů", Toast.LENGTH_SHORT).show();
    }
}
