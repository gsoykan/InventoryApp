package com.bucketsoft.user.project8inventoryappstageone;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreContract;

public class BookDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_BOOKSTORE_LOADER = 0;

    private Uri mCurrentBookUri;


    private TextView bookNameTextView;
    private TextView bookQuantityTextView;
    private TextView bookPriceTextView;
    private TextView supplierNameTextView;
    private TextView supplierPhoneTextView;
    private Button orderFromSupplierButton;

    private Button increaseBookQuantityButton;
    private Button decreaseBookQuantityButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        getLoaderManager().initLoader(EXISTING_BOOKSTORE_LOADER, null, this);


        bookNameTextView = findViewById(R.id.book_name_text_detail);
        bookPriceTextView = findViewById(R.id.book_price_text_detail);
        bookQuantityTextView = findViewById(R.id.book_quantity_text_detail);
        supplierNameTextView = findViewById(R.id.book_supplier_name_text_detail);
        supplierPhoneTextView = findViewById(R.id.book_supplier_phone_text_detail);
        orderFromSupplierButton = findViewById(R.id.book_supplier_order_button_detail);

        increaseBookQuantityButton = findViewById(R.id.book_quantity_increase_button_detail);
        decreaseBookQuantityButton = findViewById(R.id.book_quantity_decrease_button_detail);


    }

    private View.OnClickListener decreaseQuantityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: UPDATE THE QUANTITY OF THE SELECTED ITEM, FOR THAT GET THE ID OF IT AND MAKE THE UPDATE FROM PROVIDER
            // Integer bookQuantity = cursor.getInt(cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY));
            if (Integer.valueOf(bookQuantityTextView.getText().toString()) < 1) {
                Toast.makeText(BookDetailActivity.this, R.string.book_quantity_button_toast_message, Toast.LENGTH_LONG).show();
            } else {
                int prevQuantity = Integer.valueOf(bookQuantityTextView.getText().toString());
                int newQuantity = prevQuantity - 1;
                ContentValues values = new ContentValues();
                values.put(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY, newQuantity);
                int rowsAffected = BookDetailActivity.this.getContentResolver().update(mCurrentBookUri, values, null, null);
                if (rowsAffected == 1) {
                    Toast.makeText(BookDetailActivity.this, R.string.book_sold_success, Toast.LENGTH_LONG).show();
                    bookQuantityTextView.setText(String.valueOf(newQuantity));
                }
            }
        }
    };
    private View.OnClickListener increaseQuantityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int prevQuantity = Integer.valueOf(bookQuantityTextView.getText().toString());
            int newQuantity = prevQuantity + 1;
            ContentValues values = new ContentValues();
            values.put(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY, newQuantity);
            int rowsAffected = BookDetailActivity.this.getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowsAffected == 1) {
                Toast.makeText(BookDetailActivity.this, R.string.book_increase_success, Toast.LENGTH_LONG).show();
                bookQuantityTextView.setText(String.valueOf(newQuantity));
            }
        }
    };

    private View.OnClickListener dialListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (supplierPhoneTextView.getText().toString() != "") {
                dialPhoneNumber(supplierPhoneTextView.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), R.string.call_can_not_made, Toast.LENGTH_LONG).show();
            }
        }
    };

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.product_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete_book:
                deleteBook();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void deleteBook() {

        if (mCurrentBookUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.detail_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.detail_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookStoreContract.BookStoreEntry._ID,
                BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME,
                BookStoreContract.BookStoreEntry.COLUMN_PRICE,
                BookStoreContract.BookStoreEntry.COLUMN_QUANTITY,
                BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME,
                BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE};


        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        //TODO : HANDLE ITEM LOADING
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY);
            int supNameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME);
            int supPhoneColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE);


            String name = cursor.getString(nameColumnIndex);
            String supName = cursor.getString(supNameColumnIndex);
            String supPhone = cursor.getString(supPhoneColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);

            bookNameTextView.setText(name);
            bookPriceTextView.setText(String.valueOf(price));
            bookQuantityTextView.setText(String.valueOf(quantity));
            supplierNameTextView.setText(supName);
            supplierPhoneTextView.setText(supPhone);


        }

        orderFromSupplierButton.setOnClickListener(dialListener);
        decreaseBookQuantityButton.setOnClickListener(decreaseQuantityListener);
        increaseBookQuantityButton.setOnClickListener(increaseQuantityListener);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookNameTextView.setText("");
        bookPriceTextView.setText("");
        bookQuantityTextView.setText("");
        supplierNameTextView.setText("");
        supplierPhoneTextView.setText("");
    }
}
