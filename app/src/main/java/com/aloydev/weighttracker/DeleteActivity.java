package com.aloydev.weighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {

    private Button delete, cancel;
    private WeightDatabase weightDB;
    private String username, dateToDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        delete = findViewById(R.id.deleteButton);
        cancel = findViewById(R.id.cancelButton);
        weightDB = new WeightDatabase(this);
        username = getIntent().getStringExtra("username");
        dateToDelete = getIntent().getStringExtra("dateToDelete");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));
    }

    public void sendDeleteSignal(View view) {
        weightDB.deleteWeightData(dateToDelete);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void sendCancelSignal(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}
