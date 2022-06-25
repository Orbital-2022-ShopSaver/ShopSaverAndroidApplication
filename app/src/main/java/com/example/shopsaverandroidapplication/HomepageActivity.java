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
// TODO: This class is not relevant for now, refer to HomepageAltActivity
// TODO: Will do up the homepage stuff
public class HomepageActivity extends AppCompatActivity {

    // Firebase Stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Get my widgets
    private Button trackingListButton;
    private Button searchButton;

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

    // TODO: Disabled the menu of sign out on Homepage, plan to move the sign out to elsewhere
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu (with the signout option)
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        // Engage actions depending on what option is selected
//        switch (item.getItemId()) {
//            case R.id.action_signout:
//                // Sign user out if current user exists
//                if (user != null && firebaseAuth != null) {
//                    // Use the inbuilt signOut() function
//                    firebaseAuth.signOut();
//
//                    // Move back to the GetStarted Page
//                    // That is the MainActivity
//                    startActivity(new Intent(HomepageActivity.this,
//                            MainActivity.class));
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}