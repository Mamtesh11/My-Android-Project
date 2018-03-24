package com.example.coed.attendancebolo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "user";
    public static final String EXTRA_PASSWORD = "pass";
    Button button;
    EditText name, pass ;
    String TempName, TempPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.login);
        name = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);

        TempName = name.getText().toString();

        TempPass = pass.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, GetAndSendLocation.class);
                intent.putExtra(EXTRA_USERNAME, name.getText().toString());
                intent.putExtra(EXTRA_PASSWORD, pass.getText().toString());
                startActivity(intent);

            }
        });



    }
}
