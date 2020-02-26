package com.example.fyppowerbandv002;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class send_notification extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputId;


    // url to create new product
    private static String url_set_user = "http://192.168.0.44/FYPPowerbandV002/setuserband.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    Button btnSendNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification_screen);

        // Edit Text
        inputId = (EditText) findViewById(R.id.inputId);

        // Create button
        btnSendNotification = (Button) findViewById(R.id.btnSendNotification);

//        // button click event
//        btnSendNotification.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                // creating new product in background thread
//                new SendNotification().execute(inputId.getText().toString());
//            }
//        });
        String s = "";
        try {
            btnSendNotification.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    new SendNotification().execute(inputId.getText().toString());
                }
            });
        } catch (Exception e) {
            s = e.getMessage();
        }
        int j = 0;
    }

    class SendNotification extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(send_notification.this);
            pDialog.setMessage("Sending notification..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            if(args.length != 1)
                return "";

            String userid = args[0];


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", userid));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_set_user,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }

}
