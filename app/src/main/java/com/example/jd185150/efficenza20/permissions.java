package com.example.jd185150.efficenza20;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jd185150 on 3/19/2018.
 */

public class permissions {
    private static Activity act;
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final int iREAD_CALENDAR = 0;
    public static final int iWRITE_CALENDAR = 1;

    public permissions(Activity act) {
        this.act = act;
    }

    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasPermission(String permission) {
        if (useRunTimePermissions()) {
            if(act.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                switch(permission) {
                    case READ_CALENDAR:
                        Log.d("Prava", "zkousim cist kalendar");
                        RequestPermissions(permission, iREAD_CALENDAR);
                    break;
                    case WRITE_CALENDAR:
                        Log.d("Prava", "zkousim zapisovat do kalendare");
                        RequestPermissions(permission, iWRITE_CALENDAR);
                    break;
                }
                return false;
            }
        }
        return true;
    }

    public static void RequestPermissions(final String whatta, final int PERMISSION_CODE) {
        if (ActivityCompat.shouldShowRequestPermissionRationale (act, whatta)) {
            new AlertDialog.Builder(act)
                    .setTitle("KALENDAR")
                    .setMessage("Potrebuju ti zapisovat do kalendare zapasy")
                    .setPositiveButton("Tak dobre", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(act, new String[] {whatta}, PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Ne, Nechci", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(act, new String[] {whatta}, PERMISSION_CODE);
        }
    }
}
