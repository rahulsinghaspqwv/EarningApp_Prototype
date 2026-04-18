package com.example.megdeal_earning;

import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.megdeal_earning.utils.Constants;
import com.example.megdeal_earning.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONObject;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etMobile, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        etMobile = findViewById(R.id.et_mobile);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
    private void loginUser(){
        String mobile = etMobile.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (mobile.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Required!", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("mobile", mobile);
            params.put("password", password);
        } catch (JSONException e){
            e.printStackTrace();
        }
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("success")){
                        JSONObject user = jsonObject.getJSONObject("user");
                        String userId = user.getString("id");
                        String name = user.getString("name");
                        String mobile = user.getString("mobile");
                        String paytm = user.getString("paytm_number");
                        String referralCode = user.optString("referral_code", "");
                        sessionManager.createLoginSession(userId, name, mobile, paytm, referralCode);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, referralCode, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.LOGIN_URL, params, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }
}