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

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;
    ProgressBar progressBar;
    TextView tvForgetPassword, tvAlreadyHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        // Immersive UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views method calling
        init();
        // Register Button Click
        btnRegister.setOnClickListener(v -> registerUser());
        tvAlreadyHaveAnAccount.setOnClickListener(v -> tvAlreadyHaveAnAccount());

    }
    private void init(){
        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        tvAlreadyHaveAnAccount = findViewById(R.id.tvGoSignIn);
    }
    private void registerUser(){
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        // validation
        if (username.isEmpty()){
            etUsername.setError("Username required");
            etUsername.requestFocus();
            return;
        }
        if (email.isEmpty()){
            etEmail.setError("Email required");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter valid email");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            etPassword.setError("Minimum 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)){
            etConfirmPassword.setError("Password do not match");
            etConfirmPassword.requestFocus();
            return;
        }
    }
    private void tvAlreadyHaveAnAccount(){
        startActivity(new Intent(this,SignInActivity.class));
    }
}