package com.aloydev.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button submitWeight, submitSleep;

    private EditText enterWeight, enterSleep;

    private TextView selectedDate;

    private LinearLayout linearLayout;

    private BottomNavigationView bottomNav;

    private String username = "";

    private String sortChoice;

    private TrackItDataService trackItDataService;

    private int entryCount, permission;

    private String checkSleep;

    private String checkWeight;

    private Date todayDate;

    private double sleep, weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String extra_username = getIntent().getStringExtra("username");
        if(extra_username != null){
            username = extra_username;
        }

        //Have the user login if this is the first time the app has started up
        if(username.equals("")) {
            login();
        }



        //initialize variables
        submitWeight = findViewById(R.id.submitWeight);
        enterWeight = findViewById(R.id.enterWeight);
        submitSleep = findViewById(R.id.submitSleep);
        enterSleep = findViewById(R.id.enterSleep);
        linearLayout = findViewById(R.id.scrollLinearLayout);
        trackItDataService = new TrackItDataService(getApplicationContext());
        Spinner spinner = (Spinner) findViewById(R.id.sort_by_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
              R.array.sort_choices_array, R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortChoice = parent.getItemAtPosition(position).toString();
                linearLayout.removeAllViews();
                try {
                    populateScrollViewData();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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

        //Enable or disable the submit sleep button based on whether something is entered or not
        enterSleep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ensure that there is something to submit
                if(enterSleep.getText() == null || enterSleep.getText().toString().equals("")){
                    submitSleep.setEnabled(false);
                }else{
                    submitSleep.setEnabled(true);
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

    public void submitWeight(View view) throws JSONException {

        todayDate = Calendar.getInstance().getTime();

        //Ensure that the user has entered a valid floating point value
        try {
            weight = Double.parseDouble(enterWeight.getText().toString());
        } catch(NumberFormatException E) {
            enterWeight.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }


        trackItDataService.countEntry(todayDate, username, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "theres an issue";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                String countString = response.getString("Number of entries");
                MainActivity.this.entryCount = Integer.parseInt(countString);

                if(entryCount == 1){

                    trackItDataService.getEntry(todayDate, username, new TrackItDataService.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Context context = getApplicationContext();
                            CharSequence message1 = "theres an issue";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, message1, duration);
                            toast.show();
                        }

                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            MainActivity.this.checkWeight = response.getString("weight");
                        }
                    });

                    if(checkWeight.equals("0.0")){
                        trackItDataService.updateWeight(todayDate, username, weight, new TrackItDataService.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                enterWeight.setText("");
                                Context context = getApplicationContext();
                                CharSequence message1 = "theres an issue";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, message1, duration);
                                toast.show();
                            }

                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                enterWeight.setText("");
                                Context context = getApplicationContext();
                                CharSequence message = "Entry Updated";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, message, duration);
                                toast.show();
                                try {
                                    populateScrollViewData();
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        enterWeight.setText("");
                        Context context = getApplicationContext();
                        CharSequence message1 = "Weight Data Already Entered";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, message1, duration);
                        toast.show();
                    }

                } else {
                    trackItDataService.makeEntryWithWeight(todayDate, username, weight, new TrackItDataService.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            enterWeight.setText("");
                            Context context = getApplicationContext();
                            CharSequence message1 = "theres an issue";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, message1, duration);
                            toast.show();
                        }

                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            enterWeight.setText("");
                            Context context = getApplicationContext();
                            CharSequence message = "New Entry Created";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, message, duration);
                            toast.show();
                            try {
                                populateScrollViewData();
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });





        trackItDataService.getUserData(username, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "theres an issue";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                MainActivity.this.permission = Integer.parseInt(response.getString("permission"));
                if(permission == 1) {
                    Intent intent = new Intent(getApplicationContext(), TrackerIntentService.class);
                    intent.putExtra("username", username);
                    intent.putExtra("weight", weight);
                    startService(intent);
                }
            }
        });




    }

    public void submitSleep(View view) throws JSONException {

        todayDate = Calendar.getInstance().getTime();

        //Ensure that the user has entered a valid floating point value
        try {
            sleep = Double.parseDouble(enterSleep.getText().toString());
        } catch(NumberFormatException E) {
            enterSleep.setText("");
            Context context = getApplicationContext();
            CharSequence message = "You must enter a number.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return;
        }

        /**
         * To Implement: Functionality that will use the RESTful API to interact with the database
         * to check that the user has not already entered the daily weight for the day and then
         * enter the weight into the database corresponding to the username
         */


        trackItDataService.countEntry(todayDate, username, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                CharSequence message1 = "theres an issue";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message1, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response) throws JSONException {
                String countString = response.getString("Number of entries");
                MainActivity.this.entryCount = Integer.parseInt(countString);

                if(MainActivity.this.entryCount == 1){

                    trackItDataService.getEntry(MainActivity.this.todayDate, MainActivity.this.username, new TrackItDataService.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Context context = getApplicationContext();
                            CharSequence message1 = "theres an issue";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, message1, duration);
                            toast.show();
                        }

                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            MainActivity.this.checkSleep = response.getString("sleep");
                        }
                    });

                    if(MainActivity.this.checkSleep.equals("0.0")){
                        trackItDataService.updateSleep(MainActivity.this.todayDate, MainActivity.this.username, MainActivity.this.sleep, new TrackItDataService.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                MainActivity.this.enterSleep.setText("");
                                Context context = getApplicationContext();
                                CharSequence message1 = "theres an issue";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, message1, duration);
                                toast.show();
                            }

                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                MainActivity.this.enterSleep.setText("");
                                Context context = getApplicationContext();
                                CharSequence message = "Entry Updated";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, message, duration);
                                toast.show();
                                try {
                                    populateScrollViewData();
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        MainActivity.this.enterSleep.setText("");
                        Context context = getApplicationContext();
                        CharSequence message1 = "Sleep Data Already Entered";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, message1, duration);
                        toast.show();
                    }

                } else {
                    trackItDataService.makeEntryWithSleep(MainActivity.this.todayDate, MainActivity.this.username, MainActivity.this.sleep, new TrackItDataService.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            MainActivity.this.enterSleep.setText("");
                            Context context = getApplicationContext();
                            CharSequence message1 = "theres an issue";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, message1, duration);
                            toast.show();
                        }

                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            MainActivity.this.enterSleep.setText("");
                            Context context = getApplicationContext();
                            CharSequence message = "New Entry Created";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, message, duration);
                            toast.show();
                            try {
                                populateScrollViewData();
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });







    }


    // New method for displaying data to the user
    public void populateScrollViewData() throws JSONException, ParseException {

        trackItDataService.getEntries(username, new TrackItDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }

            @Override
            public void onResponse(JSONObject response){

                Context context = getApplicationContext();
                CharSequence message = response.toString();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, message, duration);
                toast.show();

                Iterator<String> iterator = response.keys();
                //HashMap for storing JSON data
                Map<Long, DataEntry> data = new HashMap<>();
                //Populate HashMap with the JSON data
                while (iterator.hasNext()) {
                    Log.i("While Loop:", "running");
                    Long key = Long.parseLong(iterator.next());

                    JSONObject innerData = null;
                    try{
                        innerData = response.getJSONObject(key.toString());
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(innerData.getString("date"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    double weight = 0;
                    try {
                        weight = Double.parseDouble(innerData.getString("weight"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    double sleep = 0;
                    try {
                        sleep = Double.parseDouble(innerData.getString("sleep"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DataEntry dataEntry = new DataEntry(date, weight, sleep);
                    data.put(key, dataEntry);
                }
                //ArrayList for storing the sorted data
                List<DataEntry> dataSorted = new ArrayList<>(data.values());

                context = getApplicationContext();
                message = MainActivity.this.sortChoice;
                toast = Toast.makeText(context, message, duration);
                toast.show();

                //Different options for sorting the data
                switch (MainActivity.this.sortChoice){
                    case "Date-New":
                        Collections.sort(dataSorted, new SortByDateHigh());
                        break;
                    case "Date-Old":
                        Collections.sort(dataSorted, new SortByDateLow());
                        break;
                    case "Weight-High":
                        Collections.sort(dataSorted, new SortByWeightHigh());
                        break;
                    case "Weight-Low":
                        Collections.sort(dataSorted, new SortByWeightLow());
                        break;
                    case "Sleep-High":
                        Collections.sort(dataSorted, new SortBySleepHigh());
                        break;
                    case "Sleep-Low":
                        Collections.sort(dataSorted, new SortBySleepLow());
                        break;

                }

                Log.i("number sorted:", Integer.toString(dataSorted.size()));

                //Create the TextViews to display the data for each data entry
                for(DataEntry de: dataSorted) {

                    Log.i("forLoop:", "running");
                    // Create TextView programmatically.
                    final TextView textView = new TextView(MainActivity.this);
                    textView.setId((int)de.getDate().getTime());
                    Log.i("id", Integer.toString(textView.getId()));
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    //Convert desired dp to px to set the padding of the TextView
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    int padding_in_dp = 3;
                    final float scale = getResources().getDisplayMetrics().density;
                    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                    textView.setPadding(0, padding_in_px, 0, padding_in_px);
                    String stringDate = format.format(de.getDate());
                    textView.setText("  " + stringDate + "  |  " + de.getWeight() + "lbs  |  " + de.getSleep() + "hrs");
                    Log.i("text", textView.getText().toString());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    textView.setBackgroundResource(R.drawable.menu_border);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //To Implement: makes the edit and delete button appear, need to send data to tell which entry (ex. the textview id)
                            String value = textView.getText().toString();
                            Pattern pattern = Pattern.compile("[0-9]{4}[-][0-9]{2}[-][0-9]{2}");
                            Matcher matcher = pattern.matcher(value);
                            String date = "";
                            if (matcher.find())
                            {
                                date = matcher.group(0);
                            }

                            Log.i("date", date);
                            Intent intent = new Intent(getApplicationContext(), EditOrDeleteActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("date", date);
                            startActivity(intent);
                        }
                    });

                    // Add TextView to LinearLayout
                    if (MainActivity.this.linearLayout != null) {
                        MainActivity.this.linearLayout.addView(textView);
                    }
                }
            }
        });

    }


}
