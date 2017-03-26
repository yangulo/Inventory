package com.example.android.torresinventario;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.torresinventario.data.Product;
import com.example.android.torresinventario.data.ProductContract;
import com.example.android.torresinventario.data.ProductProvider;

public class Inventary extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = Inventary.class.getSimpleName();
    private static final int PRODUCT_LOADER = 0;
    private ProductCursorAdapter mProductCursorAdapter;
    private ProductProvider mProductProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torres_inventario);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inventary.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mProductCursorAdapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(mProductCursorAdapter);
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

        mProductProvider = new ProductProvider();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mProductCursorAdapter.getItem(position);
                Product product = mProductProvider.retriveProduct(cursor);
                Intent intent = new Intent(view.getContext(), ProductDetail.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;

            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllEntries();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertProduct() {
        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.PRODUCT_IMAGE_ID, "");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Castrol Oil");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION, "Car Oil");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, "20");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, "10");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PURCHASE_QUANTITY, "10");

        // Insert the new row, returning the primary key value of the new row
        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
    }

    private void deleteAllEntries() {
        int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, null, null);
        Log.v("Inventary", rowsDeleted + " rows deleted from ProductInventory database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PURCHASE_QUANTITY
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                ProductContract.ProductEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mProductCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mProductCursorAdapter.swapCursor(null);
    }
}
