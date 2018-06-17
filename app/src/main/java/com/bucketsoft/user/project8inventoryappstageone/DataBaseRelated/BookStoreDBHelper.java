package com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class BookStoreDBHelper  extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookStoreDBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "BookStore.db";
    private static final int DATABASE_VERSION = 1;

    public BookStoreDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_BOOKSTORE_TABLE =  "CREATE TABLE " + BookStoreContract.BookStoreEntry.TABLE_NAME + " ("
                + BookStoreContract.BookStoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BookStoreContract.BookStoreEntry.COLUMN_PRICE + " REAL NOT NULL, "
                + BookStoreContract.BookStoreEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL DEFAULT 'UNKNOWN SUPPLIER NAME', "
                + BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL DEFAULT 'UNKNOWN SUPPLIER PHONE');";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKSTORE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
