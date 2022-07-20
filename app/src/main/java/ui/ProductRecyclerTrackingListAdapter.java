package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsaverandroidapplication.R;
import com.example.shopsaverandroidapplication.ShowProductActivity;

import com.example.shopsaverandroidapplication.TrackingListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import model.Product;


public class ProductRecyclerTrackingListAdapter extends RecyclerView.Adapter<ProductRecyclerTrackingListAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

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



    // Constructor
    public ProductRecyclerTrackingListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductRecyclerTrackingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view
        // Depends on context
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_row_tracking, parent, false);

        // Return the view with the context
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerTrackingListAdapter.ViewHolder holder, int position) {
        // Get the product depending on the position
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(String.format(Locale.ENGLISH,"%.2f", product.getPrice()));
        holder.platform.setText(product.getPlatform());
        holder.imageUrl = product.getImage();
        holder.itemPrice = product.getPrice();
        holder.priceExpectation.setText(String.format(Locale.ENGLISH,"%.2f", product.getPriceExpectation()));
        Picasso.get()
                .load(product.getImage())
                .fit()
                .into(holder.image);

        // If current price is larger than price expectation, display the holder as red
        // Else, will be displayed green
        // This is a visual cue to tell the user if the price is good or not
        if (product.getPrice() > product.getPriceExpectation()) {
            holder.price.setTextColor(Color.parseColor("#ff0000"));
            holder.priceExpectation.setTextColor(Color.parseColor("#ff0000"));
            holder.priceExpectationText.setTextColor(Color.parseColor("#ff0000"));
        }

        holder.goToItemButton.setOnClickListener(view -> {
            Intent visitIntent = new Intent(Intent.ACTION_VIEW);
            visitIntent.setData(Uri.parse(product.getUrl()));
            context.startActivity(visitIntent);
        });

        holder.editButton.setOnClickListener(view -> {
            String priceText = holder.editPriceExpectationText.getText().toString();
            if (validPriceEntered(priceText)) {
                // Create the product object to be added
                // Set progressBar visible to showcase adding process ongoing
                // TODO: Add a progress Bar
                String id = product.getDocumentId();
                double price = Double.parseDouble(priceText);
                holder.priceExpectation.setText(String.format(Locale.ENGLISH,"%.2f", price));
                db.collection("Products").document(id)
                        .update("priceExpectation", price)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Success", "Success");
                                // Make the text empty again, clear focus, hide keyboard
                                holder.editPriceExpectationText.setText("");
                                holder.editPriceExpectationText.clearFocus();
                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Failure", "Failure");
                            }
                        });


            }
            else {
                Toast.makeText(context, "Enter valid numbers", Toast.LENGTH_SHORT).show();
            }

        });

        holder.deleteButton.setOnClickListener(view -> {
            String id = product.getDocumentId();
            db.collection("Products").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "Success");
                            productList.remove(product);
                            notifyDataSetChanged();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Failure");
                        }
                    });
        });

    }

    // TODO: Should probably create a folder for this since ShowProductActivity use as well
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

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, price, platform, priceExpectation, priceExpectationText;
        public EditText editPriceExpectationText;

        public ImageView image;
        public Button deleteButton, editButton, goToItemButton;
        public String imageUrl;
        public double itemPrice;



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // Assign my widgets
            name = itemView.findViewById(R.id.item_name_tracking_list);
            price = itemView.findViewById(R.id.item_price_tracking_list);
            priceExpectation = itemView.findViewById(R.id.item_price_expectation_tracking_list);
            priceExpectationText = itemView.findViewById(R.id.price_expectation_text);
            goToItemButton = itemView.findViewById(R.id.go_to_item_button_tracking_list);
            platform = itemView.findViewById(R.id.item_platform_tracking_list);
            image = itemView.findViewById(R.id.item_image_tracking_list);
            editPriceExpectationText = itemView.findViewById(R.id.edit_price_text);
            editButton = itemView.findViewById(R.id.edit_item_button_tracking_list);
            deleteButton = itemView.findViewById(R.id.delete_item_button_tracking_list);

        }

    }
}
