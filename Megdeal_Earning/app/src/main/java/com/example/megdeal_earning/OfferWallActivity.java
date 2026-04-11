package com.example.megdeal_earning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.megdeal_earning.Models.Offer;
import com.example.megdeal_earning.utils.Constants;
import com.example.megdeal_earning.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.OfferAdapter;

public class OfferWallActivity extends AppCompatActivity {
    private RecyclerView rvOffers;
    private OfferAdapter offerAdapter;
    private List<Offer> offerList;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_offer_wall);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvOffers = findViewById(R.id.rv_offers);
        rvOffers.setLayoutManager(new LinearLayoutManager(this));
        offerList = new ArrayList<>();
        offerAdapter = new OfferAdapter(this, offerList, offer -> {
            // When user clicks an offer
            startOffer(offer);
        });
        rvOffers.setAdapter(offerAdapter);
        loadOffers();

    }
    @SuppressLint("NotifyDataSetChanged")
    private void loadOffers(){
        // This would normally come from your backend or ad network SDK
        // For Demo, we'll add dummy offers
        offerList.add(new Offer("1", "Amazon Shopping", "Install and sign up", 15.0, "https://example.com/amazon.jpg", "https://play.google.com/store/apps/details?id=com.amazon.shop"));
        offerList.add(new Offer("2", "Flipkart", "Install and make first purchase", 20.0, "https://example.com/flipkart.jpg", "https://play.google.com/store/apps/details?id=com.flipkart.android"));
        offerList.add(new Offer("3", "Paytm First Game", "Install and play one game", 10.0, "https://example.com/paytm.jpg", "https://play.google.com/store/apps/details?id=net.one97.paytm"));
        offerList.add(new Offer("4", "Meesho", "Install and browse products", 12.0, "https://example.com/meesho.jpg", "https://play.google.com/store/apps/details?id=com.meesho.supply"));
        offerList.add(new Offer("5", "Hindi Typing", "Install and just review the app", 5.00, "https://example.com/HindiTyping.jpg","https://play.google.com/store/apps/details?id=com.hindi.typing"));
        offerList.add(new Offer("6", "Myntra", "Install and browse products", 15.00, "https://example.com/Myntra.jpg", "https://play.google.com/store/apps/details?id=com.myntra.shop"));
        offerList.add(new Offer("7", "Alibaba Express", "Install and browse products", 20.00, "https://example.com/Alibaba.jpg", "https://play.google.com/store/apps/details?id=com.AlibabaExpress.shop"));
        offerAdapter.notifyDataSetChanged();
        // In real implementation, you would use OfferWall SDK like:
        // Paymentwall.showOfferwall(this, userId, callback);
    }
    private void startOffer(Offer offer){
        // Record the offer start on your server
        String url = Constants.BASE_URL+"start_offer.php";
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", sessionManager.getUserId());
            params.put("offer_id", offer.getId());
            params.put("action", "start");
        } catch (JSONException e){e.printStackTrace();}

        // Open the offer URL (or trigger SDK)
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // Offer Started Successfully
                Toast.makeText(OfferWallActivity.this, "Opening: " + offer.getTitle(), Toast.LENGTH_SHORT).show();
                // Simulate offer completion after 3 seconds (for demo only)
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completeOffer(offer);
                    }
                }, 3000);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(OfferWallActivity.this, "Failed to start offer", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }
    private void completeOffer(Offer offer){
        // Send completion to server
        String url = Constants.UPDATE_BALANCE_URL;
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", sessionManager.getUserId());
            params.put("amount", offer.getReward());
            params.put("offer_id", offer.getId());
            params.put("type", "earning");
            params.put("description", "Completed: " + offer.getTitle());
        } catch (JSONException e){e.printStackTrace();}
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(OfferWallActivity.this, "Congratulations! You earned rs" + offer.getReward(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(OfferWallActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(OfferWallActivity.this, "Error processing response", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorMsg = "Failed to process reward";
                if (volleyError.networkResponse != null){
                    errorMsg = "Error: " + volleyError.networkResponse.statusCode;
                }
                Toast.makeText(OfferWallActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }
}