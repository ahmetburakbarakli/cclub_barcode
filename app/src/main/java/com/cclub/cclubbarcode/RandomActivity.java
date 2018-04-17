package com.cclub.cclubbarcode;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Random;

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

        final FutureCallback<JsonObject> randomObject = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if(e == null) {
                    LayoutInflater inflater = getLayoutInflater();

                    View v = inflater.inflate(R.layout.dialog_random_result, null);

                    final AlertDialog.Builder dialog = new AlertDialog.Builder(RandomActivity.this);
                    dialog.create();
                    dialog.setView(v);

                    dialog.setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int which) {

                        }
                    });
                    dialog.setNegativeButton("Kapat but in different button.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int which) {

                        }
                    });
                    dialog.show();

                    ArrayList<String> usersList = new ArrayList<>();

                    JsonArray users_jsonarray = result.getAsJsonArray("users");
                    for(int i = 0; i<users_jsonarray.size(); i++) {
                        JsonObject user = users_jsonarray.get(i).getAsJsonObject();

                        usersList.add(user.get("name").getAsString() + " " +
                                user.get("surname").getAsString() + " => " + user.get("barcode").getAsString());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(RandomActivity.this, android.R.layout.simple_list_item_1, usersList);
                    ((ListView) v.findViewById(R.id.listView)).setAdapter(adapter);
                }
                else {
                    e.printStackTrace();

                    Snackbar.make(findViewById(R.id.submit), "Başaramadın. (Sistemsel Hata)", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        };

        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextRandomCount.getText().toString().equals("")) {
                    int count = Integer.parseInt(editTextRandomCount.getText().toString());

                    Ion.with(RandomActivity.this)
                            .load(Config.SERVER_URL + "/users/random?count=" + count)
                            .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                            .asJsonObject()
                            .setCallback(randomObject);
                }
            }
        });

        buttonRandomWithSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject session = sessionList.get(sessionPicker.getSelectedItemPosition());
                String sessionid = session.get("_id").getAsString();

                if(!editTextRandomCount.getText().toString().equals("") && !sessionid.equals("")) {
                    int count = Integer.parseInt(editTextRandomCount.getText().toString());

                    Ion.with(RandomActivity.this)
                            .load(Config.SERVER_URL + "/users/random?count=" + count + "&sessionid=" + sessionid)
                            .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                            .asJsonObject()
                            .setCallback(randomObject);
                }
            }
        });

        buttonRandomWithAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextRandomCount.getText().toString().equals("") && !editTextMinNumber.getText().toString().equals("")) {
                    int count = Integer.parseInt(editTextRandomCount.getText().toString());
                    int minAttendCount = Integer.parseInt(editTextMinNumber.getText().toString());

                    Ion.with(RandomActivity.this)
                            .load(Config.SERVER_URL + "/users/random?count=" + count + "&minattend=" + minAttendCount)
                            .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                            .asJsonObject()
                            .setCallback(randomObject);
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
