package com.bucketsoft.user.project8inventoryappstageone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreDBHelper;
import com.bucketsoft.user.project8inventoryappstageone.DataBaseRelated.BookStoreDBOperations;

public class MainActivity extends AppCompatActivity {

    private BookStoreDBHelper mDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new BookStoreDBHelper(this);

        BookStoreDBOperations.insertBookInstance(mDbHelper, "Book1", 15.0,5,"Grkn Books","+905350298897");
        BookStoreDBOperations.displayDBInfo(mDbHelper);
    }
}
