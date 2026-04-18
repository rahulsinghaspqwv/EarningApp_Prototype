package com.example.megdeal_earning;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuHost;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.megdeal_earning.utils.Constants;
import com.example.megdeal_earning.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tvBalance, tvTodayEarning, tvTotalEarned, tvTotalWithdrawn;
    private Button btnOfferWall, btnWithdraw, btnHistory, btnRefer;
    private SessionManager sessionManager;
    private DecimalFormat df = new DecimalFormat("#.##");
    boolean isFirstLoad = true;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvBalance = findViewById(R.id.tv_balance);
        tvTodayEarning = findViewById(R.id.tv_today_earning);
        tvTotalEarned = findViewById(R.id.tv_total_earned);
        tvTotalWithdrawn = findViewById(R.id.tv_total_withdrawn);
        btnOfferWall = findViewById(R.id.btn_offerWall);
        btnWithdraw = findViewById(R.id.btn_withdraw);
        btnHistory = findViewById(R.id.btn_history);
        btnRefer = findViewById(R.id.btn_refer);
        swipeRefresh=findViewById(R.id.swipeRefresh);
        // Load user data
        loadUserData();
        btnOfferWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OfferWallActivity.class));
            }
        });
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WithdrawActivity.class));
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        btnRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReferralCode();
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserData();
            }
        });
        swipeRefresh.setRefreshing(true);
        loadUserData();
    }
    private void loadUserData(){
        String userId = sessionManager.getUserId();
        String url = Constants.GET_BALANCE_URL + "?user_id=" + userId;
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("LOAD_RESPONSE", jsonObject.toString());
                try {
                    if (jsonObject.getBoolean("success")){
                        double balance = jsonObject.optDouble("balance", 0);
                        double todayEarning = jsonObject.optDouble("today_earning", 0);
                        double totalEarned = jsonObject.optDouble("total_earned",0);
                        double totalWithdrawn = jsonObject.optDouble("total_withdrawn", 0);
                        tvBalance.setText("rs " + df.format(balance));
                        tvTodayEarning.setText("Today: rs " + df.format(todayEarning));
                        tvTotalEarned.setText("rs " + df.format(totalEarned));
                        tvTotalWithdrawn.setText("rs " + df.format(totalWithdrawn));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
                swipeRefresh.setRefreshing(false);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "Failed to load Data", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }
    private void shareReferralCode(){
        String referralCode = sessionManager.getReferralCode();
        String shareText = "Join me on MegDeal Earning and earn real money by installing apps?\n" + "Use my referral code: " + referralCode + "\n" + "Download now: https://play.google.com/store/apps/details?id=com.MegdealEarning";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if (id==R.id.action_profile){
            // Open Profile Activity
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id==R.id.action_share) {
            shareReferralCode();
            return true;
        } else if (id==R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id==R.id.action_logout) {
            sessionManager.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onResume(){
        super.onResume();
        if (!isFirstLoad){
            loadUserData();
        }
        isFirstLoad = false;
    }
    private void showAboutDialog(){
        new AlertDialog.Builder(this).setTitle("About MegDeal Earning").setMessage("MegDeal Earning v1.0\n\nEarn Money by completing tasks and installing apps. \n\nContact: support@MegDeal.com").setPositiveButton("OK", null).show();
    }
}