package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AverageCostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_cost);

        SQLiteDatabase db = openOrCreateDatabase("minipos", Context.MODE_PRIVATE, null);

        Cursor uniqueProductNamesCursor = db.rawQuery("SELECT DISTINCT productname FROM allstocktable", null);

        MatrixCursor cursorFinal = new MatrixCursor(new String[]{"_id", "productname", "averageBP", "averageSP", "totalQTY"});

// Check if the cursor is not null and contains data
        if(uniqueProductNamesCursor !=null&&uniqueProductNamesCursor.moveToFirst())

        {
            do {
                String productName = uniqueProductNamesCursor.getString(uniqueProductNamesCursor.getColumnIndex("productname"));

                // Calculate average buying price, average selling price, and total quantity for each product
                Cursor productDetailsCursor = db.rawQuery("SELECT buyprice, sellprice, qty FROM allstocktable WHERE productname = ?",
                        new String[]{productName});

                double totalCost = 0;
                double totalIncome = 0;
                int totalQty = 0;

                // Check if the cursor contains data for the specific product
                if (productDetailsCursor != null && productDetailsCursor.moveToFirst()) {
                    do {
                        double buyPrice = productDetailsCursor.getDouble(productDetailsCursor.getColumnIndex("buyprice"));
                        double sellPrice = productDetailsCursor.getDouble(productDetailsCursor.getColumnIndex("sellprice"));
                        int qty = productDetailsCursor.getInt(productDetailsCursor.getColumnIndex("qty"));

                        totalCost += buyPrice * qty;
                        totalIncome += sellPrice * qty;
                        totalQty += qty;
                    } while (productDetailsCursor.moveToNext());

                    // Calculate average buying price and average selling price
                    double averageBuyPrice = totalCost / totalQty;
                    double averageSellPrice = totalIncome / totalQty;

                    // Add details to CursorFinal
                    cursorFinal.addRow(new Object[]{null, productName, averageBuyPrice, averageSellPrice, totalQty});
                }

                if (productDetailsCursor != null) {
                    productDetailsCursor.close();
                }
            } while (uniqueProductNamesCursor.moveToNext());
        }


        if(uniqueProductNamesCursor !=null)

        {
            uniqueProductNamesCursor.close();
        }


        String[] fromColumns = {"_id","productname", "averageBP", "averageSP", "totalQTY"};
        int[] toViews = {R.id.prodName, R.id.prodName, R.id.buyP, R.id.sellP, R.id.qtysum};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.costavglist_layout, cursorFinal, fromColumns, toViews, 0);

        // Set the adapter for the ListView
        ListView listView=findViewById(R.id.listViewD);
        listView.setAdapter(adapter);












    }//end of on create
}