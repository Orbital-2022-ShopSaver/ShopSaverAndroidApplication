package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

// This Activity is responsible for the main page of the class
// When clicked on ItemListActivity, user will be taken to see a list of items being tracked
// When clicked on SearchActivity, user will be taken to search a list of items

// TODO: Will do up the homepage stuff
public class HomepageActivity extends AppCompatActivity {

    // Get my widgets
    private Button trackingListButton;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Assign my widgets
        trackingListButton = findViewById(R.id.button_tracking_list);
        searchButton = findViewById(R.id.button_search);

        // Add onClick Listener to trackingListButton
        // When clicked, I will be taken to TrackingListActivity (will show list of tracking items)
        trackingListButton.setOnClickListener(view -> {
            startActivity(new Intent(HomepageActivity.this,
                    TrackingListActivity.class));
        });

        // Add onClick Listener to searchButton
        // When clicked, I will be taken to a SearchActivity where I can search for stuff
        searchButton.setOnClickListener(view -> {
            startActivity(new Intent(HomepageActivity.this, SearchActivity.class));
        });


    }
}