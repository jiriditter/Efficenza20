package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/*
    smajlici ve velikosti 15x15

 */

public class fetchForum extends AsyncTask<String, Void, String> {
    private Context context;
    LinearLayout cV;
    byte[] outputBytes;
    String usr="", pwd="";
    decodeUnicodeChars dcUCh;
    private _config cfg;

    public fetchForum(Context context, String u, String p) {
        this.context = context;
        this.usr = u;
        this.pwd = p;
        cfg = new _config();
        cV = (LinearLayout) ((Activity) context).findViewById(R.id.vypis);
        cV.removeAllViewsInLayout();
        dcUCh = new decodeUnicodeChars();
    }

    @Override
    protected String doInBackground(String... params) {
        //fetch latest forum messages
        Log.d("fetchForum", "Contacting URL... forum@" + params);
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader bufferedReader;
        String result;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("uziv", usr)
                .appendQueryParameter("hsl", pwd);

        String query = builder.build().getEncodedQuery();

        try {
            String link = cfg.getURL_forum();
            Log.d("fetchForum", link);

            url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestMethod("POST");

            Log.d("fetchForum", "zapisuju POST data");
            outputBytes = query.getBytes("UTF-8");
            OutputStream os = con.getOutputStream();
            os.write(outputBytes);
            os.close();
            int statusCode = con.getResponseCode();
            Log.d("fetchForum", "dostal jsem response = " + statusCode);

            /* 200 represents HTTP OK */
            if (statusCode == HttpsURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                Log.d("fetchForum", "mam output a vracim uspesne result = " + result);
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
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONArray jsonArray = null;
        if(result != null && !result.isEmpty()) {
            try {
                result = dcUCh.giveMeDecodedMessage(URLEncoder.encode(result));
                jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                String lastID = "0";

                for (int i = 0; i < length; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //get last ID from forum messages
                    if (i == 0) lastID = obj.getString("id");
                    String user = obj.getString("autor");
                    String cas = obj.getString("timer"); //103539,28032017
                    String fid = obj.getString("id");
                    Log.d("_parsuju GETCAS", cas);
                    String message = obj.getString("vyplod");
                    Log.d("_parsuju VYPLOD", message);

                    String[] parts = cas.split(",");
                    String rok = parts[1].substring(4);
                    Log.d("_parsuju rok", rok);
                    String mesic = parts[1].substring(2, 4);
                    Log.d("_parsuju mesic", mesic);
                    String den = parts[1].substring(0, 2);
                    Log.d("_parsuju den", den);

                    String hodina = parts[0].substring(0, 2);
                    Log.d("_parsuju hodinu", hodina);
                    String minuta = parts[0].substring(2, 4);
                    Log.d("_parsuju minutu", minuta);
                    String cas_modif = den + "." + mesic + "." + rok + " " + hodina + ":" + minuta;

                    Log.d("ComposeMessage", "user="+user+", cas_modif="+cas_modif+", message="+message);
                    ComposeMessage(user, cas_modif, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            ComposeMessage("", "", "Nezdařilo se mi načíst data :( Zkus to později nebo zkontroluj signál");
        }
    }

    public void ComposeMessage(final String user, String cas, String message) {
        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        ll.setLayoutParams(params);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.kruh));
        cV.addView(ll);
        Log.d("Composemessage", "message=" + message);

        //sublinear layout - user + timestamp
        LinearLayout ll2 = new LinearLayout(context);
        LinearLayout.LayoutParams paramsll2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //ll2.setWeightSum(1);
        ll2.setLayoutParams(paramsll2);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(ll2);

        //user
        TextView tv1 = new TextView(context);
        LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,0.7f);
        //params.setMargins(left, top, right, bottom);
        tvparams.setMargins(0, 0, 5, 0);

        //handle the text
        String napisto = user + ", " + cas + "\n";
        SpannableString spanString = new SpannableString(napisto);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

        //nahrad znaky obrazkama
        String mesidz = "<b>" + napisto + ": </b>" +  "<hr />";
        if(message.substring(0,1).contains("@")) {
            SpannableStringBuilder ss = new SpannableStringBuilder("@");
            Drawable d1 = context.getResources().getDrawable(R.drawable.mobile);
            d1.setBounds(0, 0, d1.getIntrinsicWidth(), d1.getIntrinsicHeight());
            ImageSpan spn = new ImageSpan(d1, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(spn, 0, 1,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv1.setText(ss);
            tv1.append(Html.fromHtml(mesidz));
            //Html.fromHtml((String) htmlCode).toString();
        } else tv1.setText(Html.fromHtml(mesidz));
        SpannableStringBuilder sP = dcUCh.RiplejsEmoji(context, message);
        tv1.append(sP);

        //tv1.append(ss);
        tv1.setLayoutParams(tvparams);
        tv1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tvbordel));
        ll2.addView(tv1);
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}