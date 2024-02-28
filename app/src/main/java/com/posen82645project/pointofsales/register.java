package com.posen82645project.pointofsales;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class register extends Activity {

    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    TextInputEditText name, uname, password, rpassword;
    Button register, login;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        name = findViewById(R.id.name);
        uname = findViewById(R.id.newUserName);
        password = findViewById(R.id.newPassword);
        rpassword = findViewById(R.id.repeatNewPassword);

        register = findViewById(R.id.registerButt);
        String pattern1 = "^[a-zA-Z0-9\\s]*$";
        String pattern2 = "^[a-z0-9]*$";



        register.setOnClickListener(view -> {

            String nameString=name.getText().toString().trim();
            String unameString=uname.getText().toString().trim();
            String passwordString=password.getText().toString().trim();
            String rpasswordString=rpassword.getText().toString().trim();

            if(!nameString.isEmpty() && !unameString.isEmpty() && !passwordString.isEmpty() && !rpasswordString.isEmpty()) {

                if (isPasswordValid(passwordString)){

                    if (unameString.matches(pattern2) && nameString.matches(pattern1)) {

                        if (password.getText().toString().equals(rpassword.getText().toString())) {
                            insertNewUser();
                        } else {
                            Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Name & username must be alphanumerics, username must be lowercase", Toast.LENGTH_SHORT).show();

                    }

            }else{
                    Toast.makeText(this,"Password must be at least 8 characters long and contain one lowercase letter, one uppercase letter, one digit, and one special character.", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(this, "Fill All", Toast.LENGTH_SHORT).show();
            }
        });//register button close
    }// end of On create State

    private void insertNewUser() {
        SQLiteDatabase database = openOrCreateDatabase("users", Context.MODE_PRIVATE,null);
        database.execSQL("create table if not exists userstable(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, username VARCHAR, password VARCHAR)");
        String statement1 ="SELECT username FROM userstable WHERE username=?";
        String[] selectionArgs={uname.getText().toString().trim()};
        Cursor cursor =database.rawQuery(statement1, selectionArgs);

        if (cursor.getCount()>0){
            Toast.makeText(this, "Try another username", Toast.LENGTH_SHORT).show();
            cursor.close();
        }
        else {
            String addNewUser= "INSERT INTO userstable (name, username, password)VALUES(?,?,?)";
            SQLiteStatement statement = database.compileStatement(addNewUser);
            statement.bindString(1, name.getText().toString());
            statement.bindString(2, uname.getText().toString());
            statement.bindString(3, password.getText().toString());
            statement.execute();

            Toast.makeText(this, "Success Registration", Toast.LENGTH_SHORT).show();
            cursor.close();
            database.close();
            Intent intent =new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();}

    }// close method - InsertNewUser()

    private boolean isPasswordValid(String password) {
        // Minimum 8 characters, at least one lowercase, one uppercase, one digit, and one special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }



}
