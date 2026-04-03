package com.example.earningapp01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earningapp01.Model.Product;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice, tvEmptyCart;
    private Button btnCheckout;
    private CartManager cartManager;
    private List<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadCartItems();
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()){
                    Toast.makeText(CartActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Process Checkout
                    Toast.makeText(CartActivity.this, "Order placed Successfully! Total: rs" + String.format("%.2f", cartManager.getTotalPrice()), Toast.LENGTH_SHORT).show();
                    cartManager.clearCart();
                    loadCartItems();
                }
            }
        });
    }
    private void initViews(){
        recyclerView = findViewById(R.id.recycleView);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        cartManager = CartManager.getInstance(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @SuppressLint("SetTextI18n")
    private void loadCartItems(){
        cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()){
            tvEmptyCart.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
            tvTotalPrice.setText("rs 0.00");
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter((Context) this, cartItems, (CartAdapter.CartUpdateListener) this);
            recyclerView.setAdapter(cartAdapter);
            updateTotalPrice();
        }
    }
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void updateTotalPrice(){
        double total = cartManager.getTotalPrice();
        tvTotalPrice.setText("Total: rs" + String.format("%.2f", total));
    }
    private void onCartUpdated(){
        loadCartItems();
    }
}