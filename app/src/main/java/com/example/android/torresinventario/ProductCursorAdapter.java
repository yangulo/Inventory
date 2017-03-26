package com.example.android.torresinventario;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.torresinventario.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        //Find the columns pf Product attributes that we are interested in
        int n = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int p = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int q = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Extract properties from cursor
        String name = cursor.getString(n);
        String price = cursor.getString(p);
        String quantity = cursor.getString(q);

        // Populate fields with extracted properties
        TextView textViewName = (TextView) view.findViewById(R.id.name);
        TextView textViewPrice = (TextView) view.findViewById(R.id.price);
        TextView textViewQuantity = (TextView) view.findViewById(R.id.quantity);

        textViewName.setText(name);
        textViewPrice.setText(price);
        textViewQuantity.setText(quantity);
    }
}
