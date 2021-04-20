package com.aloydev.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PermissionActivity extends AppCompatActivity {

    private Button yesButton, noButton;
    private String username;
    private TrackItDataService trackItDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        //Initialize variables
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        username = getIntent().getStringExtra("username");
        trackItDataService = new TrackItDataService(getApplicationContext());

        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));
    }

    //onClick for the yes button to change the value of the permission field of the users table in the database to 1, indicating permission
    public void yesPressed(View view) throws JSONException {

        trackItDataService.setPermission(username, 1, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not change permissions, try later.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                Context context = getApplicationContext();
                CharSequence message1 = "Permission Allowed";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();

                //Return to the Goal activity
                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });


    }

    //onClick for the no button to change the value of the permission field in the users table in the database to 0, indicating no permission
    public void noPressed(View view) throws JSONException {

        trackItDataService.setPermission(username, 0, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not change permissions, try later.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                Context context = getApplicationContext();
                CharSequence message1 = "Permission Declined";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();

                //Return to the Goal activity
                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }

}
