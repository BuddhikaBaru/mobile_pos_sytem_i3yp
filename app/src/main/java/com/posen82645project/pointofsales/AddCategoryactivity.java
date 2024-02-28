package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddCategoryactivity extends AppCompatActivity {

    TextInputEditText newCategoryLabal, newCategoryDescription;
    Button addCatButton, addCategoryCancelButton;
    String usernameT;


    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT",usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

//intent to get data from before
        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");

        newCategoryLabal =findViewById(R.id.newCategory);
        newCategoryDescription = findViewById(R.id.newCategoryDescription);
        addCatButton = findViewById(R.id.addCategoryDbButton);
        addCategoryCancelButton=findViewById(R.id.cancelCategoryButton);

        addCategoryCancelButton.setOnClickListener(view -> {
            Intent i = new Intent(AddCategoryactivity.this, Main.class);
            i.putExtra("usernameT",usernameT);
            startActivity(i);
        });

        addCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!newCategoryLabal.getText().toString().isEmpty() && !newCategoryDescription.getText().toString().isEmpty()){

                    insert();

                }else {}

            }
        });
    }// end of on crerate

    public void insert(){
//        try {
            String category= newCategoryLabal.getText().toString().trim();
            String categoryDescription=newCategoryDescription.getText().toString().trim();
            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS categorytable(category_id INTEGER PRIMARY KEY AUTOINCREMENT, category VARCHAR, categorydescription VARCHAR, user VARCHAR)");


            String sqlAddCategory= "INSERT INTO categorytable (category, categorydescription, user)VALUES(?,?,?)";
            SQLiteStatement statement = mydatabase.compileStatement(sqlAddCategory);
            statement.bindString(1, category);
            statement.bindString(2, categoryDescription);
            statement.bindString(3, usernameT.toString());
            statement.execute();
            Toast.makeText(this,"Category Added", Toast.LENGTH_LONG).show();
            newCategoryLabal.setText("");
            newCategoryDescription.setText("");
            newCategoryLabal.requestFocus();


   //     } catch (Exception exception){
            //Toast.makeText(AddCategoryactivity.this, "Something is Empty", Toast.LENGTH_SHORT).show();
    //    }//end try catch
    }
}