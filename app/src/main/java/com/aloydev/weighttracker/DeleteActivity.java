package com.aloydev.weighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {

    private Button delete, cancel;
    private WeightDatabase weightDB;
    private String username, dateToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        //Initialize variables
        delete = findViewById(R.id.deleteButton);
        cancel = findViewById(R.id.cancelButton);
        weightDB = new WeightDatabase(this);
        username = getIntent().getStringExtra("username");
        dateToDelete = getIntent().getStringExtra("dateToDelete");

        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));
    }

    //onClick for the delete button to delete the data entry
    public void sendDeleteSignal(View view) {
        //Delete the entry corresponding to the date passed to the activity
        weightDB.deleteWeightData(dateToDelete);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //onClick for the cancel button to return to the MainActivity with no changes
    public void sendCancelSignal(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}
