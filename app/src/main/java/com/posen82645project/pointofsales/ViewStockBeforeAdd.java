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

public class ViewStockBeforeAdd extends AppCompatActivity {
    ListView inventoryView;
    ArrayList <tstockClass> titles = new ArrayList<>();
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock_before_add);



        // Get intent from previous
        Intent getFrom=getIntent();
        String usernameT=getFrom.getStringExtra("usernameT");

        inventoryView=findViewById(R.id.confirmInventoryList);
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        final Cursor cursorTempStock =mydatabase.rawQuery("SELECT * FROM tempstocktable", null);
        //get column ids
        int barCode = cursorTempStock.getColumnIndex("barcode");
        int prodName=cursorTempStock.getColumnIndex("productname");
        int buyprice = cursorTempStock.getColumnIndex("buyprice");
        int sellPrice = cursorTempStock.getColumnIndex("sellprice");
        int expDate = cursorTempStock.getColumnIndex("expdate");
        int qty = cursorTempStock.getColumnIndex("qty");

        titles.clear();
        CustomListAdapter adapter = new CustomListAdapter(this, titles);
        //arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        inventoryView.setAdapter(adapter);
        final ArrayList<tstockClass> invList = new ArrayList<tstockClass>();
        if (cursorTempStock.moveToFirst()){

            do {
                tstockClass inv = new tstockClass();
                inv.bcode = cursorTempStock.getString(barCode);
                inv.proName = cursorTempStock.getString(prodName);
                inv.buyP = Float.valueOf(cursorTempStock.getString(buyprice));
                inv.sellP = Float.valueOf(cursorTempStock.getString(sellPrice));
                inv.expddt = cursorTempStock.getString(expDate);
                inv.Qty = Integer.parseInt(cursorTempStock.getString(qty));
                invList.add(inv);
                //titles.add(cursorTempStock.getString(prodName)+"\t\t"+cursorTempStock.getString(qty)+"\t\t"+cursorTempStock.getString(buyprice));

            }while (cursorTempStock.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            inventoryView.invalidateViews();

        }


    }
}