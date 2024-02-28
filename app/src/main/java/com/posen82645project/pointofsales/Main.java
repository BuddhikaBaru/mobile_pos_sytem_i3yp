package com.posen82645project.pointofsales;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Main extends AppCompatActivity {

    Button addStock, addProduct, addCategory, reports, logout;
    TextView expcountTxt;


    double totalcartvalue =0.0;
    double totalcartcost=0.0;
    String usernameT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");




        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        mydatabase.execSQL("DROP TABLE IF EXISTS cartitems");
        mydatabase.execSQL("DROP TABLE IF EXISTS tempstocktable");
        mydatabase.execSQL("DROP TABLE IF EXISTS ExpiringItems");
        mydatabase.execSQL("DROP TABLE IF EXISTS LowStockItems");
        mydatabase.close();

//        viewStock = findViewById(R.id.view_stock_button);
//        newBill=findViewById(R.id.new_bill_button);
        addStock=findViewById(R.id.addStockButton);
        addProduct=findViewById(R.id.addProductButton);
        addCategory=findViewById(R.id.addCategoryButton);
        //viewStock=findViewById(R.id.view_stock_button);
      //  viewCategory=findViewById(R.id.viewCategoryButton);
        MaterialCardView viewProduct=findViewById(R.id.viewProductButton);
        reports=findViewById(R.id.report_Button);
        logout=findViewById(R.id.logOutButton);
        MaterialCardView newBillCard = findViewById(R.id.newBillCard);
        MaterialCardView viewStock=findViewById(R.id.checkStockCard);
        MaterialCardView viewCategory = findViewById(R.id.viewCategoryButton);
 //       unbox=findViewById(R.id.unbox);

 //       unbox.setText(usernameT);
        TextView expcount= findViewById(R.id.expcounta);
        TextView expcountTxt=findViewById(R.id.expcountTxt);
 //       TextView lowstcount=findViewById(R.id.lowstcounta);


        /////////////////////////////////////////////

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        SQLiteDatabase db = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);


        db.execSQL("CREATE TABLE IF NOT EXISTS ExpiringItems(item_id INTEGER PRIMARY KEY AUTOINCREMENT, productname VARCHAR, expdate VARCHAR, qty VARCHAR, barcode VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS LowStockItems(item_id INTEGER PRIMARY KEY AUTOINCREMENT, productname VARCHAR, qty VARCHAR)");

// Retrieve data from allstocktable

        Cursor cursorxx = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='allstocktable'", null);

//        Cursor cursor = db.rawQuery("SELECT tstock_id AS _id, productname, expdate, qty FROM allstocktable", null);

// Create or open ExpiringItems and LowStockItems tables

// Loop through each row of allstocktable
        if (cursorxx != null && cursorxx.getCount() > 0) {
            Log.d("cursorxx", "prodname value: " + "Available");
            cursorxx.close();
            Cursor cursor = db.rawQuery("SELECT tstock_id AS _id, productname, expdate, qty, barcode FROM allstocktable", null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {

                String productName = cursor.getString(cursor.getColumnIndex("productname"));
                Log.d("prodnameexp", "prodname value: " + productName);
                String expDate = cursor.getString(cursor.getColumnIndex("expdate"));
                Log.d("expdate", "expdate value: " + expDate);
                int quantity = cursor.getInt(cursor.getColumnIndex("qty"));

                String bcodeExp = cursor.getString(cursor.getColumnIndex("barcode"));

                // Calculate the difference in days between expDate and currentDate
                long dateDifference = getDateDifferenceInDays(currentDate,expDate);

                Log.d("datediff", "datediff value: " + dateDifference);

                // Add row to ExpiringItems if difference is less than 5 days
                if (dateDifference < 5) {
                    // insertRowToExpiringItems(productName, expDate, quantity);
                    insertRowToExpiringItems(productName, expDate, quantity, bcodeExp);
                }

                // Add row to LowStockItems if quantity is less than or equal to 5
//                if (quantity <= 5) {
//                    // insertRowToLowStockItems(productName, quantity);
//                    insertRowToLowStockItems(productName, quantity);
//                }
            }
        }



// Get the count of rows in ExpiringItems and LowStockItems tables
        int countExp = getRowCount("ExpiringItems");
//        int countLS = getRowCount("LowStockItems");

// Set text views in the dashboard to display the counts
        if (countExp > 0) {
            expcount.setText(String.valueOf(countExp));
        }

//        if (countLS > 0) {
//            lowstcount.setText(String.valueOf(countLS));
//        }

// Close the cursor and database connection
        cursorxx.close();
        db.close();







        addCategory.setOnClickListener(view -> {
            Intent toAddCatActivity =new Intent(Main.this, AddCategoryactivity.class);
            toAddCatActivity.putExtra("usernameT",usernameT);
            startActivity(toAddCatActivity);
        });

        viewCategory.setOnClickListener(view -> {
            Intent toViewCategoryActivity = new Intent(Main.this, ViewCategoryActivity.class);
            toViewCategoryActivity.putExtra("usernameT",usernameT);
            startActivity(toViewCategoryActivity);
        });

        addProduct.setOnClickListener(view -> {
            Intent toAddProdActivity = new Intent(Main.this, AddProductActivity.class);
            toAddProdActivity.putExtra("usernameT",usernameT);
            startActivity(toAddProdActivity);
        });
//
//        addProduct.setOnClickListener(view -> {
//            Intent toAddProdActivity = new Intent(Main.this, AddProductActivity.class);
//
//            startActivity(toAddProdActivity);
//        });

        viewProduct.setOnClickListener(view -> {
            Intent toViewProdActivity = new Intent(Main.this, ViewProductActivity.class);
            toViewProdActivity.putExtra("usernameT",usernameT);
            startActivity(toViewProdActivity);
        });
        addStock.setOnClickListener(view -> {
            Intent toAddStockActivity = new Intent(Main.this, BuyVoucher.class);
            toAddStockActivity.putExtra("usernameT",usernameT);
            startActivity(toAddStockActivity);
        });

        viewStock.setOnClickListener(view -> {
            Intent toViewStockActivity = new Intent(Main.this, ViewCurrentStock.class);
            toViewStockActivity.putExtra("usernameT",usernameT);
            startActivity(toViewStockActivity);
        });
        newBillCard.setOnClickListener(view -> {
            Intent toNewBillActivity = new Intent(Main.this, NewBillActivity.class);
            toNewBillActivity.putExtra("totalcartvalue",totalcartvalue);
            toNewBillActivity.putExtra("totalcartcost",totalcartcost);
            toNewBillActivity.putExtra("usernameT",usernameT);

            startActivity(toNewBillActivity);
        });

        reports.setOnClickListener(view -> {
            Intent toNewBillActivity = new Intent(Main.this, ReportsActivity.class);
            toNewBillActivity.putExtra("usernameT",usernameT);

            startActivity(toNewBillActivity);
        });

        expcountTxt.setOnClickListener((view -> {
            Intent toexpire = new Intent(Main.this, ExpireItems.class);
            toexpire.putExtra("usernameT",usernameT);
            startActivity(toexpire);

        }));






        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logOutIntent=new Intent(Main.this, LoginActivity.class);
                logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logOutIntent);
            }
        });




    }//end of on create

    // Method to calculate the difference in days between two dates
    private long getDateDifferenceInDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);

            long differenceInMillis = date2.getTime() - date1.getTime();
            return TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle error or return appropriate value
        }
    }


    // Method to get the row count of a specific table
    private int getRowCount(String tableName) {
        SQLiteDatabase db =openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        int count = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }

        db.close();
        return count;
    }


    private void insertRowToExpiringItems(String productName, String expDate, int quantity, String bcodeExp) {
        SQLiteDatabase db = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        ContentValues values = new ContentValues();
        values.put("productname", productName);
        values.put("expdate", expDate);
        values.put("qty", quantity);
        values.put("barcode", bcodeExp);

        db.insert("ExpiringItems", null, values);
        db.close();
    }

    // Method to insert a row into the LowStockItems table
    private void insertRowToLowStockItems(String productName, int quantity) {
        SQLiteDatabase db = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        ContentValues values = new ContentValues();
        values.put("productname", productName);
        values.put("qty", quantity);

        db.insert("LowStockItems", null, values);
        db.close();
    }



}