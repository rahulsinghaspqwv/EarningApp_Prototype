package com.example.earningapp01;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.earningapp01.Model.Product;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView ivProductImage;
    private TextView tvProductName, tvProductPrice, tvProductDescription, tvCategory;
    private Button btnAddToCart;
    private Product product;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        // Get Product from intent
        product = (Product) getIntent().getSerializableExtra("product");
        cartManager = CartManager.getInstance(this);

        displayProductDetails();
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.addToCart(product);
                Toast.makeText(ProductDetailActivity.this, " added to cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void initViews(){
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvCategory = findViewById(R.id.tvCategory);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void displayProductDetails(){
        tvProductName.setText(product.getName());
        tvProductPrice.setText("rs" + String.format("%.2f", product.getPrice()));
        tvProductDescription.setText(product.getDescription());
        tvCategory.setText("Category: " + product.getCategory());
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.placeholder_image).into(ivProductImage);
    }
}