package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import model.Product;
import util.ShopSaverApi;

// This activity is responsible for displaying the information of the product
// We can also add item to be tracked from this page
public class ShowProductActivity extends AppCompatActivity {

    // Initialise my widgets
    private TextView productName;
    private TextView productPrice;
    private TextView productUrl;
    private ImageView productImage;
    private Button addToTrackButton;
    private ProgressBar addProductProgressBar;
    private EditText priceExpectationText;
    private Button addButton;
    private Button closeButton;
    private Button visitItemButton;

    // Initialise fields regarding the data of the product
    private String name;
    private double price;
    private String url;
    private String image;
    private String platform;

    // Initialise fields regarding user details
    private String currentUserId;
    private String currentUserName;
    private String currentUserEmail;

    // Initialise my Firebase stuff
    // It is necessary since we will be adding items to be tracked
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // Establish connection to my Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Reference to Products in my Database, which is where we will save the items the user
    // Wants to track
    private CollectionReference collectionReference = db.collection("Products");

    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);

        // Get my data from the item
        Bundle extra = getIntent().getExtras();

        // Assign my widgets
        productName = findViewById(R.id.item_name_product);
        productPrice = findViewById(R.id.item_price_product);
        productUrl = findViewById(R.id.item_url_product);
        productImage = findViewById(R.id.item_image_product);
        addToTrackButton = findViewById(R.id.add_to_track_button);
        priceExpectationText = findViewById(R.id.price_expectation_text);
        addButton = findViewById(R.id.add_button);
        closeButton = findViewById(R.id.close_button);
        visitItemButton = findViewById(R.id.visit_item_button);


        addProductProgressBar = findViewById(R.id.add_product_progress_bar);
        addProductProgressBar.setVisibility(View.INVISIBLE);
        // Initially, priceExpectationText, addButton and closeButton will be invisible
        // Only visible when user wants to add
        priceExpectationText.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.INVISIBLE);

        // Get the data from the data from the intent that I passed
        if (extra != null) {
            name = extra.getString("name");
            price = extra.getDouble("price");
            url = extra.getString("url");
            platform = extra.getString("platform");
            image = extra.getString("image");
            productName.setText(name);
            productPrice.setText(String.format(Locale.ENGLISH,"%.2f", price));
            productUrl.setText(url);

            Picasso.get()
                    .load(image)
                    .into(productImage);
        }

        // Get Firebase instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Get the user details and set it, only if instance is not null
        if (ShopSaverApi.getInstance() != null) {
            currentUserId = ShopSaverApi.getInstance().getUserId();
            currentUserName = ShopSaverApi.getInstance().getUsername();
            currentUserEmail = ShopSaverApi.getInstance().getUserEmail();
        }

        // Add the authStateListener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Get current user
                user = firebaseAuth.getCurrentUser();
            }
        };

        // Add an onClick Listener to the addItemToTrack Button
        // When clicked, we will display the addButton, priceExpectationText and closeButton
        addToTrackButton.setOnClickListener(view -> {
            addButton.setVisibility(View.VISIBLE);
            priceExpectationText.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.VISIBLE);
            addToTrackButton.setVisibility(View.INVISIBLE);
            visitItemButton.setVisibility(View.INVISIBLE);
        });

        // Add an onClick Listener to visitItem Button
        // When clicked, open up the web and go to item url
        visitItemButton.setOnClickListener(view -> {
            Intent visitIntent = new Intent(Intent.ACTION_VIEW);
            visitIntent.setData(Uri.parse(url));
            startActivity(visitIntent);
        });


        // Add an onClick Listener to the add Button
        // When clicked we will add an item to be tracked in the Database
        // TODO: Validate that price expectation entered is valid
        // That will be tied to the user's details
        addButton.setOnClickListener(view -> {

            String priceText = priceExpectationText.getText().toString();
            if (validPriceEntered(priceText)) {
                // Create the product object to be added
                // Set progressBar visible to showcase adding process ongoing
                addProductProgressBar.setVisibility(View.VISIBLE);

                Product product = new Product(name, price, url, platform, image,
                        currentUserId, currentUserName,
                        currentUserEmail, Double.parseDouble(priceText));

                // Invoke our Database, and add the item inside
                collectionReference.add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // If successful, create a toast to tell user is success
                                // Make progressBar invisible after this since process done
                                addProductProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(ShowProductActivity.this, "Successfully added"
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                addProductProgressBar.setVisibility(View.INVISIBLE);
                                Log.d("Failure", "Failed to add item");
                            }
                        });
            }
            else {
                Toast.makeText(this, "Enter valid numbers", Toast.LENGTH_SHORT).show();
            }

        });

        // Add an onClickListener to closeButton
        // When clicked, revert back to original state
        // That means add, close and text invisible, but addToTrack visible
        closeButton.setOnClickListener(view -> {
            addButton.setVisibility(View.INVISIBLE);
            priceExpectationText.setVisibility(View.INVISIBLE);
            closeButton.setVisibility(View.INVISIBLE);
            addToTrackButton.setVisibility(View.VISIBLE);
            visitItemButton.setVisibility(View.VISIBLE);
        });

    }

    /**
     * This method checks if the priceText entered is valid
     * E.g should not have dollar sign, characters, etc...
     * @param priceText
     * @return boolean value that tells us if the price entered is valid or not
     */
    private boolean validPriceEntered(String priceText) {

        if (priceText.isEmpty()) {
            return false;
        }

        try {
            double price = Double.parseDouble(priceText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * This method is responsible for the start of the Activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


    /**
     * This method is responsible for the stop of the Activity
     */
    @Override
    protected void onStop() {
        // When stopped, we do not want to listen anymore
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}