package com.fhey.equestricard;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SeriesOverviewActivity extends AppCompatActivity implements GetCardOverviewBySet.AsyncResponse{
    private String series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_overview);

        Intent intent = getIntent();
        this.series = intent.getStringExtra("series");

        new GetCardOverviewBySet(this).execute(this.series);
        //getCardOverviewBySet.execute(this.series);
    }

    public void processFinish(String response) {
        TextView txtview = (TextView) findViewById(R.id.txt_series);
        txtview.setText(response);
    }
}

/**
 * Created by Fhey on 2016-11-03.
 */
class GetCardOverviewBySet extends AsyncTask<String, Void, String> {
    private String response;
    private AsyncResponse delegate = null;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public GetCardOverviewBySet(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected String doInBackground(String... set) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://www.ferrictorus.com/mlpapi1/cards" + "?query=set:\"" + set[0] + "\"");
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                response = readStream(in);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return response;
    }

    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        delegate.processFinish(response);
    }


// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
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