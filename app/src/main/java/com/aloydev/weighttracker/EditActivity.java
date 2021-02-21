package com.aloydev.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

        submitNewWeight = findViewById(R.id.submitNewWeight);
        cancelButton = findViewById(R.id.cancelButton);
        enterNewWeight = findViewById(R.id.enterNewWeight);
        weightDB = new WeightDatabase(this);
        username = getIntent().getStringExtra("username");
        dateToEdit = getIntent().getStringExtra("dateToEdit");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));


    }

    public void setNewWeight(View view) {

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

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void cancelEdit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
