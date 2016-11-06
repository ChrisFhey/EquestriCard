package com.fhey.equestricard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class CardDetailActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String cardVersionGuid = intent.getStringExtra("cardVersionGuid");
        String fullName = intent.getStringExtra("fullName");
        TextView txt_versionGuid = (TextView) findViewById(R.id.txt_GUID);
        txt_versionGuid.setText(cardVersionGuid);

        Context context = CardDetailActivity.this;

        this.setTitle(fullName);

        new GetCardDetail(this, context).execute(cardVersionGuid);
    }

    public void processFinish(String response) {
    }

}
