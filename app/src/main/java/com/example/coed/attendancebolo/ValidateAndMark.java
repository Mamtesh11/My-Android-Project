package com.example.coed.attendancebolo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ValidateAndMark extends AppCompatActivity {

    String username;
    String pass;
    String longitude;
    String latitude;
    Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_and_mark);

        Intent intent = getIntent();
        username = intent.getStringExtra(GetAndSendLocation.EXTRA_USERNAME);
        pass = intent.getStringExtra(GetAndSendLocation.EXTRA_PASS);
        longitude = intent.getStringExtra(GetAndSendLocation.EXTRA_LONGI);
        latitude = intent.getStringExtra(GetAndSendLocation.EXTRA_LATI);

        validate = (Button)findViewById(R.id.validatnMark);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ValidateAndMark.this,username +  longitude.toString() +  latitude.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
