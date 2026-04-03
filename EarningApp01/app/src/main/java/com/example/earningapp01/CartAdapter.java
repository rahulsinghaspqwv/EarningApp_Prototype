package com.example.earningapp01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earningapp01.Model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<Product> cartItems;
    private CartManager cartManager;
    private CartUpdateListener updateListener;
    public interface CartUpdateListener {
        void onCartUpdated();
    }
    public CartAdapter(Context context, List<Product> cartItems, CartUpdateListener cartUpdateListener){
        this.context = context;
        this.cartItems = cartItems;
        this.cartManager = CartManager.getInstance(context);
        this.updateListener = cartUpdateListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText("rs" + String.format("%.2f", product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        double itemTotal = product.getPrice() * product.getQuantity();
        holder.tvTotal.setText("rs" + String.format("%.2f", itemTotal));
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.placeholder_image).into(holder.ivProductImage);
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = product.getQuantity() + 1;
                cartManager.updateQuantity(product, newQuantity);
                updateListener.onCartUpdated();
            }
        });
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = product.getQuantity() - 1;
                cartManager.updateQuantity(product, newQuantity);
                updateListener.onCartUpdated();
            }
        });
        holder .btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.removeFromCart(product);
                updateListener.onCartUpdated();
            }
        });
    }

    public int getItemCount(){
        return cartItems.size();
    }
    public static class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvQuantity, tvTotal;
        Button btnIncrease, btnDecrease, btnRemove;
        public CartViewHolder(@NonNull View itemView){
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
