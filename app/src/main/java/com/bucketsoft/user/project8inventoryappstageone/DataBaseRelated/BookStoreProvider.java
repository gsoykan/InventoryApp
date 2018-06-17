package com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.net.ssl.SSLProtocolException;

public class BookStoreProvider extends ContentProvider {

    public static final String LOG_TAG = BookStoreProvider.class.getSimpleName();
    private static final int BOOK_STORE_ALL_CODE = 100;
    private static final int BOOK_STORE_SPECIFIC_CODE = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_BOOKSTORE, BOOK_STORE_ALL_CODE);
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_BOOKSTORE + "/#", BOOK_STORE_SPECIFIC_CODE);
    }

    private BookStoreDBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new BookStoreDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase bookstoreDB = mDBHelper.getReadableDatabase();
        Cursor cursor;

        int uriMatchResultCode = sUriMatcher.match(uri);
        switch (uriMatchResultCode) {
            case BOOK_STORE_ALL_CODE:
                cursor = bookstoreDB.query(BookStoreContract.BookStoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_STORE_SPECIFIC_CODE:
                selection = BookStoreContract.BookStoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = bookstoreDB.query(BookStoreContract.BookStoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_STORE_ALL_CODE:
                return BookStoreContract.BookStoreEntry.CONTENT_LIST_TYPE;
            case BOOK_STORE_SPECIFIC_CODE:
                return BookStoreContract.BookStoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_STORE_ALL_CODE:
                return insertBookToBookstore(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
    }

    private Uri insertBookToBookstore(Uri uri, ContentValues contentValues) {
        String productName = contentValues.getAsString(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("BookStore entry - Book - requires a name ! ");
        }
        Double price = contentValues.getAsDouble(BookStoreContract.BookStoreEntry.COLUMN_PRICE);
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price can not have value less than O ! ");
        }
        Integer quantity = contentValues.getAsInteger(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY);
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be less than 0 ! ");
        }
        String supName = contentValues.getAsString(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME);
        String supPhone = contentValues.getAsString(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE);
        if (supName == null || supPhone == null) {
            throw new IllegalArgumentException("Please provide valid Supplier Information ! ");
        }

        SQLiteDatabase bookstoreDB = mDBHelper.getWritableDatabase();

        long id = bookstoreDB.insert(BookStoreContract.BookStoreEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row to BOOKSTORE DB the URI is  " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase bookstoreDB = mDBHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_STORE_ALL_CODE:
                rowsDeleted = bookstoreDB.delete(BookStoreContract.BookStoreEntry.TABLE_NAME, s, strings);
                break;
            case BOOK_STORE_SPECIFIC_CODE:
                s = BookStoreContract.BookStoreEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = bookstoreDB.delete(BookStoreContract.BookStoreEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("this uri can not be operated on " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_STORE_ALL_CODE:
                return updateBookstore(uri, contentValues, s, strings);
            case BOOK_STORE_SPECIFIC_CODE:
                s = BookStoreContract.BookStoreEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBookstore(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("This URI can not be used for update " + uri);
        }
    }

    private int updateBookstore(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {


        if (contentValues.containsKey(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME)) {
            String name = contentValues.getAsString(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("a BOOK needs a name !");
            }
        }


        if (contentValues.containsKey(BookStoreContract.BookStoreEntry.COLUMN_PRICE)) {
            Double price = contentValues.getAsDouble(BookStoreContract.BookStoreEntry.COLUMN_PRICE);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Price requires valid value!");
            }
        }


        if (contentValues.containsKey(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY)) {

            Integer quantity = contentValues.getAsInteger(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY);
            if (quantity == null || quantity < 0) {
                throw new IllegalArgumentException("Book should have valid quantity !");
            }
        }

        if (contentValues.containsKey(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME)) {
            String supName = contentValues.getAsString(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME);
            if (supName == null) {
                throw new IllegalArgumentException("a SUPPLIER needs a name !");
            }
        }

        if (contentValues.containsKey(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE)) {
            String supPhone = contentValues.getAsString(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE);
            if (supPhone == null) {
                throw new IllegalArgumentException("a SUPPLIER needs a valid phone !");
            }
        }

        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase bookstoreDB = mDBHelper.getWritableDatabase();

        int rowsUpdated = bookstoreDB.update(BookStoreContract.BookStoreEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
