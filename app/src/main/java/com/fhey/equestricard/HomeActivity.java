package com.fhey.equestricard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
