package com.aloydev.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private Button submitNewWeight, submitNewSleep, cancelButton;
    private EditText enterNewWeight, enterNewSleep;
    private String username, dateToEdit;
    private TrackItDataService trackItDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Initialize variables
        submitNewWeight = findViewById(R.id.edit);
        submitNewSleep = findViewById(R.id.delete);
        cancelButton = findViewById(R.id.cancel);
        enterNewWeight = findViewById(R.id.enterNewWeight);
        enterNewSleep = findViewById(R.id.enterNewSleep);
        username = getIntent().getStringExtra("username");
        dateToEdit = getIntent().getStringExtra("dateToEdit");
        trackItDataService = new TrackItDataService(getApplicationContext());

        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .4));

        //Enable or disable the submit weight button based on whether something is entered or not
        enterNewWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ensure that there is something to submit
                if(enterNewWeight.getText() == null || enterNewWeight.getText().toString().equals("")){
                    submitNewWeight.setEnabled(false);
                }else{
                    submitNewWeight.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        //Enable or disable the submit sleep button based on whether something is entered or not
        enterNewSleep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ensure that there is something to submit
                if(enterNewSleep.getText() == null || enterNewSleep.getText().toString().equals("")){
                    submitNewSleep.setEnabled(false);
                }else{
                    submitNewSleep.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

    }

    //onClick for the submit button to change the weight entered for the given date
    public void setNewWeight(View view) throws ParseException, JSONException {

        //Validate the user input as a floating point number
        double weight;
        try {
            weight = Double.parseDouble(enterNewWeight.getText().toString());
        } catch(NumberFormatException E) {
            enterNewWeight.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(dateToEdit);

        trackItDataService.updateWeight(date, username, weight, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not update entry, try later.";
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
                CharSequence message1 = "Entry Updated";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    public void setNewSleep(View view) throws ParseException, JSONException {

        //Validate the user input as a floating point number
        double sleep;
        try {
            sleep = Double.parseDouble(enterNewSleep.getText().toString());
        } catch(NumberFormatException E) {
            enterNewSleep.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(dateToEdit);

        trackItDataService.updateSleep(date, username, sleep, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "Could not update entry, try later.";
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
                CharSequence message1 = "Entry Updated";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }

    //onClick for the cancel button to return to the Main activity with no changes
    public void cancelEdit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
