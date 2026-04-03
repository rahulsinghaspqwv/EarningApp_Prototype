package com.example.earningapp01;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.earningapp01.Model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Product> cartItems;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private CartManager(Context context){
        sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadCart();
    }
    public static synchronized CartManager getInstance(Context context){
        if (instance == null){
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }
    private void loadCart(){
        String json = sharedPreferences.getString("cart_items", "");
        if (json.isEmpty()){
            cartItems = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<Product>>(){}.getType();
            cartItems = gson.fromJson(json, type);
        }
    }
    private void saveCart(){
        String json = gson.toJson(cartItems);
        sharedPreferences.edit().putString("cart_items", json).apply();
    }
    public void addToCart(Product product){
        for (Product item : cartItems){
            if (item.getId() == product.getId()){
                item.setQuantity(item.getQuantity() + 1);
                saveCart();
                return;
            }
        }
        product.setQuantity(1);
        cartItems.add(product);
        saveCart();
    }
    public void removeFromCart(Product product){
        cartItems.removeIf(item -> item.getId() == product.getId());
        saveCart();
    }
    public void updateQuantity(Product product, int quantity){
        for (Product item : cartItems){
            if (item.getId() == product.getId()){
                if (quantity <= 0){
                    cartItems.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                saveCart();
                return;
            }
        }
    }
    public List<Product> getCartItems(){
        return new ArrayList<>(cartItems);
    }
    public double getTotalPrice(){
        double total = 0;
        for (Product product : cartItems){
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
    public int getCartCount(){
        int count = 0;
        for (Product product : cartItems){
            count += product.getQuantity();
        }
        return count;
    }
    public void clearCart(){
        cartItems.clear();
        saveCart();
    }
}
