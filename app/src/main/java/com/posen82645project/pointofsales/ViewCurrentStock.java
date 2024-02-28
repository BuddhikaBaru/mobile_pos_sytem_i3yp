package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewCurrentStock extends Activity {

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



    private SQLiteDatabase database;
    private ListView listView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_current_stock);

try {

    // Get intent from previous
    Intent getFrom=getIntent();
    usernameT=getFrom.getStringExtra("usernameT");

    // Open the database (replace "minipos.db" with your actual database name)
    database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE, null);

    // Execute the raw SQL query to fetch data from allstocktable
    cursor = database.rawQuery("SELECT tstock_id AS _id, productname, buyprice, sellprice, barcode, expdate, qty FROM allstocktable", null);



    // Find the ListView in your layout
    listView = findViewById(R.id.listView);

    // Create an adapter to display data in the custom ListView
    String[] fromColumns = {"productname", "buyprice", "sellprice", "barcode", "expdate", "qty"};
    int[] toViews = {R.id.textProductNameQuantity, R.id.textBuySellPrice, R.id.textBarcodeExpDate,R.id.qnttyview,R.id.bcodeview, R.id.expdateview};
    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_layout, cursor, fromColumns, toViews, 0);

    // Set the adapter for the ListView
    listView.setAdapter(adapter);

}catch (Exception exception){}

    }
}