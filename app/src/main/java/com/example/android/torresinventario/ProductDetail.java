package com.example.android.torresinventario;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.torresinventario.data.Product;
import com.example.android.torresinventario.data.ProductContract;

public class ProductDetail extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent(); // gets the previously created intent
        product = (Product) intent.getSerializableExtra("product");

        TextView textViewName = (TextView) findViewById(R.id.product_detail_name);
        TextView textViewDescription = (TextView) findViewById(R.id.product_detail_description);
        TextView textViewPrice = (TextView) findViewById(R.id.product_detail_price);
        final TextView textViewStock = (TextView) findViewById(R.id.product_detail_stock);
        TextView textViewQuantityToPurchase = (TextView) findViewById(R.id.product_detail_quantity_to_purcahse);

        textViewName.setText(product.getName());
        textViewDescription.setText(product.getDescription());
        textViewPrice.setText(product.getPrice() + ""); //+"" define a new string on the fly -instead of toString
        textViewStock.setText(product.getStock() + "");
        textViewQuantityToPurchase.setText(product.getQuantityToPurchase() + "");

        Button sale = (Button) findViewById(R.id.button_sale);
        Button receive = (Button) findViewById(R.id.button_receive);
        Button order = (Button) findViewById(R.id.button_order);
        Button delete = (Button) findViewById(R.id.button_delete);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getStock() != 0) {
                    String id = ProductContract.ProductEntry._ID + " = " + product.getId();
                    product.sale();

                    ContentValues values = new ContentValues();
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getStock());
                    getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI, values, id, null);
                    textViewStock.setText(product.getStock() + "");
                }
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ProductContract.ProductEntry._ID + " = " + product.getId();
                product.receive();

                ContentValues values = new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, product.getStock());
                getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI, values, id, null);
                textViewStock.setText(product.getStock() + "");
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderProduct();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void orderProduct() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Place Order");
        intent.putExtra(Intent.EXTRA_TEXT, "This is the body of the product order");

        startActivity(Intent.createChooser(intent, "Send Email"));

        Toast.makeText(this, "Place your order", Toast.LENGTH_LONG).show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete entry");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = ProductContract.ProductEntry._ID + " = " + product.getId();
                getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, id, null);
                Intent intent = new Intent(ProductDetail.this, Inventary.class);
                startActivity(intent);
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });

        alert.show();
    }
}
