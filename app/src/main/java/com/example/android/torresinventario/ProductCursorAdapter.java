package com.example.android.torresinventario;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.torresinventario.data.Product;
import com.example.android.torresinventario.data.ProductContract;
import com.example.android.torresinventario.data.ProductProvider;

public class ProductCursorAdapter extends CursorAdapter {
    private final ProductProvider mProductProvider;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mProductProvider = new ProductProvider();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Product product = mProductProvider.retriveProduct(cursor);
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
        final TextView textViewQuantity = (TextView) view.findViewById(R.id.quantity);

        textViewName.setText(name);
        textViewPrice.setText(price);
        textViewQuantity.setText(quantity);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.list_item_layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetail.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            }
        });

        Button listViewButton = (Button) view.findViewById(R.id.list_view_sale_button);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getStock() != 0) {
                    String id = ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = " + product.getId();
                    product.sale();

                    ContentValues values = new ContentValues();
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getStock());
                    context.getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI, values, id, null);
                    textViewQuantity.setText(product.getStock() + "");
                }
            }
        });
    }
}
