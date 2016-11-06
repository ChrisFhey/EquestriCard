package com.fhey.equestricard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Shows an overview of cards for a specific series.
 *
 * Created by Fhey on 2016-11-03.
 */
public class SeriesOverviewActivity extends Activity implements GetCardOverviewBySet.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_overview);

        Intent intent = getIntent();
        String set = intent.getStringExtra("series");
        Context context = SeriesOverviewActivity.this;

        new GetCardOverviewBySet(this, context).execute(set);
    }

    /**
     * Send response data to the screen.
     *
     * @param response String, contains the response from the API
     */
    public void processFinish(String response) {
        JSONArray cardArray;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("data");

            JSONArray sortedData = getSortedList(data);
            JSONObject[] jsonObjects = new JSONObject[sortedData.length()];
            final ListView list = (ListView) SeriesOverviewActivity.this.findViewById(R.id.lst_Cards);

            for (int i = 0; i < sortedData.length(); i++) {
                JSONObject o = new JSONObject(sortedData.getString(i));
                jsonObjects[i] = o;
            }

            CardListAdapter cardAdapter = new CardListAdapter(this, R.layout.card_list_row, jsonObjects);
            list.setAdapter(cardAdapter);

            /**
             * OnItemClick listener for cardlistview.
             * Starts the CardDetailActivity for a card.
             */
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    try {
                        JSONObject listItem = (JSONObject) list.getItemAtPosition(pos);
                        String cardVersionGuid = listItem.getString("card_version_guid");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sort JSONArray
     *
     * @param array JsonArray
     *
     * @return JSONArray
     *
     * @throws JSONException
     */
    public static JSONArray getSortedList(JSONArray array) throws JSONException {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i));
        }

        Collections.sort(list, new sortByGUID());

        return new JSONArray(list);
    }
}

/**
 * Fetch Pony Cards based on their set in the background.
 * <p>
 * Created by Fhey on 2016-11-03.
 */
class GetCardOverviewBySet extends AsyncTask<String, Void, String> {
    private String response;
    private AsyncResponse delegate = null;
    private ProgressDialog dialog;
    private Context context;

    /**
     * AsyncResponse interface
     */
    interface AsyncResponse {
        void processFinish(String output);
    }

    /**
     * GetCardOverviewBySet Constructor.
     *
     * @param delegate AsyncResponse
     * @param context Context
     */
    GetCardOverviewBySet(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    /**
     * Show a progress dialog while fetching pony cards
     */
    protected void onPreExecute() {
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setMessage("Loading cards");
        this.dialog.show();
    }

    /**
     * Fetch pony cards in background.
     *
     * @param set String, set name of the cards we're fetching.
     * @return String
     */
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
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return response;
    }

    /**
     * Send response from the webservice to the processFinish function and hide the progress dialog.
     *
     * @param response Response from the API.
     */
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
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

/**
 * Sort JSONObjects by card_guid.
 */
class sortByGUID implements Comparator<JSONObject>{
    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {
        try {
            return lhs.getInt("card_guid") > rhs.getInt("card_guid") ? 1 : (lhs
                    .getInt("card_guid") < rhs.getInt("card_guid") ? -1 : 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }
}