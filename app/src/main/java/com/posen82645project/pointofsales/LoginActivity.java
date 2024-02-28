package com.posen82645project.pointofsales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {





    TextInputEditText loginUname, passwordIn;
    Button loginButt, registerButt;

    String pattern1 ="^[a-zA-Z0-9]*$";
    String pattern2 ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginUname=findViewById(R.id.unameInput);
        passwordIn=findViewById(R.id.passwordBox);
        loginButt=findViewById(R.id.logInButt);
        registerButt=findViewById(R.id.registerButt);


        loginButt.setOnClickListener(view -> login_method());

        registerButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toreg= new Intent(getApplicationContext(), register.class);
                startActivity(toreg);

            }
        });
    }


    public void login_method(){
        String usernameT = loginUname.getText().toString().trim();
        String password = passwordIn.getText().toString();

        if (!usernameT.isEmpty() && !password.isEmpty()){
            if(usernameT.matches(pattern1) && password.matches(pattern2)){
                SQLiteDatabase db = openOrCreateDatabase("users", Context.MODE_PRIVATE, null );
                db.execSQL("create table if not exists userstable(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, username VARCHAR, password VARCHAR)");

                String statement1 = "SELECT username,password FROM userstable WHERE username=?";
                String[] selectionArgs={usernameT};

                Cursor cursor=db.rawQuery(statement1, selectionArgs);


                if (cursor.getCount()==0){
                    Toast.makeText(this, "Username does not exists", Toast.LENGTH_SHORT).show();
                    loginUname.setText("");
                    passwordIn.setText("");
                }else {
                    if (cursor.moveToFirst()){
                        String storedPassword = cursor.getString(cursor.getColumnIndex("password"));
                        if (password.equals(storedPassword)){
                            cursor.close();
                            db.close();
                            // Intent set send to
                            Intent sendTo = new Intent(getApplicationContext(), Main.class);
                            sendTo.putExtra("usernameT",usernameT);
                            startActivity(sendTo);
                            finish();
                        }else {Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show(); cursor.close();}
                    }else {Toast.makeText(this, "Something Error", Toast.LENGTH_SHORT).show(); cursor.close(); cursor.close();}

                }





            }else {
                Toast.makeText(this, "Username & password wrong format", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Username or password is empty", Toast.LENGTH_SHORT).show();
        }

    }// close of login method


//
//
//    public void login_method() {
//        String username = loginUname.getText().toString();
//        String password = passwordIn.getText().toString();
//
//        if (!username.isEmpty() || !password.isEmpty()) {
//
//            Toast.makeText(this, getString(R.string.user_name_or_password_is_empty), Toast.LENGTH_SHORT).show();}
//
//
//        else if (username.equals("John") && password.equals("123")) {
//            Intent i = new Intent(LoginActivity.this, Main.class);
//            startActivity(i);
//        }
//
//        else {
//            Toast.makeText(this, user_name_or_password_is_wrong,Toast.LENGTH_SHORT).show();}
//
//    }


}