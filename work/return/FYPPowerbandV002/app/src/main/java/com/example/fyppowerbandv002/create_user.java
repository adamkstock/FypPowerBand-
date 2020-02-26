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

public class create_user extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputForename;
    EditText inputSurname;

    // url to create new product
    private static String url_create_user = "http://192.168.0.44/FYPPowerbandV002/create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_screen);

        // Edit Text
        inputForename = (EditText) findViewById(R.id.inputForename);
        inputSurname = (EditText) findViewById(R.id.inputSurname);


        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateUser);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewUser().execute(inputForename.getText().toString(), inputSurname.getText().toString());
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(create_user.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            if(args.length != 2)
                return "";

            String forename = args[0];
            String surname = args[1];


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("forename", forename));
            params.add(new BasicNameValuePair("surname", surname));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
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
