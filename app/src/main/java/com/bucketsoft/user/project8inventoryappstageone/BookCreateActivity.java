package com.bucketsoft.user.project8inventoryappstageone;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreContract;

public class BookCreateActivity extends AppCompatActivity {


    private EditText bookNameEditText;
    private EditText bookQuantityEditText;
    private EditText bookPriceEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;
    private Button createBookButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_create);

        bookNameEditText = findViewById(R.id.book_name_edit_text_detail);
        bookQuantityEditText = findViewById(R.id.book_quantity_edit_text_detail);
        bookPriceEditText = findViewById(R.id.book_price_edit_text_detail);
        supplierNameEditText = findViewById(R.id.book_supplier_name_edit_text_detail);
        supplierPhoneEditText = findViewById(R.id.book_supplier_phone_text_edit_detail);

        createBookButton = findViewById(R.id.book_create_button);

        createBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBook();
                finish();
            }
        });


    }

    private void createBook() {

        String nameString = bookNameEditText.getText().toString().trim();
        String priceString = bookPriceEditText.getText().toString().trim();
        String quantityString = bookQuantityEditText.getText().toString().trim();
        String supNameString = supplierNameEditText.getText().toString().trim();
        String supPhoneString = supplierPhoneEditText.getText().toString().trim();


        int quantityInt = -1;
        Double priceDouble = -1.0;

        if (!TextUtils.isEmpty(priceString) && !TextUtils.isEmpty(quantityString)) {
            quantityInt = Integer.valueOf(quantityString);
            priceDouble = Double.valueOf(priceString);
        }

        if (
                TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                        TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supNameString) | TextUtils.isEmpty(supPhoneString)
                        || quantityInt < 0 || priceDouble < 0
                ) {

            Toast.makeText(this, getString(R.string.insert_book_failed),
                    Toast.LENGTH_SHORT).show();

            return;
        }

        ContentValues values = new ContentValues();
        values.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_PRICE, priceDouble);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_QUANTITY, quantityInt);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_NAME, supNameString);
        values.put(BookStoreContract.BookStoreEntry.COLUMN_SUPPLIER_PHONE, supPhoneString);

        Uri newUri = getContentResolver().insert(BookStoreContract.BookStoreEntry.CONTENT_URI, values);

        if (newUri == null) {

            Toast.makeText(this, getString(R.string.book_insert_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.book_insert_successful),
                    Toast.LENGTH_SHORT).show();


        }
    }
}
