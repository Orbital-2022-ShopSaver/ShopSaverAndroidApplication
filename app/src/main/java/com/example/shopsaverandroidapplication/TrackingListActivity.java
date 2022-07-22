package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import model.Product;
import ui.ProductRecyclerTrackingListAdapter;
import util.ShopSaverApi;

// This activity is responsible for showcasing the list of items the user is tracking
// We can use a similar layout to our Displaying Results for now
public class TrackingListActivity extends AppCompatActivity {

    // Track the number of requests
    int numOfRequests = 0;

    // Get my widgets
    private Button updateButton;

    // Initialise Firebase stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Initialise Product Stuff
    // Will aid in displaying the products
    private List<Product> productList;
    private RecyclerView recyclerView;
    private ProductRecyclerTrackingListAdapter productRecyclerTrackingListAdapter;

    // Get the path to our Products DB
    private CollectionReference collectionReference = db.collection("Products");

    // Initialise the Text Widget
    // It will be shown when we have no products that are being tracked
    private TextView noProductEntry;

    // Initialise a Queue, to be used by Volley
    RequestQueue queue;

    // apiEndpoint to make the GET Calls to
    private String apiEndpoint = "https://testwebapiformyself.herokuapp.com/";

    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_list);

        // Assign Firebase Stuff
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Create my queue
        queue = Volley.newRequestQueue(this);


        // Building stuff to showcase Products
        noProductEntry = findViewById(R.id.list_no_products);
        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewTrackingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get my widgets
        updateButton = findViewById(R.id.update_button);

        // Set onClick Button on updateButton
        // When clicked, it will fetch the latest prices
        // Of the products that the user is currently tracking
        updateButton.setOnClickListener(view -> {
            // Loop through the products
            // Check platform
            // Then do the API Call
            // Get updated price then attach it to the firebase document as well
            for (Product product: productList) {
                numOfRequests += 1;
                getCurrentPrice(product);


            }
        });


    }

    /**
     * This method is responsible for getting the current price of the item
     * @param product the product we are currently looking at
     * @return void
     */
    private void getCurrentPrice(Product product) {
        String currentPlatform = product.getPlatform().toLowerCase();
        String currentUrl = product.getUrl();
        String urlToCall = apiEndpoint + currentPlatform + "/single/" + currentUrl;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                urlToCall, null, response -> {
                try {
                    // Get the current price
                    // Update it
                    numOfRequests -= 1;
                    double currentPrice = response.getDouble(0);
                    product.setPrice(currentPrice);
                    DocumentReference productDocumentReference = db.collection("Products")
                            .document(product.getDocumentId());
                    productDocumentReference.update("price", currentPrice);

                    if (numOfRequests == 0) {
                        productRecyclerTrackingListAdapter.notifyDataSetChanged();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }, error -> Toast.makeText(this, "Error Getting Product", Toast.LENGTH_SHORT).show());

        queue.add(jsonArrayRequest);
        
    }

    /**
     * This method is responsible for the start of the Activity
     * We need to get the items that the user is tracking at the start
     */
    @Override
    protected void onStart() {
        // We need to get the items that the user is tracking at the start
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
                                product.setDocumentId(products.getId());
                                productList.add(product);
                            }

                            // After that, invoke the RecyclerView and pass the productList
                            productRecyclerTrackingListAdapter = new ProductRecyclerTrackingListAdapter(TrackingListActivity.this,
                                    productList);
                            recyclerView.setAdapter(productRecyclerTrackingListAdapter);

                            // Update if any changes
                            productRecyclerTrackingListAdapter.notifyDataSetChanged();

                        } else {
                            // If is empty, we show the text that no product entry
                            noProductEntry.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TrackingListActivity.this, "Failed to Load", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}