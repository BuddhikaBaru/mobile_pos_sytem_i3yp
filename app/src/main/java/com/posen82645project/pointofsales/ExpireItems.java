package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

public class ExpireItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire_items);
        Intent intent = getIntent();
        String usernameT=intent.getStringExtra("usernameT");
        ListView listView=findViewById(R.id.listViewC);


        SQLiteDatabase database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE, null);

        Cursor cursor = database.rawQuery("SELECT item_id AS _id, productname, expdate, qty, barcode FROM ExpiringItems", null);


        String[] fromColumns = {"_id","productname", "expdate", "qty", "barcode"};
        int[] toViews = {R.id.itemId, R.id.prodName, R.id.expDate, R.id.qty, R.id.bcodetxt};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.expire_items_list_temp, cursor, fromColumns, toViews, 0);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor selectedItemCursor = (Cursor) parent.getItemAtPosition(position);

                // Retrieve values from the clicked item
                int itemId = selectedItemCursor.getInt(selectedItemCursor.getColumnIndex("_id"));
                String productName = selectedItemCursor.getString(selectedItemCursor.getColumnIndex("productname"));
                String expDate = selectedItemCursor.getString(selectedItemCursor.getColumnIndex("expdate"));
                int quantity = selectedItemCursor.getInt(selectedItemCursor.getColumnIndex("qty"));
                String barcodeexp= selectedItemCursor.getString(selectedItemCursor.getColumnIndex("barcode"));

                // Start DeleteExpItems activity and pass the values
                Intent intent = new Intent(ExpireItems.this, DeleteExpItems.class);
                intent.putExtra("usernameT",usernameT);
                intent.putExtra("itemId", itemId);
                intent.putExtra("productName", productName);
                intent.putExtra("expDate", expDate);
                intent.putExtra("quantity", quantity);
                intent.putExtra("bcodeexp", barcodeexp);
                startActivity(intent);
                finish();
            }
        });





    }
}