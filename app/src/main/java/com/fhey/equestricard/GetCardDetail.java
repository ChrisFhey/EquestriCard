package com.fhey.equestricard;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Fhey on 2016-11-06.
 */

public class GetCardDetail extends AsyncTask<String, Void, String> {
    private String response;
    private AsyncResponse delegate = null;
    private Context context;

    GetCardDetail (AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    protected String doInBackground (String... cardVersionGuid) {
        URL url;
        HttpURLConnection urlConnection = null;


        try {
            url = new URL("http://www.ferrictorus.com/mlpapi1/cards/" + cardVersionGuid[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                response = readStream(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        delegate.processFinish(response);
    }

    /**
     * Convert InputStream to String.
     *
     * @param in Inputstream.
     * @return String
     */
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response.toString();
    }
}
