package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import  java.util.Date;
import java.util.Locale;

public class ReportsActivity extends AppCompatActivity {
    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        startActivity(intent);
        finish();
    }


    MaterialCardView todayCard, allCard, chequeCard;
    TextView todayhead, todaySales, todayProfit, todayPays, allSales, allProfit, allPays, cqCount, cqValue, cqNextDate;
    String usernameT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");


        todayCard =findViewById(R.id.todayCard);    allCard=findViewById(R.id.allTimeCard);     chequeCard=findViewById(R.id.chequesCard);      todayhead=findViewById(R.id.todayDate);
        todaySales=findViewById(R.id.todaySales);   todayProfit=findViewById(R.id.todayProfit);
        //todayPays=findViewById(R.id.todayPays);
        allSales=findViewById(R.id.allSales);       allProfit=findViewById(R.id.allProfit);
//        allPays=findViewById(R.id.payments);
        cqCount=findViewById(R.id.chequecount); cqValue=findViewById(R.id.chequeVal); cqNextDate=findViewById(R.id.nextChequeDate);
        RelativeLayout chequesBoxLayout = findViewById(R.id.chequesbox);
        RelativeLayout AlltimeRlayoutBut=findViewById(R.id.AlltimeRlayout);
        Button avgcost = findViewById(R.id.avgCost);






        Date today=new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate= dateformat.format(today);
        todayhead.setText(formattedDate.toString());

        SQLiteDatabase db = openOrCreateDatabase("minipos", MODE_PRIVATE, null);
//        SQLiteDatabase db2 = openOrCreateDatabase("minipos", MODE_PRIVATE, null);

        if (tableExists(db, "todaybills")){
            Cursor cursorA=db.rawQuery("SELECT item_id AS _id, totalval, profit FROM todaybills", null);
//            Cursor cursorA2=db.rawQuery("SELECT * FROM todaybills", null);
//            Cursor cursorB=db.rawQuery("SELECT * FROM todaybills", null);
//            Cursor cursorC=db.rawQuery("SELECT * FROM todaybills", null);

//            if(cursorA.moveToFirst() && cursorB.moveToFirst() && cursorC.moveToFirst()){
//                String firstRowDate=cursor.getString(cursor.getColumnIndex("billdate"));
//                if(today.equals(firstRowDate.toString())){// check dates
            if(cursorA.moveToFirst()){
                    double SalesVal1=getSum(cursorA, "totalval");
                Log.d("TAG_All_sales_all_bills", "My total Sales of All bills value: " + SalesVal1);
                if(cursorA!=null){
//                    int columnIndexaaa = cursorA.getColumnIndex("profit");
//                    Log.d("TAG_Profit_column_Index_allbills", "col index: " + columnIndexaaa);
                }
                    double ProfitVal1=getSum(cursorA,"profit");
//                Log.d("TAG_All_profit_all_bills", "My total Profit of All bills value: " + ProfitVal1);
//                    double costVal=getSum(cursorA, "totalcost");

                todaySales.setText(String.format("%.2f", SalesVal1));
                todayProfit.setText(String.format("%.2f", ProfitVal1));
//                    allPays.setText(String.format("%.2f", costVal));



//                }else {// check dates if not equal then,
//                    todayhead.setText("Fresh Date");
//                    todaySales.setText("0.00");
//                    todayProfit.setText("0.00");
//                }

            }else {
                db.execSQL("DELETE FROM todaybills");
                todayhead.setText("Fresh Date");
                todaySales.setText("0.00");
                todayProfit.setText("0.00");
            }
            cursorA.close();
//            cursorB.close();
//            cursorC.close();


        }else{
            todayhead.setText("Fresh Date");
            todaySales.setText("0.00");
            todayProfit.setText("0.00");
        }


        /// update all time card
        if (tableExists(db, "allbills")){
            Cursor cursor2A=db.rawQuery("SELECT * FROM allbills", null);
//            Cursor cursor2B=db.rawQuery("SELECT  FROM allbills", null);
//            Cursor cursor2B=db.rawQuery("SELECT * FROM allbills", null);
//            Cursor cursor2C=db.rawQuery("SELECT * FROM allbills", null);

            if (cursor2A.moveToFirst()){
                double salesVal=getSum(cursor2A, "totalval");
                double costVal=getSum(cursor2A, "totalcost");
//                double payVal=getSum(cursor2A, "totalcost");

//                allPays.setText(String.format("%.2f", payVal));
                double profitVal=salesVal-costVal;
                allProfit.setText(String.format("%.2f",profitVal));
                allSales.setText(String.format("%.2f", salesVal));

            }else {//if cursor not move on allbills table

            }

            cursor2A.close();
//            cursor2B.close();
//            cursor2C.close();






            // end implementing new function

//            if(cursor.moveToFirst()){
//                String firstRowDate=cursor.getString(cursor.getColumnIndex("billdate"));
//                if(today.equals(firstRowDate.toString())){
//                    double todaySalesVal=getSum(cursor, "totalval");
//                    double todayProfitVal=getSum(cursor,"profit");
//
//                    todaySales.setText(String.format("%.2f", todaySalesVal));
//                    todayProfit.setText(String.format("%.2f", todayProfitVal));
//
//
//                }else {
//                    todayhead.setText("Fresh Date");
//                    todaySales.setText("0.00");
//                    todayProfit.setText("0.00");
//                }
//
//            }else { // table not move first()
////                db.execSQL("DELETE FROM todaybills");
////                todayhead.setText("Fresh Date");
////                todaySales.setText("0.00");
////                todayProfit.setText("0.00");
//            }

        }else{
            todayhead.setText("Fresh Date");
            todaySales.setText("0.00");
            todayProfit.setText("0.00");
        }
        // end of updating alltime card

        //cheques table
        //cheques card
        if(tableExists(db, "cheques")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayDate = dateFormat.format(new Date());

            Cursor cursor = db.rawQuery("SELECT * FROM cheques", null);

            int cqCounts = 0;
            double cqSum = 0.0;

            while (cursor.moveToNext()) {
                String payDate = cursor.getString(cursor.getColumnIndex("paydate"));
                double cqValue = Double.parseDouble(cursor.getString(cursor.getColumnIndex("cqvalue")));

                // Step 4: Compare the paydate of each row with today's date
                if (payDate.compareTo(todayDate) >= 0) {
                    // Step 5: Perform necessary actions based on the comparison
                    cqCounts++;
                    cqSum += cqValue;
                } else {
                    // Step 5: If paydate is a past date, delete the entire row
                    int chequeId = cursor.getInt(cursor.getColumnIndex("cheque_id"));
                    db.delete("cheques", "cheque_id=?", new String[]{String.valueOf(chequeId)});
                }
            }

            //get next check date
            String query = "SELECT paydate FROM cheques WHERE paydate >= date('now') ORDER BY paydate ASC LIMIT 1";
            Cursor cursor3 = db.rawQuery(query, null);
            String nextCqDate = "No Cheques";
            if (cursor3 != null && cursor3.moveToFirst()) {
                nextCqDate = cursor3.getString(cursor3.getColumnIndex("paydate"));
                cursor.close();
            }

// get stock value
            String[] columns = {"tstock_id", "buyprice", "qty"};
            Cursor cursor4 = db.query("allstocktable", columns, null, null, null, null, null);

            double total = 0.0;

            if (cursor4.moveToFirst()) {
                do {
                    int quantity = cursor4.getInt(cursor4.getColumnIndex("qty"));
                    double buyPrice = cursor4.getDouble(cursor4.getColumnIndex("buyprice"));

                    double value = quantity * buyPrice;
                    total += value;
                } while (cursor4.moveToNext());
            }

// Setting the total value to a TextView named stockVal
            TextView stockVal = findViewById(R.id.stockVal);
            stockVal.setText(String.valueOf(total));



            cqCount.setText(String.valueOf(cqCounts));
            cqValue.setText(String.format("%.2f",cqSum));
            cqNextDate.setText(nextCqDate);
//            Cursor cursor3A=db.rawQuery("SELECT * FROM cheques WHERE date(paydate)>=date(?)",new String[]{String.valueOf(formattedDate)});
////            Cursor cursor3B=db.rawQuery("SELECT * FROM cheques WHERE date(paydate)>=date(?)",new String[]{String.valueOf(formattedDate)});
////            Cursor cursor3C=db.rawQuery("SELECT * FROM cheques WHERE date(paydate)>=date(?)",new String[]{String.valueOf(formattedDate)});
//
// //          if(cursor3A.moveToFirst() && cursor3B.moveToFirst() && cursor3C.moveToFirst()){
//            if(cursor3A.moveToFirst()){
//                    int chequesCount=cursor3A.getCount();
//                    double totalPayCq=0.0;
//
//                    do{
//                        String cqValue=cursor3A.getString(cursor3A.getColumnIndex("cqvalue"));
//                        double cqValDouble=Double.parseDouble(cqValue);
//                        totalPayCq+=cqValDouble;
//                    }while (cursor3A.moveToNext());
//                    String nextCqDate="No";
//                    if (cursor3A.moveToLast()){
//                        nextCqDate=cursor3A.getString(cursor3A.getColumnIndex("paydate"));
//                    }
//                    cqCount.setText(chequesCount); cqValue.setText(String.format("%.2f",totalPayCq));
//                    cqNextDate.setText(nextCqDate);
//
//                }else{
//
//                }
            }
//
//             cursor3A.close();
//            cursor3B.close();
//            cursor3C.close();

        else{//table not exists

        }



        db.close();

        chequesBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for chequesbox layout
                Intent intent = new Intent(ReportsActivity.this, cqsActivity.class);
                startActivity(intent);
            }
        });


        AlltimeRlayoutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 =new Intent(ReportsActivity.this, StockDataActivity.class);
                intent2.putExtra("usernameT",usernameT);
                startActivity(intent2);
            }
        });


        avgcost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ReportsActivity.this, AverageCostActivity.class);
                intent3.putExtra("usernameT",usernameT);
                startActivity(intent3);
            }
        });



    }// end of oncreate



    private double getSum(Cursor cursor, String columnName) {
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            double sum = 0.0;
            int columnIndex = cursor.getColumnIndex(columnName);
            do {
                double value = cursor.getDouble(columnIndex);
                sum += value;
            } while (cursor.moveToNext());
            return sum;
        }else {
            double sum = 0.11;
            return sum;
        }
    }

    private double getSum2(Cursor cursor, String columnName) {
        if (cursor != null && cursor.getCount() > 0){
            double sum = 0.0;
            int columnIndex = cursor.getColumnIndex(columnName);
            do {
                double value = cursor.getDouble(columnIndex);
                sum += value;
            } while (cursor.moveToNext());
            return sum;
        }else {
            double sum = 0.11;
            return sum;
        }
    }


    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] { tableName });
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}