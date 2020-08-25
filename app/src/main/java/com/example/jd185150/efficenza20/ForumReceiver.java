package com.example.jd185150.efficenza20;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ForumReceiver extends BroadcastReceiver {
    private HTTP_checkForumNewMessages HTP;

    public ForumReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] s = new String[2];
        SharedPreferences sharedpreferences = context.getSharedPreferences("Effi", Context.MODE_PRIVATE);
        s[0] = sharedpreferences.getString("user", null);
        s[1] = sharedpreferences.getString("password", null);
        //Log.d("ForumReceiver", "zkousim nacist user=" + s[0] + " | pwd=" + s[1]);
        HTP = (HTTP_checkForumNewMessages) new HTTP_checkForumNewMessages(context, s[0], s[1]).execute();
        //Toast.makeText(context, "ForumReceiver fired", Toast.LENGTH_SHORT).show();
    }

}
