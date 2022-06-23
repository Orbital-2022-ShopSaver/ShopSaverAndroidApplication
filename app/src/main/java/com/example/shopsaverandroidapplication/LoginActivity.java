package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.ShopSaverApi;

// This activity handles logging in
// Verification will be done depending on the Email and Password the user enters
// When Login is Clicked, details will be validated, and user will be taken to Homepage (Activity)
// When Create Account is clicked, user will be taken to create an account
public class LoginActivity extends AppCompatActivity {

    // Initialise Firebase Fields
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth cuurentUser;

    // Establish connection to our DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get path to the Users part of the DB
    private CollectionReference collectionReference = db.collection("Users");

    // Initialise my widgets
    private Button loginButton;
    private Button createAcctButton;

    private AutoCompleteTextView emailAddress;
    private EditText password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Firebase instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Get my widgets
        loginButton = findViewById(R.id.email_sign_in_button);
        createAcctButton = findViewById(R.id.create_acct_button_login);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progress);

        // Assign a clickListener to the loginButton
        // Once clicked, we will attempt to login the user
        // When successful, user will be brought to the Homepage Activity
        loginButton.setOnClickListener(view -> {
            String emailDetails = emailAddress.getText().toString().trim();
            String passwordDetails = password.getText().toString().trim();
            loginEmailPasswordUser(emailDetails, passwordDetails);
        });

        // Assign a clickListener to createAcctButton
        // Once clicked, users will be taken to the CreateAccount Page (Activity)
        createAcctButton.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        });


    }

    // This method will handle logging in of the user
    private void loginEmailPasswordUser(String email, String pwd) {

//        // We set progressBar to visible, to indicate something is ongoing
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Validate that the fields are not empty
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
//            // Use the inbuilt Firebase login method
//            firebaseAuth.signInWithEmailAndPassword(email, pwd)
//                    // Complete Listener
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            // Get the user and the ID
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            assert user != null;
//                            String currentUserId = user.getUid();
//
//                            // From the collectionReference (database relevant to users),
//                            // We try to find the currentUserId
//                            collectionReference
//                                    .whereEqualTo("userId", currentUserId)
//                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                            // If have error, don't proceed
//                                            if (error != null) {
//                                                return;
//                                            }
//
//                                            assert value != null;
//
//                                            // Check if the value is not empty
//                                            if (!value.isEmpty()) {
//                                                // Can set to invisible, since nothing on going behind the scenes now
//                                                progressBar.setVisibility(View.INVISIBLE);
//
//                                                // Loop through the values and get the data
//                                                // We set it with our global ShopSaverApi
//                                                // So that it knows the user, id and email
//                                                for (QueryDocumentSnapshot snapshot : value) {
//                                                    ShopSaverApi shopSaverApi = ShopSaverApi.getInstance();
//                                                    shopSaverApi.setUsername(snapshot.getString("username"));
//                                                    shopSaverApi.setUserId(snapshot.getString("userId"));
//                                                    shopSaverApi.setUserEmail(snapshot.getString("userEmail"));
//
//                                                    // After that, we go to the Homepage Page (Activity)
//                                                    startActivity(new Intent(LoginActivity.this,
//                                                            HomepageActivity.class));
//
//                                                    // Finish since we are not coming back to this activity
//                                                    finish();
//                                                }
//                                            }
//                                        }
//                                    });
//                        }
//                    })
//                    // Add a failure listener
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressBar.setVisibility(View.INVISIBLE);
//                            // Log that failed to login
//                            // TODO: Probably should tell the user
//                            Log.d("Failure", "Failed to login");
//                        }
//                    });
//
//        } else {
//            // If fields are empty, inform the user
//            // Make progressBar invisible since nothing ongoing behind now
//            progressBar.setVisibility(View.INVISIBLE);
//
//            Toast.makeText(LoginActivity.this,
//                    "Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
//        }

        // TODO: Testing code below, uncomment the above and delete the starting activity below
        startActivity(new Intent(LoginActivity.this,
                HomepageAltActivity.class));
        finish();
    }
}