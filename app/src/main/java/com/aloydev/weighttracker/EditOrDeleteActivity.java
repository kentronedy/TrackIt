package com.aloydev.weighttracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditOrDeleteActivity extends AppCompatActivity {

    private Button edit, delete, cancel;
    private String username, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete);

        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        cancel = findViewById(R.id.cancel);
        username = getIntent().getStringExtra("username");
        date = getIntent().getStringExtra("date");

        Log.i("date", date);
        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));
    }

    public void delete(View view){
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToDelete", date);
        startActivity(intent);
    }

    public void edit(View view){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToEdit", date);
        startActivity(intent);
    }

    public void cancel(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
