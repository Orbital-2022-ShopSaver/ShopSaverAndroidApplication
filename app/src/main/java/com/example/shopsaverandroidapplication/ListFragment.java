package com.example.shopsaverandroidapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import ui.ProductRecyclerAdapter;
import util.ShopSaverApi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // Initialise Firebase stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Initialise Product Stuff
    // Will aid in displaying the products
    private List<Product> productList;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private RecyclerView recyclerViewGeneral, recyclerViewSpecific;

    // Get the path to our Products DB
    private CollectionReference collectionReference = db.collection("Products");

    // Initialise the Text Widget
    // It will be shown when we have no products that are being tracked
//    private TextView noProductEntry;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Assign Firebase Stuff
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get my data from the item
        Bundle extra = this.getActivity().getIntent().getExtras();

        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list, container, false);

        // Building stuff to showcase Products
//        noProductEntry = view.findViewById(R.id.list_no_products);
        recyclerViewGeneral = view.findViewById(R.id.recyclerViewGeneral);
        recyclerViewSpecific = view.findViewById(R.id.recyclerViewSpecific);
        productList = new ArrayList<>();
        recyclerViewSpecific.setHasFixedSize(true);
        recyclerViewSpecific.setLayoutManager(new LinearLayoutManager(this.getContext()));

//        // Dummy data to populate the list
//        ArrayList<String> array = new ArrayList<>();
//        array.add("item1");
//        array.add("item2");
//        array.add("item3");
//
//        recyclerViewSpecific.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        //recyclerViewSpecific.setAdapter(new ListAdapter(array));

        return view;
    }

    // We need to get the items that the user is tracking at start
    @Override
    public void onStart() {
        super.onStart();

        // Get all the stuff pertaining to the current user
        collectionReference.whereEqualTo("userId", ShopSaverApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Set the no product text to be invisible
//                            noProductEntry.setVisibility(View.INVISIBLE);
                            // queryDocumentSnapshots have the items we want
                            // Add them to the productList
                            for (QueryDocumentSnapshot products : queryDocumentSnapshots) {
                                // Convert what we get back to the Journal
                                Product product = products.toObject(Product.class);
                                productList.add(product);
                            }

                            // After that, invoke the RecyclerView and pass the productList
//                          productRecyclerAdapter = new ProductRecyclerAdapter(HomepageAltActivity.this,
//                                    productList);
                            productRecyclerAdapter = new ProductRecyclerAdapter(productList);
                            recyclerViewSpecific.setAdapter(productRecyclerAdapter);

                            // Update if any changes
                            productRecyclerAdapter.notifyDataSetChanged();

                        } else {
                            // If is empty, we show the text that no product entry
//                            noProductEntry.setVisibility(View.VISIBLE);
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