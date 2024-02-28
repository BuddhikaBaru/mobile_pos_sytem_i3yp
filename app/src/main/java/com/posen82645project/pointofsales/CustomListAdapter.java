package com.posen82645project.pointofsales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomListAdapter  extends ArrayAdapter<tstockClass> {
    public CustomListAdapter(Context context, ArrayList<tstockClass> data) {
        super(context, R.layout.custom_list_row, data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_row, null);
        }

        // Get references to the TextViews in the custom layout
        TextView prodname = view.findViewById(R.id.prodname);
        TextView qnty = view.findViewById(R.id.qnty);
        TextView sellprice = view.findViewById(R.id.sellprice);
        TextView buyprice = view.findViewById(R.id.buyprice);
        TextView barcode = view.findViewById(R.id.barcode);
        TextView expireDate = view.findViewById(R.id.expireDate);

        // Get the DataObject at the current position
        tstockClass data = getItem(position);

        // Set the values of the TextViews
        prodname.setText(data.proName);
        qnty.setText(data.Qty);
        buyprice.setText(data.buyP.toString());
        sellprice.setText(data.sellP.toString());
        expireDate.setText(data.expddt);
        barcode.setText(data.bcode);

        return view;
    }


}
