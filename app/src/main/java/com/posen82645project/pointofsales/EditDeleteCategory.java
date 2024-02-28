package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditDeleteCategory extends AppCompatActivity {

    String usernameT;
    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, ViewCategoryActivity.class);
        intent.putExtra("usernameT",usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    EditText catId, editCat, editCatDest;
    Button updateCatButton, deleteCatButton,backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_category);

        catId=findViewById(R.id.categoryID);
        editCat=findViewById(R.id.editCategory);
        editCatDest=findViewById(R.id.editCategoryDescription);
        updateCatButton=findViewById(R.id.updateCategoryButton);
        deleteCatButton=findViewById(R.id.deleteCategoryButton);
        backButton=findViewById(R.id.toViewCatButton);


        //intent to get data from before


        Intent getFrom = getIntent();
        String id  = getFrom.getStringExtra("id");
        String categoryName = getFrom.getStringExtra("category");
        String description = getFrom.getStringExtra("description");
        usernameT=getFrom.getStringExtra("usernameT");

        catId.setText(id);
        editCat.setText(categoryName);
        editCatDest.setText(description);




         updateCatButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 editCategory();
             }
         });

         deleteCatButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 deleteCat();

             }
         });

         backButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(EditDeleteCategory.this, ViewCategoryActivity.class);
                 i.putExtra("usernameT",usernameT);
                 startActivity(i);
             }
         });



    }//endOncreate state

//delete category function declaration
    public void deleteCat(){

        try {

            String id= catId.getText().toString();



            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);

            String sqlAddCategory= "delete from categorytable where category_id = ?";
            SQLiteStatement statement = mydatabase.compileStatement(sqlAddCategory);
            statement.bindString(1, id);
            statement.execute();
            Toast.makeText(this,"Category Deleted", Toast.LENGTH_LONG).show();
            mydatabase.close();
            Intent i = new Intent(EditDeleteCategory.this, ViewCategoryActivity.class);
            i.putExtra("usernameT",usernameT);
            startActivity(i);
            finish();


        }catch (Exception ex){
            Toast.makeText(this,"Category Deletion Failed!", Toast.LENGTH_LONG).show();
        }
    }




    //start category function declaration

    public void editCategory(){

        try {

            String id= catId.getText().toString();
            String categoryNameNew = editCat.getText().toString();
            String descriptionNew=editCatDest.getText().toString();
            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);


            if (!id.isEmpty() && !categoryNameNew.isEmpty()  && !descriptionNew.isEmpty() ){

                String sqlAddCategory= "update categorytable set category=?, categorydescription=? where category_id = ?";
                SQLiteStatement statement = mydatabase.compileStatement(sqlAddCategory);
                statement.bindString(1, categoryNameNew);
                statement.bindString(2, descriptionNew);
                statement.bindString(3, id);
                statement.execute();
                Toast.makeText(this,"Category Updated", Toast.LENGTH_LONG).show();
                Intent i = new Intent(EditDeleteCategory.this, ViewCategoryActivity.class);

                i.putExtra("usernameT",usernameT);
                startActivity(i);
                finish();


            }else {}





        }catch (Exception ex){
            Toast.makeText(this,"Category Failed!", Toast.LENGTH_LONG).show();
        }



    }
}