package com.example.earningapp01.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.earningapp01.CartManager;
import com.example.earningapp01.Model.Product;
import com.example.earningapp01.ProductDetailActivity;
import com.example.earningapp01.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    private List<Product> productList;
    private CartManager cartManager;

    public ProductAdapter(Context context, List<Product> productList){
        this.context = context;
        this.productList = productList;
        this.cartManager = CartManager.getInstance(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText("rs" + String.format("%.2f", product.getPrice()));
        holder.tvProductDescription.setText(product.getDescription());
        // Load image using Picasso
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.placeholder_image).error(R.drawable.error_image).into(holder.ivProductImage);
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.addToCart(product);
                Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_LONG).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            }
        });

    }

    public int getItemCount(){
        return productList.size();
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvProductDescription;
        Button btnAddToCart;
        public ProductViewHolder(@NonNull View itemView){
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}

