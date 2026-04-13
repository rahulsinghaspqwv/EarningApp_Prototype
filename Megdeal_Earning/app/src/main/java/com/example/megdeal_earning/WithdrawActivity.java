package com.example.megdeal_earning;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.megdeal_earning.utils.Constants;
import com.example.megdeal_earning.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends AppCompatActivity {
    private TextView tvAvailableBalance, tvPaytmNumber;
    private TextInputEditText etAmount;
    private Button btnWithdrawNow, btnChangePaytm;
    private SessionManager sessionManager;
    private DecimalFormat df = new DecimalFormat("#.##");
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_withdraw);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAvailableBalance = findViewById(R.id.tv_available_balance);
        tvPaytmNumber = findViewById(R.id.tv_paytm_number);
        etAmount = findViewById(R.id.et_amount);
        btnWithdrawNow = findViewById(R.id.btn_withdraw_now);
        btnChangePaytm = findViewById(R.id.btn_change_paytm);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        loadBalance();
        tvPaytmNumber.setText("+91" + sessionManager.getUserPaytm());
        btnWithdrawNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processWithdrawal();
            }
        });
        btnChangePaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WithdrawActivity.this, "Contact support to change Paytm Number", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadBalance(){
        String userId = sessionManager.getUserId();
        String url = Constants.GET_BALANCE_URL + "?user_id" +userId;
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("success")){
                        double balance = jsonObject.getDouble("balance");
                        tvAvailableBalance.setText("₹ " + df.format(balance));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(WithdrawActivity.this, "Failed to load balance", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }

    private void processWithdrawal(){
        String amountStr = etAmount.getText().toString().trim();
        if (amountStr.isEmpty()){
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr);
        if (amount < Constants.MIN_WITHDRAWAL){
            Toast.makeText(this, "Minimum Withdrawal is ₹" + Constants.MIN_WITHDRAWAL, Toast.LENGTH_SHORT).show();
            return;
        }
        // Get paytm checksum from server
        progressDialog.show();
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", sessionManager.getUserId());
            params.put("amount", amount);
            params.put("paytm_number", sessionManager.getUserPaytm());
        } catch (JSONException e){
            e.printStackTrace();
        }
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                try {
                    if (jsonObject.getBoolean("success")){
                        String txnToken = jsonObject.getString("txn_token");
                        String orderId = jsonObject.getString("order_id");
                        initiatePaytmPayment(txnToken, orderId, amount);
                    } else {
                        Toast.makeText(WithdrawActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(WithdrawActivity.this, "Network Error: ", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.GENERATE_CHECKSUM_URL, params, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }

    private void initiatePaytmPayment(String txnToken, String orderId, double amount){
        PaytmPGService service = PaytmPGService.getStagingService(null);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.PAYTM_MID);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", sessionManager.getUserId());
        paramMap.put("CHANNEL_ID", Constants.PAYTM_CHANNEL_ID);
        paramMap.put("TXN_AMOUNT", String.valueOf(amount));
        paramMap.put("WEBSITE", Constants.PAYTM_WEBSITE);
        paramMap.put("INDUSTRY_TYPE_ID", Constants.PAYTM_INDUSTRY_TYPE);
        paramMap.put("CALLBACK_URL", Constants.PAYOUT_CALLBACK_URL);
        paramMap.put("CHECKSUMHASH", txnToken);
        PaytmOrder order = new PaytmOrder(paramMap);
        service.initialize(order, null);
        service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable Bundle bundle) {
                Toast.makeText(WithdrawActivity.this, "Withdrawal Successful!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(WithdrawActivity.this, "Network not available", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorProceed(String s) {
                Toast.makeText(WithdrawActivity.this, "Error: " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Toast.makeText(WithdrawActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Toast.makeText(WithdrawActivity.this, "UI Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Toast.makeText(WithdrawActivity.this, "Error loading page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Toast.makeText(WithdrawActivity.this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Toast.makeText(WithdrawActivity.this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}