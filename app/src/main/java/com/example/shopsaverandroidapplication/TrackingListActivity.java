package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Product;
import ui.ProductRecyclerTrackingListAdapter;
import util.ShopSaverApi;

// This activity is responsible for showcasing the list of items the user is tracking
// TODO: Not too sure about the UI for this yet
// We can use a similar layout to our Displaying Results for now
public class TrackingListActivity extends AppCompatActivity {

    // Initialise Firebase stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Initialise Product Stuff
    // Will aid in displaying the products
    private List<Product> productList;
    private RecyclerView recyclerView;
    private ProductRecyclerTrackingListAdapter productRecyclerAdapter;

    // Get the path to our Products DB
    private CollectionReference collectionReference = db.collection("Products");

    // Initialise the Text Widget
    // It will be shown when we have no products that are being tracked
    private TextView noProductEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_list);

        // Assign Firebase Stuff
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Building stuff to showcase Products
        noProductEntry = findViewById(R.id.list_no_products);
        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // We need to get the items that the user is tracking at start

    @Override
    protected void onStart() {
        super.onStart();

        // Get all the stuff pertaining to the current user
        collectionReference.whereEqualTo("userId", ShopSaverApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Set the no product text to be invisible
                            noProductEntry.setVisibility(View.INVISIBLE);
                            // queryDocumentSnapshots have the items we want
                            // Add them to the productList
                            for (QueryDocumentSnapshot products : queryDocumentSnapshots) {
                                // Convert what we get back to the Journal
                                Product product = products.toObject(Product.class);
                                productList.add(product);
                            }

                            // After that, invoke the RecyclerView and pass the productList
                            productRecyclerAdapter = new ProductRecyclerTrackingListAdapter(TrackingListActivity.this,
                                    productList);
                            recyclerView.setAdapter(productRecyclerAdapter);

                            // Update if any changes
                            productRecyclerAdapter.notifyDataSetChanged();

                        } else {
                            // If is empty, we show the text that no product entry
                            noProductEntry.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure", "Failed to Load");
                    }
                });
    }
}