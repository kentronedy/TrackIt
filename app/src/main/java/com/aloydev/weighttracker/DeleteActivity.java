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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeleteActivity extends AppCompatActivity {

    private Button delete, cancel;
    private String username, dateToDelete;
    private TrackItDataService trackItDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        //Initialize variables
        delete = findViewById(R.id.deleteButton);
        cancel = findViewById(R.id.cancel);
        username = getIntent().getStringExtra("username");
        dateToDelete = getIntent().getStringExtra("dateToDelete");
        trackItDataService = new TrackItDataService(getApplicationContext());

        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));
    }

    //onClick for the delete button to delete the data entry
    public void sendDeleteSignal(View view) throws ParseException, JSONException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(dateToDelete);

        trackItDataService.deleteEntry(date, username, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not delete entry, try later.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                Context context = getApplicationContext();
                CharSequence message1 = "Entry Deleted";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }

    //onClick for the cancel button to return to the MainActivity with no changes
    public void sendCancelSignal(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}
