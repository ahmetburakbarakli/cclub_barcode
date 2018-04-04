package com.cclub.cclubbarcode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class StatsActivity extends Activity {
    ArrayList<String> usersList;

    TextView textViewStats;

    // We are getting users registered to server and showing them in a listview.
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_stats);

        textViewStats = findViewById(R.id.textViewStats);

        Ion.with(this)
                .load(Config.SERVER_URL + "/attends/statics")
                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            textViewStats.setText(result.toString());
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });
    }
}