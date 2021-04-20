package com.aloydev.weighttracker;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button signIn;
    private Button createAccount;

    private TrackItDataService trackItDataService;

    private String checkResponse, checkUsername, enteredUsername, enteredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize variables
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        createAccount = findViewById(R.id.createAccount);
        trackItDataService = new TrackItDataService(getApplicationContext());

    }

    //onClick for the sign in button checks the values against the ones stored in the database
    public void checkLoginCredentials(View view) throws JSONException {
        enteredUsername = username.getText().toString();
        enteredPassword = password.getText().toString();



        trackItDataService.loginUser(enteredUsername, enteredPassword, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not log on, retry.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                LoginActivity.this.checkResponse = response.getString("message");
                if(LoginActivity.this.checkResponse.equals("logged in successfully")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("username", LoginActivity.this.enteredUsername);
                    startActivity(intent);
                }
            }
        });




    }

    //onClick for the create account button, creates a new record in the users table in the database
    public void createNewAccount(View view) throws JSONException {
        enteredUsername = username.getText().toString();
        enteredPassword = password.getText().toString();


        trackItDataService.registerUser(enteredUsername, enteredPassword, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not register, retry.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                LoginActivity.this.checkUsername = response.getString("username");
                if(LoginActivity.this.checkUsername.equals(LoginActivity.this.enteredUsername)){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("username", LoginActivity.this.enteredUsername);
                    startActivity(intent);
                }
            }
        });





    }
}
