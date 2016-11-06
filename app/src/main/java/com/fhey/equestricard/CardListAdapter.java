package com.fhey.equestricard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by Fhey on 2016-11-06.
 */

public class CardListAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private JSONObject[] values;

    public  CardListAdapter(Context context, int textViewResourceId, JSONObject[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.card_list_row, null);
        }

        JSONObject o = values[position];

        if (o != null) {
            try {
                TextView tt1 = (TextView) v.findViewById(R.id.txt_cardNumber);
                TextView tt2 = (TextView) v.findViewById(R.id.txt_cardName);

                if (tt1 != null) {
                    tt1.setText(o.getString("number"));
                }

                if (tt2 != null) {
                    tt2.setText(o.getString("fullname"));
                }

                v.setTag(o.getString("card_version_guid"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    public int getCount() {
        return values.length;
    }

    public JSONObject getItem(int pos) {
        return values[pos];
    }
}
