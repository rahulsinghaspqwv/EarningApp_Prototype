package com.example.earningapp01.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.earningapp01.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    ArrayList<Product> productList;
    public ProductAdapter(Context context, ArrayList<Product> productList){
        this.context = context;
        this.productList = productList;
    }
}
