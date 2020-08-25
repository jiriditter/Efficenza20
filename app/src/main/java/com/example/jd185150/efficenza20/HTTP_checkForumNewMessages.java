package com.example.jd185150.efficenza20;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HTTP_checkForumNewMessages extends AsyncTask<String, String, String> {
    String uziv="", pwd="";
    private Context ctx;
    byte[] outputBytes;
    private _config cfg;

    public HTTP_checkForumNewMessages(Context c, String u, String p) {
        this.uziv = u;
        this.pwd = p;
        this.ctx = c;
        cfg = new _config();
    }

    @Override
    protected String doInBackground(String... strings) {
        if(!uziv.isEmpty()) {
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader bufferedReader;
            String result;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("uziv", uziv)
                    .appendQueryParameter("hsl", pwd);

            String query = builder.build().getEncodedQuery();

            try {
                String link = cfg.getURL_isnewmsg();
                Log.d("HTTP_isnewmessage", link);

                url = new URL(link);
                con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestMethod("POST");

                Log.d("HTTP_isnewmessage", "zapisuju POST data = " + builder.toString());
                outputBytes = query.getBytes("UTF-8");
                OutputStream os = con.getOutputStream();
                os.write(outputBytes);
                os.close();
                int statusCode = con.getResponseCode();
                Log.d("HTTP_isnewmessage", "dostal jsem response = " + statusCode);

                /* 200 represents HTTP OK */
                if (statusCode == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    result = bufferedReader.readLine();
                    Log.d("HTTP_isnewmessage", "mam output a vracim uspesne result = " + result);
                    return result;
                }
                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null)
                    con.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //Log.d("HTTP_isnewmessage", "Wracim s = " + s);
        if((s != null) && (!s.isEmpty())) {
            if (s.equals("1")) {
                int notifyID = 1;
                String CHANNEL_ID = "efficenza.ac";
                CharSequence name = ctx.getString(R.string.channel_name);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                Intent pIntent = new Intent(ctx, forum.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, pIntent, PendingIntent.FLAG_ONE_SHOT);

                //Log.d("HTTP_isnewmessage", "rovna se 1 a zobrazuju notifikaci");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    Notification notification = new Notification.Builder(ctx)
                            .setContentTitle("Nová zpráva")
                            .setContentText("Na fóru Efficenzy je nová zpráva")
                            .setSmallIcon(R.drawable.effi)
                            .setChannelId(CHANNEL_ID)
                            .addAction(R.drawable.effi, "Zobraz fórum", pendingIntent)
                            .build();
                    NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.createNotificationChannel(mChannel);

                    mNotificationManager.notify(notifyID, notification);
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                            .setContentTitle(name)
                            .setContentText("Nová zpráva na fóru Efficenzy")
                            .setSmallIcon(R.drawable.effi)
                            .addAction(R.drawable.effi, "Zobraz fórum", pendingIntent);
                    builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(notifyID, builder.build());
                }
            }
        }
   }
}
