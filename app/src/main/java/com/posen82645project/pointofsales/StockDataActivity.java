package com.posen82645project.pointofsales;

import static com.posen82645project.pointofsales.DateHelper.calculateDaysDifference;
import static com.posen82645project.pointofsales.DateHelper.convertDateStringToTimestamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StockDataActivity extends AppCompatActivity {

    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        Intent intent = new Intent(this, ReportsActivity.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }




    ListView listView1;
    String usernameT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_data);

        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");

        listView1=findViewById(R.id.fastsellinglist);

        SQLiteDatabase db =openOrCreateDatabase("minipos", Context.MODE_PRIVATE, null);


// Retrieve data from solditems table
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='solditems'", null);

        if (cursor!=null && cursor.getCount()>0){
            cursor.close();

            Cursor cursor1 = db.rawQuery("SELECT item_id AS _id, productname, qty, selldate FROM solditems", null);

// Process data to create a summary table
            if (cursor1 != null && cursor1.moveToFirst()) {
                HashMap<String, Integer> totalSoldItemsMap = new HashMap<>(); // To store total sold items per product
                HashMap<String, Long> firstSaleDateMap = new HashMap<>(); // To store the first sale date per product
                HashMap<String, Long> lastSaleDateMap = new HashMap<>(); // To store the last sale date per product

                // Loop through the cursor1 data to calculate total sold items and determine first & last sale dates
                do {
                    String productName = cursor1.getString(cursor1.getColumnIndex("productname"));
                    int qty = cursor1.getInt(cursor1.getColumnIndex("qty"));
                    long sellDate = convertDateStringToTimestamp(cursor1.getString(cursor1.getColumnIndex("selldate")));

                    // Calculate total sold items per product
                    if (totalSoldItemsMap.containsKey(productName)) {
                        int currentTotal = totalSoldItemsMap.get(productName);
                        totalSoldItemsMap.put(productName, currentTotal + qty);
                    } else {
                        totalSoldItemsMap.put(productName, qty);
                        firstSaleDateMap.put(productName, sellDate);
                    }

                    // Update last sale date for the product
                    lastSaleDateMap.put(productName, sellDate);
                } while (cursor1.moveToNext());

                // Initialize cursor2
                MatrixCursor cursor2 = new MatrixCursor(new String[]{"_id","product_name", "frequency", "current_stock"});

                int Ix=0;

                // Loop through products to calculate frequency and fetch current stock from allstocktable
                for (Map.Entry<String, Integer> entry : totalSoldItemsMap.entrySet()) {
                    String productName = entry.getKey();
                    Log.d("prodname11", "My total Sales of All bills value: " + productName);


                    int totalSoldItems = entry.getValue();
                    Log.d("totalsolditems11", "My total Sales of All bills value: " + totalSoldItems);

                    long daysAmount = calculateDaysDifference(firstSaleDateMap.get(productName), lastSaleDateMap.get(productName))+1;
                    Log.d("daysamout11", "My total Sales of All bills value: " + daysAmount);
                    double frequency = (double) totalSoldItems / (double) daysAmount;

                    Log.d("freq11", "My total Sales of All bills value: " + frequency);


                    // Fetch current stock from allstocktable for the corresponding product
                    Cursor stockCursor = db.rawQuery("SELECT SUM(qty) AS total_qty FROM allstocktable WHERE productname = ?", new String[]{productName});
                    int currentStock = 0;
                    if (stockCursor != null && stockCursor.moveToFirst()) {
                        currentStock = stockCursor.getInt(stockCursor.getColumnIndex("total_qty"));
                    }


                    // Add a row to cursor2 with product name, frequency, and current stock
                    cursor2.addRow(new Object[]{Ix++, productName, frequency, currentStock});

                    if (stockCursor != null) {
                        stockCursor.close(); // Close the stockCursor if not null
                    }
                }

                ///////////////Sort Data
                // Retrieve data from cursor2 and store it in a list of objects
                List<Object[]> rows = new ArrayList<>();
                cursor2.moveToFirst();
                while (!cursor2.isAfterLast()) {
                    Object[] rowData = new Object[cursor2.getColumnCount()];
                    for (int i = 0; i < cursor2.getColumnCount(); i++) {
                        rowData[i] = cursor2.getString(i);
                    }
                    rows.add(rowData);
                    cursor2.moveToNext();
                }

// Sort the list based on the "frequency" column
                Collections.sort(rows, new Comparator<Object[]>() {
                    @Override
                    public int compare(Object[] row1, Object[] row2) {

                        double frequency1 = Double.parseDouble(row1[2].toString());
                        double frequency2 = Double.parseDouble(row2[2].toString());

                        // Sort in descending order
                        return Double.compare(frequency2, frequency1);
                    }
                });

// Recreate the cursor with the sorted data
                MatrixCursor sortedCursor = new MatrixCursor(new String[]{"_id", "product_name", "frequency", "current_stock"});
                for (Object[] row : rows) {
                    sortedCursor.addRow(row);
                }


                /////////////////end Sort data


                String[] fromColumns = {"product_name", "frequency", "current_stock"};
                int[] toViews = {R.id.prodName, R.id.prodFreq, R.id.stock};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.fastsellinglist, sortedCursor, fromColumns, toViews, 0);

                // Set the adapter for the ListView
                listView1.setAdapter(adapter);


                // Close cursor1 after use
                cursor1.close();
            }



        }










    }
}