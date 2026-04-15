package com.example.megdeal_earning;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.megdeal_earning.Models.Transaction;
import com.example.megdeal_earning.utils.Constants;
import com.example.megdeal_earning.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.megdeal_earning.Adapters.TransactionAdapter;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList;
    private TabLayout tabLayout;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
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
        tabLayout = findViewById(R.id.tab_layout);
        rvTransactions = findViewById(R.id.rv_transactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionList);
        rvTransactions.setAdapter(adapter);

        loadTransactions("all");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String filter;
                switch (tab.getPosition()){
                    case 1:
                        filter="earning";
                        break;
                    case 2:
                        filter = "withdrawal";
                        break;
                    default:
                        filter = "all";
                }
                loadTransactions(filter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void loadTransactions(String filter){
        String url = Constants.GET_TRANSACTION_URL + "?user_id="+sessionManager.getUserId() + "&filter=" + filter;
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                transactionList.clear();
                try {
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Transaction transaction = new Transaction();
                        transaction.setId(object.getString("id"));
                        transaction.setDescription(object.getString("description"));
                        transaction.setAmount(object.getDouble("amount"));
                        transaction.setType(object.getString("type"));
                        transaction.setStatus(object.getString("status"));
                        transaction.setTimeStamp(object.getLong("timestamp"));
                        transactionList.add(transaction);
                    }
                    adapter.notifyDataSetChanged();
                    if (transactionList.isEmpty()){
                        Toast.makeText(HistoryActivity.this, "No Transaction Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(HistoryActivity.this, "Failed to load Transactions!", Toast.LENGTH_SHORT).show();
            }
        };
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, responseListener, errorListener);
        Volley.newRequestQueue(this).add(request);
    }
}