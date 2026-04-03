package com.example.earningapp01.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.earningapp01.CartManager;
import com.example.earningapp01.Model.Product;
import com.example.earningapp01.R;

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

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        Glide.with(context).load(product.getImageUrl()).into(holder.image);
    }

    public int getItemCount(){
        return productList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, price;
        ImageView image;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            image = itemView.findViewById(R.id.productImage);
        }
    }
}

