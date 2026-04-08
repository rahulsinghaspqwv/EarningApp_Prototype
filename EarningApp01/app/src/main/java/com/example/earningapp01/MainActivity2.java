package com.example.earningapp01;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    private TextView tvBalance;
    private Button btnOfferWall, btnWithdraw;
    private RequestQueue requestQueue;
    private String userToken = "USER_UNIQUE_ID_123";
    private double currentBalance = 0.0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBalance = findViewById(R.id.tv_balance);
        btnOfferWall = findViewById(R.id.btn_offerWall);
        btnWithdraw = findViewById(R.id.btn_withdraw);
        requestQueue = Volley.newRequestQueue(this);
        // 1. App Open hote hi User ka Balance check karo (Apne Server se)
        fetchUserBalance(userToken);
        // 2. OfferWall Open karne ka Logic
        btnOfferWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOfferWallDummy();
            }
        });
        // 3. Paytm payout ka logic
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBalance >= 10){
                    initiatePaytmPayout(userToken, currentBalance);
                }else {
                    Toast.makeText(MainActivity2.this, "Kam se kam rs10 chahiye nikalne ke liye", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // ===================== Section 1: user balance fetch (apne server se) ===============================
    private void fetchUserBalance(String userId){
        String url = "https://yourserver.com/api/get_balance?user_id=" + userId;
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("success")){
                        currentBalance = jsonObject.getDouble("balance");
                        tvBalance.setText("rs " + currentBalance);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity2.this, "Unable to load Balance ", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        requestQueue.add(request);
    }
    // ===================== Section 2: OFFER WALL (Apps Install Earning) =================================
    private void openOfferWallDummy(){
        //
        Toast.makeText(this, "OfferWall is Opening Please Wait...", Toast.LENGTH_SHORT).show();
    }
    // IMPORTANT: if user successfully completed the offer then a Postback will receive to our backend.
    // after receiving the Postback we can use postback response to update our balance using this method which mentioned below.
    public void updateBalanceOnServer(double amountEarned){
        String url = "https://yourserver.com/api/update_balance";
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userToken);
            params.put("amount", amountEarned);
        } catch (JSONException e){
            e.printStackTrace();
        }
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // Success par UI update
                currentBalance += amountEarned;
                tvBalance.setText("rs " + currentBalance);
                Toast.makeText(MainActivity2.this, "Congratulations rs" + amountEarned, Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity2.this, "Server Error, Please Try Again", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        requestQueue.add(request);
    }
    // ======================= Section 3: Paytm payout Integration (Payout) ===============================
    private void initiatePaytmPayout(String userId, double amount){
        //1. First we need to call Server and then server will generate Paytm checksum
        // We can not create directly checksum from Mobile (Security Risk).
        String url = "https://yourserver.com/api/generate_paytm_checksum";
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userId);
            params.put("amount", amount);
        } catch (JSONException e){
            e.printStackTrace();
        }
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String txnToken = jsonObject.getString("txn_token");
                    String orderId = jsonObject.getString("order_id");
                    // Here we call the Paytm SDK for sending money
                    startPayment(txnToken, orderId, amount);
                } catch (JSONException e){
                    Toast.makeText(MainActivity2.this, "Payment Gateway Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity2.this, "Network Issue", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        requestQueue.add(request);
    }
    private void startPayment(String token, String orderId, double amount){
        String callbackUrl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId;

    }
}