package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

// This class handles the initial page that the user will see
// When Get Started button is clicked, they will be moved to the Login Page
public class MainActivity extends AppCompatActivity {

    // Initialise my widgets
    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign my widgets
        getStartedButton = findViewById(R.id.get_started_button);

        // Assign a Click Listener to getStartedButton
        // When getStartedButton is clicked, user will be brought to the LoginActivity
        // They will not be brought back to this activity
        getStartedButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}