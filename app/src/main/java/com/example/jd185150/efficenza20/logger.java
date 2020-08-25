package com.example.jd185150.efficenza20;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class logger {
    private Context ctx;
    private File log;
    private String path;

    public logger(Context c) {
        this.ctx = c;
        path = Environment.getExternalStorageDirectory() + File.separator  + "upload";
        File folder = new File(path);
        folder.mkdirs();
        Log.d("logger", "Vytvarim folder " + folder);
        log = new File(folder, "effi_log.txt");
    }

    public void Zapis(String message) {
        try
        {
            Log.d("logger", "Zkousim createNewFile");
            log.createNewFile();
            FileOutputStream fOut = new FileOutputStream(log);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            Log.d("logger", "Zapisuju: " + message);
            myOutWriter.append(message);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            Log.d("logger", "soubor zavren");
        }
        catch (IOException e)
        {
            Log.d("logger", "Se nepovedlo: " + e.toString());
        }
    }
}
