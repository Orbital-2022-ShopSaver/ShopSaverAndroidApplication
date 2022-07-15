package ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopsaverandroidapplication.R;
import com.example.shopsaverandroidapplication.ShowProductActivity;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import model.Product;

public class ProductRecyclerTrackingListAdapter extends RecyclerView.Adapter<ProductRecyclerTrackingListAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

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
        holder.url.setText(product.getUrl());
        holder.platform.setText(product.getPlatform());
        holder.imageUrl = product.getImage();
        holder.itemPrice = product.getPrice();
        holder.priceExpectation.setText(String.format(Locale.ENGLISH,"%.2f", product.getPriceExpectation()));
        Picasso.get()
                .load(product.getImage())
                .fit()
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, price, url, platform, priceExpectation;

        public ImageView image;
        public Button deleteButton;
        public String imageUrl;
        public double itemPrice;



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // Assign my widgets
            name = itemView.findViewById(R.id.item_name_tracking_list);
            price = itemView.findViewById(R.id.item_price_tracking_list);
            priceExpectation = itemView.findViewById(R.id.item_price_expectation_tracking_list);
            url = itemView.findViewById(R.id.item_url_tracking_list);
            platform = itemView.findViewById(R.id.item_platform_tracking_list);
            image = itemView.findViewById(R.id.item_image_tracking_list);
            deleteButton = itemView.findViewById(R.id.delete_item_button_tracking_list);


            // When button is clicked, we should add it to a tracking list
            // TODO: Do up the delete
            // TODO: Make an edit button
            deleteButton.setOnClickListener(view -> {
                // Test out if can get the item first
                String nameValue = name.getText().toString();
                double priceValue = itemPrice;
                String urlValue = url.getText().toString();
                String platformValue = platform.getText().toString();
                String imageValue = imageUrl;
                // TODO: Not sure how to add the item to a list
                // TODO: I think probably can create another activity for it
                // TODO: Then we pass these values
                // Start an intent to go to the activity that showcase product details
                // For now we just show the same field values
                Log.d("Item", nameValue + priceValue + urlValue);
                Intent intent = new Intent(context, ShowProductActivity.class);
                intent.putExtra("name", nameValue);
                intent.putExtra("price", priceValue);
                intent.putExtra("url", urlValue);
                intent.putExtra("platform", platformValue);
                intent.putExtra("image", imageValue);
                context.startActivity(intent);
            });
        }

    }
}
