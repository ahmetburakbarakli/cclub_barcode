package com.cclub.cclubbarcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText  barcode;
    Button submit,users,stats;
    Spinner session_picker;
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
        submit.setText("Gönder");

        //Getting the potential options. We are getting this from local xml file, but we will have gotten from internet by 14 April 2018.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sessions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session_picker.setAdapter(adapter);

        //Showing snackbar in order to give additional feedback to user.
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rn = new Random();
                if(rn.nextBoolean()) {
                    Snackbar.make(findViewById(R.id.submit), "Başarıyla Gönderildi!", Snackbar.LENGTH_LONG)
                            .show();
                }
                else {
                    Snackbar.make(findViewById(R.id.submit), "Başaramadın.", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        //Shows all registered users in the server.
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetUsersActivity.class);
                startActivityForResult(intent,1337);
            }
        });

        //Will show statistic among all users in the server.
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SimpleScannerActivity.class);
                startActivityForResult(intent,1337);
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
                .load("https://cclubbarcode.herokuapp.com/sessions")
                .setHeader("cclub","hallederiz")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray sessions_jsonarray = result.getAsJsonArray("sessions");
                        ArrayList<JsonObject> sessionList = new ArrayList<>();
                        for(int i = 0; i<sessions_jsonarray.size(); i++) {
                            JsonElement user = sessions_jsonarray.get(i);

                            sessionList.add(user.getAsJsonObject());
                        }

                        SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, android.R.layout.simple_list_item_1, sessionList);
                        session_picker.setAdapter(adapter);
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
    
    //TODO Ozan buraya açıklama yazar mısın? ty.
    public static class SpinnerAdapter extends ArrayAdapter<JsonObject> {
        ArrayList<JsonObject> objects = new ArrayList<>();
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            ((TextView) v.findViewById(android.R.id.text1)).setText(objects.get(position).getAsJsonObject().get("description").getAsString());

            v.findViewById(android.R.id.text1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("text", objects.get(position).getAsJsonObject().get("_id").getAsString());
                }
            });
            return v;
        }

        public SpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<JsonObject> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            this.objects = objects;
        }
    }
}
