package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class cqsActivity extends AppCompatActivity {



    private ListView listView1, listView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cqs);

        listView1 =findViewById(R.id.listViewA);
        listView2 =findViewById(R.id.listViewB);

        SQLiteDatabase db = openOrCreateDatabase("minipos", MODE_PRIVATE, null);
//        Cursor cursor = db.rawQuery("SELECT * FROM cheques", null);

        List<Cheque> todayCheques = new ArrayList<>(); // Category A: Today's cheques
        List<Cheque> futureCheques = new ArrayList<>(); // Category B: Future cheques


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());


        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='cheques'", null);


        if(cursor!=null && cursor.getCount()>0){
            cursor.close();

            Cursor cursor1 = db.rawQuery("SELECT cheque_id AS _id, issuedate, paydate, toaccount, cqvalue FROM cheques WHERE paydate=?", new String[]{currentDate});




            String[] fromColumns = {"paydate", "toaccount", "cqvalue"};
            int[] toViews = {R.id.textProductNameQuantity, R.id.textBuySellPrice, R.id.expdateview};
            SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this, R.layout.cq_layout, cursor1, fromColumns, toViews, 0);
            listView1.setAdapter(adapter1);



            Cursor cursor2 = db.rawQuery("SELECT cheque_id AS _id, issuedate, paydate, toaccount, cqvalue FROM cheques WHERE paydate!=?", new String[]{currentDate});
            SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, R.layout.cq_layout, cursor2, fromColumns, toViews, 0);
            listView2.setAdapter(adapter2);

//
//        cursor1.close();
//        cursor2.close();
            db.close();




// Iterate through the rows and categorize them
//        while (cursor.moveToNext()) {
//            int chequeId = cursor.getInt(cursor.getColumnIndex("cheque_id"));
//            String toAccount = cursor.getString(cursor.getColumnIndex("toaccount"));
//            String payDate = cursor.getString(cursor.getColumnIndex("paydate"));
//            String cqValue = cursor.getString(cursor.getColumnIndex("cqvalue"));
//
//            // Check if the paydate matches the current date
//            if (payDate.equals(currentDate)) {
//                // Category A: Today's cheques
//                todayCheques.add(new Cheque(chequeId, toAccount, cqValue));
//            } else if (payDate.compareTo(currentDate) > 0) {
//                // Category B: Future cheques
//                futureCheques.add(new Cheque(chequeId, toAccount, payDate, cqValue));
//            }
//        }
//        cursor.close();





        }









    }
}