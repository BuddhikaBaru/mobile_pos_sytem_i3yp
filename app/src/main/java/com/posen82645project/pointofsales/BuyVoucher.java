package com.posen82645project.pointofsales;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BuyVoucher extends AppCompatActivity {

    //TextInputEditText paymentDateEditText    vouDate
    TextInputEditText vouDate, supName, totVal, paymentDateEditText1;
    Button next, cancel,mPickDateButton;
    RadioGroup cocGroup;
    RadioButton radioCash;
    RadioButton radioCheque;

    //String voucherDate pyDate
    String voucherDate, suppName, pyDate1,totalVal,csch, usernameT;
    TextView mShowSelectedDateText, unbox;




    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        Intent intent = new Intent(this, Main.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_voucher);

        //intent to get data from before
        Intent getFrom=getIntent();
        usernameT=getFrom.getStringExtra("usernameT");


        vouDate =findViewById(R.id.stockDate);
        supName=findViewById(R.id.suppName);
        totVal=findViewById(R.id.totVal);
        //paymentDateEditText1 =findViewById(R.id.payDate);
        cocGroup=findViewById(R.id.cocGroup);
        next=findViewById(R.id.nextB);
        cancel=findViewById(R.id.cancelVou);
        radioCash = findViewById(R.id.cash);
        radioCheque = findViewById(R.id.cheque);
        mPickDateButton=findViewById(R.id.pick_date_button);
        mShowSelectedDateText=findViewById(R.id.show_selected_date);
        unbox=findViewById(R.id.unbox);
        unbox.setText(usernameT);


        String billdateToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        vouDate.setText(billdateToday);

        cocGroup.clearCheck();

//        cocGroup.setOnCheckedChangeListener((radioGroup, i) -> {
//            RadioButton coc = (RadioButton) radioGroup.findViewById(i);
//            if (coc.getText()=="Cash"){
//                payDate.setText(date_now);
//            }
//            else {
//                payDate.setText("");
//            }
//        });



        // calender for payment date
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("Select Payment Date");
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

                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        mShowSelectedDateText.setText( materialDatePicker.getHeaderText());
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });






        radioCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // "Cash" radio button is selected, set the payment date to the current date
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


                    mShowSelectedDateText.setText(currentDate);
                    mShowSelectedDateText.setEnabled(false); // Disable editing
                    csch="cash";
                }
            }
        });

        radioCheque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mShowSelectedDateText.setText("Select Cheque Date");
                    // "Cheque" radio button is selected, clear the payment date and request focus
//                    paymentDateEditText1.setText("");
//                    paymentDateEditText1.setEnabled(true); // Enable editing
//                    paymentDateEditText1.requestFocus(); // Request focus for manual input
                    csch="cheque";
                }
            }
        });



        next.setOnClickListener(view -> {

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String paydate=mShowSelectedDateText.getText().toString();
            String formattedPayDate=null;

            try {
                Date payDateFormatted = null;
                payDateFormatted = inputDateFormat.parse(paydate);
                formattedPayDate = outputDateFormat.format(payDateFormatted);
                Log.d("formattedPayDateTAG", "Value of formattedPayDate: " + formattedPayDate);
            }catch (Exception e){
                e.printStackTrace();
            }



//            String cqDatesql = outputFormat.format(mShowSelectedDateText.getText().toString());


            voucherDate = vouDate.getText().toString();
            suppName=supName.getText().toString();
            totalVal = totVal.getText().toString();
            pyDate1 = formattedPayDate;
            Log.d("formattedPayDateOutofTryCatchTAG", "Value of formattedPayDate outside trycatch: " + formattedPayDate);
//            pyDate1 = mShowSelectedDateText.getText().toString();
            usernameT=unbox.getText().toString();


            if ((!voucherDate.isEmpty() && !suppName.isEmpty() && !totalVal.isEmpty()) && (!pyDate1.equals("Payment Date") && !pyDate1.equals("Select Cheque Date"))){
                Intent toAddstock =new Intent(BuyVoucher.this, AddStock.class);


                // Intent set send to
                toAddstock.putExtra("usernameT",usernameT);
                toAddstock.putExtra("voucherDate", voucherDate);
                toAddstock.putExtra("suppName",suppName);
                toAddstock.putExtra("totalVal",totalVal);
                toAddstock.putExtra("pyDate", pyDate1);
                toAddstock.putExtra("csch",csch);
                startActivity(toAddstock);
                finish();

            }else {
                Toast.makeText(BuyVoucher.this, "Something is empty", Toast.LENGTH_SHORT).show();
            }

            //new Intent


        });

        cancel.setOnClickListener(view -> {
            Intent i = new Intent(BuyVoucher.this, Main.class);
            i.putExtra("usernameT",usernameT);
            startActivity(i);
            finish();
        });

        // Apply the InputFilter to the TextInputEditText
//        paymentDateEditText1.setFilters(new InputFilter[]{dateFilter});
//        vouDate1.setFilters(new InputFilter[]{dateFilter});








    }// end of on create method
}