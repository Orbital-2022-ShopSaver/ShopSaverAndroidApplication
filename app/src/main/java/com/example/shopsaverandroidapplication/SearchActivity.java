package com.example.shopsaverandroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    // Count the number of requests
    // This is to track such that once all the requests are fulfilled, we can display results
    private int numOfRequests = 0;

    // Initialise my widgets
    private EditText searchItem;

    // Use checkboxes to check if the boxes are checked, to execute the search
    // This helps us determine which platform the user wants
    private CheckBox amazonCheckbox;
    private CheckBox lazadaCheckbox;
    private CheckBox qootenCheckbox;

    private Button searchButton;
    private ProgressBar progressBar;

    // apiEndpoint to make the GET Calls to
    private String apiEndpoint = "https://testwebapiformyself.herokuapp.com/";

    // Initialise a queue, which will be used by Volley
    RequestQueue queue;

    // Initialise an ArrayList, to send our list of products over to DisplayResultsActivity
    // To display the list of products
    ArrayList<Product> productArrayList = new ArrayList<>();

    /**
     * This method will run upon creation of the Activity
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        queue = Volley.newRequestQueue(this);

        // Assign my widgets
        searchItem = findViewById(R.id.item_name);
        amazonCheckbox = findViewById(R.id.amazonCheckbox);
        lazadaCheckbox = findViewById(R.id.lazadaCheckbox);
        qootenCheckbox = findViewById(R.id.qootenCheckbox);
        searchButton = findViewById(R.id.search_item_button);
        progressBar = findViewById(R.id.search_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        // Set a onClick Listener for the searchButton
        // When the searchButton is clicked, get the item results that the user has searched
        // After that, we will show the results in a DisplayResultActivity
        // Test
        searchButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String item = searchItem.getText().toString();

            addProductsToListThenDisplayResults(item);

        });
    }

    /**
     * This method is responsible for creating the Json Request, based on the API Endpoint
     * @param api then API Endpoint to make the request to
     */
    private void createJsonArrayRequest(String api) {
        numOfRequests += 1;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                api, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    String url = jsonObject.getString("url");
                    String platform = jsonObject.getString("platform");
                    String image = jsonObject.getString("image");
                    Product itemProduct = new Product(name, price, url, platform, image);
                    productArrayList.add(itemProduct);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // Subtract from the numOfRequests once request has been fulfilled
            numOfRequests -= 1;

            progressBar.setVisibility(View.INVISIBLE);
            // After we get the products, we set it to be global with the ShopSaverApi
            ShopSaverApi shopSaverApi = ShopSaverApi.getInstance();
            shopSaverApi.setProductArrayList(productArrayList);

            if (numOfRequests == 0) {
                // Once we have gotten all the items, we move on to the DisplayResultsActivity
                // This is achieved when we finally have zero requests left
                startActivity(new Intent(SearchActivity.this,
                        DisplayResultsActivity.class));
            }
        }, error -> {
            Toast.makeText(this, "Error Creating Products, Please Try Again", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(SearchActivity.this, HomepageActivity.class));
        }

        );

        queue.add(jsonArrayRequest);

    }

    /**
     * This method is responsible for adding the products to the arrayList
     * The products will be retrieved from an API Call
     * @param item the item that the user is searching for
     */
    private void addProductsToListThenDisplayResults(String item) {
        productArrayList.clear();
        String apiAmazon = apiEndpoint + "amazon/" + item;
        String apiLazada = apiEndpoint + "lazada/" + item;
        String apiQooten = apiEndpoint + "qooten/" + item;

        // Initialise the JsonArrayRequest depending on the platform the user wants to search
        if (amazonCheckbox.isChecked()) {
            createJsonArrayRequest(apiAmazon);
        }

        if (lazadaCheckbox.isChecked()) {
            createJsonArrayRequest(apiLazada);
        }

        if (qootenCheckbox.isChecked()) {
            createJsonArrayRequest(apiQooten);
        }
    }
}