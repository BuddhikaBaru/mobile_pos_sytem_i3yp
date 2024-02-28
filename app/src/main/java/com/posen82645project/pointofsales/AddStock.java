package com.posen82645project.pointofsales;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//uhiuihui

public class AddStock extends AppCompatActivity {

    String usernameT;
    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        mydatabase.execSQL("DROP TABLE IF EXISTS tempstocktable");
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    TextInputEditText barcodeText, buyP,sellP, qty;
    Spinner prodSpinner;
    ArrayList<String> titles=new ArrayList<>();
    ArrayAdapter arrayAdapter;

    Button anotherB, cancel, finish,mPickDateButton;
    String voucherDate, suppName, totalVal, pyDate,csch;
    TextView expDt, unbox;
    Cursor cursorProds;

    private CompoundBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        barcodeText=findViewById(R.id.barCode); buyP=findViewById(R.id.buyPrice); sellP=findViewById(R.id.sellPrice); expDt = findViewById(R.id.expireDate); qty=findViewById(R.id.qty);
        prodSpinner=findViewById(R.id.prodSpinner); anotherB=findViewById(R.id.anothStock); cancel=findViewById(R.id.cancelStockButton); finish=findViewById(R.id.finishAdd);
        barcodeScannerView = findViewById(R.id.barcodeScannerView);
        mPickDateButton=findViewById(R.id.pick_date_button);
        unbox=findViewById(R.id.unbox);





        //Button scanButton = findViewById(R.id.scanButton);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        // get intent from voucher page
        //intent to get data from before



        Intent getFrom = getIntent();
        voucherDate  = getFrom.getStringExtra("voucherDate"); suppName = getFrom.getStringExtra("suppName");
        totalVal = getFrom.getStringExtra("totalVal"); pyDate = getFrom.getStringExtra("pyDate"); csch = getFrom.getStringExtra("csch");
        usernameT=getFrom.getStringExtra("usernameT");

        unbox.setText(usernameT);



        // select from product spinner
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS producttable(product_id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR, category VARCHAR, productdescription VARCHAR)");
        cursorProds =mydatabase.rawQuery("SELECT * FROM producttable", null);
        int productName=cursorProds.getColumnIndex("product");
        titles.clear();

        arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        prodSpinner.setAdapter(arrayAdapter);

        final ArrayList<prodClass> productList = new ArrayList<>();
        if (cursorProds.moveToFirst()){

            do {
                prodClass items = new prodClass();
                items.productName = cursorProds.getString(productName);
                productList.add(items);
                titles.add(cursorProds.getString(productName));
            }while (cursorProds.moveToNext());
            arrayAdapter.notifyDataSetChanged();
        }
        // end spinner product



        // Pick date

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("Select expire Date");
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();


        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        String mShowSelectedDateText=materialDatePicker.getHeaderText();



                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String paydate=mShowSelectedDateText.toString();
                        String formattedPayDate=null;

                        try {
                            Date payDateFormatted = null;
                            payDateFormatted = inputDateFormat.parse(paydate);
                            formattedPayDate = outputDateFormat.format(payDateFormatted);
                            Log.d("formattedPayDateTAG", "Value of formattedPayDate: " + formattedPayDate);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        expDt.setText(formattedPayDate);
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });




        //add another inventory
        anotherB.setOnClickListener(view -> {

            if ((!barcodeText.getText().toString().isEmpty() && !buyP.getText().toString().isEmpty() && !sellP.getText().toString().isEmpty() && !expDt.getText().toString().equals("select expire date") && (!qty.getText().toString().isEmpty()))){

                if (cursorProds.moveToFirst()){
                    insertStock();
                    Intent againAdd=new Intent(getApplicationContext(), AddStock.class);
                    againAdd.putExtra("voucherDate",voucherDate);
                    againAdd.putExtra("suppName",suppName);
                    againAdd.putExtra("totalVal",totalVal);
                    againAdd.putExtra("pyDate",pyDate );
                    againAdd.putExtra("csch",csch);
                    againAdd.putExtra("usernameT",usernameT);
                    startActivity(againAdd);
                    finish();


                }else {
                    Toast.makeText(AddStock.this, "No Products", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(AddStock.this, "Something is Empty", Toast.LENGTH_SHORT).show();
            }
        });




        //cancel button
        cancel.setOnClickListener(view -> {
            SQLiteDatabase mydatabase1 = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            mydatabase1.execSQL("DROP TABLE IF EXISTS tempstocktable");
            Intent toMainActivity =new Intent(AddStock.this, Main.class);
            toMainActivity.putExtra("usernameT",usernameT);
            startActivity(toMainActivity);
            mydatabase1.close();
        });


        //finish button
        finish.setOnClickListener(view -> {
            if ((!barcodeText.getText().toString().isEmpty() && !buyP.getText().toString().isEmpty() && !sellP.getText().toString().isEmpty() && !expDt.getText().toString().equals("select expire date") && (!qty.getText().toString().isEmpty()))){
              if (cursorProds.moveToFirst()){
                  insertAndNext();
                  Intent toViewBefAdd=new Intent(getApplicationContext(), InventoryActivity.class);
                  toViewBefAdd.putExtra("voucherDate",voucherDate);
                  toViewBefAdd.putExtra("suppName",suppName);
                  toViewBefAdd.putExtra("totalVal",totalVal);
                  toViewBefAdd.putExtra("pyDate",pyDate );
                  toViewBefAdd.putExtra("csch",csch);
                  toViewBefAdd.putExtra("usernameT",usernameT);
                  startActivity(toViewBefAdd);
                  finish();
              }else {Toast.makeText(AddStock.this, "No Products", Toast.LENGTH_SHORT).show();}
            }else {
                SQLiteDatabase mydatabase12 = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
                mydatabase12.execSQL("CREATE TABLE IF NOT EXISTS tempstocktable(tstock_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR, buydate VARCHAR, buyprice REAL,  sellprice REAL, expdate VARCHAR, qty INTIGER, user VARCHAR)");
                Intent toViewBefAdd=new Intent(getApplicationContext(), InventoryActivity.class);
                toViewBefAdd.putExtra("voucherDate",voucherDate);
                toViewBefAdd.putExtra("suppName",suppName);
                toViewBefAdd.putExtra("totalVal",totalVal);
                toViewBefAdd.putExtra("pyDate",pyDate );
                toViewBefAdd.putExtra("csch",csch);
                toViewBefAdd.putExtra("usernameT",usernameT);
                startActivity(toViewBefAdd);
                finish();
            }
        });


        barcodeScannerView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                // Handle the scanned barcode value
                String scannedBarcode = result.getText();

                // Fill the barcode EditText
                barcodeText.setText(scannedBarcode);

                // You can also stop the scanning process here
                // barcodeScannerView.pause();
            }
        });

//        scanButton.setOnClickListener(view -> {
//            // Start scanning when the button is clicked
//            barcodeScannerView.resume();
//        });



        // Input Filter for date

        // Apply the InputFilter to the TextInputEditText
//        expDt.setFilters(new InputFilter[]{dateFilter});







    } //end of oncreate


public void insertStock(){

        try {
            String stockBarCode= barcodeText.getText().toString();
            String buyPrice=buyP.getText().toString();
            String sellPrice=sellP.getText().toString();
            String expireD=expDt.getText().toString();
            String quantity=qty.getText().toString();
            String product=prodSpinner.getSelectedItem().toString();
            //String vddate=voucherDate.toString();
            String vddate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());





            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tempstocktable(tstock_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR, buydate VARCHAR, buyprice REAL,  sellprice REAL, expdate VARCHAR, qty INTIGER, user VARCHAR)");


            String sqlTempStock= "INSERT INTO tempstocktable (barcode, productname, buydate, buyprice, sellprice, expdate, qty, user)VALUES(?,?,?,?,?,?,?,?)";
            SQLiteStatement statement = mydatabase.compileStatement(sqlTempStock);
            statement.bindString(1, stockBarCode);
            statement.bindString(2, product);
            statement.bindString(3, vddate);
            statement.bindString(4, buyPrice);
            statement.bindString(5, sellPrice);
            statement.bindString(6, expireD);
            statement.bindString(7, quantity);
            statement.bindString(8, usernameT);
            statement.execute();
            mydatabase.close();
//            Toast.makeText(this,"Stock Success", Toast.LENGTH_SHORT).show();
            barcodeText.setText("");
            buyP.setText("");
            sellP.setText("");
            expDt.setText("");
            qty.setText("");
            buyP.requestFocus();

        }
        catch (Exception exception){

        }

}// end of Insert


    public void insertAndNext(){

        try {
            String stockBarCode= barcodeText.getText().toString();
            String buyPrice=buyP.getText().toString();
            String sellPrice=sellP.getText().toString();
            String expireD=expDt.getText().toString();
            String quantity=qty.getText().toString();
            String product=prodSpinner.getSelectedItem().toString();
            //String vddate=voucherDate.toString();
            String vddate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());




            SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tempstocktable(tstock_id INTEGER PRIMARY KEY AUTOINCREMENT, barcode VARCHAR, productname VARCHAR, buydate VARCHAR, buyprice REAL,  sellprice REAL, expdate VARCHAR, qty INTIGER, user VARCHAR)");


            String sqlTempStock= "INSERT INTO tempstocktable (barcode, productname, buydate, buyprice, sellprice, expdate, qty, user)VALUES(?,?,?,?,?,?,?,?)";
            SQLiteStatement statement = mydatabase.compileStatement(sqlTempStock);
            statement.bindString(1, stockBarCode);
            statement.bindString(2, product);
            statement.bindString(3, vddate);
            statement.bindString(4, buyPrice);
            statement.bindString(5, sellPrice);
            statement.bindString(6, expireD);
            statement.bindString(7, quantity);
            statement.bindString(8, usernameT);
            statement.execute();
            Toast.makeText(this,"Stock Success", Toast.LENGTH_LONG).show();
            mydatabase.close();



        }
        catch (Exception exception){

        }
    }//end of insert and next method



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