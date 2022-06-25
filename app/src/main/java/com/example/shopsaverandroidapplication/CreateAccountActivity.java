package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.ShopSaverApi;

// This class is focused on creating an account for the user
// After validating the fields, we will create an account for the user
// The user's data will be stored in Firebase Store
public class CreateAccountActivity extends AppCompatActivity {

    // Initialise Firebase Fields
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // Establish connection to the database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get the path to our "Users" in the Database
    private CollectionReference collectionReference = db.collection("Users");

    // Initialise our widgets
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private EditText usernameEditText;
    private Button createAcctButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Get the instance of our FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Get our widgets
        createAcctButton = findViewById(R.id.create_acct_button);
        progressBar = findViewById(R.id.create_acct_progress);
        emailEditText = findViewById(R.id.email_account);
        passwordEditText = findViewById(R.id.password_account);
        usernameEditText = findViewById(R.id.username_account);

        // Create our authStateListener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
            }
        };

        // Set an onClickListener for Create Account
        // Upon clicking, we validate the input fields
        // If possible, we create an account in the database
        // Else, we notify the users of any issue when making database (empty fields)
        createAcctButton.setOnClickListener(view -> {
            // Validate the input fields
            // TODO: For now we set it to non-empty, but probably need better validations
            if (!TextUtils.isEmpty(emailEditText.getText().toString()) &&
            !TextUtils.isEmpty(passwordEditText.getText().toString()) &&
            !TextUtils.isEmpty(usernameEditText.getText().toString())) {
                // If input fields valid, get the field values
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();

                // Use the createUserEmailAccount method to create the account
                createUserEmailAccount(email, password, username);
            } else {
                // If invalid, notify the user
                Toast.makeText(CreateAccountActivity.this,
                        "Empty Fields are not Allowed", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Creates the account inside our Firebase Database
     * @param email the email entered by the user
     * @param password the password entered by the user
     * @param username the username entered by the user
     */
    // This is the method to create the account
    private void createUserEmailAccount(String email, String password, String username) {
        // Validate the input fields
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            // Set the progress bar to be visible (visual cue to signal to user process ongoing)
            progressBar.setVisibility(View.VISIBLE);

            // Create the user in the Database
            // Use the inbuilt createUserWithEmailAndPassword method
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    // On Complete Listener
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // If successful, add user to the Firestore DB
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                // Get the userId
                                String currentUserId = currentUser.getUid();

                                // Create a Map to put details relevant to the user
                                // Using key-value pairs
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);
                                userObj.put("userEmail", email);

                                // Save the user to our Firestore DB
                                collectionReference.add(userObj)
                                        // Success Listener
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // We will bring the user to the home page after that
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                // Validate the task
                                                                if (task.getResult().exists()) {
                                                                    // Set progressBar to invisible, since we no longer have stuff rendering behind the scenes
                                                                    progressBar.setVisibility(View.INVISIBLE);

                                                                    // Get the username of the user
                                                                    String name = task.getResult().getString("username");
                                                                    String email = task.getResult().getString("userEmail");

                                                                    // We will use a global API to hold the current user details
                                                                    // This is for easier access, so we don't have to keep putExtras() and pass Intent()
                                                                    // Get the instance
                                                                    ShopSaverApi shopSaverApi = ShopSaverApi.getInstance();

                                                                    // Set the current user details inside
                                                                    shopSaverApi.setUserId(currentUserId);
                                                                    shopSaverApi.setUsername(username);
                                                                    shopSaverApi.setUserEmail(email);

                                                                    // Create new intent to go to the Homepage Activity
                                                                    // Pass the user details
                                                                    // TODO: Quite sure this part don't need anymore, but testing out
                                                                    Intent intent = new Intent(CreateAccountActivity.this,
                                                                            HomepageAltActivity.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentUserId);
                                                                    startActivity(intent);


                                                                } else {
                                                                    // Else, we just make the progressBar invisible
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // If failed, log a warning
                                                                // Failed to go to homepage
                                                                Log.d("Failure", "Failed to go to HomepageActivity");
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // If failed, log a warning
                                                // Failed to save value to DB
                                                Log.d("Failure", "Failed to save to DB");
                                            }
                                        });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // If failed, log a warning
                            // Failed to create the user
                            Log.d("Failure", "Failed to create user");
                        }
                    });
        }
    }

    // Before we show user stuff on the screen,
    // We want to make sure all the Firebase stuff is set up
    // So we do it in onStart()

    @Override
    protected void onStart() {
        super.onStart();

        // Get current user
        currentUser = firebaseAuth.getCurrentUser();

        // Listen to changes in Firebase Authorization
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}

