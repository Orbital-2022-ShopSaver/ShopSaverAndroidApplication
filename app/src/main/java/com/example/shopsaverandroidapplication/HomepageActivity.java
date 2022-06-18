package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// This Activity is responsible for the main page of the class
// When clicked on ItemListActivity, user will be taken to see a list of items being tracked
// When clicked on SearchActivity, user will be taken to search a list of items
public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }
}