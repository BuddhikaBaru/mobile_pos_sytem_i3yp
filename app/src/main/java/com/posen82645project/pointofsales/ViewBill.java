package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ViewBill extends AppCompatActivity {

    String usernameT;
    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        mydatabase.execSQL("DROP TABLE IF EXISTS cartitems");
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    Button back, confirm;
    TextView totalBill;
    ListView listView;
    private Cursor cursor;
    private SQLiteDatabase database;
    double totalcartvalue;
    double totalcartcost, billprofit;
    String currentDate;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);


        // Get intent from previous
        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");
        totalcartvalue = getFrom.getDoubleExtra("totalcartvalue",0);
        totalcartcost = getFrom.getDoubleExtra("totalcartcost",0);


        currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());


        confirm=findViewById(R.id.confirmButton); totalBill=findViewById(R.id.totalBill);
        listView=findViewById(R.id.listView);
        totalBill.setText(String.format("%2f",totalcartvalue));


        // open minipos db
        database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);


        // Execute the raw SQL query to fetch data
        cursor = database.rawQuery("SELECT item_id AS _id, barcode, productname, sellprice, amount, qty, user FROM cartitems", null);


        String[] fromColumns = {"productname", "sellprice", "qty","amount"};
        int[] toViews = {R.id.textProductNameQuantity, R.id.textBuySellPrice, R.id.qnttyview,R.id.bcodeview};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_layout, cursor, fromColumns, toViews, 0);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

//        database.close();
//        cursor.close();


    }// end of on create

    @SuppressLint("DefaultLocale")
    public void onConfirmButtonClick(View view) {
        SQLiteDatabase database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);

        Cursor salesCursor = database.rawQuery("SELECT * FROM cartitems", null);

        while (salesCursor.moveToNext()) {
            String barcode = salesCursor.getString(salesCursor.getColumnIndex("barcode"));
            int saleQuantity = salesCursor.getInt(salesCursor.getColumnIndex("qty"));


            // Find the corresponding row in the stocks table
            Cursor stocksCursor = database.rawQuery("SELECT * FROM allstocktable WHERE barcode=?", new String[]{barcode});

            if (stocksCursor.moveToFirst()) {
                int stockQuantity = stocksCursor.getInt(stocksCursor.getColumnIndex("qty"));
                int newQuantity = stockQuantity - saleQuantity;

                // Update the stocks table with the new quantity
                ContentValues values = new ContentValues();
                values.put("qty", newQuantity);

                database.update("allstocktable", values, "barcode=?", new String[]{barcode});
            }
            stocksCursor.close();
        }

        salesCursor.close();





        // Execute SQL to move data to "allstocktable" (CREATE TABLE IF NOT EXISTS)
        database.execSQL("CREATE TABLE IF NOT EXISTS solditems(item_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR,  sellprice VARCHAR, amount REAL, qty INTIGER, selldate VARCHAR, buycost REAL, user VARCHAR)");
        database.execSQL("INSERT INTO solditems (barcode, productname,  sellprice, amount, qty, selldate, buycost, user) SELECT barcode, productname,  sellprice, amount, qty, selldate, buycost, user FROM cartitems");

        database.execSQL("CREATE TABLE IF NOT EXISTS todaysolditems(item_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR,  sellprice VARCHAR, amount REAL, qty INTIGER, selldate VARCHAR, buycost REAL, user VARCHAR)");
        database.execSQL("INSERT INTO todaysolditems (barcode, productname,  sellprice, amount, qty, selldate, buycost, user) SELECT barcode, productname,  sellprice, amount, qty, selldate, buycost, user FROM cartitems");


        billprofit = totalcartvalue-totalcartcost;

        database.execSQL("CREATE TABLE IF NOT EXISTS todaybills(item_id INTEGER PRIMARY KEY AUTOINCREMENT, billdate VARCHAR, totalcost REAL,  totalval REAL, profit REAL, user VARCHAR)");
        String sqltodaybillinsert="INSERT INTO todaybills (billdate, totalcost,  totalval, profit,user) VALUES(?,?,?,?,?)";
        SQLiteStatement statement3 = database.compileStatement(sqltodaybillinsert);
        statement3.bindString(1, currentDate);
        statement3.bindString(2, String.format("%2f",totalcartcost));
        statement3.bindString(3, String.format("%2f",totalcartvalue));
        statement3.bindString(4, String.format("%2f",billprofit));
        statement3.bindString(5, usernameT);
        statement3.execute();


        database.execSQL("CREATE TABLE IF NOT EXISTS allbills(item_id INTEGER PRIMARY KEY AUTOINCREMENT, billdate VARCHAR, totalcost REAL,  totalval REAL, profit REAL, user VARCHAR)");
        String sqlallbillinsert="INSERT INTO allbills(billdate, totalcost,  totalval, profit, user) VALUES(?,?,?,?,?)";
        SQLiteStatement statement4 = database.compileStatement(sqlallbillinsert);
        statement4.bindString(1, currentDate);
        statement4.bindString(2, String.format("%2f",totalcartcost));
        statement4.bindString(3, String.format("%2f",totalcartvalue));
        statement4.bindString(4, String.format("%2f",billprofit));
        statement4.bindString(5, usernameT.toString());
        statement4.execute();


        //Add details to buy bill table

        // Delete the "tempstocktable"
        database.execSQL("DROP TABLE IF EXISTS cartitems");

        // Close the database cursor and connection
        Toast.makeText(this,"Sold!", Toast.LENGTH_SHORT).show();

        Intent intent =new Intent(ViewBill.this, Main.class);
        intent.putExtra("usernameT", usernameT);
        startActivity(intent);
        finish();


        // Refresh the ListView if needed


    }
}