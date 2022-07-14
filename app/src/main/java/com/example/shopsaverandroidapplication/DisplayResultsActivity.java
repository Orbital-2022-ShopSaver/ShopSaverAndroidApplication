package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Product;
import ui.ProductRecyclerAdapter;
import util.ShopSaverApi;

// This Activity is responsible for displaying the list of results from our search results
public class DisplayResultsActivity extends AppCompatActivity {

    // List Stuff
    private List<Product> productList;
    private RecyclerView recyclerView;
    private ProductRecyclerAdapter productRecyclerAdapter;

    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        // Get my RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * This method is responsible for the start of the Activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Get my productList from ShopSaverApi
        ShopSaverApi shopSaverApi = ShopSaverApi.getInstance();
        productList = shopSaverApi.getProductArrayList();
        Log.d("Product", productList.toString());

        // After that, invoke my recycler to get my items
        productRecyclerAdapter = new ProductRecyclerAdapter(DisplayResultsActivity.this,
                productList);
        recyclerView.setAdapter(productRecyclerAdapter);

        // Will update itself when there is change
        productRecyclerAdapter.notifyDataSetChanged();
    }
}