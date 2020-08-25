package com.example.jd185150.efficenza20;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class forumInsertMessage extends AsyncTask<String, Void, String> {
    private Context context;
    TextView tV;
    String mesidz=null;
    String userse, hsl;
    private _config cfg;

    public forumInsertMessage(Context context, String juzr, String hsl) {
        this.context = context;
        cfg = new _config();
        tV = (TextView) ((Activity) context).findViewById(R.id.forumtext);
        mesidz = tV.getText().toString();
        this.userse = juzr;
        this.hsl = hsl;
        Log.d("Async forumInsert", "uzivatel =" + userse);
        Log.d("Async forumInsert", "heslo =" + hsl);
    }

    @Override
    protected String doInBackground(String... strings) {
        String phpPOST = null;
        try {
            // deviceSIG is defined in another part of the code, and is a text string of values.
            // below, the contents of deviceSIG are encoded and populated into the phpPOST variable for POSTing.
            // the LACK of encoding was one reason my previous POST attempts failed.
            phpPOST = URLEncoder.encode("@" + mesidz, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            String link = cfg.getURL_forumInsertmsg();
            Log.d("NAMESPACE", link);
            Log.d("++++MESSAGE", phpPOST);
            //String completePOST = "hsl=" + hsl + "&uziv=" + userse + "&values=" + phpPOST;

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("uziv", userse);
            postDataParams.put("hsl", hsl);
            postDataParams.put("values", phpPOST);

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // "(true)" here allows the POST action to happen.
            connection.setDoOutput(true);
            // I will use this to get a string response from the PHP script, using InputStream below.
            connection.setDoInput(true);

            // set the request method.
            connection.setRequestMethod("POST");
            // This is the point where you'll know if the connection was
            // successfully established. If an I/O error occurs while creating
            // the output stream, you'll see an IOException.
            //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            // write the formatted string to the connection.
            // "values=" is a variable name that is passed to the PHP script.
            // The "=" MUST remain on the Android side, and MUST be removed on the PHP side.
            // the LACK of formatting was another reason my previous POST attempts failed.
            //writer.write(completePOST);
            //Log.d("complete post", completePOST);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            // Close the output stream and release any system resources associated with this stream.
            // Only the outputStream is closed at this point, not the actual connection.
            writer.flush();
            writer.close();
            os.close();

            //if there is a response code AND that response code is 200 OK, do stuff in the first if block
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // OK
                BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                Log.d("RESULT RETURN++++++++", sb.toString());
                return sb.toString();
                // otherwise, if any other status code is returned, or no status
                // code is returned, do stuff in the else block
            } else {
                // Server returned HTTP error code.
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    @Override
    protected void onPostExecute(String result) {
        tV.setText("");
    }
}
