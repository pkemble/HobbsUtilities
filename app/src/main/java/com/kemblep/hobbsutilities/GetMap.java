package com.kemblep.hobbsutilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pete on 3/11/2015.
 */

public class GetMap extends AsyncTask<String, Void, Bitmap> {

    private final String TAG = GetMap.class.getName();

    @Override
    protected Bitmap doInBackground(String... mapUrl) {
        Bitmap bmp = null;
        //TODO station is a string list. Add in multiple station support
        URL url;

        HttpURLConnection huc;
        try {
            url = new URL(mapUrl[0]);
            //Log.d(TAG, "Opening connection to " + url);
            huc = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "Getting input stream from " + url);
            InputStream is = huc.getInputStream();
            Log.d(TAG, "Input stream received from " + url);
            bmp = BitmapFactory.decodeStream(is);
            //Log.d(TAG, "Done parsing stream from" + url);
            huc.disconnect();
            //Log.d(TAG, "Disconnected from " + url);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp;
    }
}
