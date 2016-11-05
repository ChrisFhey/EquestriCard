package com.fhey.equestricard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SeriesSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_select);
    }

    protected void seriesOverview(View view) {
        Intent intent = new Intent(this, SeriesOverviewActivity.class);
        intent.putExtra("series", view.getTag().toString());

        startActivity(intent);
    }
}
