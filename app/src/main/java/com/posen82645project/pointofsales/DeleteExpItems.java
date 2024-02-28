package com.posen82645project.pointofsales;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeleteExpItems extends AppCompatActivity {

    String usernameT;

    public void onBackPressed() {
        // Add your custom logic here to control the back button behavior
        // For example, you can start a new instance of the activity to reset it
        SQLiteDatabase mydatabase = openOrCreateDatabase("minipos", Context.MODE_PRIVATE,null);
        Intent intent = new Intent(this, ExpireItems.class);
        intent.putExtra("usernameT", usernameT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_exp_items);

        SQLiteDatabase database = openOrCreateDatabase("minipos", Context.MODE_PRIVATE, null);

        // Assuming you have EditText fields for displaying values and a button for delete operation
        TextView itemIdEditText = findViewById(R.id.setId);
        TextView productNameEditText = findViewById(R.id.prodNameSet);
        TextView expDateEditText = findViewById(R.id.expSet);
        TextView quantityEditText = findViewById(R.id.qtySet);
        Button deleteButton = findViewById(R.id.deleteButton);

        // Retrieve values passed from ExpireItems activity
        Intent intent = getIntent();
        usernameT=intent.getStringExtra("usernameT");
        if (intent != null) {
            int itemId = intent.getIntExtra("itemId", 0);
            String productName = intent.getStringExtra("productName");
            String expDate = intent.getStringExtra("expDate");
            int quantity = intent.getIntExtra("quantity", 0);
            String barcodeexp=intent.getStringExtra("bcodeexp");

            // Display values in EditText fields
            itemIdEditText.setText(String.valueOf(itemId));
            productNameEditText.setText(productName);
            expDateEditText.setText(expDate);
            quantityEditText.setText(String.valueOf(quantity));

            // Perform delete operation on button click
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform delete operation in the database for the corresponding row
                    database.execSQL("DELETE FROM ExpiringItems WHERE item_id = ?", new Object[]{itemId});

                    database.execSQL("DELETE FROM allstocktable WHERE barcode = ?", new Object[]{barcodeexp});

                    // Navigate back to ExpireItems activity
                    Intent backIntent = new Intent(DeleteExpItems.this, ExpireItems.class);
                    backIntent.putExtra("usernameT",usernameT);
                    startActivity(backIntent);
                    finish();




                }
            });
        }




    }
}