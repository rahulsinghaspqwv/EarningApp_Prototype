package com.example.megdeal_earning.utils;

import android.content.Context;
import android.view.PixelCopy;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiManager {
    private static  ApiManager instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private ApiManager(Context context){
        ctx=context;
        requestQueue=getRequestQueue();
    }
    public static synchronized ApiManager getInstance(Context context){
        if (instance == null){
            instance = new ApiManager(context);
        }
        return instance;
    }
    private RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }
    public void makePostRequest(String url, JSONObject params, final VolleyCallback callback){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                callback.onSuccess(jsonObject);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError.toString());
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, responseListener, errorListener);
        getRequestQueue().add(request);
    }
    public interface VolleyCallback{
        void onSuccess(JSONObject result);
        void onError(String error);
    }
}
