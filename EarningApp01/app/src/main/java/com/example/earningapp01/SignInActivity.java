package com.example.earningapp01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    ProgressBar progressBar;
    TextView tvForget, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        // Login Button
        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> goToRegister());
        tvForget.setOnClickListener(v -> forgetPassword());
    }
    private void init(){
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.loginProgressBar);
        tvForget = findViewById(R.id.tvLoginForgetPassword);
        tvRegister = findViewById(R.id.tvGoRegister);
    }
    private void loginUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (email.isEmpty()){
            etEmail.setError("Email required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Invalid Email");
            return;
        }
        if (password.isEmpty()){
            etPassword.setError("Password required");
        }
    }
    // Forget password
    private void forgetPassword(){
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()){
            etEmail.setError("Enter email First");
        }
    }
    private void goToRegister(){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}