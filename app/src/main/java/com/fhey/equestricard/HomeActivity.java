package com.fhey.equestricard;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = 40 * 1024 * 1024; // 40 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i("Error", "HTTP response cache installation failed:" + e);
        }
    }

    public void openSeriesSelect(View view) {
        Intent intent = new Intent(this, SeriesSelectActivity.class);
        startActivity(intent);
    }

    public void openDeckList(View view) {
        Intent intent = new Intent(this, DeckListActivity.class);
        startActivity(intent);
    }
}
