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
                .inflate(R.layout.item_row_result, parent, false);

        // Return the view with the context
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerAdapter.ViewHolder holder, int position) {
        // Get the product depending on the position
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(String.format(Locale.ENGLISH,"%.2f", product.getPrice()));
        holder.url.setText(product.getUrl());
        holder.platform.setText(product.getPlatform());
        holder.imageUrl = product.getImage();
        holder.itemPrice = product.getPrice();
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

        public TextView name, price, url, platform;
        public ImageView image;
        public Button addToListButton;
        public String imageUrl;
        public double itemPrice;



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // Assign my widgets
            name = itemView.findViewById(R.id.item_name_list);
            price = itemView.findViewById(R.id.item_price_list);
            url = itemView.findViewById(R.id.item_url_list);
            platform = itemView.findViewById(R.id.item_platform_list);
            image = itemView.findViewById(R.id.item_image_list);
            addToListButton = itemView.findViewById(R.id.go_to_item_button);

            // When button is clicked, we should add it to a tracking list
            addToListButton.setOnClickListener(view -> {
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
