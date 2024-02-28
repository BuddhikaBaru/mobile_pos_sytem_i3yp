package com.posen82645project.pointofsales;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    String usernameT;
    Cursor cursorCategories;

    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    TextInputEditText newProdText, newProdDescriptText;
    Button addProductButton, cancelProductButton;
    Spinner catSnip;
    ArrayList<String> titles=new ArrayList<>();

    ArrayAdapter arrayAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
//intent to get data from before
        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");

        newProdText = findViewById(R.id.newProduct);
        newProdDescriptText = findViewById(R.id.newProductDescription);
        addProductButton=findViewById(R.id.addProdDbButton);
        cancelProductButton=findViewById(R.id.cancelProdButton);
        catSnip=findViewById(R.id.catSpinner);



        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS categorytable(category_id INTEGER PRIMARY KEY AUTOINCREMENT, category VARCHAR, categorydescription VARCHAR, user VARCHAR)");
        cursorCategories =mydatabase.rawQuery("SELECT * FROM categorytable", null);
        int categoryName=cursorCategories.getColumnIndex("category");
        titles.clear();

        arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        catSnip.setAdapter(arrayAdapter);

        final ArrayList<categor> categorList = new ArrayList<>();
        if (cursorCategories.moveToFirst()){

            do {
                categor cats = new categor();

                cats.categoryName = cursorCategories.getString(categoryName);

                categorList.add(cats);
                titles.add(cursorCategories.getString(categoryName));


            }while (cursorCategories.moveToNext());
            arrayAdapter.notifyDataSetChanged();

        }
        cursorCategories.close();
        mydatabase.close();

        addProductButton.setOnClickListener(view ->{
            if(!newProdText.getText().toString().isEmpty() && !newProdDescriptText.getText().toString().isEmpty()){
                insertProduct();
            }else{}


        });


        cancelProductButton.setOnClickListener(view -> {
            Intent sendTo =new Intent(AddProductActivity.this, Main.class);
            // Intent set send to
            sendTo.putExtra("usernameT",usernameT);
            startActivity(sendTo);
        });

    }
    //end of on create method

    public void insertProduct(){

        try {
            String prodName= newProdText.getText().toString().trim();
            String productDescription="|"+newProdDescriptText.getText().toString().trim();
            String category= "|"+ catSnip.getSelectedItem().toString();

            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS producttable(product_id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR, category VARCHAR, productdescription VARCHAR, user VARCHAR)");


            String sqlAddProduct= "INSERT INTO producttable (product, category, productdescription, user)VALUES(?,?,?,?)";
            SQLiteStatement statement = mydatabase.compileStatement(sqlAddProduct);
            statement.bindString(1, prodName);
            statement.bindString(2, category);
            statement.bindString(3, productDescription);
            statement.bindString(4, usernameT);
            statement.execute();
            Toast.makeText(this,"Product Added", Toast.LENGTH_LONG).show();
            newProdText.setText("");
            newProdDescriptText.setText("");
            newProdText.requestFocus();


        } catch (Exception exception){
            Toast.makeText(AddProductActivity.this, "Something is Empty", Toast.LENGTH_SHORT).show();
        }//end try catch


    }
}