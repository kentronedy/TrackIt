package com.aloydev.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GoalActivity extends AppCompatActivity {

    private Button submitGoal, notifyMe;
    private EditText enterGoal;
    private BottomNavigationView bottomNav;
    private WeightDatabase weightDB;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        submitGoal = findViewById(R.id.submitGoal);
        notifyMe = findViewById(R.id.notifyMe);
        enterGoal = findViewById(R.id.enterGoal);
        weightDB = new WeightDatabase(this);

        username = getIntent().getStringExtra("username");

        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_DailyWeight:
                        Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        break;
                    case R.id.action_Goal:
                        Context context = getApplicationContext();
                        CharSequence message = "Current Window";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, message, duration);
                        toast.show();
                        break;
                }
                return true;
            }
        });

        //Enable or disable the submit goal button based on whether something is entered or not
        enterGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ensure that there is something to submit
                if(enterGoal.getText() == null || enterGoal.getText().toString().equals("")){
                    submitGoal.setEnabled(false);
                }else{
                    submitGoal.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void submitGoal(View view) {

        float weightGoal;
        try {
            weightGoal = Float.parseFloat(enterGoal.getText().toString());
        } catch(NumberFormatException E) {
            enterGoal.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }

        boolean goalUpdated = weightDB.updateUserData(username, weightGoal);
        if(goalUpdated) {
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
    }

    public void notifyMe(View view) {
        Intent intent = new Intent(this, PermissionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
