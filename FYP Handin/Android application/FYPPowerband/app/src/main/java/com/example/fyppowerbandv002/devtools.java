package com.example.fyppowerbandv002;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class devtools extends Activity{

    Button btnDeleteUser;
    Button btnLedTog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devtools);

        // Buttons
        btnDeleteUser = (Button) findViewById(R.id.btnDeleteUser);
        btnLedTog = (Button) findViewById(R.id.btnLedTog);

        // view development tools
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching c
                Intent i = new Intent(getApplicationContext(), delete_user.class);
                startActivity(i);

            }
        });

    }
}