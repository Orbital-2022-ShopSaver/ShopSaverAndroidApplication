package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.ShopSaverApi;

// This class handles the initial page that the user will see
// It is like a Get Started Page
// If there already exists a user session ongoing, we move directly to the Homepage
// When Get Started button is clicked, they will be moved to the Login Page
public class MainActivity extends AppCompatActivity {

    // Initialise my widgets
    private Button getStartedButton;

    // Initialise Firebase Stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // Establish connection to Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get the path to the "Users" part of DB
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign my widgets
        getStartedButton = findViewById(R.id.get_started_button);

        // If we already have a user, we should just get the user details straight away
        // Then we take user to the Homepage
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                // If a user exists, get the UserId, and find user details
                if (currentUser != null) {
                    currentUser = firebaseAuth.getCurrentUser();
                    String currentUserId = currentUser.getUid();

                    // Find the user details
                    collectionReference.whereEqualTo("userId", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    // If havde error, do not proceed
                                    if (error != null) {
                                        return;
                                    }

                                    // Set the ShopSaverApi to have details of that user
                                    // And we move to the Homepage Activity
                                    if (!value.isEmpty()) {
                                        for (QueryDocumentSnapshot snapshot : value) {
                                            ShopSaverApi shopSaverApi = ShopSaverApi.getInstance();
                                            shopSaverApi.setUserId(snapshot.getString("userId"));
                                            shopSaverApi.setUsername(snapshot.getString("username"));
                                            shopSaverApi.setUserEmail(snapshot.getString("userEmail"));

                                            startActivity(new Intent(MainActivity.this,
                                                    HomepageActivity.class));

                                            // Finish since we do not want to come back
                                            // To this MainActivity
                                            finish();
                                        }
                                    }
                                }
                            });
                }
            }
        };

        // Assign a Click Listener to getStartedButton
        // When getStartedButton is clicked, user will be brought to the LoginActivity
        // They will not be brought back to this activity
        getStartedButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    // When we start, engage the Firebase variables

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}