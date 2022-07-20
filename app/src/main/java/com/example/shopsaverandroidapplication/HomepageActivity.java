package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// This Activity is responsible for the main page of the class
// When clicked on ItemListActivity, user will be taken to see a list of items being tracked
// When clicked on SearchActivity, user will be taken to search a list of items

public class HomepageActivity extends AppCompatActivity {

    // Firebase Stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Get my widgets
    private Button trackingListButton;
    private Button searchButton;
    private Button userGuideButton;

    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Assign Firebase Stuff
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Assign my widgets
        trackingListButton = findViewById(R.id.button_tracking_list);
        searchButton = findViewById(R.id.button_search);
        userGuideButton = findViewById(R.id.button_user_guide);

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

        // Add onClick Listener to userGuideButton
        // When clicked, I will be taken to a UserGuideActivity to understand how the App works
        userGuideButton.setOnClickListener(view -> {
            startActivity(new Intent(HomepageActivity.this, UserGuideActivity.class));
        });
    }

    /**
     * This method is responsible for creating my Menu
     * @param menu the menu and its options
     * @return the inflated version of the menu, so it can be interacted with
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu (with the signout option)
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is responsible for triggering other activities when my menu options are clicked
     * The activity triggered depends on which menu options was clicked
     * @param item the menu item that was selected
     * @return the onOptionsItemSelected, with the item being clicked
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Engage actions depending on what option is selected
        switch (item.getItemId()) {
            case R.id.action_signout:
                // Sign user out if current user exists
                if (user != null && firebaseAuth != null) {
                    // Use the inbuilt signOut() function
                    firebaseAuth.signOut();

                    // Move back to the GetStarted Page
                    // That is the MainActivity
                    startActivity(new Intent(HomepageActivity.this,
                            MainActivity.class));
                }
                break;

            case R.id.change_password:
                startActivity(new Intent(HomepageActivity.this,
                        ChangePasswordActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}