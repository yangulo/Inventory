package com.example.android.torresinventario.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    //Name of the database file.
    public static final String DATABASE_NAME = "ProductInventory.db";

    //Database version. If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table.
        String SQL_CREATE_PRODUCT_INFORMATION_TABLE =
                "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME
                        + " ("
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE + " TEXT, "
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT NOT NULL, "
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                        + ProductContract.ProductEntry.COLUMN_PRODUCT_PURCHASE_QUANTITY + " INTEGER NOT NULL"
                        + ");";
        db.execSQL(SQL_CREATE_PRODUCT_INFORMATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
