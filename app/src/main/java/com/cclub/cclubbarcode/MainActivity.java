package com.cclub.cclubbarcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText  barcode;
    Button submit,users,stats;
    Spinner session_picker;

    ArrayList<JsonObject> sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //These are the elements used in main activity.
        barcode         = findViewById(R.id.barcode);
        stats           = findViewById(R.id.stats);
        users           = findViewById(R.id.users);
        submit          = findViewById(R.id.submit);
        session_picker  = findViewById(R.id.session_picker);

        //Setting the default texts.
        barcode.setHint("Buraya barkodu yazabilirsin!");
        submit.setText(R.string.submit);

        //Showing snackbar in order to give additional feedback to user.
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionList.size() > 0) {
                    JsonObject session = sessionList.get(session_picker.getSelectedItemPosition());
                    String sessionid = session.getAsJsonObject("_id").getAsString();
                    Log.d("selected session id", sessionid);

                    String barcodeText = barcode.getText().toString();

                    if(!sessionid.equals("") && !barcodeText.equals("")) {
                        Ion.with(MainActivity.this)
                                .load(Config.SERVER_URL + "/attends")
                                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                                .setBodyParameter("barcode", barcodeText)
                                .setBodyParameter("sessionid", sessionid)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if(e == null) {
                                            Log.d("Attending result", result.toString());

                                            Snackbar.make(findViewById(R.id.submit), "Başarıyla Gönderildi!", Snackbar.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
                    }


                }
            }
        });

        //Shows all registered users in the server.
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetUsersActivity.class);
                startActivity(intent);
            }
        });

        //Will show statistic among all users in the server.
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/*
                LayoutInflater inflater = getLayoutInflater();

                View v = inflater.inflate(R.layout.view_about, null);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
                dialog.show();*/
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });

        //Whenever edittext barcode is clicked, it request permission to access camera. After granting permission, barcode reader from ZXingScanner starts and search for barcode, and if it finds one return it as string.
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting permission
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                } else {
                    //Starting scanner.
                    Intent intent = new Intent(MainActivity.this, SimpleScannerActivity.class);
                    startActivityForResult(intent,1337);
                }
            }
        });

        //Getting sessions from server, and adding them to spinner. However, it is not fully functional yet.
        Ion.with(this)
                .load(Config.SERVER_URL + "/sessions")
                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            sessionList = new ArrayList<>();

                            ArrayList sessionNameList = new ArrayList();

                            JsonArray sessions_jsonarray = result.getAsJsonArray("sessions");
                            for(int i = 0; i<sessions_jsonarray.size(); i++) {
                                JsonElement session = sessions_jsonarray.get(i);

                                sessionList.add(session.getAsJsonObject());
                                sessionNameList.add(session.getAsJsonObject().get("description").getAsString());
                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,sessionNameList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            session_picker.setAdapter(adapter);
                        }
                    }
                });
    }

    //That's where the result from scanner is set as edittext's text.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String message=data.getStringExtra("MESSAGE");
            Log.i("Message is",message);
            barcode.setText(message);
        }
    }
}
