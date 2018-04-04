package com.cclub.cclubbarcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class RandomActivity extends AppCompatActivity {
    ArrayList<JsonObject> sessionList;

    Spinner sessionPicker;
    EditText editTextMinNumber;
    EditText editTextRandomCount;

    Button buttonRandomWithSession, buttonRandomWithAttend, buttonRandom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        sessionPicker = findViewById(R.id.session_picker);
        editTextMinNumber = findViewById(R.id.editTextMinNumber);
        editTextRandomCount = findViewById(R.id.editTextRandomCount);
        buttonRandom = findViewById(R.id.buttonRandom);
        buttonRandomWithSession = findViewById(R.id.buttonRandomWithSession);
        buttonRandomWithAttend = findViewById(R.id.buttonRandomWithAttend);

        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // /users/random?count=10
            }
        });

        buttonRandomWithSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // /users/random?count=10&sessionid=gfdgfdg
            }
        });

        buttonRandomWithAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // /users/random?count=10&minattend=2
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
                            sessionList = new ArrayList<>();
                            Log.d("sessions", "sessionlist");

                            ArrayList sessionNameList = new ArrayList();

                            JsonArray sessions = result.getAsJsonArray("sessions");

                            for(int i = 0; i< sessions.size(); i++) {
                                sessionList.add(sessions.get(i).getAsJsonObject());
                                sessionNameList.add(sessions.get(i).getAsJsonObject().get("description").getAsString());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RandomActivity.this, android.R.layout.simple_spinner_dropdown_item, sessionNameList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sessionPicker.setAdapter(adapter);
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });


    }
}
