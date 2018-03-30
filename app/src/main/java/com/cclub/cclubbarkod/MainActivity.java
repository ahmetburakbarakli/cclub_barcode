package com.cclub.cclubbarkod;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText barcodeName    = findViewById(R.id.barcode_name);
        Button submit           = findViewById(R.id.submit);
        Spinner session_picker  = findViewById(R.id.session_picker);

        barcodeName.setText("");
        submit.setText("Gönder");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sessions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session_picker.setAdapter(adapter);
    }
}
