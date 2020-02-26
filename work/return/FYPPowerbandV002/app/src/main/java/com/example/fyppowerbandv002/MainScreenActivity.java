package com.example.fyppowerbandv002;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity{

    Button btnCreateUser;
    Button btnSetUserBand;
    Button btnGoToDev;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Buttons
        btnCreateUser = (Button) findViewById(R.id.btnCreateUser);
        btnSetUserBand = (Button) findViewById(R.id.btnSetUserBand);
        btnGoToDev = (Button) findViewById(R.id.btnGoToDev);

        // view products click event
        btnCreateUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), create_user.class);
                startActivity(i);

            }
        });

        // view products click event
        btnSetUserBand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), send_notification.class);
                startActivity(i);

            }
        });

        // view products click event
        btnGoToDev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), devtools.class);
                startActivity(i);

            }
        });


    }
}