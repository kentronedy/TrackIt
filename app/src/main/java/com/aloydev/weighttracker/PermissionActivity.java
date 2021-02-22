package com.aloydev.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionActivity extends AppCompatActivity {

    private Button yesButton, noButton;
    private String username;
    private WeightDatabase weightDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        //Initialize variables
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        username = getIntent().getStringExtra("username");
        weightDB = new WeightDatabase(this);

        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));
    }

    //onClick for the yes button to change the value of the permission field of the users table in the database to 1, indicating permission
    public void yesPressed(View view) {
        boolean isUpdated = weightDB.updateUserData(username, 1);
        if(isUpdated){
            Context context = getApplicationContext();
            CharSequence message = "Notifications Now Allowed";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        } else {
            Context context = getApplicationContext();
            CharSequence message = "No permission change made.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }

        //Return to the Goal activity
        Intent intent = new Intent(this, GoalActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //onClick for the no button to change the value of the permission field in the users table in the database to 0, indicating no permission
    public void noPressed(View view) {
        boolean isUpdated = weightDB.updateUserData(username, 0);
        if(isUpdated){
            Context context = getApplicationContext();
            CharSequence message = "Notifications Not Allowed";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        } else {
            Context context = getApplicationContext();
            CharSequence message = "No permission change made.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
        //Return to the Goal activity
        Intent intent = new Intent(this, GoalActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
