package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Product;
import util.ShopSaverApi;

public class SearchActivity extends AppCompatActivity {

    // Initialise my widgets
    private EditText searchItem;
    // TODO: By right searchPlatform should be like we can search multiple platforms
    private EditText searchPlatform;
    private Button searchButton;
    private ProgressBar progressBar;

    // TODO: Hardcoded endpoint, but will change
    private String apiEndpoint = "https://testwebapiformyself.herokuapp.com/";

    // Initialise a queue, which will be used by Volley
    RequestQueue queue;

    // Initialise an ArrayList, to send our list of products over to DisplayResultsActivity
    // To display the list of products
    ArrayList<Product> productArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        queue = Volley.newRequestQueue(this);

        // Assign my widgets
        searchItem = findViewById(R.id.item_name);
        // TODO: Platform should be toggle version, so toggle between the shopping platforms
        // TODO: But for now we just assume is from Amazon (use String Amazon)
        searchPlatform = findViewById(R.id.search_platform);
        searchButton = findViewById(R.id.search_item_button);
        progressBar = findViewById(R.id.search_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        // Set a onClick Listener for the searchButton
        // When the searchButton is clicked, get the item results that the user has searched
        // After that, we will show the results in a DisplayResultActivity
        searchButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String item = searchItem.getText().toString();
            String platform = searchItem.getText().toString();

            addProductsToListThenDisplayResults(item, platform);

        });
    }

    // This method is responsible for adding the products to the arrayList
    // The products will be retrieved from an API Call
    // After we get the products, we move on to the DisplayResultsActivity
    private void addProductsToListThenDisplayResults(String item, String platform) {
        productArrayList.clear();
        String apiUrl = apiEndpoint + item;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                apiUrl, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String price = jsonObject.getString("price");
                    String url = jsonObject.getString("url");
                    Log.d("Product", name + price + url);
                    Product itemProduct = new Product(name, price, url);
                    productArrayList.add(itemProduct);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            progressBar.setVisibility(View.INVISIBLE);
            // After we get the products, we set it to be global with the ShopSaverApi
            ShopSaverApi shopSaverApi = ShopSaverApi.getInstance();
            shopSaverApi.setProductArrayList(productArrayList);

            // Once we have gotten the items, we move on to the DisplayResultsActivity
            startActivity(new Intent(SearchActivity.this,
                    DisplayResultsActivity.class));
        }, error -> Log.d("JSON", "Error creating Object"));

        queue.add(jsonArrayRequest);
    }
}