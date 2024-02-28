package com.posen82645project.pointofsales;

import static com.posen82645project.pointofsales.R.string.user_name_or_password_is_wrong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText user_name_text, password_text;
    Button button_login, button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user_name_text = findViewById(R.id.user);
        password_text = findViewById(R.id.password);
        button_login = findViewById(R.id.login_button);
        button_cancel = findViewById(R.id.cancel_button);

        //call set on click listner method of button object(arguiment: onclick listner object with button action method)
        button_login.setOnClickListener(view -> login_method());

        button_cancel.setOnClickListener(view -> resetFields());


    } //end of oncreate method


    //start reset fields method
    public void resetFields(){
        String empty_username= "";
        String empty_password="";
        user_name_text.setText(empty_username);
        password_text.setText(empty_password);
    }


    //end rest fields method
    //start public void login method
    public void login_method() {
        String username = user_name_text.getText().toString();
        String password = password_text.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, getString(R.string.user_name_or_password_is_empty), Toast.LENGTH_SHORT).show();}


        else if (username.equals("John") && password.equals("123")) {
            Intent i = new Intent(login.this, Main.class);
            startActivity(i);
        }

        else {
        Toast.makeText(this, user_name_or_password_is_wrong,Toast.LENGTH_SHORT).show();}

        }
        //end of public void login method
    }
