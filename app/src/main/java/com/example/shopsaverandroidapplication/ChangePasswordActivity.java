package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import util.ShopSaverApi;

public class ChangePasswordActivity extends AppCompatActivity {

    // Initialise my widgets
    private EditText originalPasswordText;
    private EditText newPasswordText;
    private Button changePasswordButton;

    // Initialise Firebase Fields
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Assign my widgets
        originalPasswordText = findViewById(R.id.email_text_input);
        newPasswordText = findViewById(R.id.new_password_text);
        changePasswordButton = findViewById(R.id.send_button);

        // Assign Firebase stuff
        firebaseAuth = FirebaseAuth.getInstance();

        // Create our authStateListener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
            }
        };

        changePasswordButton.setOnClickListener(view -> {
            AuthCredential credential = EmailAuthProvider.getCredential(ShopSaverApi.getInstance().getUserEmail(),
                    originalPasswordText.getText().toString());

            if (validatePassword(newPasswordText.getText().toString())) {
                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    currentUser.updatePassword(newPasswordText.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ChangePasswordActivity.this,
                                                                "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                        try {
                                                            TimeUnit.SECONDS.sleep(2);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        startActivity(new Intent(ChangePasswordActivity.this,
                                                                HomepageActivity.class));

                                                    }
                                                    else {
                                                        Toast.makeText(ChangePasswordActivity.this,
                                                                "Error in Updating", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this,
                                            "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "New Password is not at least 10 characters", Toast.LENGTH_SHORT).show();
            }


        });
    }

    /**
     * This function validates if the new password that the user entered is valid or not
     * @param password the password that the user entered
     * @return a boolean value to denote if the password is valid or not
     */
    private boolean validatePassword(String password) {
        boolean passwordValid = !TextUtils.isEmpty(password) && password.length() >= 10;
        return passwordValid;
    }

    // Before we show user stuff on the screen,
    // We want to make sure all the Firebase stuff is set up
    // So we do it in onStart()
    /**
     * This method is responsible for the start of the Activity
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Get current user
        currentUser = firebaseAuth.getCurrentUser();

        // Listen to changes in Firebase Authorization
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}