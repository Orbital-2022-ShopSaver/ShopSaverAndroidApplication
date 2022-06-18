package ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsaverandroidapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import model.Product;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

    // Constructor
    public ProductRecyclerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view
        // Depends on context
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_row, parent, false);

        // Return the view with the context
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerAdapter.ViewHolder holder, int position) {
        // Get the product depending on the position
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        holder.url.setText(product.getUrl());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, price, url;
        public Button addToListButton;



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // Assign my widgets
            name = itemView.findViewById(R.id.item_name_list);
            price = itemView.findViewById(R.id.item_price_list);
            url = itemView.findViewById(R.id.item_url_list);
            addToListButton = itemView.findViewById(R.id.add_item_to_list_button);

            // When button is clicked, we should add it to a tracking list
            addToListButton.setOnClickListener(view -> {
                // Test out if can get the item first
                String nameValue = name.getText().toString();
                String priceValue = price.getText().toString();
                String urlValue = url.getText().toString();
                // TODO: Not sure how to add the item to a list
                Log.d("Item", nameValue + priceValue + urlValue);
            });
        }

    }
}
