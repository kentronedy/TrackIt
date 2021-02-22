package com.aloydev.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private Button submitNewWeight, cancelButton;
    private EditText enterNewWeight;
    private WeightDatabase weightDB;
    private String username, dateToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Initialize variables
        submitNewWeight = findViewById(R.id.submitNewWeight);
        cancelButton = findViewById(R.id.cancelButton);
        enterNewWeight = findViewById(R.id.enterNewWeight);
        weightDB = new WeightDatabase(this);
        username = getIntent().getStringExtra("username");
        dateToEdit = getIntent().getStringExtra("dateToEdit");

        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));


    }

    //onClick for the submit button to change the weight entered for the given date
    public void setNewWeight(View view) {

        //Validate the user input as a floating point number
        float weight;
        try {
            weight = Float.parseFloat(enterNewWeight.getText().toString());
        } catch(NumberFormatException E) {
            enterNewWeight.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }

        //Check that the update worked
        boolean isUpdated = weightDB.updateWeightData(dateToEdit, weight, username);
        if (isUpdated) {
            Context context = getApplicationContext();
            CharSequence message = "Updated.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        } else {
            Context context = getApplicationContext();
            CharSequence message = "Not Updated.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }

        //Return to the Main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //onClick for the cancel button to return to the Main activity with no changes
    public void cancelEdit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
