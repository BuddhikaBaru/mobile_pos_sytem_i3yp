package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewCategoryActivity extends AppCompatActivity {


    String usernameT;

    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    ListView categoryList;
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);


        // Get intent from previous
        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");

        categoryList=findViewById(R.id.categoriesList);


        try {

            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            final Cursor cursorCategories =mydatabase.rawQuery("SELECT * FROM categorytable", null);
            int id = cursorCategories.getColumnIndex("category_id");
            int categoryName=cursorCategories.getColumnIndex("category");
            int catDescript = cursorCategories.getColumnIndex("categorydescription");
            titles.clear();

            arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
            categoryList.setAdapter(arrayAdapter);

            final ArrayList<categor> categorList = new ArrayList<categor>();
            if (cursorCategories.moveToFirst()){

                do {
                    categor cats = new categor();
                    cats.id = cursorCategories.getString(id);
                    cats.categoryName = cursorCategories.getString(categoryName);
                    cats.catDescript = cursorCategories.getString(catDescript);
                    categorList.add(cats);
                    titles.add(cursorCategories.getString(id)+"\t\t"+cursorCategories.getString(categoryName)+"\t\t"+cursorCategories.getString(catDescript));

                }while (cursorCategories.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                categoryList.invalidateViews();

            }

            cursorCategories.close();
            mydatabase.close();


            categoryList.setOnItemClickListener((adapterView, view, i, l) -> {
                String temp1=titles.get(i).toString();
                categor cats = categorList.get(i);
                Intent intentToEditCats =new Intent(getApplicationContext(), EditDeleteCategory.class);
                intentToEditCats.putExtra("id",cats.id);
                intentToEditCats.putExtra("category",cats.categoryName);
                intentToEditCats.putExtra("description",cats.catDescript);
                intentToEditCats.putExtra("usernameT",usernameT);
                startActivity(intentToEditCats);


            });

        }catch (Exception exception){}












    }
}