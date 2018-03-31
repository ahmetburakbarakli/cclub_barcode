package com.cclub.cclubbarcode;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //These are the elements used in main activity
        EditText barcodeName    = findViewById(R.id.barcode_name);
        Button submit           = findViewById(R.id.submit);
        Spinner session_picker  = findViewById(R.id.session_picker);

        //Setting the default texts
        barcodeName.setHint("Buraya barkodu yazabilirsin!");
        submit.setText("Gönder");   

        //Getting the potential options. We are getting this from local xml file, but we will have gotten from internet by 14 April 2018
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sessions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session_picker.setAdapter(adapter);

        //Showing snackbar in order to give additional feedback to user
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
    }
}
