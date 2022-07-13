package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// This activity is responsible for displaying the Guide for the User
// This is so that the user knows how to use the Application
public class UserGuideActivity extends AppCompatActivity {

    // Initialise our Widgets
    private Button searchInfoButton;
    private Button addInfoButton;
    private Button trackInfoButton;
    private Button homeButton;

    private TextView guideScrollText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        // Assign our Widgets
        searchInfoButton = findViewById(R.id.guide_button_search);
        addInfoButton = findViewById(R.id.guide_button_add);
        trackInfoButton = findViewById(R.id.guide_button_track);
        homeButton = findViewById(R.id.guide_home_button);
        guideScrollText = findViewById(R.id.guide_scroll_text);

        // When searchInfoButton is clicked, display search info
        searchInfoButton.setOnClickListener(view -> {
            guideScrollText.setText(R.string.search_info);
        });

        // When addInfoButton is clicked, display add info
        addInfoButton.setOnClickListener(view -> {
            guideScrollText.setText(R.string.add_info);
        });

        // When trackInfoButton is clicked, display track info
        trackInfoButton.setOnClickListener(view -> {
            guideScrollText.setText(R.string.track_info);
        });

        // When Home Button is clicked, go back to Homepage
        homeButton.setOnClickListener(view -> {
            startActivity(new Intent(UserGuideActivity.this, HomepageActivity.class));
        });


    }
}