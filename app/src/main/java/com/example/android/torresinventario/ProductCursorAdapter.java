package com.example.android.torresinventario;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.torresinventario.data.Product;
import com.example.android.torresinventario.data.ProductContract;
import com.example.android.torresinventario.data.ProductProvider;

public class ProductCursorAdapter extends CursorAdapter {
    private ProductProvider productProvider;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        productProvider = new ProductProvider();
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        Product product = productProvider.retriveProduct(cursor);

        TextView textViewName = (TextView) view.findViewById(R.id.name);
        TextView textViewPrice = (TextView) view.findViewById(R.id.price);
        TextView textViewQuantity = (TextView) view.findViewById(R.id.quantity);

        //Find the columns pf Product attributes that we are interested in
        int n = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int p = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int q = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Extract properties from cursor
        String name = cursor.getString(n);
        String price = cursor.getString(p);
        String quantity = cursor.getString(q);

        // Populate fields with extracted properties
        textViewName.setText(name);
        textViewPrice.setText(price);
        textViewQuantity.setText(quantity);
    }
}
