package com.posen82645project.pointofsales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class InventoryActivity extends Activity {

    String usernameT;
    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        mydatabase.execSQL("DROP TABLE IF EXISTS tempstocktable");
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT",usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    private ListView listView;
    private Cursor cursor;
    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

    String voucherDate,suppName,totalVal,pyDate,csch;

    TextView billvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // get extra from addstock activity
        Intent getFrom = getIntent();
        voucherDate  = getFrom.getStringExtra("voucherDate");
        suppName = getFrom.getStringExtra("suppName");
        totalVal = getFrom.getStringExtra("totalVal");
        pyDate = getFrom.getStringExtra("pyDate");
        csch = getFrom.getStringExtra("csch");
        usernameT=getFrom.getStringExtra("usernameT");





        // Open the database (replace "minipos.db" with your actual database name)
        SQLiteDatabase database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);

        // Execute the raw SQL query to fetch data
        cursor = database.rawQuery("SELECT tstock_id AS _id, productname, buyprice, sellprice, barcode, expdate, qty, user FROM tempstocktable", null);

        // Find the ListView in your layout
        listView = findViewById(R.id.listView);
        billvalue=findViewById(R.id.billval);

        billvalue.setText(totalVal.toString());

        // Create an adapter to display data in the custom ListView
        String[] fromColumns = {"productname", "buyprice", "sellprice", "barcode", "expdate", "qty"};
        int[] toViews = {R.id.textProductNameQuantity, R.id.textBuySellPrice, R.id.textBarcodeExpDate,R.id.qnttyview,R.id.bcodeview, R.id.expdateview};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_layout, cursor, fromColumns, toViews, 0);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }// end of on create

    // Implement the confirmation button's click event to move data and delete table
    public void onConfirmButtonClick(View view) {

        // check tempstock table is empty?
        if (cursor.moveToFirst()){

            SQLiteDatabase database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            // Execute SQL to move data to "allstocktable" (CREATE TABLE IF NOT EXISTS)
            database.execSQL("CREATE TABLE IF NOT EXISTS allstocktable(tstock_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR, buydate VARCHAR, buyprice REAL,  sellprice REAL, expdate VARCHAR, qty INTIGER, user VARCHAR)");
            database.execSQL("INSERT INTO allstocktable (barcode, productname, buydate, buyprice,  sellprice, expdate, qty, user) SELECT barcode, productname, buydate, buyprice,  sellprice, expdate, qty,user FROM tempstocktable");



            //Add details to buy bill table
            database.execSQL("create table if not exists buybillstable(buybill_id INTEGER PRIMARY KEY AUTOINCREMENT, billdate VARCHAR, supplier VARCHAR, totalvalue VARCHAR)");
            String sqlAddBilldetails= "INSERT INTO buybillstable (billdate, supplier, totalvalue)VALUES(?,?,?)";
            SQLiteStatement statement = database.compileStatement(sqlAddBilldetails);
            statement.bindString(1, voucherDate);
            statement.bindString(2, suppName);
            statement.bindString(3, totalVal.toString());
            statement.execute();

            // add buy voucher details to today buy details
//
//            database.execSQL("create table if not exists todaybuybillstable(buybill_id INTEGER PRIMARY KEY AUTOINCREMENT, billdate VARCHAR, supplier VARCHAR, totalvalue VARCHAR)");
//            Cursor curTdByBills=database.rawQuery("SELECT * FROM todaybuybillstable", null);
//
//            String sqlAddBilldetailstdy= "INSERT INTO buybillstable (billdate, supplier, totalvalue)VALUES(?,?,?)";
//            SQLiteStatement statementx = database.compileStatement(sqlAddBilldetailstdy);
//            statementx.bindString(1, voucherDate);
//            statementx.bindString(2, suppName);
//            statementx.bindString(3, totalVal.toString());
//            statementx.execute();



            if (Objects.equals(csch, "cheque")){
                database.execSQL("create table if not exists cheques(cheque_id INTEGER PRIMARY KEY AUTOINCREMENT, issuedate VARCHAR, paydate VARCHAR, toaccount VARCHAR, cqvalue VARCHAR)");
                String sqlAddCheqdetails= "INSERT INTO cheques (issuedate, paydate, toaccount, cqvalue)VALUES(?,?,?,?)";
                SQLiteStatement statement2 = database.compileStatement(sqlAddCheqdetails);
                statement2.bindString(1, currentDate);
                statement2.bindString(2, pyDate);
                statement2.bindString(3, suppName);
                statement2.bindString(4, totalVal.toString());
                statement2.execute();
            }

            // Delete the "tempstocktable"
            database.execSQL("DROP TABLE IF EXISTS tempstocktable");

            // Close the database cursor and connection
            cursor.close();
            database.close();
            Toast.makeText(this,"STOCK ADDED SUCCESS", Toast.LENGTH_SHORT).show();

            // Intent set send to
            Intent sendTo = new Intent(getApplicationContext(), Main.class);
            sendTo.putExtra("usernameT",usernameT);
            startActivity(sendTo);



        }else {
            Toast.makeText(InventoryActivity.this, "No inventory to Add! Bye! Bye!", Toast.LENGTH_SHORT).show();
            Intent sendTo = new Intent(getApplicationContext(), Main.class);
            sendTo.putExtra("usernameT",usernameT);
            startActivity(sendTo);

        }



        // Refresh the ListView if needed
        // ...
    }
}
