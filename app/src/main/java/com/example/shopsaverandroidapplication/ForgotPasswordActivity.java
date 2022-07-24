package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

// This class is responsible for helping the user in the case of a forgotten password
public class ForgotPasswordActivity extends AppCompatActivity {

    // Initialise my Widgets
    private EditText emailText;
    private Button sendButton;

    // Initialise Firebase Stuff
    FirebaseAuth firebaseAuth;

    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Assign my widgets
        emailText = findViewById(R.id.email_text_forget_password);
        sendButton = findViewById(R.id.send_button);

        // Assign Firebase stuff
        firebaseAuth = FirebaseAuth.getInstance();

        sendButton.setOnClickListener(view -> {
            String email = emailText.getText().toString();

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ForgotPasswordActivity.this, "Password Sent to Email", Toast.LENGTH_SHORT).show();
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(ForgotPasswordActivity.this,
                                    LoginActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to re-create Email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}