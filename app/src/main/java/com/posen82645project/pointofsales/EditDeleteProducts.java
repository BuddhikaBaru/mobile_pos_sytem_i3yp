package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class EditDeleteProducts extends AppCompatActivity {


    String usernameT;
    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, ViewProductActivity.class);
        intent.putExtra("usernameT",usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    EditText prodid, prodName, prodDes;
    Spinner catSpinner;
    Button update, delete, back;
    ArrayList<String> titles=new ArrayList<>();
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_products);
        prodid = findViewById(R.id.proID);
        prodName= findViewById(R.id.editProd);
        prodDes= findViewById(R.id.editProDes);
        catSpinner= findViewById(R.id.catSpinner2);
        update=findViewById(R.id.updateButton);
        delete=findViewById(R.id.deleteButton);
        back =findViewById(R.id.backButton);



        Intent getFrom = getIntent();
        String id  = getFrom.getStringExtra("id").toString();
        String productName = getFrom.getStringExtra("productname").toString();
        String description = getFrom.getStringExtra("description").toString();
        String category = getFrom.getStringExtra("productCat").toString();
        usernameT=getFrom.getStringExtra("usernameT");

        prodid.setText(id);
        prodName.setText(productName);
        prodDes.setText(description);




        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        final Cursor cursorCategories =mydatabase.rawQuery("SELECT * FROM categorytable", null);
        int categoryName=cursorCategories.getColumnIndex("category");
        titles.clear();

        arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        catSpinner.setAdapter(arrayAdapter);

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProduct();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });


    }




    public void editProduct(){
        try {

            String id= prodid.getText().toString();
            String productNameNew = prodName.getText().toString();
            String descriptionNew="|"+prodDes.getText().toString();
            String category= "|"+ catSpinner.getSelectedItem().toString();


            if(!id.isEmpty() && !productNameNew.isEmpty() && !descriptionNew.isEmpty() && !category.isEmpty()){

                SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);

                String sqlUpProd= "update producttable set product=?,productdescription = ?, category=? where product_id = ?";
                SQLiteStatement statement = mydatabase.compileStatement(sqlUpProd);
                statement.bindString(1, productNameNew);
                statement.bindString(2, descriptionNew);
                statement.bindString(3, category);
                statement.bindString(4, id);
                statement.execute();
                Toast.makeText(this,"Product Updated", Toast.LENGTH_LONG).show();
                Intent i = new Intent(EditDeleteProducts.this, ViewProductActivity.class);
                i.putExtra("usernameT",usernameT);
                startActivity(i);
                finish();


            }else {}






        }catch (Exception ex){
            Toast.makeText(this,"Product Update Failed!", Toast.LENGTH_LONG).show();
        };
    }

    public void deleteProduct(){

        try {

            String id= prodid.getText().toString();



            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);

            String sqlAddCategory= "delete from producttable where product_id = ?";
            SQLiteStatement statement = mydatabase.compileStatement(sqlAddCategory);
            statement.bindString(1, id);
            statement.execute();
            Toast.makeText(this,"Product Deleted", Toast.LENGTH_LONG).show();
            Intent i = new Intent(EditDeleteProducts.this, ViewProductActivity.class);
            i.putExtra("usernameT",usernameT);
            startActivity(i);
            finish();


        }catch (Exception ex){
            Toast.makeText(this,"Product Deletion Failed!", Toast.LENGTH_LONG).show();
        };
    }


}