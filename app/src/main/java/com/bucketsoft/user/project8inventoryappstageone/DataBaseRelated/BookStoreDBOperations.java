package com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.bucketsoft.user.project8inventoryappstageone.R;

public final class BookStoreDBOperations {

    private BookStoreDBOperations() {
    }

    public static void insertBookInstance(BookStoreDBHelper mDbHelper, String productName, Double price, int quantity, String supplierName, String supplierPhoneNumber) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_PRICE, price);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY, quantity);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneNumber);

        long newRowId = db.insert(BookStoreContract.BookStoreEntry.TABLE_NAME, null, values);


    }

    public static void displayDBInfo(BookStoreDBHelper mDbHelper) {


        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BookStoreContract.BookStoreEntry._ID,
                BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME,
                BookStoreContract.BookStoreEntry.COLUMN_PRICE,
                BookStoreContract.BookStoreEntry.COLUMN_QUANTITY,
                BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME,
                BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                BookStoreContract.BookStoreEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        try {

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY);
            int supNameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME);
            int supPhoneColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                Double currentPrice = cursor.getDouble(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupName = cursor.getString(supNameColumnIndex);
                String currentSupPhone = cursor.getString(supPhoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                Log.d("DB_DEBUG", ("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupName + " - " + currentSupPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }


    }

}
