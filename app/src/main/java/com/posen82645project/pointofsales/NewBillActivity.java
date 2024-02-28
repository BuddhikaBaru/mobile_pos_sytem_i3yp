package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewBillActivity extends AppCompatActivity {

    String usernameT;


    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    TextView bcode, prodName, unitPrice, Amount,totValue, tempcartval;
    EditText cusName,qtyu, prodNameM,unitPriceM,totPriceM,qtyuM;

    Button scan, addCart,addCartM, checkout;


    private CompoundBarcodeView barcodeScannerView;
    int qty=0;
    double totalcartvalue;
    double totalCost;
    double totalcartcost;
    double temp=0.0;

    double tempCartValue;
    String currentDate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);

        // Get intent from previous
        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");
        totalcartvalue = getFrom.getDoubleExtra("totalcartvalue",0);
        totalcartcost = getFrom.getDoubleExtra("totalcartcost",0);


        bcode = findViewById(R.id.barcode); prodName= findViewById(R.id.prodName); unitPrice = findViewById(R.id.unitPrice); Amount= findViewById(R.id.amount); totValue= findViewById(R.id.totValue);
        qtyu= findViewById(R.id.qty); prodNameM= findViewById(R.id.prodNameM); totPriceM= findViewById(R.id.totPriceM);
        scan= findViewById(R.id.scanButton); addCart= findViewById(R.id.addtocart); addCartM= findViewById(R.id.addtocartM); checkout= findViewById(R.id.checkout);
        barcodeScannerView= findViewById(R.id.barcodeScannerView);
        tempcartval=findViewById(R.id.tempCartVal);
        totValue.setText(String.format("%2f",totalcartvalue));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }



        ///////Functions   ////////////////////////////////////////////////////
        barcodeScannerView.decodeSingle(result -> {




            // Handle the scanned barcode value
            String scannedBarcode = result.getText();

            // Fill the barcode EditText
            bcode.setText(scannedBarcode);

                // You can also stop the scanning process here
            // barcodeScannerView.pause();
        });//end of barcode view function


        // check stock for relevent barcode
        scan.setOnClickListener(view -> {
            if (!qtyu.toString().equals("")){

                String scannedBarcode= (String) bcode.getText();
                SQLiteDatabase mydatabase=openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
                String[] columns = {"productname","buyprice", "sellprice", "qty"};
                String selection = "barcode = ?";
                String[] selectionArgs = {scannedBarcode};

                Cursor cursorx=mydatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='allstocktable'", null);


                if(cursorx!=null && cursorx.getCount()>0){

                    cursorx.close();
                    Cursor cursor = mydatabase.query("allstocktable", columns, selection, selectionArgs, null, null, null);

                    if (cursor!=null){
                        try {
                            if (cursor.moveToFirst()) {
                                // Product details found in the database
                                String productName = cursor.getString(cursor.getColumnIndex("productname"));
                                double sellPrice = cursor.getDouble(cursor.getColumnIndex("sellprice"));
                                double buyPrice = cursor.getDouble(cursor.getColumnIndex("buyprice"));
                                int availableQty = cursor.getInt(cursor.getColumnIndex("qty"));

                                // Get the user-entered quantity
                                int qty = Integer.parseInt(qtyu.getText().toString());

                                // Calculate the total amount
                                double totalAmount = sellPrice * qty;
                                totalCost = buyPrice * qty;

                                // Display the retrieved data in TextViews
                                prodName.setText(productName);
                                unitPrice.setText(String.valueOf(sellPrice));
                                Amount.setText(String.valueOf(totalAmount));

                                temp=totalcartvalue+totalAmount;
                                tempcartval.setText(String.format("%2f",temp));
                                mydatabase.close();
                                cursor.close();


                            }
                            else {
                                // Product not found in the database
                                // You can handle this case as per your requirements, e.g., display an error message.
                            }

                        } catch (Exception e) {

                        }
                    }
                }
//                cursor.close();
            }
            // Start scanning when the button is clicked
        });


        // Add cart Button
        addCart.setOnClickListener(view -> {


            if ((!bcode.getText().toString().equals("")) && (!prodName.getText().toString().equals("")) && !unitPrice.getText().toString().equals("")
                    && !Amount.getText().toString().equals("") && !totValue.getText().toString().equals("") && !qtyu.getText().toString().equals("")){
                String scannedBarcode =bcode.getText().toString();
                String productName =prodName.getText().toString();
                String sellPrice =unitPrice.getText().toString();
                String totalAmount =Amount.getText().toString();
                String qty =qtyu.getText().toString();

               String cost=String.format("%2f",totalCost);

                //Add details to new bill table
                SQLiteDatabase mydatabase=openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);

                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cartItems(item_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR,  sellprice VARCHAR, amount REAL, qty VARCHAR, selldate VARCHAR, buycost REAL, user VARCHAR)");
                String sqlNewBill= "INSERT INTO cartItems (barcode, productname, sellprice, amount, qty, selldate,buycost, user)VALUES(?,?,?,?,?,?,?,?)";
                SQLiteStatement statement = mydatabase.compileStatement(sqlNewBill);
                statement.bindString(1, scannedBarcode);
                statement.bindString(2, productName);
                statement.bindString(3, sellPrice);
                statement.bindString(4, totalAmount);
                statement.bindString(5, qty);
                statement.bindString(6, currentDate1.toString());
                statement.bindString(7, cost);
                statement.bindString(8, usernameT);
                statement.execute();

                totalcartvalue= Double.parseDouble(totalAmount)+totalcartvalue;
                totalcartcost=totalCost+totalcartcost;
//                String totalString = String.format("%.2f", totalcartvalue);


//                Cursor totCursor = mydatabase.rawQuery("SELECT SUM(amount) AS totalcartvalue FROM cartitems", null);
//
//                if (totCursor.moveToFirst()) {
//                    totalcartvalue = totCursor.getDouble(totCursor.getColumnIndex("totalcartvalue")); // Get the sum of the "amount" column
//                }

                // Close the cursor
//                totCursor.close();


                // Display the total amount in the "totValue" TextView
//                totValue.setText((totalString));
                mydatabase.close();
                Intent addItemtoCart=new Intent(getApplicationContext(), NewBillActivity.class);
                addItemtoCart.putExtra("usernameT",usernameT);
                addItemtoCart.putExtra("totalcartvalue",totalcartvalue);
                addItemtoCart.putExtra("totalcartcost",totalcartcost);

                startActivity(addItemtoCart);

            }
            else{
                //add else for empty inputs automatic entering
                Toast.makeText(this, "Something is empty", Toast.LENGTH_SHORT).show();

            }

        });

//
//        addCartM.setOnClickListener(view -> {
//            if (!prodNameM.getText().toString().equals("") || !totPriceM.getText().toString().equals("")){
//                String productN=prodName.getText().toString();
//                String totpriceM=totPriceM.getText().toString();
//                String scannedBarcode ="N";
//                String sellP= "N";
//                String qty="N";
//
//                SQLiteDatabase mydatabase=openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
//
//                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cartItems(_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR,  sellprice VARCHAR, amount REAL, qty VARCHAR)");
//                String sqlNewBill= "INSERT INTO cartItems (barcode, productname, sellprice,amount, qty)VALUES(?,?,?,?,?)";
//                SQLiteStatement statement = mydatabase.compileStatement(sqlNewBill);
//                statement.bindString(1, scannedBarcode);
//                statement.bindString(2, productN);
//                statement.bindString(3, sellP);
//                statement.bindString(4, totpriceM);
//                statement.bindString(4, qty);
//                statement.execute();
//
//
////                Cursor totCursor = mydatabase.rawQuery("SELECT SUM(amount) FROM cartitems", null);
////                double totalCartAmount = 0.0;
////                if (totCursor.moveToFirst()) {
////                    totalCartAmount = totCursor.getDouble(0); // Get the sum of the "amount" column
////                }
////
////                // Close the cursor
////                totCursor.close();
//                Double totalAm=Double.parseDouble(totpriceM);
//
//                // Display the total amount in the "totValue" TextView
//                totalcartvalue= totalAm+totalcartvalue;
////                String totalString = String.format("%.2f", totalcartvalue);
//////                totValue.setText((int) totalcartvalue);
////                prodNameM.setText("");
////                totPriceM.setText("");
//                mydatabase.close();
//
//
//
//            }else {

//            }
//        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.parseDouble(totValue.getText().toString())!=0){
                    Intent billView = new Intent(getApplicationContext(), ViewBill.class);
                    billView.putExtra("usernameT",usernameT);
                    billView.putExtra("totalcartvalue",totalcartvalue);
                    billView.putExtra("totalcartcost",totalcartcost);
                    startActivity(billView);
                }
                else {
                    Toast.makeText(NewBillActivity.this, "No Items in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }// end of Oncreate

    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        barcodeScannerView.pause();
        super.onPause();
    }



}