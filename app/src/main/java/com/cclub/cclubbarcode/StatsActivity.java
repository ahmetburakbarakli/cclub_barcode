package com.cclub.cclubbarcode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class StatsActivity extends AppCompatActivity {
    ArrayList<String> usersList;

    TextView textViewStats1;
    TextView textViewStats2;
    TextView textViewStats3;

    // We are getting users registered to server and showing them in a listview.
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_stats);

        textViewStats1 = findViewById(R.id.textViewStats1);
        textViewStats2 = findViewById(R.id.textViewStats2);
        textViewStats3 = findViewById(R.id.textViewStats3);

        Ion.with(this)
                .load(Config.SERVER_URL + "/attends/statics")
                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            String text = "";
                            JsonObject statics = result.get("statics").getAsJsonObject();

                            Set<Map.Entry<String, JsonElement>> staticsSet =  statics.entrySet();

                            for(Map.Entry<String,JsonElement> entry : staticsSet){

                                text += entry.getKey() + " oturuma katılanların yüzdesi = " + entry.getValue().getAsJsonObject().get("percentage").getAsInt() + "%, sayısı = " +
                                        entry.getValue().getAsJsonObject().get("count").getAsInt() +  "\n";
                            }


                            textViewStats1.setText(text);
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });

        Ion.with(this)
                .load(Config.SERVER_URL + "/sessions")
                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            String text = "";
                            JsonArray sessions = result.get("sessions").getAsJsonArray();

                            for(int i =0; i < sessions.size(); i++) {
                                text += sessions.get(i).getAsJsonObject().get("name").getAsString() + " a katılanların sayısı = "
                                        + sessions.get(i).getAsJsonObject().get("attendcount").getAsInt() + "\n";
                            }


                            textViewStats2.setText(text);
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });

        Ion.with(this)
                .load(Config.SERVER_URL + "/users/count")
                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            textViewStats3.setText("Toplam kişi sayısı: " + result.get("usercount").getAsString());
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });
    }
}