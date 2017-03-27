package com.example.android.torresinventario;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.torresinventario.data.ProductContract;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private EditText mPriceEditText;
    private EditText mStockEditText;
    private EditText mQuantityToPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_product_description);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mStockEditText = (EditText) findViewById(R.id.edit_product_stock);
        mQuantityToPurchase = (EditText) findViewById(R.id.edit_product_quantity_to_purchase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Insert Product
                insertProduct();
                // Exist activity
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertProduct() {
        String nameString = mNameEditText.getText().toString().trim();
        String descriptionString = mDescriptionEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String stockString = mStockEditText.getText().toString().trim();
        String quantityToPurchaseString = mQuantityToPurchase.getText().toString().trim();

        boolean flag = dataValidation(nameString, descriptionString, priceString, stockString,
                quantityToPurchaseString);

        if (flag) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION, descriptionString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, stockString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PURCHASE_QUANTITY, quantityToPurchaseString);

        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, "insert product failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "insert product successful", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean dataValidation
            (String name, String description, String price, String stock, String quantityToPurchase) {

        boolean flag = false;

        if (name.length() == 0 && description.length() == 0 && price.length() == 0 && stock.length() == 0
                && quantityToPurchase.length() == 0) {
            flag = true;
            Toast.makeText(this, "You can't create an empty product", Toast.LENGTH_SHORT).show();
        } else {

            if (name.length() == 0) {
                flag = true;
                Toast.makeText(this, "Product requires a name", Toast.LENGTH_SHORT).show();
            }
            if (description.length() == 0) {
                flag = true;
                Toast.makeText(this, "Product requires description", Toast.LENGTH_SHORT).show();
            }
            if (price.length() == 0 || isNotInteger(price)) {
                flag = true;
                Toast.makeText(this, "Product requires a valid price", Toast.LENGTH_SHORT).show();
            }
            if (stock.length() == 0 || isNotInteger(stock)) {
                flag = true;
                Toast.makeText(this, "Product requires valid value for stock", Toast.LENGTH_SHORT).show();
            }
            if (quantityToPurchase.length() == 0 || isNotInteger(quantityToPurchase)) {
                flag = true;
                Toast.makeText(this, "Product requires valid value for quantity to purchase", Toast.LENGTH_SHORT).show();
            }
        }
        return flag;
    }

    private boolean isNotInteger(String text) {
        return !isInteger(text);
    }

    private boolean isInteger(String text) {
        boolean flag = false;
        try {
            Integer.valueOf(text);
            flag = true;
        } catch (NumberFormatException exc) {
        }
        return flag;
    }
}
