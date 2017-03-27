package com.example.android.torresinventario;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.torresinventario.data.Product;
import com.example.android.torresinventario.data.ProductContract;

public class ProductDetail extends AppCompatActivity {

    public static final String LOG_TAG = ProductDetail.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Product product;
    private ImageView mImageView;
    private Uri mUri;

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
        mImageView = (ImageView) findViewById(R.id.product_picture);

        if (product.getImageUri() != null && product.getImageUri().length() > 0) {
            mUri = Uri.parse(product.getImageUri());
            mImageView.setImageURI(mUri);
        }

        textViewName.setText(product.getName());
        textViewDescription.setText(product.getDescription());
        textViewPrice.setText(product.getPrice() + ""); //+"" define a new string on the fly -instead of toString
        textViewStock.setText(product.getStock() + "");
        textViewQuantityToPurchase.setText(product.getQuantityToPurchase() + "");

        Button sale = (Button) findViewById(R.id.button_sale);
        Button receive = (Button) findViewById(R.id.button_receive);
        Button order = (Button) findViewById(R.id.button_order);
        Button delete = (Button) findViewById(R.id.button_delete);
        Button addPhoto = (Button) findViewById(R.id.add_photo);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getStock() != 0) {
                    String id = ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = " + product.getId();
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
                String id = ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = " + product.getId();
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

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });
    }

    private void orderProduct() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Place Order for " + product.getName() + ".");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Product Name: " + product.getName() + "\n" +
                        "Product Description: " + product.getDescription() + "\n" +
                        "Product Price: " + product.getPrice() + "\n" + "Quantity to Purchase: " + product.getQuantityToPurchase());

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
                String id = ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = " + product.getId();
                getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, id, null);
                Intent intent = new Intent(ProductDetail.this, Inventory.class);
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

    public void openImageSelector() {
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Log.i(LOG_TAG, "Received an \"Activity Result\"");
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                mUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + mUri.toString());

                String id = ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " = " + product.getId();
                ContentValues values = new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, mUri.toString());
                getContentResolver().update(ProductContract.ProductEntry.CONTENT_URI, values, id, null);
                mImageView.setImageURI(mUri);
            }
        }
    }
}

