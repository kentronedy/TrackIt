package com.aloydev.weighttracker;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button signIn;
    private Button createAccount;

    private WeightDatabase weightDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize variables
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        createAccount = findViewById(R.id.createAccount);

        weightDB = new WeightDatabase(this);

    }

    //onClick for the sign in button checks the values against the ones stored in the database
    public void checkLoginCredentials(View view){
        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();

        Cursor cursor = weightDB.readUserData(enteredUsername);

        //Check that the username exists
        if(cursor.getCount() == 0) {
            username.setText("");
            password.setText("");
            Context context = getApplicationContext();
            CharSequence message = "Account does not exist, create account.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        } else {
            cursor.moveToLast();
            //Validate that the password is correct
            if(enteredPassword.equals(cursor.getString(1))){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username", enteredUsername);
                startActivity(intent);
            } else {
                username.setText("");
                password.setText("");
                Context context = getApplicationContext();
                CharSequence message = "Incorrect Password";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }
        }


    }

    //onClick for the create account button, creates a new record in the users table in the database
    public void createNewAccount(View view){
        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();

        boolean result = weightDB.createUserData(enteredUsername, enteredPassword);

        if(result){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", enteredUsername);
            startActivity(intent);
        }else{
            username.setText("");
            password.setText("");

            Context context = getApplicationContext();
            CharSequence message = "Account already exists, sign in.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }
}
