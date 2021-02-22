package com.aloydev.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button submitWeight, lastDelete, secondLastDelete, thirdLastDelete, fourthLastDelete, lastEdit, secondLastEdit, thirdLastEdit, fourthLastEdit;
    private TextView lastDate, secondLastDate, thirdLastDate, fourthLastDate, lastWeight, secondLastWeight, thirdLastWeight, fourthLastWeight;
    private EditText enterWeight;

    private BottomNavigationView bottomNav;

    private WeightDatabase weightDB;

    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        submitWeight = findViewById(R.id.submitWeight);
        lastDelete = findViewById(R.id.LastDelete);
        secondLastDelete = findViewById(R.id.secondLastDelete);
        thirdLastDelete = findViewById(R.id.thirdLastDelete);
        fourthLastDelete = findViewById(R.id.fourthLastDelete);
        lastEdit = findViewById(R.id.lastEdit);
        secondLastEdit = findViewById(R.id.secondLastEdit);
        thirdLastEdit = findViewById(R.id.thirdLastEdit);
        fourthLastEdit = findViewById(R.id.fourthLastEdit);
        enterWeight = findViewById(R.id.enterWeight);
        lastDate = findViewById(R.id.lastDate);
        secondLastDate = findViewById(R.id.secondLastDate);
        thirdLastDate = findViewById(R.id.thirdLastDate);
        fourthLastDate = findViewById(R.id.fourthLastDate);
        lastWeight = findViewById(R.id.lastWeight);
        secondLastWeight = findViewById(R.id.secondLastWeight);
        thirdLastWeight = findViewById(R.id.thirdLastWeight);
        fourthLastWeight = findViewById(R.id.fourthLastWeight);

        weightDB = new WeightDatabase(this);

        username = getIntent().getStringExtra("username");

        //Have the user login if this is the first time the app has started up
        if(username == null){
            login();
        }

        //Set the onNavigationItemSelectedListener for the bottom navbar
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_DailyWeight:
                        Context context = getApplicationContext();
                        CharSequence message = "Current Window";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, message, duration);
                        toast.show();
                        break;
                    case R.id.action_Goal:
                        Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        //Set the values of the grid
        showGrid();


        //Enable or disable the submit weight button based on whether something is entered or not
        enterWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ensure that there is something to submit
                if(enterWeight.getText() == null || enterWeight.getText().toString().equals("")){
                    submitWeight.setEnabled(false);
                }else{
                    submitWeight.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    //The login function will start the Login activity
    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void submitWeight(View view) {

        float weight;
        Date todayDate = Calendar.getInstance().getTime();

        //Ensure that the user has entered a valid floating point value
        try {
            weight = Float.parseFloat(enterWeight.getText().toString());
        } catch(NumberFormatException E) {
            enterWeight.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }

        //check that the user has not submitted the weight for the day already
        Cursor cursor = weightDB.readWeightData(todayDate, username);
        if(cursor.getCount() == 0){
            weightDB.createWeightData(todayDate,weight,username);
            Context context = getApplicationContext();
            CharSequence message = "Today's weight submitted";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            showGrid();
        } else {
            Context context = getApplicationContext();
            CharSequence message = "Today's weight has already been entered.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }

        //Start service to notify user if goal is met and user has given permission
        Cursor permCursor = weightDB.readUserData(username);
        permCursor.moveToNext();
        int permission = permCursor.getInt(3);
        if(permission == 1) {
            Intent intent = new Intent(this, TrackerIntentService.class);
            intent.putExtra("username", username);
            intent.putExtra("weight", weight);
            startService(intent);
        }


    }

    //showGrid sets the text of the TextViews in the GridView
    public void showGrid() {
        //show the dates and the weights or "no data" if there are not enough data entries
        Cursor cursor = weightDB.readWeightData(username);
        if(cursor.getCount() > 3) {
            cursor.moveToNext();
            lastDate.setText(cursor.getString(1));
            lastWeight.setText(cursor.getString(2) + " lbs");
            cursor.moveToNext();
            secondLastDate.setText(cursor.getString(1));
            secondLastWeight.setText(cursor.getString(2) + " lbs");
            cursor.moveToNext();
            thirdLastDate.setText(cursor.getString(1));
            thirdLastWeight.setText(cursor.getString(2) + " lbs");
            cursor.moveToNext();
            fourthLastDate.setText(cursor.getString(1));
            fourthLastWeight.setText(cursor.getString(2) + " lbs");
        } else if (cursor.getCount() == 3) {
            cursor.moveToNext();
            lastDate.setText(cursor.getString(1));
            lastWeight.setText(cursor.getString(2) + " lbs");
            cursor.moveToNext();
            secondLastDate.setText(cursor.getString(1));
            secondLastWeight.setText(cursor.getString(2) + " lbs");
            cursor.moveToNext();
            thirdLastDate.setText(cursor.getString(1));
            thirdLastWeight.setText(cursor.getString(2) + " lbs");
            fourthLastDate.setText("No Data");
            fourthLastWeight.setText("No Data");
        } else if (cursor.getCount() == 2) {
            cursor.moveToNext();
            lastDate.setText(cursor.getString(1));
            lastWeight.setText(cursor.getString(2) + " lbs");
            cursor.moveToNext();
            secondLastDate.setText(cursor.getString(1));
            secondLastWeight.setText(cursor.getString(2) + " lbs");
            thirdLastDate.setText("No Data");
            thirdLastWeight.setText("No Data");
            fourthLastDate.setText("No Data");
            fourthLastWeight.setText("No Data");
        } else if (cursor.getCount() == 1) {
            cursor.moveToNext();
            lastDate.setText(cursor.getString(1));
            lastWeight.setText(cursor.getString(2) + " lbs");
            secondLastDate.setText("No Data");
            secondLastWeight.setText("No Data");
            thirdLastDate.setText("No Data");
            thirdLastWeight.setText("No Data");
            fourthLastDate.setText("No Data");
            fourthLastWeight.setText("No Data");
        } else if (cursor.getCount() == 0) {
            lastDate.setText("No Data");
            lastWeight.setText("No Data");
            secondLastDate.setText("No Data");
            secondLastWeight.setText("No Data");
            thirdLastDate.setText("No Data");
            thirdLastWeight.setText("No Data");
            fourthLastDate.setText("No Data");
            fourthLastWeight.setText("No Data");
        }

    }

    //onClick to edit the most recent data entry
    public void editOnePressed(View view) {
        //Check that there is data to edit
        if(lastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to edit.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Edit activity
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToEdit", lastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to edit the second most recent data entry
    public void editTwoPressed(View view) {
        //Check that there is data to edit
        if(secondLastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to edit.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Edit activity
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToEdit", secondLastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to edit the third most recent data entry
    public void editThreePressed(View view) {
        //Check that there is data to edit
        if(thirdLastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to edit.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Edit activity
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToEdit", thirdLastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to edit the fourth most recent data entry
    public void editFourPressed(View view) {
        //Check that there is data to edit
        if(fourthLastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to edit.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Edit activity
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToEdit", fourthLastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to delete the most recent data entry
    public void deleteOnePressed(View view) {
        //Check that there is data to delete
        if(lastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to delete.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Delete activity
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToDelete", lastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to delete the second most recent data entry
    public void deleteTwoPressed(View view) {
        //Check that there is data to delete
        if(secondLastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to delete.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Delete activity
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToDelete", secondLastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to delete the third most recent data entry
    public void deleteThreePressed(View view) {
        //Check that there is data to delete
        if(thirdLastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to delete.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Delete activity
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToDelete", thirdLastDate.getText().toString());
        startActivity(intent);
    }

    //onClick to delete the fourth most recent data entry
    public void deleteFourPressed(View view) {
        //Check that there is data to delete
        if(fourthLastDate.getText().toString().equals("No Data")){
            Context context = getApplicationContext();
            CharSequence message = "Nothing to delete.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }
        //Start the Delete activity
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("dateToDelete", fourthLastDate.getText().toString());
        startActivity(intent);
    }

}
