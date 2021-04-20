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
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class GoalActivity extends AppCompatActivity {

    private Button submitGoal, notifyMe;
    private EditText enterGoal;
    private BottomNavigationView bottomNav;

    private String username;

    private TrackItDataService trackItDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        //initialize variables
        submitGoal = findViewById(R.id.submitGoal);
        notifyMe = findViewById(R.id.notifyMe);
        enterGoal = findViewById(R.id.enterGoal);
        trackItDataService = new TrackItDataService(getApplicationContext());

        username = getIntent().getStringExtra("username");

        //Set the onNavigationItemSelectedListener for the bottom navbar
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

    //onClick for the submit button to update the goal field in the users table in the database
    public void submitGoal(View view) throws JSONException {

        //Check that the goal is a valid floating point number
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


        trackItDataService.setGoal(username, weightGoal, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                enterGoal.setText("");
                Context context = getApplicationContext();
                CharSequence message1 = "Could not update goal, try later.";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                enterGoal.setText("");
                Context context = getApplicationContext();
                CharSequence message1 = "Goal Set";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }
        });


    }

    //onClick for the notify me button to start the permission activity
    public void notifyMe(View view) {
        Intent intent = new Intent(this, PermissionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
